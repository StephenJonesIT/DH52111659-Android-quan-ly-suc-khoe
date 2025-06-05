package vn.edu.stu.tranthanhsang.healthy_app.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogHelper {
    fun showConfirmDialog(
        context: Context,
        title: String,
        message: String,
        positiveAction: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Có") { _, _ ->
                positiveAction()
            }
            .setNegativeButton("Không", null) // Không làm gì khi chọn "Không"
            .setCancelable(false) // Không cho phép tắt dialog bằng cách nhấp ngoài
            .show()
    }

}