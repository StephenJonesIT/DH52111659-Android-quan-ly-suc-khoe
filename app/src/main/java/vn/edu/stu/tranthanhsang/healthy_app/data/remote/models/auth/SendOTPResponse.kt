package vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth

import com.google.gson.annotations.SerializedName

data class SendOTPResponse(
    @SerializedName("message") val message: String,
    @SerializedName("email") val email: String,
    @SerializedName("result") val success: Boolean,
)
