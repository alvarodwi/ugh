package me.varoa.ugh.ui.screen.detail.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import me.varoa.ugh.core.data.remote.api.SearchType
import me.varoa.ugh.core.domain.model.User
import me.varoa.ugh.core.domain.repository.UserRepository
import me.varoa.ugh.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val user: UserRepository,
    private val handle: SavedStateHandle
) : BaseViewModel() {
    private val username = handle.get<String>("username") ?: ""
    private val searchType = handle.get<String>("search_type") ?: ""

    val users: Flow<PagingData<User>>
        get() = when (searchType) {
            SearchType.SEARCH_FOLLOWERS.name -> user.getFollowers(username)
            else -> user.getFollowing(username)
        }.cachedIn(viewModelScope)
}
