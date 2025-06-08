package vn.edu.stu.tranthanhsang.healthy_app.features.auth.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs.UserPreferenceRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.auth.ForgotPasswordUseCase
import vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.auth.VerifyEmailUseCase
import vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.auth.VerifyOtpUseCase
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.SendOtpUiState
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.VerifyOtpUiState
import vn.edu.stu.tranthanhsang.healthy_app.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class VerifyOTPViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase
):ViewModel() {
    private var _navigateToLogin = SingleLiveEvent<Pair<String, String>>()
    val navigateToLogin: LiveData<Pair<String, String>> get() = _navigateToLogin

    var password: String = ""

    var isForgotPassword: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _authStatus = MutableLiveData<VerifyOtpUiState>()
    val authState: LiveData<VerifyOtpUiState> = _authStatus

    private val _sendOtpStatus = MutableLiveData<SendOtpUiState>()
    val sendOtpState: LiveData<SendOtpUiState> = _sendOtpStatus

    // Private MutableLiveData to store OTP digits as a list
    private var _otpDigits = MutableLiveData(MutableList(6){""})
    val otpDigits: MutableLiveData<MutableList<String>> get() = _otpDigits

    // MediatorLiveData để theo dõi trạng thái hoàn thành của OTP
    private val _isOtpComplete = MediatorLiveData<Boolean>()
    val isOtpComplete: LiveData<Boolean> get() = _isOtpComplete

    var emailSend: MutableLiveData<String> = MutableLiveData("")

    init {
        _isOtpComplete.addSource(_otpDigits) {
            checkOtpCompletion(it)
        }
        checkOtpCompletion(_otpDigits.value)
    }

    fun updatePassword(password: String){
        this.password = password
    }

    fun setEmailFromIntent(email: String){
        emailSend.value = email
    }
    // Hàm này được gọi mỗi khi _otpDigits thay đổi
    private fun checkOtpCompletion(digits: MutableList<String>?){
        val complete = digits?.all { it.isNotBlank() } ?: false
        _isOtpComplete.value = complete
    }

    private fun getEmailDatastore():String{
        if (!isForgotPassword.value!!){
            val email = runBlocking {
                userPreferenceRepository.email.first()
            }
            Log.d("DATASTORE_EMAIL", email.toString())
            emailSend.value= email
        }
        Log.d("EMAIL_RETURN", emailSend.value.toString())
        return emailSend.value?:""
    }

    fun verifyOTP() {
        val otp = getFullOtp()
        val email = getEmailDatastore()
        Log.d("email", email)
        _authStatus.value = VerifyOtpUiState.Initial
        viewModelScope.launch (Dispatchers.IO){
            _authStatus.postValue(VerifyOtpUiState.Loading)
            if (!isForgotPassword.value!!){
                val uiState = when (val result = verifyEmailUseCase(email, otp)){
                    is Result.Success -> {
                            if(!isForgotPassword.value!!){
                                _navigateToLogin.postValue(Pair(email, password))
                            }
                            VerifyOtpUiState.Success(result.data.message)
                    }
                    is Result.Error -> VerifyOtpUiState.Error(result.message)
                    Result.Loading -> VerifyOtpUiState.Loading
                }
                _authStatus.postValue(uiState)
            }else {
                    val uiState = when(val result = verifyOtpUseCase(email, otp)){
                        is Result.Success -> VerifyOtpUiState.Success(result.data.message)
                        is Result.Error -> VerifyOtpUiState.Error(result.message)
                        is Result.Loading -> VerifyOtpUiState.Loading
                    }
                _authStatus.postValue(uiState)
            }
        }
    }

    fun resetState() {
        _authStatus.value = VerifyOtpUiState.Initial
    }

    // Get the complete OTP string
    private fun getFullOtp(): String {
        return _otpDigits.value?.joinToString("") ?: ""
    }

    // Reset OTP (ví dụ: khi người dùng yêu cầu gửi lại)
    fun resetOtp() {
        _otpDigits.value = MutableList(6){""}
        checkOtpCompletion(_otpDigits.value)
        val email = emailSend.value ?: ""

        viewModelScope.launch {
            _sendOtpStatus.value = SendOtpUiState.Loading
            when(val result = forgotPasswordUseCase(email)){
                is Result.Success -> {
                    _sendOtpStatus.value = SendOtpUiState.Success(result.data.message)
                }
                is Result.Error -> {
                    _sendOtpStatus.value = SendOtpUiState.Error(result.message)
                }
                Result.Loading -> VerifyOtpUiState.Loading
            }
        }
    }

    // Cập nhật một chữ số OTP cụ thể tại vị trí index
    fun updateOtpDigit(index: Int, digit: String) {
        val currentList = _otpDigits.value ?: MutableList(6){""}
        if (index in 0 until currentList.size) {
            currentList[index] = digit
            // Quan trọng: Phải postValue (hoặc setValue) lại toàn bộ list
            // để LiveData phát hiện sự thay đổi và kích hoạt observer.
            _otpDigits.value = currentList // Sử dụng setValue nếu trên main thread, postValue nếu trên background thread
        }
    }
}