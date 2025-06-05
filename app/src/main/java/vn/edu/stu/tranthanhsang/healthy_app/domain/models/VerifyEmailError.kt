package vn.edu.stu.tranthanhsang.healthy_app.domain.models

sealed class VerifyEmailError (val message: String){
    object InvalidVerifyEmail : VerifyEmailError("OTP không đúng định dạng")
    data class ServerError(val exception: Throwable, val messageError: String = "Xác thực thất bại. Vui lòng thử lại.") : RegisterError(messageError)
}