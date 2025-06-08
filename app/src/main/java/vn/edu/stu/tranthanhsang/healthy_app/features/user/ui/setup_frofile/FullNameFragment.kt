package vn.edu.stu.tranthanhsang.healthy_app.features.user.ui.setup_frofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.stu.tranthanhsang.healthy_app.R
import vn.edu.stu.tranthanhsang.healthy_app.databinding.FragmentFullNameBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.user.viewmodel.ProfileViewModel
import vn.edu.stu.tranthanhsang.healthy_app.utils.disable
import vn.edu.stu.tranthanhsang.healthy_app.utils.enable
import vn.edu.stu.tranthanhsang.healthy_app.utils.hide
import vn.edu.stu.tranthanhsang.healthy_app.utils.show

@AndroidEntryPoint
class FullNameFragment : Fragment() {
    private var _binding: FragmentFullNameBinding ?= null
    val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEvents()
        setupTextWatcher()
    }

    private fun addEvents() {
        binding.completeButton.setOnClickListener {
            val fullName = binding.edtFullName.text.toString()
            profileViewModel.updateFullName(fullName)
            (requireActivity() as SetupProfileActivity).onclickNextButton()
        }
    }

    private fun setupTextWatcher() {
        binding.edtFullName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                validateInput(s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun validateInput(text: String) {
        if (text.trim().length >= 3) { // Example validation: input must be at least 3 characters long
            binding.completeButton.enable()
            binding.tvError.hide() // Hide error message if input is valid
        } else {
            binding.completeButton.disable()
            binding.tvError.show() // Show error message
            binding.tvError.text = getString(R.string.error_name_length) // Set error message text
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}