package me.varoa.ugh.core.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.ugh.core.domain.model.User

interface UserRepository {
    fun getUsers(username: String): Flow<PagingData<User>>
    suspend fun getUser(username: String): Flow<Result<User>>
    fun getFollowers(username: String): Flow<PagingData<User>>
    fun getFollowing(username: String): Flow<PagingData<User>>
}
