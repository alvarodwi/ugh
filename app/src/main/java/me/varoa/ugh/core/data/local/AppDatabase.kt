package me.varoa.ugh.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import me.varoa.ugh.core.data.local.dao.FavoriteDao
import me.varoa.ugh.core.data.local.entity.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val favoriteDao: FavoriteDao
}
