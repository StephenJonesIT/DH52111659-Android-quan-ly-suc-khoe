package vn.edu.stu.tranthanhsang.healthy_app.domain.usecase

import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.VerifyEmailResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.VerifyEmailError
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.AuthRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, otp: String) : Result<VerifyEmailResponse> {
        if (otp.trim().isEmpty()) {
            return Result.Error(VerifyEmailError.InvalidVerifyEmail)
        }

        return when (val result = authRepository.verifyEmail(email, otp)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(
                VerifyEmailError.ServerError(
                    result.exception as Throwable,
                    result.message ?: "Xác thực thất bại. Vui lòng thử lại."
                )
            )

            Result.Loading -> Result.Loading
        }
    }
}