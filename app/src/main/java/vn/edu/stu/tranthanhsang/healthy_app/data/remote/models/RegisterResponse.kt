package vn.edu.stu.tranthanhsang.healthy_app.data.remote.models

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("message") val message: String,
    @SerializedName("email") val email: String,
)
