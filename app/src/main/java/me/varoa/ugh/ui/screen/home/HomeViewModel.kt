package me.varoa.ugh.ui.screen.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import me.varoa.ugh.core.domain.model.User
import me.varoa.ugh.core.domain.repository.UserRepository
import me.varoa.ugh.ui.base.BaseEvent
import me.varoa.ugh.ui.base.BaseViewModel
import me.varoa.ugh.ui.screen.home.HomeViewModel.HomeEvent.SearchCleared
import me.varoa.ugh.ui.screen.home.HomeViewModel.HomeEvent.SearchTriggered
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val user: UserRepository
) : BaseViewModel() {
    sealed class HomeEvent : BaseEvent() {
        object SearchCleared : BaseEvent()
        object SearchTriggered : BaseEvent()
    }

    private val _currentUsername = MutableStateFlow<String>("")
    val currentUsername = _currentUsername.asStateFlow()

    val users: Flow<PagingData<User>>
        get() = _currentUsername.flatMapLatest { it ->
            if (it.isEmpty()) {
                sendNewEvent(SearchCleared)
            } else {
                sendNewEvent(SearchTriggered)
            }
            user.getUsers(it)
        }.cachedIn(viewModelScope)

    fun searchUser(username: String) {
        viewModelScope.launch {
            _currentUsername.emit(username)
        }
    }

    fun onRefresh() {
        val temp = currentUsername.value
        searchUser("")
        searchUser(temp)
    }
}
