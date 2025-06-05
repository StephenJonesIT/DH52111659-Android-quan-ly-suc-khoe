package vn.edu.stu.tranthanhsang.healthy_app.domain.usecase

import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.UpdatePasswordResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.ResetPasswordError
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.AuthRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, confirmPassword: String):Result<UpdatePasswordResponse> {
        // Thực hiện logic reset password
        if (password.trim().isEmpty()){
            return Result.Error(ResetPasswordError.EmptyPassword)
        }
        if (password.length < 8){
            return Result.Error(ResetPasswordError.PasswordLengthError)
        }
        if (password != confirmPassword){
            return Result.Error(ResetPasswordError.PasswordNotMatch)
        }

        return when(val result = authRepository.resetPassword(email, password)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(
                ResetPasswordError.ServerError(
                    result.exception as Throwable,
                    result.message ?: "Thay đổi mật khẩu thất bại. Vui lòng thử lại."
                )
            )
            is Result.Loading -> Result.Loading
        }

    }

}