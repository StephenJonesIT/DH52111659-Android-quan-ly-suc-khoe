package vn.edu.stu.tranthanhsang.healthy_app.domain.models

sealed class RegisterError (val message: String){
    object EmptyFieldsEmail : RegisterError("Email không được để trống")
    object EmptyFieldsPassword : RegisterError("Mật khẩu không được để trống")
    object InvalidEmail : RegisterError("Email không đúng định dạng")
    object WeakPassword : RegisterError("Mật khẩu phải có ít nhất 8 ký tự")
    object PasswordNotMatch : RegisterError("Mật khẩu không khớp")
    data class ServerError(val exception: Throwable, val messageError: String = "Đăng ký thất bại. Vui lòng thử lại.") : RegisterError(messageError)
}