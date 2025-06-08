package vn.edu.stu.tranthanhsang.healthy_app.features.user.ui.setup_frofile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.stu.tranthanhsang.healthy_app.databinding.ActivitySetupProfileBinding
import vn.edu.stu.tranthanhsang.healthy_app.features.user.adapter.StepsPagerAdapter
import vn.edu.stu.tranthanhsang.healthy_app.features.user.viewmodel.ProfileViewModel

@AndroidEntryPoint
class SetupProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetupProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPager()
    }

    private fun setupViewPager() {
        val viewPagerAdapter = StepsPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.isUserInputEnabled = false

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {

            }
        })
    }

    fun onclickPreviousButton() {
            if (binding.viewPager.currentItem > 0) {
                binding.viewPager.currentItem -= 1
            }
    }

    fun onclickNextButton() {
            if (binding.viewPager.currentItem < ((binding.viewPager.adapter?.itemCount ?: 0) - 1)) {
                binding.viewPager.currentItem += 1
            }
    }
}