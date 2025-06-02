package vn.edu.stu.tranthanhsang.healthy_app.domain.usecase

import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.LoginResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.AuthRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import vn.edu.stu.tranthanhsang.healthy_app.utils.isValidEmail
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<LoginResponse> {
        if (username.isBlank() || password.isBlank()) {
            return Result.Error(IllegalArgumentException("Username and password cannot be empty"))
        }

        if (!isValidEmail(username)) {
            return Result.Error(IllegalArgumentException("Invalid email format"))
        }

        if (password.length < 8) {
            return Result.Error(IllegalArgumentException("Password must be at least 8 characters long"))
        }

        val result = authRepository.login(username, password)
        return when (result) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.exception, result.message ?: "Đăng nhập thất bại. Vui lòng thử lại.")
            Result.Loading -> Result.Loading
        }
    }
}