package vn.edu.stu.tranthanhsang.healthy_app.common.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.stu.tranthanhsang.healthy_app.R
import vn.edu.stu.tranthanhsang.healthy_app.data.local.prefs.UserPreferenceRepository
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.ui.AuthActivity
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
        startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
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