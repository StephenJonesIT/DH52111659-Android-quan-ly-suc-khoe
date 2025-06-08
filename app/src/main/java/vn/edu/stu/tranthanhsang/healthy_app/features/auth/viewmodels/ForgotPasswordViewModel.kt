package vn.edu.stu.tranthanhsang.healthy_app.features.auth.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.ForgotPasswordError
import vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.auth.ForgotPasswordUseCase
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.ForgotPasswordUiState
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
):ViewModel(){
    private var _forgotPasswordUiState = MutableLiveData<ForgotPasswordUiState>()
    val forgotPasswordUiState get() = _forgotPasswordUiState

    private var _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> get() = _emailError

    private var _emailResponse = MutableLiveData<String?>()
    val emailResponse: LiveData<String?> get() = _emailResponse

    fun resetState(){
        _forgotPasswordUiState.value = ForgotPasswordUiState.Initial
    }

    fun resetError(){
        _emailError.value = null
    }

    fun forgotPassword(email: String) {
        _forgotPasswordUiState.value = ForgotPasswordUiState.Initial
        viewModelScope.launch(Dispatchers.IO) {
            _forgotPasswordUiState.postValue(ForgotPasswordUiState.Loading)
            val uiState = when(val result = forgotPasswordUseCase(email)) {
                is Result.Success -> {
                    _emailResponse.postValue(result.data.email)
                    ForgotPasswordUiState.Success(result.data.message)
                }

                is Result.Error -> {
                    when (val error = result.exception) {
                        is ForgotPasswordError.EmptyFieldsEmail -> _emailError.postValue(error.message)
                        is ForgotPasswordError.InvalidEmail -> _emailError.postValue(error.message)
                        is ForgotPasswordError.ServerError -> _emailError.postValue(error.message)
                        else -> _emailError.postValue("Lỗi không xác định")
                    }
                    ForgotPasswordUiState.Error(result.message)
                }
                is Result.Loading -> ForgotPasswordUiState.Loading
            }
            _forgotPasswordUiState.postValue(uiState)
        }
    }
}