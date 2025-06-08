package vn.edu.stu.tranthanhsang.healthy_app.features.user.ui.setup_frofile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.stu.tranthanhsang.healthy_app.MainActivity
import vn.edu.stu.tranthanhsang.healthy_app.R
import vn.edu.stu.tranthanhsang.healthy_app.databinding.FragmentGenderBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.user.uistate.CreateProfileUiState
import vn.edu.stu.tranthanhsang.healthy_app.features.user.viewmodel.ProfileViewModel
import vn.edu.stu.tranthanhsang.healthy_app.utils.ToastUtils
import vn.edu.stu.tranthanhsang.healthy_app.utils.disable
import vn.edu.stu.tranthanhsang.healthy_app.utils.enable
import vn.edu.stu.tranthanhsang.healthy_app.utils.hide
import vn.edu.stu.tranthanhsang.healthy_app.utils.show

@AndroidEntryPoint
class GenderFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentGenderBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEvents()
        observeData()
    }



    private fun addEvents() {
        binding.btnBack.setOnClickListener {
            (requireActivity() as SetupProfileActivity).onclickPreviousButton()
        }
        binding.cardViewFemale.setOnClickListener {
            profileViewModel.updateGender(false)
            binding.btnDone.enable()
            binding.cardViewFemale.setBackgroundResource(R.drawable.card_border_blue)
            binding.cardViewMale.setBackgroundResource(R.drawable.card_border_grey)
        }
        binding.cardViewMale.setOnClickListener {
            profileViewModel.updateGender(true)
            binding.btnDone.enable()
            binding.cardViewMale.setBackgroundResource(R.drawable.card_border_blue)
            binding.cardViewFemale.setBackgroundResource(R.drawable.card_border_grey)
        }
        binding.btnDone.setOnClickListener {
            profileViewModel.createProfile()
        }
    }

    private fun observeData() {
        profileViewModel.profileUiState.observe(viewLifecycleOwner){state->
            when(state){
                is CreateProfileUiState.Initial ->{
                    hideLoading()
                }
                is CreateProfileUiState.Loading -> {
                    isLoading()
                }
                is CreateProfileUiState.Success<*> -> {
                    hideLoading()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
                is CreateProfileUiState.Error -> {
                    hideLoading()
                    state.message?.let { ToastUtils.showToast(requireContext(), it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isLoading(){
        binding.progressBar.show()
        binding.darkOverlay.show()
        binding.btnBack.disable()
        binding.btnDone.disable()
        binding.cardViewMale.disable()
        binding.cardViewFemale.disable()
    }

    private fun hideLoading(){
        binding.progressBar.hide()
        binding.darkOverlay.hide()
        binding.btnBack.enable()
        binding.btnDone.enable()
        binding.cardViewMale.enable()
        binding.cardViewFemale.enable()
    }
}