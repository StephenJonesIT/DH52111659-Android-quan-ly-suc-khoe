package vn.edu.stu.tranthanhsang.healthy_app.domain.repositories

import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.LoginResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.RegisterResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.SendOTPResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.UpdatePasswordResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.VerifyEmailResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<LoginResponse>
    suspend fun logout():Result<Unit>
    suspend fun register(email: String, password: String): Result<RegisterResponse>
    suspend fun verifyEmail(email: String, otp: String): Result<VerifyEmailResponse>
    suspend fun forgetPassword(email: String): Result<SendOTPResponse>
    suspend fun verifyOTP(email: String, otp: String): Result<VerifyEmailResponse>
    suspend fun resetPassword(email: String, password: String): Result<UpdatePasswordResponse>
}