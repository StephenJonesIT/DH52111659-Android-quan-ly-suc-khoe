package vn.edu.stu.tranthanhsang.healthy_app.domain.usecase.user

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs.UserPreferenceRepository
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.user.ProfileRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.user.ProfileResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.models.CreateProfileError
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.ProfileRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import javax.inject.Inject

class CreateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) {
    suspend operator fun invoke(
        fullName: String,
        yearOld: Int,
        height: Int,
        weight:Int,
        gender:Boolean,
        ):Result<ProfileResponse>{
        val userID =  runBlocking { userPreferenceRepository.userId.first() }
        val profileRequest = ProfileRequest(
            userID.toString(),
            fullName,
            yearOld,
            height,
            weight,
            gender,
            null
        )

        return when(val result = profileRepository.createProfile(null, profileRequest)){
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(
                CreateProfileError.ServerError(
                    result.exception as Throwable,
                    result.message ?: "Tạo hồ sơ thất bại. Vui lòng thử lại."
                )
            )
            is Result.Loading -> Result.Loading
        }
    }
}