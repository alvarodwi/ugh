package me.varoa.ugh.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.varoa.ugh.core.data.local.dao.FavoriteDao
import me.varoa.ugh.core.data.local.entity.FavoriteEntity
import me.varoa.ugh.core.domain.model.User
import me.varoa.ugh.core.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
) : FavoriteRepository {
    override fun loadFavorites(): Flow<PagingData<User>> =
        Pager(
            PagingConfig(pageSize = 10, initialLoadSize = 10)
        ) {
            dao.pagingSource()
        }.flow.map { data ->
            data.map(FavoriteEntity::toModel)
        }

    override suspend fun addToFavorite(user: User) =
        dao.insert(user.toEntity())

    override suspend fun removeFromFavorite(user: User) =
        dao.delete(user.toEntity())

    override fun isUserInFavorites(username: String): Flow<Int> =
        dao.isItemWithUsernameExists(username)
}
