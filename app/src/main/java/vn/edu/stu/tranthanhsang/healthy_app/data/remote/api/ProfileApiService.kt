package vn.edu.stu.tranthanhsang.healthy_app.data.remote.api

import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.user.ProfileResponse

interface ProfileApiService {
    @Multipart
    @POST("profile")
    suspend fun createProfile(
        @Part image: MultipartBody.Part?,
        @Part("metadata") metadata: RequestBody
    ) : Response<ProfileResponse>
}