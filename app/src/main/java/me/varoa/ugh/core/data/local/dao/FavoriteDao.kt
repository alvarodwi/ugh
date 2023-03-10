package me.varoa.ugh.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.varoa.ugh.core.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun pagingSource(): PagingSource<Int, FavoriteEntity>

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE username = :username)")
    fun isItemWithUsernameExists(
        username: String
    ): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: FavoriteEntity): Long

    @Delete
    suspend fun delete(entity: FavoriteEntity): Int
}
