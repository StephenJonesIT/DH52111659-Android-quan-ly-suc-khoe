package vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.user

data class ProfileRequest (
    val account_id: String,
    val full_name: String,
    val year_old: Int,
    val weight: Int,
    val height: Int,
    val gender: Boolean,
    val avatar_url: String?,
)