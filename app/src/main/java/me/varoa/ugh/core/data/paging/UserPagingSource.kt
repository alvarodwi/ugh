package me.varoa.ugh.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.varoa.ugh.core.data.remote.api.GithubApiService
import me.varoa.ugh.core.data.remote.json.UserJson
import me.varoa.ugh.core.data.toModel
import me.varoa.ugh.core.domain.model.User
import retrofit2.HttpException
import java.io.IOException

class UserPagingSource(
    private val api: GithubApiService,
    private val username: String
) : PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        try {
            val nextPage = params.key ?: 1
            val response = api.getUsers(query = username, page = nextPage)
            val pagedResponse = response.body()

            val data: List<User>? = pagedResponse?.items?.map(UserJson::toModel)

            return LoadResult.Page(
                data = data.orEmpty(),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (data.isNullOrEmpty()) null else nextPage + 1
            )
        } catch (ex: HttpException) {
            throw IOException(ex)
        } catch (ex: IOException) {
            return LoadResult.Error(ex)
        }
    }
}
