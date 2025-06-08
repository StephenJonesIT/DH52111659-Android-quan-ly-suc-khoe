package vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.auth

import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.LoginResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.AuthRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.LoginError
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import vn.edu.stu.tranthanhsang.healthy_app.utils.isValidEmail
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<LoginResponse> {
        if (username.trim().isEmpty()) {
            return Result.Error(LoginError.EmptyFieldsEmail)
        }

        if (password.trim().isEmpty()){
            return Result.Error(LoginError.EmptyFieldsPassword)
        }
        if (!isValidEmail(username)) {
            return Result.Error(LoginError.InvalidEmail)
        }

        if (password.length < 8) {
            return Result.Error(LoginError.WeakPassword)
        }

        val result = authRepository.login(username, password)
        return when (result) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(
                LoginError.ServerError(
                result.exception as Throwable,
                result.message ?: "Đăng nhập thất bại. Vui lòng thử lại."))
            Result.Loading -> Result.Loading
        }
    }
}