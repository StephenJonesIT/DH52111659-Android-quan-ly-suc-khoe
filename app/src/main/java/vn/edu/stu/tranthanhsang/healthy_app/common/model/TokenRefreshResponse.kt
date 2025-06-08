package vn.edu.stu.tranthanhsang.healthy_app.common.model

import com.google.gson.annotations.SerializedName

data class TokenRefreshResponse(
    @SerializedName("access_token")val accessToken: String,
)
