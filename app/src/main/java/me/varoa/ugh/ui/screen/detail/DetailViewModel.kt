package me.varoa.ugh.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.varoa.ugh.core.domain.model.User
import me.varoa.ugh.core.domain.repository.FavoriteRepository
import me.varoa.ugh.core.domain.repository.UserRepository
import me.varoa.ugh.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val user: UserRepository,
    private val favorite: FavoriteRepository,
    private val handle: SavedStateHandle
) : BaseViewModel() {

    private val username = handle.get<String>("username") ?: ""

    private val _detailUser = MutableSharedFlow<User>()
    val detailUser get() = _detailUser.asSharedFlow()

    private val _isFavorite = MutableStateFlow<Boolean>(false)
    val isFavorite get() = _isFavorite.asStateFlow()

    init {
        onRefresh()
    }

    fun onRefresh() {
        checkUserInFavorite()
        viewModelScope.launch {
            user.getUser(username)
                .catch { setErrorMessage(it.message ?: "") }
                .collect { result ->
                    if (result.isSuccess) {
                        _detailUser.emit(result.getOrThrow())
                    } else if (result.isFailure) setErrorMessage(result.exceptionOrNull()?.message)
                }
        }
    }

    private fun checkUserInFavorite() {
        viewModelScope.launch {
            favorite.isUserInFavorites(username).collect {
                _isFavorite.emit(it == 1)
            }
        }
    }

    fun toggleFavorite(user: User) {
        viewModelScope.launch {
            if (isFavorite.value) {
                favorite.removeFromFavorite(user)
            } else {
                favorite.addToFavorite(user)
            }
        }
    }
}
