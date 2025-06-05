package vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate

sealed class ForgotPasswordUiState {
    object Initial : ForgotPasswordUiState()
    object Loading : ForgotPasswordUiState()
    data class Success<T>(val data: T) : ForgotPasswordUiState()
    data class Error(val message: String?) : ForgotPasswordUiState()
}