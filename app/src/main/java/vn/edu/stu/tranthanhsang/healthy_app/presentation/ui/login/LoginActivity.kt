package vn.edu.stu.tranthanhsang.healthy_app.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.stu.tranthanhsang.healthy_app.MainActivity
import vn.edu.stu.tranthanhsang.healthy_app.R
import vn.edu.stu.tranthanhsang.healthy_app.databinding.ActivityLoginBinding
import vn.edu.stu.tranthanhsang.healthy_app.presentation.utils.hide
import vn.edu.stu.tranthanhsang.healthy_app.presentation.utils.show

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addEvent()
        setupObservers()
    }

    private fun addEvent() {
        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.editText?.text.toString()
            val password = binding.edtPassword.editText?.text.toString()
            loginViewModel.login(username, password)
        }
    }
    private fun setupObservers() {
        loginViewModel.loginState.observe(this){ state ->
            when(state) {
                LoginUiState.Initial -> {
                    binding.progressBar.hide()
                    binding.btnLogin.isEnabled = true
                }
                LoginUiState.Loading -> {
                    binding.progressBar.show()
                    binding.btnLogin.isEnabled = false
                }
                is LoginUiState.Success -> {
                    binding.progressBar.hide()
                    binding.btnLogin.isEnabled = true
                    loginViewModel.resetState()
                    state.data.role

                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is LoginUiState.Error -> {
                    binding.progressBar.hide()
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    loginViewModel.resetState()
                }
            }
        }
    }
}