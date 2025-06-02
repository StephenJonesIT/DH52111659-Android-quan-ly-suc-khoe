package vn.edu.stu.tranthanhsang.healthy_app.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vn.edu.stu.tranthanhsang.healthy_app.MainActivity
import vn.edu.stu.tranthanhsang.healthy_app.R
import vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs.UserPreferenceRepository
import vn.edu.stu.tranthanhsang.healthy_app.presentation.ui.login.LoginActivity
import javax.inject.Inject

// Example in a SplashActivity
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // No layout needed, just logic
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
//        lifecycleScope.launch {
//            userPreferencesRepository.accessToken.first().let { token ->
//                val intent = if (token.isNullOrEmpty()) {
//                    Intent(this@SplashActivity, LoginActivity::class.java)
//                } else {
//                    Intent(this@SplashActivity, MainActivity::class.java)
//                }
//                startActivity(intent)
//                finish()
//            }
//        }
    }
}