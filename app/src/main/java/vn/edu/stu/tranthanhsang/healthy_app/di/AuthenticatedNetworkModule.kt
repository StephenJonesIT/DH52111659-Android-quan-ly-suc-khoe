package vn.edu.stu.tranthanhsang.healthy_app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs.UserPreferenceRepository
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.api.AuthApiService
import vn.edu.stu.tranthanhsang.healthy_app.data.remote.api.ProfileApiService
import vn.edu.stu.tranthanhsang.healthy_app.utils.Constants
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticatedNetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(userPreferencesRepository: UserPreferenceRepository): Interceptor {
        return Interceptor { chain ->
            val originRequest = chain.request()
            // Lấy access token một cách đồng bộ bằng runBlocking.
            val accessToken = runBlocking {
                userPreferencesRepository.accessToken.first()
            }
            val requestBuilder = originRequest.newBuilder()
            // Thêm header Authorization nếu access token tồn tại.
            accessToken?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }
            chain.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        authApiService: AuthApiService, // AuthApiService này đã sử dụng Retrofit không xác thực
        userPreferenceRepository: UserPreferenceRepository,
        @ApplicationContext context: Context // Cần thiết để gửi broadcast logout
    ): Authenticator {
        return TokenAuthenticator(authApiService, userPreferenceRepository, context)
    }


    @Provides
    @Named("auth")
    @Singleton
    fun provideOkHttpClientWithAuth(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor,
        authenticator: TokenAuthenticator// Inject AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor) // Thêm AuthInterceptor
            .authenticator(authenticator)
            .connectTimeout(Constants.TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(Constants.TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(Constants.TIME_OUT.toLong(), TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Named("auth")
    @Singleton
    fun provideRetrofitWithAuth(
        @Named("auth") okHttpClientWithAuthFactory: OkHttpClient,
        gsonConvertFactory: GsonConverterFactory
    ):Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClientWithAuthFactory)
            .addConverterFactory(gsonConvertFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideProfileApiWithAuth(@Named("auth") retrofit: Retrofit) : ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }
}