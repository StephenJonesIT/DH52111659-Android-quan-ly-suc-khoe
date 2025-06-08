package vn.edu.stu.tranthanhsang.healthy_app.domain.repositories

import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.user.ProfileRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.user.ProfileResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import java.io.File

interface ProfileRepository {
//    suspend fun getProfile(): Result<ProfileResponse>
//    suspend fun updateProfile(profileRequest: ProfileRequest): Result<ProfileResponse>
    suspend fun createProfile(imageFile: File?,profileRequest: ProfileRequest): Result<ProfileResponse>
}