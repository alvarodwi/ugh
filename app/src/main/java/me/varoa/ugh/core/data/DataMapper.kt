package me.varoa.ugh.core.data

import me.varoa.ugh.core.data.local.entity.FavoriteEntity
import me.varoa.ugh.core.data.remote.json.UserJson
import me.varoa.ugh.core.domain.model.User

fun UserJson.toModel() =
    User(
        id = id,
        username = username,
        name = name ?: "",
        avatar = avatar,
        followersCount = followersCount,
        followingCount = followingCount
    )

fun User.toEntity() =
    FavoriteEntity(
        username = username,
        avatar = avatar
    )

fun FavoriteEntity.toModel() =
    User(
        id = 0L,
        username = username,
        name = "",
        avatar = avatar,
        followersCount = 0,
        followingCount = 0
    )
