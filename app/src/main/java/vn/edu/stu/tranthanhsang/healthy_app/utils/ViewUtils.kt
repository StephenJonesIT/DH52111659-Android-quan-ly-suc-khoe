package vn.edu.stu.tranthanhsang.healthy_app.utils

import android.view.View

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.enable(){
    this.isEnabled = true
}

fun View.disable(){
    this.isEnabled = false
}