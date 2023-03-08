package me.varoa.ugh.core.data.remote.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserJson(
    val id: Long,
    @SerialName("login") val username: String,
    val name: String = "",
    @SerialName("avatar_url") val avatar: String,
    @SerialName("followers") val followersCount: Int = 0,
    @SerialName("following") val followingCount: Int = 0
)
