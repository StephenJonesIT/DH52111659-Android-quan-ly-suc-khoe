package vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("role") val role: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)
