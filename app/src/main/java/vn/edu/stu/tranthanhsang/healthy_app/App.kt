package vn.edu.stu.tranthanhsang.healthy_app

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        Log.d("App","OnCreate")
    }
}