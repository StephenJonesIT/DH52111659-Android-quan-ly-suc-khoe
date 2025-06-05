package vn.edu.stu.tranthanhsang.healthy_app.data.remote.models

import com.google.gson.annotations.SerializedName

data class VerifyEmailResponse(
    @SerializedName("message") val message: String,
    @SerializedName("result") val isVerify: Boolean,
)
