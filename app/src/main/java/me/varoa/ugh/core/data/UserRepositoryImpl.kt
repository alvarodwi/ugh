package me.varoa.ugh.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import me.varoa.ugh.core.data.paging.UserDetailPagingSource
import me.varoa.ugh.core.data.paging.UserPagingSource
import me.varoa.ugh.core.data.remote.SafeApiRequest
import me.varoa.ugh.core.data.remote.api.GithubApiService
import me.varoa.ugh.core.data.remote.api.SearchType
import me.varoa.ugh.core.data.remote.json.GithubErrorJson
import me.varoa.ugh.core.domain.model.User
import me.varoa.ugh.core.domain.repository.UserRepository
import me.varoa.ugh.utils.ApiException
import me.varoa.ugh.utils.NoInternetException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: GithubApiService
) : UserRepository, SafeApiRequest() {
    override fun getUsers(username: String): Flow<PagingData<User>> =
        Pager(
            PagingConfig(pageSize = 10, initialLoadSize = 10)
        ) {
            UserPagingSource(api, username)
        }.flow

    override suspend fun getUser(username: String): Flow<Result<User>> = flow {
        try {
            val user: User = apiRequest(
                { api.getUserDetail(username = username) },
                ::decodeErrorJson
            ).toModel()
            emit(Result.success(user))
        } catch (ex: ApiException) {
            emit(Result.failure(ex))
        } catch (ex: NoInternetException) {
            emit(Result.failure(ex))
        }
    }

    override fun getFollowers(username: String): Flow<PagingData<User>> =
        Pager(
            PagingConfig(pageSize = 10, initialLoadSize = 10)
        ) {
            UserDetailPagingSource(api, username, SearchType.SEARCH_FOLLOWERS)
        }.flow

    override fun getFollowing(username: String): Flow<PagingData<User>> =
        Pager(
            PagingConfig(pageSize = 10, initialLoadSize = 10)
        ) {
            UserDetailPagingSource(api, username, SearchType.SEARCH_FOLLOWING)
        }.flow

    private fun decodeErrorJson(str: String): String =
        Json.decodeFromString(GithubErrorJson.serializer(), str).message
}
