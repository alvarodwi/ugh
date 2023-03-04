package me.varoa.ugh.core.data.remote.api

import me.varoa.ugh.core.data.remote.json.GithubUserPagingJson
import me.varoa.ugh.core.data.remote.json.UserJson
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    // search user(s) by username
    @GET("search/users")
    suspend fun getUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): Response<GithubUserPagingJson>

    // get user detail
    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): Response<UserJson>

    // get users that is following this username
    @GET("users/{username}/followers")
    suspend fun getUserFollowers(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): Response<List<UserJson>>

    // get users that is followed by this username
    @GET("users/{username}/following")
    suspend fun getUserFollowing(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): Response<List<UserJson>>
}
