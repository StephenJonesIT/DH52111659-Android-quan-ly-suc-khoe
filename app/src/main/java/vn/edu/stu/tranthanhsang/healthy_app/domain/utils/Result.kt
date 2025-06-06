package vn.edu.stu.tranthanhsang.healthy_app.domain.utils

sealed class Result <out R>{
    data class Success<out R>(val data: R): Result<R>()
    data class Error(val exception: Any, val message: String? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}