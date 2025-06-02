package vn.edu.stu.tranthanhsang.healthy_app.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.LoginResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.LoginUseCase
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _loginStatus = MutableLiveData<LoginUiState>()
    val loginState: LiveData<LoginUiState> = _loginStatus

    fun login(username: String, password: String) {
        _loginStatus.value = LoginUiState.Loading
        viewModelScope.launch {
            when(val result = loginUseCase(username, password)){
                is Result.Success -> {
                    _loginStatus.value = LoginUiState.Success(result.data)
                }
                is Result.Error -> {
                    _loginStatus.value = LoginUiState.Error(result.message ?: "Đăng nhập thất bại. Vui lòng thử lại.")
                }
                Result.Loading -> {
                    _loginStatus.value = LoginUiState.Loading
                }
            }
        }
    }

    fun resetState() {
        _loginStatus.value = LoginUiState.Initial
    }
}

sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val data: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}