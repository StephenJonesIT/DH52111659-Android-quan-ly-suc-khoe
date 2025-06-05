package vn.edu.stu.tranthanhsang.healthy_app.features.auth.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.stu.tranthanhsang.healthy_app.databinding.FragmentSignUpBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.uistate.RegisterUiState
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.viewmodels.AuthViewModel
import vn.edu.stu.tranthanhsang.healthy_app.utils.ToastUtils
import vn.edu.stu.tranthanhsang.healthy_app.utils.applyTransition
import vn.edu.stu.tranthanhsang.healthy_app.utils.hide
import vn.edu.stu.tranthanhsang.healthy_app.utils.show

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding?= null
    val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEvents()
        observeData()
    }

    private fun addEvents() {
        binding.txtLogIn.setOnClickListener {
            (requireActivity() as AuthActivity).switchToLoginTab()
        }

        binding.btnSignup.setOnClickListener {
//            val username = binding.edtUsername.editText?.text.toString()
//            val password = binding.edtPassword.editText?.text.toString()
//            val confirmPassword = binding.edtConfirmPassword.editText?.text.toString()
//            authViewModel.resetErrors()
//            authViewModel.register(username, password, confirmPassword)
            startActivity(Intent(requireContext(), VerifyOTPActivity::class.java))
        }
    }

    private fun observeData() {
        authViewModel.usernameError.observe(viewLifecycleOwner) {state ->
            binding.edtUsername.error = state
        }

        authViewModel.passwordError.observe(viewLifecycleOwner) {state ->
            binding.edtPassword.error = state
        }

        authViewModel.confirmPasswordError.observe(viewLifecycleOwner) {state ->
            binding.edtConfirmPassword.error = state
        }

        authViewModel.registerState.observe(viewLifecycleOwner) { state ->
            when (state){
                RegisterUiState.Initial -> {
                    binding.progressBar.hide()
                    binding.btnSignup.isEnabled = true
                }
                RegisterUiState.Loading -> {
                    binding.progressBar.show()
                    binding.btnSignup.isEnabled = false
                }
                is RegisterUiState.Success<*> -> {
                    binding.progressBar.hide()
                    binding.btnSignup.isEnabled = true
                    authViewModel.resetRegisterState()
                    ToastUtils.showToast(requireContext(), "Đăng ký thành công")
                    startActivity(Intent(requireContext(), VerifyOTPActivity::class.java))
                    requireActivity().applyTransition()
                }
                is RegisterUiState.Error -> {
                    binding.progressBar.hide()
                    binding.btnSignup.isEnabled = true
                    state.message?.let { ToastUtils.showToast(requireContext(), it) }
                    authViewModel.resetRegisterState()
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}