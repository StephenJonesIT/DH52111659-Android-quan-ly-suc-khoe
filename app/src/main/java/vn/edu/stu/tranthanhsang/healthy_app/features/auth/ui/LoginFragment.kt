package vn.edu.stu.tranthanhsang.healthy_app.features.auth.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.stu.tranthanhsang.healthy_app.MainActivity
import vn.edu.stu.tranthanhsang.healthy_app.databinding.FragmentLoginBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.LoginUiState
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.viewmodels.AuthViewModel
import vn.edu.stu.tranthanhsang.healthy_app.utils.ToastUtils
import vn.edu.stu.tranthanhsang.healthy_app.utils.applyTransition
import vn.edu.stu.tranthanhsang.healthy_app.utils.hide
import vn.edu.stu.tranthanhsang.healthy_app.utils.show

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEvent()
        observeData()
    }

    private fun addEvent() {
        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.editText?.text.toString()
            val password = binding.edtPassword.editText?.text.toString()
            authViewModel.resetErrors()
            authViewModel.login(username, password)
        }

        binding.txtSignUp.setOnClickListener {
            (requireActivity() as AuthActivity).switchToSignUpTab()
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(requireContext(), ForgotPasswordActivity::class.java))
        }
    }

    private fun observeData() {
        authViewModel.usernameError.observe(viewLifecycleOwner) { errorMessage ->
            binding.edtUsername.error = errorMessage
        }

        authViewModel.passwordError.observe(viewLifecycleOwner) { errorMessage ->
            binding.edtPassword.error = errorMessage
        }

        authViewModel.generalError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let { ToastUtils.showToast(requireContext(), errorMessage) }
        }

        authViewModel.authState.observe(viewLifecycleOwner){ state ->
            when(state) {
                LoginUiState.Initial -> {
                    binding.progressBar.hide()
                    binding.btnLogin.isEnabled = true
                }
                is LoginUiState.Loading -> {
                    binding.progressBar.show()
                    binding.btnLogin.isEnabled = false
                }
                is LoginUiState.Success<*> -> {
                    binding.progressBar.hide()
                    binding.btnLogin.isEnabled = true
                    authViewModel.resetLoginState()
                    Log.d("LOGIN", "observeData: ${state.data}")
                    ToastUtils.showToast(requireContext(), "Đăng nhập thành công")
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().applyTransition()
                    requireActivity().finish()
                }
                is LoginUiState.Error -> {
                    binding.progressBar.hide()
                    binding.btnLogin.isEnabled = true
                    state.message?.let { ToastUtils.showToast(requireContext(), it) }
                    authViewModel.resetLoginState()
                }

                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}