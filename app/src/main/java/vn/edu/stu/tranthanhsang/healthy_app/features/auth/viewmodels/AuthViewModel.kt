package vn.edu.stu.tranthanhsang.healthy_app.features.auth.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.LoginError
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.RegisterError
import vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.auth.LoginUseCase
import vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.auth.RegisterUseCase
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.LoginUiState
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.RegisterUiState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    val roleUser: MutableLiveData<String> = MutableLiveData()

    private val _authStatus = MutableLiveData<LoginUiState>()
    val authState: LiveData<LoginUiState> = _authStatus

    private val _registerStatus = MutableLiveData<RegisterUiState>()
    val registerState: LiveData<RegisterUiState> = _registerStatus

    private val _usernameError = MutableLiveData<String?>()
    val usernameError: LiveData<String?> get() = _usernameError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> get() = _passwordError

    private val _generalError = MutableLiveData<String?>()
    val generalError: LiveData<String?> get() = _generalError

    private val _confirmPasswordError = MutableLiveData<String?>()
    val confirmPasswordError: LiveData<String?> get() = _confirmPasswordError

    fun login(username: String, password: String) {
        _authStatus.value = LoginUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val uiState = when(val result = loginUseCase(username, password)){
                is Result.Success -> {
                        roleUser.postValue(result.data.role)
                        LoginUiState.Success(result.data)
                }
                is Result.Error -> {
                        when(val error = result.exception){
                            is LoginError.InvalidEmail -> _usernameError.postValue(error.message)
                            is LoginError.WeakPassword -> _passwordError.postValue(error.message)
                            is LoginError.EmptyFieldsEmail -> _usernameError.postValue(error.message)
                            is LoginError.EmptyFieldsPassword -> _passwordError.postValue(error.message)
                            is LoginError.ServerError -> _generalError.postValue(error.message)
                        }
                    LoginUiState.Error(result.message)
                }
                is Result.Loading -> LoginUiState.Loading
            }
            _authStatus.postValue(uiState)
        }
    }

    fun register(email: String, password: String, rePassword: String) {
        _registerStatus.value = RegisterUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val uiState = when(val result = registerUseCase(email, password, rePassword)) {
                is Result.Success -> RegisterUiState.Success(result.data)
                is Result.Error -> {
                    when (val error = result.exception) {
                        is RegisterError.InvalidEmail -> _usernameError.postValue(error.message)
                        is RegisterError.WeakPassword -> _passwordError.postValue(error.message)
                        is RegisterError.EmptyFieldsEmail -> _usernameError.postValue(error.message)
                        is RegisterError.EmptyFieldsPassword -> _passwordError.postValue(error.message)
                        is RegisterError.PasswordNotMatch -> _confirmPasswordError.postValue(error.message)
                        is RegisterError.ServerError -> _generalError.postValue(error.message)
                        else -> _generalError.value = "Đăng ký thất bại. Vui lòng thử lại."
                    }
                    RegisterUiState.Error(result.message)
                }

                Result.Loading -> RegisterUiState.Loading
            }
            _registerStatus.postValue(uiState)
        }
    }
    fun resetLoginState() {
        _authStatus.value = LoginUiState.Initial
    }

    fun resetRegisterState(){
       _registerStatus.value = RegisterUiState.Initial
    }
    fun resetErrors() {
        _usernameError.value = null
        _passwordError.value = null
        _generalError.value = null
        _confirmPasswordError.value = null
    }
}



