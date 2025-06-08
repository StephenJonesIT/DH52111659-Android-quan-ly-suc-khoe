package vn.edu.stu.tranthanhsang.healthy_app.features.auth.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.stu.tranthanhsang.healthy_app.databinding.ActivityForgotPasswordBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.ForgotPasswordUiState
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.viewmodels.ForgotPasswordViewModel
import vn.edu.stu.tranthanhsang.healthy_app.utils.Constants
import vn.edu.stu.tranthanhsang.healthy_app.utils.ToastUtils
import vn.edu.stu.tranthanhsang.healthy_app.utils.applyTransition
import vn.edu.stu.tranthanhsang.healthy_app.utils.disable
import vn.edu.stu.tranthanhsang.healthy_app.utils.enable
import vn.edu.stu.tranthanhsang.healthy_app.utils.hide
import vn.edu.stu.tranthanhsang.healthy_app.utils.show

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addViews()
        observeData()
    }

    private fun addViews() {
        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.btnSendOtp.setOnClickListener {
            val email = binding.edtEmail.editText?.text.toString()
            forgotPasswordViewModel.resetError()
            forgotPasswordViewModel.forgotPassword(email)
        }
    }

    private fun observeData() {
        forgotPasswordViewModel.emailError.observe(this){
            binding.edtEmail.error = it
        }

        forgotPasswordViewModel.forgotPasswordUiState.observe(this) {
            when(it){
                is ForgotPasswordUiState.Initial -> {
                   stopLoading()
                }
                is ForgotPasswordUiState.Loading -> {
                    isLoading()
                }
                is ForgotPasswordUiState.Success<*> ->{
                    stopLoading()
                    forgotPasswordViewModel.resetState()
                    ToastUtils.showToast(this, it.data.toString())
                    navigateToVerifyOTP()
                }
                is ForgotPasswordUiState.Error -> {
                    stopLoading()
                    it.message?.let { it1 -> ToastUtils.showToast(this, it.message) }
                    forgotPasswordViewModel.resetState()
                }
            }
        }
    }
    private fun isLoading(){
        binding.progressBar.show()
        binding.btnSendOtp.disable()
        binding.edtEmail.disable()
        binding.darkOverlay.show()
    }

    private fun stopLoading(){
        binding.progressBar.hide()
        binding.btnSendOtp.isEnabled = true
        binding.edtEmail.enable()
        binding.darkOverlay.hide()
    }

    private fun navigateToVerifyOTP(){
        val intent = Intent(this, VerifyOTPActivity::class.java)
        intent.putExtra(Constants.EMAIL, forgotPasswordViewModel.emailResponse.value)
        intent.putExtra(Constants.VERIFY_TYPE, Constants.RESET_PASSWORD)
        startActivity(intent)
        applyTransition()
    }
}