package vn.edu.stu.tranthanhsang.healthy_app.data.repositories

import vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs.UserPreferenceRepository
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.api.AuthApiService
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.LoginRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.LoginResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.AuthRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userPreferenceRepository: UserPreferenceRepository
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try{
            val request = LoginRequest(username, password)
            val response = authApiService.login(request)
            if (response.isSuccessful){
                val loginResponse = response.body()
                loginResponse?.let {
                    userPreferenceRepository.saveUserSession(
                        it.userId,
                        it.role,
                        it.accessToken,
                        it.refreshToken
                    )
                    Result.Success(response.body()!!)
                } ?: Result.Error(Exception("Empty response body"), "Lỗi: Không có dữ liệu trả về.")
            }else{
                val errorBody = response.errorBody()?.string()
                Result.Error(Exception("API Error: ${response.code()}"), errorBody ?: "Đăng nhập thất bại. Mã lỗi: ${response.code()}")
            }
        }catch (e: Exception) {
            Result.Error(e, "Lỗi kết nối hoặc không xác định: ${e.localizedMessage}")
        }
    }

    override suspend fun logout() : Result<Unit>{
        return try {
            userPreferenceRepository.clearUserSession()
            Result.Success(Unit)
        } catch (e: Exception){
            Result.Error(e, "Lỗi khi đăng xuất: ${e.localizedMessage}")
        }
    }
}