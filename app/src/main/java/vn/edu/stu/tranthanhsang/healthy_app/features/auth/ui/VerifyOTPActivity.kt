package vn.edu.stu.tranthanhsang.healthy_app.features.auth.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint // Đảm bảo đã import
import kotlinx.coroutines.runBlocking
import vn.edu.stu.tranthanhsang.healthy_app.MainActivity
import vn.edu.stu.tranthanhsang.healthy_app.R
import vn.edu.stu.tranthanhsang.healthy_app.databinding.ActivityVerifyTokenBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.SendOtpUiState
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.VerifyOtpUiState
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.viewmodels.VerifyOTPViewModel
import vn.edu.stu.tranthanhsang.healthy_app.utils.Constants
import vn.edu.stu.tranthanhsang.healthy_app.utils.ToastUtils
import vn.edu.stu.tranthanhsang.healthy_app.utils.hide
import vn.edu.stu.tranthanhsang.healthy_app.utils.show

@AndroidEntryPoint // Đảm bảo annotation này có mặt nếu bạn dùng Hilt
class VerifyOTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyTokenBinding
    private val viewModel: VerifyOTPViewModel by viewModels()
    private lateinit var editTexts: List<EditText> // Khai báo list EditText ở đây

    private lateinit var email: String
    private lateinit var verifyType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_token)
        binding.verifyTokenViewModel = viewModel // Đảm bảo tên biến trong layout khớp
        binding.lifecycleOwner = this

        // Khởi tạo list editTexts sau khi binding đã được thiết lập
        editTexts = listOf(
            binding.edtOTP1,
            binding.edtOTP2,
            binding.edtOTP3,
            binding.edtOTP4,
            binding.edtOTP5,
            binding.edtOTP6
        )
        getDataFromIntent()
        addEvents()
        observeData()
        setOtpInputListeners() // Đổi tên hàm để rõ ràng hơn
    }

    private fun getDataFromIntent() {
        val intent = getIntent()
        email = intent.getStringExtra(Constants.EMAIL)?:""
        email.let {
            viewModel.setEmailFromIntent(it)
            Log.d("EMAIL", it)
        }

        verifyType = intent.getStringExtra(Constants.VERIFY_TYPE)?:""
        verifyType.let {
            viewModel.isForgotPassword.value = verifyType == Constants.RESET_PASSWORD
        }
    }


    private fun addEvents() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun observeData() {
        viewModel.authState.observe(this){ state ->
            when(state){
                VerifyOtpUiState.Initial -> {
                    binding.progressBar.hide()
                    binding.btnVerify.isEnabled = true
                }

                is VerifyOtpUiState.Loading -> {
                    binding.progressBar.show()
                    binding.btnVerify.isEnabled = false
                }
                is VerifyOtpUiState.Success<*> -> {
                    binding.progressBar.hide()
                    binding.btnVerify.isEnabled = true
                    viewModel.resetState()
                    ToastUtils.showToast(this, state.data.toString())
                    if (!viewModel.isForgotPassword.value!!){
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else{
                        val intent = Intent(this, ResetPasswordActivity::class.java)
                        intent.putExtra(Constants.EMAIL, email)
                        startActivity(intent)
                        finish()
                    }
                }
                is VerifyOtpUiState.Error -> {
                    binding.progressBar.hide()
                    binding.btnVerify.isEnabled = true
                    state.message?.let { ToastUtils.showToast(this, it) }
                    viewModel.resetState()
                }
                else ->{}
            }
        }


        viewModel.sendOtpState.observe(this) {
            when (it) {
                SendOtpUiState.Initial -> {
                    binding.progressBar.hide()
                }
                is SendOtpUiState.Loading -> {
                    binding.progressBar.show()
                }
                is SendOtpUiState.Success<*> -> {
                    binding.progressBar.hide()
                    ToastUtils.showToast(this, it.data.toString())
                }
                is SendOtpUiState.Error -> {
                    binding.progressBar.hide()
                    it.message?.let { it1 -> ToastUtils.showToast(this, it1) }
                }                }

        }
    }

    private fun setOtpInputListeners() { // Đổi tên hàm
        editTexts.forEachIndexed { index, editText ->
            // Truyền ViewModel vào OtpTextWatcher
            editText.addTextChangedListener(OtpTextWatcher(editText, index, editTexts, viewModel))

            // Xử lý sự kiện nhấn phím Backspace (DEL)
            editText.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (editText.text.isEmpty() || editText.selectionStart == 0) {
                        if (index > 0) {
                            editTexts[index - 1].requestFocus()
                            editTexts[index - 1].text?.clear()
                            // RẤT QUAN TRỌNG: Cập nhật ViewModel khi xóa ký tự ở ô trước đó
                            viewModel.updateOtpDigit(index - 1, "")
                        }
                        return@setOnKeyListener true // Đã xử lý sự kiện
                    }
                }
                return@setOnKeyListener false // Cho phép sự kiện mặc định tiếp tục
            }

            // Đảm bảo con trỏ luôn ở cuối khi người dùng chạm vào ô
            editText.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    (view as? EditText)?.setSelection(view.text?.length ?: 0)
                }
            }
        }
    }

    // TextWatcher tùy chỉnh để giao tiếp với ViewModel
    private inner class OtpTextWatcher(
        private val currentEditText: EditText,
        private val currentIndex: Int,
        private val allEditTexts: List<EditText>,
        private val viewModel: VerifyOTPViewModel // Thêm ViewModel vào constructor
    ) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val digit = s?.toString() ?: ""
            // GỌI HÀM updateOtpDigit TẠI ĐÂY
            viewModel.updateOtpDigit(currentIndex, digit)

            // Đảm bảo mỗi ô chỉ chứa 1 ký tự (ngăn chặn paste nhiều ký tự)
            if ((s?.length ?: 0) > 1) {
                // Nếu có nhiều hơn 1 ký tự (ví dụ: paste), chỉ giữ ký tự đầu tiên
                s?.delete(1, s.length)
                // Quan trọng: Gọi lại updateOtpDigit để ViewModel biết chỉ còn 1 ký tự
                viewModel.updateOtpDigit(currentIndex, s.toString())
            }

            // Logic chuyển focus khi thêm ký tự
            if (s?.length == 1) {
                if (currentIndex < allEditTexts.size - 1) {
                    allEditTexts[currentIndex + 1].requestFocus()
                } else if (currentIndex == allEditTexts.size - 1) {
                    // Khi ô cuối cùng được điền, ẩn bàn phím
                    currentEditText.context.hideKeyboard() // Sử dụng hàm mở rộng
                }
            }
        }
    }

    // Hàm mở rộng để ẩn bàn phím (đặt ở ngoài class Activity nếu muốn tái sử dụng)
    fun Context.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    fun onResendOtpClicked(view: android.view.View) {
        // Thực hiện logic gửi lại OTP
        viewModel.resetOtp() // Reset các ô OTP trong ViewModel
        editTexts[0].requestFocus() // Chuyển focus về ô đầu tiên
        ToastUtils.showToast(this, "Đã gửi lại OTP!")
    }
}