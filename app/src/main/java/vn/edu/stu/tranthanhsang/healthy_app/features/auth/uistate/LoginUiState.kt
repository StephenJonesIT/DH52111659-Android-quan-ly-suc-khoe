package vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate

sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    data class Success<T>(val data: T) : LoginUiState()
    data class Error(val message: String?) : LoginUiState()
}