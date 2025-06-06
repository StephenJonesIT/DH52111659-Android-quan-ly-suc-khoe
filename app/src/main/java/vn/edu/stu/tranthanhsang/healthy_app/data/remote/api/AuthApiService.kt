package vn.edu.stu.tranthanhsang.healthy_app.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.AuthRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.LoginResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.RegisterRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.RegisterResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.SendOTPRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.SendOTPResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.UpdatePasswordResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.VerifyEmailRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.VerifyEmailResponse

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
}