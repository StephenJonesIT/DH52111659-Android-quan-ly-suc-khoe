package vn.edu.stu.tranthanhsang.healthy_app.domain.models

sealed class CreateProfileError {
    data class ServerError(val exception: Throwable, val messageError: String = "Tạo hồ sơ thất bại. Vui lòng thử lại."):CreateProfileError()
}