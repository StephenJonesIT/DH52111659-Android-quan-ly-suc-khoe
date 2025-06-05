package vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate
sealed class RegisterUiState {
    object Initial : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success<T>(val data: T) : RegisterUiState()
    data class Error(val message: String?) : RegisterUiState()
}