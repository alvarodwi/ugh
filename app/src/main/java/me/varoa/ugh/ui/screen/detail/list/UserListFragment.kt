package me.varoa.ugh.ui.screen.detail.list

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.varoa.ugh.R
import me.varoa.ugh.core.data.remote.api.SearchType
import me.varoa.ugh.databinding.FragmentUserListBinding
import me.varoa.ugh.ui.adapter.UserAdapter
import me.varoa.ugh.ui.base.BaseFragment
import me.varoa.ugh.ui.ext.snackbar
import me.varoa.ugh.ui.ext.viewBinding
import me.varoa.ugh.ui.screen.detail.DetailFragmentDirections

class UserListFragment() : BaseFragment(R.layout.fragment_user_list) {

    companion object {
        fun newInstance(username: String, searchType: SearchType): UserListFragment {
            return UserListFragment().also { fragment ->
                fragment.arguments = Bundle().also {
                    it.putString("username", username)
                    it.putString("search_type", searchType.name)
                }
            }
        }
    }

    private val binding by viewBinding<FragmentUserListBinding>()
    private val viewModel by viewModels<UserListViewModel>()

    private lateinit var userAdapter: UserAdapter

    override fun bindView() {
        val searchType = requireArguments().getString("search_type") ?: SearchType.SEARCH_FOLLOWING.name

        userAdapter = UserAdapter(
            imageLoader = imageLoader,
            onClick = { user ->
                moveToDetail(user.username)
            }
        )

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.users.collectLatest(userAdapter::submitData)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userAdapter.loadStateFlow.map { it.refresh }
                .distinctUntilChanged()
                .collect {
                    if (it is LoadState.NotLoading) {
                        if (userAdapter.itemCount < 1) {
                            modifyErrorLayout(
                                "Ugh, this user has no ${
                                if (searchType == SearchType.SEARCH_FOLLOWING.name) {
                                    "following"
                                } else {
                                    "followers"
                                }
                                }",
                                "🤌"
                            )
                            toggleErrorLayout(true)
                        } else {
                            toggleErrorLayout(false)
                        }
                    } else if (it is LoadState.Error) {
                        modifyErrorLayout(it.error.message ?: "", "🙏")
                        snackbar(it.error.message ?: "")
                    }
                }
        }
    }

    private fun modifyErrorLayout(message: String, emoji: String) {
        binding.layoutError.apply {
            tvMessage.text = message
            tvEmoji.text = emoji
        }
    }

    private fun toggleErrorLayout(flag: Boolean) {
        binding.layoutError.root.isVisible = flag
        binding.recyclerview.isVisible = !flag
    }

    private fun moveToDetail(username: String) {
        findNavController().navigate(
            DetailFragmentDirections.actionDetailSelf(username)
        )
    }
}
