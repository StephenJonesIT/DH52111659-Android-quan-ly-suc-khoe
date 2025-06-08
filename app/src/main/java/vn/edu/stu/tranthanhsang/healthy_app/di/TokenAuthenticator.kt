package vn.edu.stu.tranthanhsang.healthy_app.di

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import vn.edu.stu.tranthanhsang.healthy_app.common.model.TokenRefreshRequest
import vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs.UserPreferenceRepository
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.api.AuthApiService
import java.io.IOException
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val authApiService: AuthApiService,
    private val userPreferenceRepository: UserPreferenceRepository,
    @ApplicationContext private val context: Context // Inject context để gửi broadcast
) : Authenticator {

    private val mutex = Mutex() // Mutex để ngăn chặn nhiều yêu cầu làm mới token đồng thời

    override fun authenticate(route: Route?, response: Response): Request? {
        val originalRequest = response.request

        // Tránh vòng lặp vô hạn:
        // 1. Nếu không có header Authorization (yêu cầu không xác thực).
        // 2. Nếu không phải lỗi 401 Unauthorized.
        // 3. Nếu đã có quá nhiều lần thử lại (để tránh vòng lặp cực đoan).
        if (originalRequest.header("Authorization") == null ||
            response.code != 401 ||
            responseCount(response) >= 2 // Giới hạn số lần thử lại từ Authenticator
        ) {
            return null
        }

        // Thực hiện logic làm mới token trong một khối runBlocking
        // với mutex để đảm bảo chỉ một luồng làm mới tại một thời điểm.
        return runBlocking {
            mutex.withLock {
                val currentAccessToken = userPreferenceRepository.accessToken.firstOrNull()
                val staleAccessToken = originalRequest.header("Authorization")?.removePrefix("Bearer ")

                // Kiểm tra xem token đã được làm mới bởi một yêu cầu đồng thời khác chưa.
                // Nếu access token HIỆN TẠI trong kho lưu trữ khác với access token CŨ đã gây ra lỗi 401,
                // điều đó có nghĩa là một luồng khác đã làm mới thành công token.
                if (currentAccessToken != null && currentAccessToken != staleAccessToken) {
                    // Token đã được làm mới, thử lại yêu cầu ban đầu với token mới
                    return@runBlocking originalRequest.newBuilder()
                        .header("Authorization", "Bearer $currentAccessToken")
                        .build()
                }

                // Nếu đến đây, có nghĩa là access token hiện tại trong kho lưu trữ
                // giống với token đã gây ra lỗi 401, hoặc null.
                // Bây giờ chúng ta cần thực sự làm mới token.
                val refreshToken = userPreferenceRepository.refreshToken.firstOrNull()
                    ?: return@runBlocking clearPreferencesAndLogout() // Không có refresh token, buộc đăng xuất

                try {
                    // Gọi API làm mới token. AuthApiService này đã được cấu hình không xác thực.
                    val tokenRefreshResponse = authApiService.refreshToken(TokenRefreshRequest(refreshToken))

                    if (tokenRefreshResponse.isSuccessful) {
                        val newAccessToken = tokenRefreshResponse.body()?.accessToken
                        if (!newAccessToken.isNullOrEmpty()) {
                            // Lưu access token mới (và refresh token nếu API làm mới cũng trả về nó)
                            // Đảm bảo UserPreferenceRepository có hàm này:
                            // suspend fun saveAccessAndRefreshToken(accessToken: String, refreshToken: String)
                            userPreferenceRepository.saveNewAccessToken(newAccessToken)

                            // Thử lại yêu cầu ban đầu với access token mới
                            return@runBlocking originalRequest.newBuilder()
                                .header("Authorization", "Bearer $newAccessToken")
                                .build()
                        } else {
                            // Phản hồi làm mới token trống hoặc không hợp lệ, coi là thất bại
                            return@runBlocking clearPreferencesAndLogout()
                        }
                    } else {
                        // Endpoint làm mới token trả về lỗi (ví dụ: 400, 401, 403)
                        // Điều này có nghĩa là refresh token cũng không hợp lệ/hết hạn.
                        return@runBlocking clearPreferencesAndLogout()
                    }
                } catch (e: HttpException) {
                    // Bắt các lỗi HTTP (như 400, 401, 403) từ chính cuộc gọi làm mới token
                    if (e.code() == 400 || e.code() == 401 || e.code() == 403) {
                        return@runBlocking clearPreferencesAndLogout() // Buộc đăng xuất
                    }
                    // Các lỗi HTTP khác, không thử lại
                    e.printStackTrace() // Log lỗi
                    return@runBlocking null
                } catch (e: IOException) {
                    // Lỗi mạng trong quá trình làm mới token
                    e.printStackTrace() // Log lỗi
                    return@runBlocking null
                } catch (e: Exception) {
                    // Các lỗi chung khác
                    e.printStackTrace() // Log lỗi
                    return@runBlocking null
                }
            }
        }
    }

    // Hàm hỗ trợ để đếm số lần phản hồi trước đó, giúp tránh vòng lặp vô hạn.
    private fun responseCount(response: Response?): Int {
        var result = 1
        var currentResponse = response
        while (currentResponse?.priorResponse != null) {
            currentResponse = currentResponse.priorResponse
            result++
        }
        return result
    }

    // Xóa tất cả tùy chọn người dùng và thông báo để đăng xuất người dùng.
    private suspend fun clearPreferencesAndLogout(): Request? {
        userPreferenceRepository.clearUserSession() // Xóa toàn bộ session

        // TODO: Gửi một sự kiện để thông báo cho lớp UI điều hướng đến màn hình đăng nhập.
        // Ví dụ sử dụng LocalBroadcastManager:
        val intent = Intent("vn.edu.stu.tranthanhsang.healthy_app.ACTION_USER_LOGOUT")
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

        return null
    }
}