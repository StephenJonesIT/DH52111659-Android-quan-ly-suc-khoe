package vn.edu.stu.tranthanhsang.healthy_app.utils

import android.app.Activity
import android.os.Build

fun Activity.applyTransition(openTransition: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34+
        overrideActivityTransition(
            if (openTransition) Activity.OVERRIDE_TRANSITION_OPEN else Activity.OVERRIDE_TRANSITION_CLOSE,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    } else {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
