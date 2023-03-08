package me.varoa.ugh.ui.screen.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.varoa.ugh.core.data.remote.api.SearchType.SEARCH_FOLLOWERS
import me.varoa.ugh.core.data.remote.api.SearchType.SEARCH_FOLLOWING
import me.varoa.ugh.ui.screen.detail.list.UserListFragment

class DetailAdapter(
    activity: FragmentActivity,
    private val username: String,
    private val itemsSize: Int
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsSize
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserListFragment.newInstance(username, SEARCH_FOLLOWERS)
            else -> UserListFragment.newInstance(username, SEARCH_FOLLOWING)
        }
    }
}
