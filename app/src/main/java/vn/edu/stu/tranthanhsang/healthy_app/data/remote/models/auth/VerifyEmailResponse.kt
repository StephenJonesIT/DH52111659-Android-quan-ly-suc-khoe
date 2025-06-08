package vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.auth

import com.google.gson.annotations.SerializedName

data class VerifyEmailResponse(
    @SerializedName("message") val message: String,
    @SerializedName("result") val isVerify: Boolean,
)
