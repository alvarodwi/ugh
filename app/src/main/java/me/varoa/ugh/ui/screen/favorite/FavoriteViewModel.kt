package me.varoa.ugh.ui.screen.favorite

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import me.varoa.ugh.core.domain.model.User
import me.varoa.ugh.core.domain.repository.FavoriteRepository
import me.varoa.ugh.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favorite: FavoriteRepository
) : BaseViewModel() {
    val users: Flow<PagingData<User>>
        get() = favorite.loadFavorites().cachedIn(viewModelScope)
}
