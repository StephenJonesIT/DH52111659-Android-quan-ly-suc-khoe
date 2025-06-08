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
import vn.edu.stu.tranthanhsang.healthy_app.databinding.FragmentWeightBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.user.viewmodel.ProfileViewModel
import vn.edu.stu.tranthanhsang.healthy_app.utils.disable
import vn.edu.stu.tranthanhsang.healthy_app.utils.enable
import vn.edu.stu.tranthanhsang.healthy_app.utils.hide
import vn.edu.stu.tranthanhsang.healthy_app.utils.show

@AndroidEntryPoint
class WeightFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentWeightBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addViews()
        setupTextWatcher()
    }

    private fun addViews() {
        binding.btnNext.setOnClickListener {
            val weight = binding.edtScale.text.toString()
            profileViewModel.updateWeight(weight.toInt())
            (requireActivity() as SetupProfileActivity).onclickNextButton()
        }
        binding.btnBack.setOnClickListener {
            (requireActivity() as SetupProfileActivity).onclickPreviousButton()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupTextWatcher() {
        binding.edtScale.addTextChangedListener(object: TextWatcher {
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
        val weightInput = text.trim().toIntOrNull() // Chuyển thành số
        if (weightInput == null || text.trim().length > 3) { // Kiểm tra số hợp lệ
            binding.btnNext.disable()
            binding.tvError.show()
            binding.tvError.text = getString(R.string.error_weight_length)
            return
        }
        when{
            weightInput < 30 -> {
                binding.btnNext.disable()
                binding.tvError.show()
                binding.tvError.text = getString(R.string.error_weight_length)
            }
            weightInput > 200 -> {
                binding.btnNext.disable()
                binding.tvError.show()
                binding.tvError.text = getString(R.string.error_weight_length)
            }
            else -> {
                binding.btnNext.enable()
                binding.tvError.hide()
            }
        }
    }
}