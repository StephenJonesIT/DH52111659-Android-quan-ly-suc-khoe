package vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.user

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("profile_id") val profileId: String?,
    @SerializedName("account_id") val accountId: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("year_old") val yearOld: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("gender") val gender: Boolean,
    @SerializedName("bmi") val bmi: Double?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("created_at") val createdAt: String?
)
