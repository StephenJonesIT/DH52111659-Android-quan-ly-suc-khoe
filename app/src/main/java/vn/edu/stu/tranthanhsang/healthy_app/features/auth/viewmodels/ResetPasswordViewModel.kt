package vn.edu.stu.tranthanhsang.healthy_app.features.auth.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.ResetPasswordError
import vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.auth.ResetPasswordUseCase
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.ResetPasswordUiState
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    private var _resetPasswordUiState = MutableLiveData<ResetPasswordUiState>()
    val resetPasswordUiState get() = _resetPasswordUiState

    private var _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> get() = _passwordError

    private var _confirmPasswordError = MutableLiveData<String?>()
    val confirmPasswordError: LiveData<String?> get() = _confirmPasswordError

    fun resetState(){
        _resetPasswordUiState.value = ResetPasswordUiState.Initial
    }

    fun resetError(){
        _passwordError.value = null
        _confirmPasswordError.value = null
    }

    fun resetPassword(email: String, password: String, confirmPassword: String) {
        _resetPasswordUiState.value = ResetPasswordUiState.Initial
        viewModelScope.launch(Dispatchers.IO) {
            _resetPasswordUiState.postValue(ResetPasswordUiState.Loading)
            val uiState = when(val result = resetPasswordUseCase(email, password, confirmPassword)){
                is Result.Success ->  ResetPasswordUiState.Success(result.data.message)
                is Result.Error -> {
                        when(val error = result.exception) {
                            is ResetPasswordError.EmptyPassword -> _passwordError.postValue(error.message)
                            is ResetPasswordError.PasswordLengthError -> _passwordError.postValue(error.message)
                            is ResetPasswordError.PasswordNotMatch -> _confirmPasswordError.postValue(error.message)
                        }
                        ResetPasswordUiState.Error(result.message)
                }
                is Result.Loading -> ResetPasswordUiState.Loading
            }
            _resetPasswordUiState.postValue(uiState)
        }

    }
}