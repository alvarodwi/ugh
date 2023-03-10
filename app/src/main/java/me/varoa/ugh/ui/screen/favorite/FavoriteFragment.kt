package me.varoa.ugh.ui.screen.favorite

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
import me.varoa.ugh.databinding.FragmentFavoriteBinding
import me.varoa.ugh.ui.adapter.UserAdapter
import me.varoa.ugh.ui.adapter.UserLoadStateAdapter
import me.varoa.ugh.ui.base.BaseFragment
import me.varoa.ugh.ui.ext.snackbar
import me.varoa.ugh.ui.ext.viewBinding

class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {
    private val binding by viewBinding<FragmentFavoriteBinding>()
    private val viewModel by viewModels<FavoriteViewModel>()

    private lateinit var userAdapter: UserAdapter

    override fun bindView() {
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        userAdapter = UserAdapter(
            imageLoader = imageLoader,
            onClick = { user ->
                moveToDetail(user.username)
            }
        )

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter.withLoadStateHeaderAndFooter(
                header = UserLoadStateAdapter(userAdapter::retry),
                footer = UserLoadStateAdapter(userAdapter::retry)
            )
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
                                "Ugh, you don't have any favorites yet",
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
            FavoriteFragmentDirections.actionFavoriteToDetail(username)
        )
    }
}
