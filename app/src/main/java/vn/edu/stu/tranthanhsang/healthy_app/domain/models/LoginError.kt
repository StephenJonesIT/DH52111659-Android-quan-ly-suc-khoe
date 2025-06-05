package vn.edu.stu.tranthanhsang.healthy_app.domain.models

sealed class LoginError(val message: String, val field: String?) {
    object EmptyFieldsEmail : LoginError("Email không được để trống", "username")
    object EmptyFieldsPassword : LoginError("Mật khẩu không được để trống", "password")
    object InvalidEmail : LoginError("Email không đúng định dạng", "username")
    object WeakPassword : LoginError("Mật khẩu phải có ít nhất 8 ký tự", "password")
    data class ServerError(val exception: Throwable, val messageError: String = "Đăng nhập thất bại. Vui lòng thử lại.") : LoginError(messageError, null)
}