package com.example.ECommerceApp.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class ViewPagerAdapter(
    list: ArrayList<Fragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val viewPager: ViewPager2,
    ) : FragmentStateAdapter(fm, lifecycle) {
    val fragmentList: ArrayList<Fragment> = list
    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    createFragment(0)
                }
            }
        })
    }
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = fragmentList[position]

        // animate the fragment's contents
        fragment.view?.apply {
            alpha = 0f
            translationY = 100f
            animate()
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(100)
                .start()
        }

        return fragment
    }

}