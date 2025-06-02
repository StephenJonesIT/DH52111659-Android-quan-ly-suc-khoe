package vn.edu.stu.tranthanhsang.healthy_app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs.UserPreferenceRepository
import vn.edu.stu.tranthanhsang.healthy_app.utils.Constants
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthInterceptorModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(userPreferencesRepository : UserPreferenceRepository) : Interceptor {
        return Interceptor { chain ->
            val originRequest = chain.request()
            val accessToken = runBlocking {
                userPreferencesRepository.accessToken.first()
            }
            val requestBuilder = originRequest.newBuilder()
            accessToken?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }
            chain.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Named("auth")
    @Singleton
    fun provideOkHttpClientWithAuth(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor // Inject AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor) // Thêm AuthInterceptor
            .connectTimeout(Constants.TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(Constants.TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(Constants.TIME_OUT.toLong(), TimeUnit.SECONDS)
            .build()
    }
}