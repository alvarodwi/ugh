package me.varoa.ugh.ui.screen.detail

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.ugh.R
import me.varoa.ugh.core.domain.model.User
import me.varoa.ugh.databinding.FragmentDetailBinding
import me.varoa.ugh.ui.base.BaseEvent.ShowErrorMessage
import me.varoa.ugh.ui.base.BaseFragment
import me.varoa.ugh.ui.ext.snackbar
import me.varoa.ugh.ui.ext.viewBinding

class DetailFragment : BaseFragment(R.layout.fragment_detail) {
    private val binding by viewBinding<FragmentDetailBinding>()
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var detailAdapter: DetailAdapter
    private lateinit var _user: User

    override fun onStart() {
        super.onStart()
        eventJob = viewModel.events
            .onEach { event ->
                when (event) {
                    is ShowErrorMessage -> {
                        binding.swipeRefresh.isRefreshing = false
                        logcat { "Error : ${event.message}" }
                        snackbar("Error : ${event.message}")
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun bindView() {
        val tabTitles = resources.getStringArray(R.array.title_detail_tabs)
        val username = requireArguments().getString("username") ?: ""

        detailAdapter = DetailAdapter(requireActivity(), username, tabTitles.size)
        binding.swipeRefresh.isEnabled = false
        binding.swipeRefresh.isRefreshing = true
        binding.toolbar.apply {
            setNavigationOnClickListener { findNavController().popBackStack() }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_favorite -> {
                        if (::_user.isInitialized) {
                            viewModel.toggleFavorite(_user)
                        } else {
                            snackbar("User hadn't been loaded yet")
                        }
                        true
                    }
                    R.id.action_share -> {
                        val shareText = getString(
                            R.string.share_detail,
                            username
                        )
                        Intent(Intent.ACTION_SEND).also {
                            it.type = "text/html"
                            it.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                            it.putExtra(Intent.EXTRA_TEXT, shareText)
                            startActivity(Intent.createChooser(it, "Share via"))
                        }
                        true
                    }
                    else -> false
                }
            }
        }
        binding.viewPager.adapter = detailAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailUser.collectLatest(::loadUser)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFavorite.collectLatest {
                binding.toolbar.menu.getItem(0).icon = ContextCompat.getDrawable(
                    requireContext(),
                    if (it) R.drawable.ic_heart_filled else R.drawable.ic_heart
                )
            }
        }
    }

    private fun loadUser(user: User) {
        _user = user
        binding.swipeRefresh.isRefreshing = false
        binding.tvName.text = user.name
        binding.tvUsername.text = getString(R.string.label_username, user.username)
        binding.tvFollowerFollowing.text =
            getString(
                R.string.label_follower_following,
                user.followersCount,
                user.followingCount
            )
        binding.ivAvatar.apply {
            val imgData = ImageRequest.Builder(this.context)
                .data(user.avatar)
                .target(this)
                .transformations(CircleCropTransformation())
                .allowHardware(true)
                .build()
            imageLoader.enqueue(imgData)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onRefresh()
    }
}
