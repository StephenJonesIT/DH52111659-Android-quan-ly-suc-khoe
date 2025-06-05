package vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate

sealed class ResetPasswordUiState {
    object Initial : ResetPasswordUiState()
    object Loading : ResetPasswordUiState()
    data class Success<T>(val data: T) : ResetPasswordUiState()
    data class Error(val message: String?) : ResetPasswordUiState()
}