package vn.edu.stu.tranthanhsang.healthy_app.domain.models

sealed class ForgotPasswordError(val message: String){
    object EmptyFieldsEmail : ForgotPasswordError("Email không được để trống")
    object InvalidEmail : ForgotPasswordError("Email không đúng định dạng")
    data class ServerError(val exception: Throwable, val messageError: String = "Xác thực thất bại. Vui lòng thử lại.") : ForgotPasswordError(messageError)
}
