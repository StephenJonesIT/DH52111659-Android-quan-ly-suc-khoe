package vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate

sealed class SendOtpUiState {
    object Initial : SendOtpUiState()
    object Loading : SendOtpUiState()
    data class Success<T>(val data: T) : SendOtpUiState()
    data class Error(val message: String?) : SendOtpUiState()

}