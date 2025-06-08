package vn.edu.stu.tranthanhsang.healthy_app.features.user.uistate

import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.LoginUiState

sealed class CreateProfileUiState {
    object Initial : CreateProfileUiState()
    object Loading : CreateProfileUiState()
    data class Success<T>(val data: T) : CreateProfileUiState()
    data class Error(val message: String?) : CreateProfileUiState()
}