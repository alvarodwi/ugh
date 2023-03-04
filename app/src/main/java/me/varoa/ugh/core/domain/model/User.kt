package me.varoa.ugh.core.domain.model

data class User(
    val id: Long,
    val username: String,
    val name: String,
    val avatar: String,
    val followersCount: Int,
    val followingCount: Int,
    val createdAt: String
)
