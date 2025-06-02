package vn.edu.stu.tranthanhsang.healthy_app.domain.repositories

import vn.edu.stu.tranthanhsang.healthy_app.data.remote.models.LoginResponse
import vn.edu.stu.tranthanhsang.healthy_app.domain.utils.Result

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<LoginResponse>
    suspend fun logout():Result<Unit>
}