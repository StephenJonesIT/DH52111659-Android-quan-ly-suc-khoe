package vn.edu.stu.tranthanhsang.healthy_app.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import vn.edu.stu.tranthanhsang.healthy_app.common.model.TokenRefreshRequest
import vn.edu.stu.tranthanhsang.healthy_app.common.model.TokenRefreshResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.AuthRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.LoginResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.RegisterRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.RegisterResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.SendOTPRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.SendOTPResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.UpdatePasswordResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.VerifyEmailRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth.VerifyEmailResponse

/**
 * Giao diện dịch vụ API Retrofit cho các điểm cuối xác thực.
 * Bao gồm các phương thức cho đăng nhập, đăng ký, xác minh email, quản lý mật khẩu,
 * và làm mới token.
 */
interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: AuthRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("auth/verify-email")
    suspend fun verifyEmail(@Body verifyEmailRequest: VerifyEmailRequest): Response<VerifyEmailResponse>

    @POST("auth/password/forgot")
    suspend fun forgetPassword(@Body resendOTPRequest: SendOTPRequest): Response<SendOTPResponse>

    @POST("auth/password/verify-otp")
    suspend fun verifyOTP(@Body verifyEmailRequest: VerifyEmailRequest): Response<VerifyEmailResponse>

    @POST("auth/password/reset")
    suspend fun resetPassword(@Body resetPasswordRequest: AuthRequest): Response<UpdatePasswordResponse>

    @POST("auth/token/refresh")
    suspend fun refreshToken(@Body tokenRefreshRequest: TokenRefreshRequest): Response<TokenRefreshResponse>
}