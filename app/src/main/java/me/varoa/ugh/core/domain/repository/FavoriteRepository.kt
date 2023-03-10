package me.varoa.ugh.core.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.ugh.core.domain.model.User

interface FavoriteRepository {
    fun loadFavorites(): Flow<PagingData<User>>
    suspend fun addToFavorite(user: User): Long
    suspend fun removeFromFavorite(user: User): Int
    fun isUserInFavorites(username: String): Flow<Int>
}
