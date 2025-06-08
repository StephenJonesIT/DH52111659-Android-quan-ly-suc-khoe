package vn.edu.stu.tranthanhsang.healthy_app.data.repositories

import android.util.Log
import com.google.gson.Gson
import vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs.UserPreferenceRepository
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.api.AuthApiService
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.ErrorResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.AuthRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.LoginResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.RegisterRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.RegisterResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.SendOTPRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.SendOTPResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.UpdatePasswordResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.VerifyEmailRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.VerifyEmailResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.AuthRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userPreferenceRepository: UserPreferenceRepository
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try{
            val request = AuthRequest(username, password)
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
                val errorMessage = try {
                    errorBody?.let {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorResponse.error
                    }
                }catch (e: Exception) {
                    "Login error"
                }

                Result.Error(Exception("API Error: ${response.code()}"), errorMessage ?: "Đăng nhập thất bại. Mã lỗi: ${response.code()}")
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

    override suspend fun register(email: String, password: String): Result<RegisterResponse> {
        return try{
            val registerRequest = RegisterRequest(email, password)
            val response = authApiService.register(registerRequest)
            if(response.isSuccessful){
                val registerResponse = response.body()
                Log.d("REGISTER",registerResponse.toString())
                registerResponse?.let {
                    userPreferenceRepository.saveEmail(
                        it.email
                    )
                }?: Result.Error(Exception("Empty response body"), "Lỗi: Không có dữ liệu trả về.")
                Result.Success(response.body()!!)
            }else{
                val errorBody = response.errorBody()?.string()
                val errorMessage = try{
                    errorBody?.let {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorResponse.error
                    }
                }catch (e: Exception){
                    "Register error"
                }
                Result.Error(Exception("API Error: ${response.code()}"), errorMessage ?: "Đăng ký thất bại. Mã lỗi: ${response.code()}")
            }
        }catch (e: Exception){
            Result.Error("Lỗi kết nối hoặc không xác định: ${e.localizedMessage}")
        }
    }

    override suspend fun verifyEmail(email: String, otpCode: String): Result<VerifyEmailResponse> {
        return try{
            val verifyEmailRequest = VerifyEmailRequest(email, otpCode)
            val response = authApiService.verifyEmail(verifyEmailRequest)
            if (response.isSuccessful){
                Result.Success(response.body()!!)
            }else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorResponse.error
                    }
                }catch(e: Exception){
                    "Verify email error"
                }
                Result.Error(Exception("API Error: ${response.code()}"), errorMessage ?: "Xác thực thất bại. Mã lỗi: ${response.code()}")
            }
        }catch (e: Exception){
            Result.Error("Lỗi kết nối hoặc không xác định: ${e.localizedMessage}")
        }
    }

    override suspend fun forgetPassword(email: String): Result<SendOTPResponse> {
        return try {
            val emailRequest = SendOTPRequest(email)
            val response = authApiService.forgetPassword(emailRequest)
            if (response.isSuccessful){
                Result.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()
                val errorMessage = try {
                    errorBody?.let {
                        val errorResponse = Gson().fromJson(errorBody.string(), ErrorResponse::class.java)
                        errorResponse.error
                    }
                }catch(e: Exception){
                    "Verify email error"
                }
                Result.Error(Exception("API Error: ${response.code()}"), errorMessage ?: "Xác thực thất bại. Mã lỗi: ${response.code()}")
            }
        }catch (e: Exception){
            Result.Error("Lỗi kết nối hoặc không xác định: ${e.localizedMessage}")
        }
    }

    override suspend fun verifyOTP(email: String, otp: String): Result<VerifyEmailResponse> {
        return try{
            val verifyEmailRequest = VerifyEmailRequest(email, otp)
            val response = authApiService.verifyOTP(verifyEmailRequest)
            if (response.isSuccessful){
                Result.Success(response.body()!!)
            }else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorResponse.error
                    }
                }catch(e: Exception){
                    "Verify otp error"
                }
                Result.Error(Exception("API Error: ${response.code()}"), errorMessage ?: "Xác thực thất bại. Mã lỗi: ${response.code()}")
            }
        }catch (e: Exception){
            Result.Error("Lỗi kết nối hoặc không xác định: ${e.localizedMessage}")
        }
    }

    override suspend fun resetPassword(
        email: String,
        password: String
    ): Result<UpdatePasswordResponse> {
        return try {
            val resetPasswordRequest = AuthRequest(email, password)
            val response = authApiService.resetPassword(resetPasswordRequest)
            if (response.isSuccessful){
                Result.Success(response.body()!!)
            }
            else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorResponse.error
                    }
                }
                catch (e: Exception){
                    "Reset password error"
                }
                Result.Error(Exception("API Error: ${response.code()}"), errorMessage ?: "Xác thực thất bại. Mã lỗi: ${response.code()}")

            }
        } catch (e: Exception){
            Result.Error("Lỗi kết nối hoặc không xác định: ${e.localizedMessage}")
        }
    }
}