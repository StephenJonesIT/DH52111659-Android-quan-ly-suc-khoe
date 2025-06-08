package vn.edu.stu.tranthanhsang.healthy_app.features.auth.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.stu.tranthanhsang.healthy_app.databinding.ActivityResetPasswordBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.ResetPasswordUiState
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.viewmodels.ResetPasswordViewModel
import vn.edu.stu.tranthanhsang.healthy_app.utils.Constants
import vn.edu.stu.tranthanhsang.healthy_app.utils.DialogHelper
import vn.edu.stu.tranthanhsang.healthy_app.utils.ToastUtils
import vn.edu.stu.tranthanhsang.healthy_app.utils.disable
import vn.edu.stu.tranthanhsang.healthy_app.utils.enable
import vn.edu.stu.tranthanhsang.healthy_app.utils.hide
import vn.edu.stu.tranthanhsang.healthy_app.utils.show

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private val viewModel: ResetPasswordViewModel by viewModels()
    private lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataIntent()
        addEvents()
        observeData()
    }

    private fun getDataIntent() {
        val intent = getIntent()
        email = intent.getStringExtra(Constants.EMAIL)?:""
    }

    private fun addEvents() {
        binding.btnBack.setOnClickListener {
            DialogHelper.showConfirmDialog(
                this,
                "Confirm cancellation",
                "Are you sure you want to cancel the password reset?"
            ) {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }

        binding.btnUpdatePassword.setOnClickListener {
            val newPassword = binding.edtPassword.editText?.text.toString()
            val confirmPassword = binding.edtRePassword.editText?.text.toString()
            viewModel.resetError()
            viewModel.resetPassword(email, newPassword, confirmPassword)
        }
    }

    private fun observeData() {
        viewModel.passwordError.observe(this) { errorMessage ->
            binding.edtPassword.error = errorMessage
        }

        viewModel.confirmPasswordError.observe(this) { errorMessage ->
            binding.edtRePassword.error = errorMessage
        }
        viewModel.resetPasswordUiState.observe(this) { state ->
            when (state) {
                ResetPasswordUiState.Initial -> {
                    binding.progressBar.hide()
                    binding.darkOverlay.hide()
                    binding.btnUpdatePassword.enable()
                }

                is ResetPasswordUiState.Loading -> {
                    isLoading()
                }

                is ResetPasswordUiState.Success<*> -> {
                    stopLoading()
                    viewModel.resetState()
                    ToastUtils.showToast(this, state.data.toString())
                    val intent = Intent(this, AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

                is ResetPasswordUiState.Error -> {
                    stopLoading()
                    state.message?.let { ToastUtils.showToast(this, state.message) }
                    viewModel.resetState()
                }
            }
        }
    }

    private fun isLoading(){
        binding.progressBar.show()
        binding.darkOverlay.show()
        binding.btnUpdatePassword.disable()
        binding.edtPassword.disable()
        binding.edtRePassword.disable()
    }

    private fun stopLoading() {
        binding.progressBar.hide()
        binding.darkOverlay.hide()
        binding.btnUpdatePassword.enable()
        binding.edtPassword.enable()
        binding.edtRePassword.enable()
    }
}