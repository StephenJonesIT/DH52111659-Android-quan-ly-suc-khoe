package vn.edu.stu.tranthanhsang.healthy_app.features.auth.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.stu.tranthanhsang.healthy_app.databinding.ActivityAuthBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.viewmodels.AuthViewModel
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.adapter.AuthViewPagerAdapter


@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = AuthViewPagerAdapter(this)
        binding.viewPagerAuth.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPagerAuth) { tab, position ->
            tab.text = when (position) {
                0 -> "Log In"
                else -> "Sign Up"
            }
        }.attach()
    }

    fun switchToSignUpTab() {
        binding.viewPagerAuth.currentItem = 1
    }

    fun switchToLoginTab() {
        binding.viewPagerAuth.currentItem = 0
    }
}