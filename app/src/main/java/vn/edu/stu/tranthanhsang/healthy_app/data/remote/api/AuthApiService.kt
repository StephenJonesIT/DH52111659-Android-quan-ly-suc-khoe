package vn.edu.stu.tranthanhsang.healthy_app.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.LoginRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.LoginResponse

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}