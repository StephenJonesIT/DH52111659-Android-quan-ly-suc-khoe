package vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.auth

import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.VerifyEmailResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.VerifyEmailError
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.AuthRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, otp: String): Result<VerifyEmailResponse> {
       if (email.trim().isEmpty()) {
           return Result.Error(VerifyEmailError.InvalidVerifyEmail)
       }
        return when(val result = authRepository.verifyOTP(email, otp)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(
                VerifyEmailError.ServerError(
                    result.exception as Throwable,
                    result.message ?: "Xác thực thất bại. Vui lòng thử lại."
                ))
            is Result.Loading -> Result.Loading
        }
    }
}