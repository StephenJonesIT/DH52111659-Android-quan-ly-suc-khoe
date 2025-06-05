package vn.edu.stu.tranthanhsang.healthy_app.domain.models

sealed class ResetPasswordError(val message: String){
    object EmptyPassword: ResetPasswordError("Mật khẩu không được để trống")
    object PasswordLengthError: ResetPasswordError("Mật khẩu phải có ít nhất 8 ký tự")
    object PasswordNotMatch : ResetPasswordError("Mật khẩu không khớp")
    data class ServerError(val exception: Throwable, val messageError: String = "Thay đổi mật khẩu thất bại. Vui lòng thử lại.") : ResetPasswordError(messageError)
}