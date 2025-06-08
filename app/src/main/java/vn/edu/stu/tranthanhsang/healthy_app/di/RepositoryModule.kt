// di/RepositoryModule.kt
package vn.edu.stu.tranthanhsang.healthy_app.di // Đảm bảo đúng package

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import vn.edu.stu.tranthanhsang.healthy_app.data.repositories.AuthRepositoryImpl
import vn.edu.stu.tranthanhsang.healthy_app.data.repositories.ProfileRepositoryImpl
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.AuthRepository
import vn.edu.stu.tranthanhsang.healthy_app.domain.repositories.ProfileRepository


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule { // PHẢI LÀ abstract class

    @Binds // SỬ DỤNG @Binds cho các interface
    @Singleton // Scope phải khớp với AuthRepositoryImpl (nếu AuthRepositoryImpl là Singleton)
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl // Hilt sẽ tự động cung cấp AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository
}

