package vn.edu.stu.tranthanhsang.healthy_app.domain.usecase

import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.SendOTPResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.ForgotPasswordError
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.AuthRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import vn.edu.stu.tranthanhsang.healthy_app.utils.isValidEmail
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun  invoke(email: String): Result<SendOTPResponse> {
        if (email.trim().isEmpty()){
            Result.Error(ForgotPasswordError.EmptyFieldsEmail)
        }

        if(!isValidEmail(email)){
            Result.Error(ForgotPasswordError.InvalidEmail)
        }

        val response = authRepository.forgetPassword(email)
        return when (response){
            is Result.Success -> Result.Success(response.data)
            is Result.Error -> Result.Error(
                ForgotPasswordError.ServerError(
                    response.exception as Throwable,
                    response.message ?: "Xác thực thất bại. Vui lòng thử lại."
                )
            )
            Result.Loading -> Result.Loading
        }
    }
}