package vn.edu.stu.tranthanhsang.healthy_app.domain.usecase

import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.RegisterResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.AuthRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.RegisterError
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import vn.edu.stu.tranthanhsang.healthy_app.utils.isValidEmail
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, rePassword: String): Result<RegisterResponse>{
        if (email.trim().isEmpty()) {
            return Result.Error(RegisterError.EmptyFieldsEmail)
        }

        if (password.trim().isEmpty()){
            return Result.Error(RegisterError.EmptyFieldsPassword)
        }

        if (!isValidEmail(email)) {
            return Result.Error(RegisterError.InvalidEmail)
        }

        if (password.length < 8) {
            return Result.Error(RegisterError.WeakPassword)
        }

        if (password != rePassword) {
            return Result.Error(RegisterError.PasswordNotMatch)
        }

        val result = authRepository.register(email, password)

        return when (result) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(
                RegisterError.ServerError(
                result.exception as Throwable,
                result.message ?: "Đăng ký thất bại. Vui lòng thử lại."))

            Result.Loading -> Result.Loading
        }
    }
}