package vn.edu.stu.tranthanhsang.healthy_app.data.repositories

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs.UserPreferenceRepository
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.api.ProfileApiService
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.ErrorResponse
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.user.ProfileRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.user.ProfileResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.ProfileRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result
import java.io.File
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileApiService: ProfileApiService,
) :  ProfileRepository{
//    override suspend fun getProfile(): Result<ProfileResponse> {
//
//    }
//
//    override suspend fun updateProfile(profileRequest: ProfileRequest): Result<ProfileResponse> {
//
//    }

    override suspend fun createProfile(
        imageFile: File?,
        profileRequest: ProfileRequest
    ): Result<ProfileResponse> {
        val metaData = Gson().toJson(profileRequest)
        val metaDataRequest = metaData.toRequestBody("application/json".toMediaTypeOrNull())
        var imagePart: MultipartBody.Part? = null

        if (imageFile != null) {
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        }

        return try {
            val response = profileApiService.createProfile(imagePart, metaDataRequest)
            if (response.isSuccessful){
                Result.Success(response.body()!!)
            }else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let {
                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                        errorResponse.error
                    }
                    } catch (e: Exception) {
                    "Create profile error"
                }
                Result.Error(Exception("API Error: ${response.code()}"), errorMessage ?: "Tạo hồ sơ thất bại. Mã lỗi: ${response.code()}")
            }
        }catch (e: Exception) {
            Result.Error(e, "Lỗi kết nối hoặc không xác định: ${e.localizedMessage}")
        }
    }
}