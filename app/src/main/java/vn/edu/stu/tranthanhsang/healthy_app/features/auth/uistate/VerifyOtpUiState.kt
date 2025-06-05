package vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate

sealed class VerifyOtpUiState {
    object Initial : VerifyOtpUiState()
    object Loading : VerifyOtpUiState()
    data class Success<T>(val data: T) : VerifyOtpUiState()
    data class Error(val message: String?) : VerifyOtpUiState()
}