package vn.edu.stu.tranthanhsang.healthy_app.features.auth.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.ui.LoginFragment
import vn.edu.stu.tranthanhsang.healthy_app.features.auth.ui.SignUpFragment

class AuthViewPagerAdapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    private val fragments = listOf(LoginFragment(), SignUpFragment())
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}