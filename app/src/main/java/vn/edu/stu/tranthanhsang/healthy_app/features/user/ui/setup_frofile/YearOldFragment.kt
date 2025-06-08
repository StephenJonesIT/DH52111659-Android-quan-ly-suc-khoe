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
import vn.edu.stu.tranthanhsang.healthy_app.databinding.FragmentYearOldBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.user.viewmodel.ProfileViewModel
import vn.edu.stu.tranthanhsang.healthy_app.utils.disable
import vn.edu.stu.tranthanhsang.healthy_app.utils.enable
import vn.edu.stu.tranthanhsang.healthy_app.utils.hide
import vn.edu.stu.tranthanhsang.healthy_app.utils.show
import java.time.Year

@AndroidEntryPoint
class YearOldFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentYearOldBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYearOldBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEvents()
        setupTextWatcher()
    }

    private fun addEvents() {
        binding.completeButton.setOnClickListener {
            val yearBirth = binding.edtYearBirth.text.toString()
            profileViewModel.updateYearOfBirth(yearBirth.toInt())
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
        binding.edtYearBirth.addTextChangedListener(object: TextWatcher {
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
        val yearInput = text.trim().toIntOrNull() // Chuyển thành số
        if (yearInput == null || text.trim().length != 4) { // Kiểm tra số hợp lệ
            binding.completeButton.disable()
            binding.tvError.show()
            binding.tvError.text = getString(R.string.error_year_length)
            return
        }

        val resultTotalAge = totalYearOld(yearInput) // Truyền năm sinh thay vì độ dài
        when {
            resultTotalAge < 18 -> {
                binding.completeButton.disable()
                binding.tvError.show()
                binding.tvError.text = getString(R.string.error_year_old)
            }
            resultTotalAge > 120 -> {
                binding.completeButton.disable()
                binding.tvError.show()
                binding.tvError.text = getString(R.string.error_year_length)
            }
            else -> {
                binding.completeButton.enable()
                binding.tvError.hide()
            }
        }
    }

    private fun totalYearOld(yearInput: Int):Int{
        val yearCurrent = Year.now().value
        return yearCurrent - yearInput
    }
}