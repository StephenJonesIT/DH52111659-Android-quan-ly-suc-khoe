package vn.edu.stu.tranthanhsang.healthy_app.features.user.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.edu.stu.tranthanhsang.healthy_app.features.user.ui.setup_frofile.FullNameFragment
import vn.edu.stu.tranthanhsang.healthy_app.features.user.ui.setup_frofile.GenderFragment
import vn.edu.stu.tranthanhsang.healthy_app.features.user.ui.setup_frofile.HeightFragment
import vn.edu.stu.tranthanhsang.healthy_app.features.user.ui.setup_frofile.WeightFragment
import vn.edu.stu.tranthanhsang.healthy_app.features.user.ui.setup_frofile.YearOldFragment

class StepsPagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FullNameFragment()
            1 -> YearOldFragment()
            2 -> WeightFragment()
            3 -> HeightFragment()
            4 -> GenderFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}