package me.varoa.ugh.ui.screen.home

import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.ugh.R
import me.varoa.ugh.databinding.FragmentHomeBinding
import me.varoa.ugh.ui.base.BaseEvent.ShowErrorMessage
import me.varoa.ugh.ui.base.BaseFragment
import me.varoa.ugh.ui.ext.snackbar
import me.varoa.ugh.ui.ext.viewBinding
import me.varoa.ugh.ui.screen.home.HomeViewModel.HomeEvent

class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private val binding by viewBinding<FragmentHomeBinding>()
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var userAdapter: UserAdapter

    override fun onStart() {
        super.onStart()
        eventJob = viewModel.events
            .onEach { event ->
                when (event) {
                    is HomeEvent.SearchTriggered, HomeEvent.SearchCleared -> {
                        toggleLoading(false)
                        toggleErrorLayout(true)
                    }
                    is ShowErrorMessage -> {
                        toggleLoading(false)
                        modifyErrorLayout(event.message, "🙏")
                        toggleErrorLayout(true)
                        snackbar("Error : ${event.message}")
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun bindView() {
        binding.toolbar.apply {
            val searchItem = menu.findItem(R.id.action_search)
            val searchView = searchItem.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    toggleLoading(true)
                    val temp = viewModel.currentUsername.value
                    if (query == temp) {
                        viewModel.searchUser("")
                        viewModel.searchUser(temp)
                    } else {
                        viewModel.searchUser(query ?: "")
                    }
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        userAdapter = UserAdapter(
            imageLoader = imageLoader,
            onClick = { user ->
                logcat { "${user.name} clicked!" }
            }
        )
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = userAdapter.withLoadStateHeaderAndFooter(
            header = UserLoadStateAdapter(userAdapter::retry),
            footer = UserLoadStateAdapter(userAdapter::retry)
        )

        binding.swipeRefresh.setOnRefreshListener { refresh() }

        // lifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.users.collectLatest {
                userAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUsername.collectLatest {
                toggleLoading(false)
                binding.tvSubtitle.text = getString(R.string.label_subtitle, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userAdapter.loadStateFlow.map { it.refresh }
                .distinctUntilChanged()
                .collect {
                    if (it is LoadState.NotLoading) {
                        if (userAdapter.itemCount < 1) {
                            if (viewModel.currentUsername.value.isEmpty()) {
                                modifyErrorLayout("Ugh, you need to search an username first", "🙏")
                            } else {
                                modifyErrorLayout("Ugh, your query ended up in zero result", "🤌")
                            }

                            toggleErrorLayout(true)
                        } else {
                            toggleErrorLayout(false)
                        }
                    }
                }
        }
    }

    private fun refresh() {
        toggleErrorLayout(false)

        // then refresh the content
        toggleLoading(true)
        viewModel.onRefresh()
        userAdapter.refresh()
        toggleLoading(false)
    }

    private fun toggleLoading(show: Boolean) {
        binding.recyclerview.isVisible = !show
        binding.swipeRefresh.isRefreshing = show
    }

    private fun modifyErrorLayout(message: String, emoji: String, withRetryButton: Boolean = false) {
        binding.layoutError.apply {
            tvMessage.text = message
            tvEmoji.text = emoji
        }
    }

    private fun toggleErrorLayout(flag: Boolean) {
        binding.layoutError.root.isVisible = flag
        binding.recyclerview.isVisible = !flag
    }
}
