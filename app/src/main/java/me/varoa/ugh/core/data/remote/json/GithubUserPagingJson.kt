package me.varoa.ugh.core.data.remote.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubUserPagingJson(
    @SerialName("total_count") val totalCount: Int,
    @SerialName("incomplete_results") val isIncomplete: Boolean,
    val items: List<UserJson>
)
