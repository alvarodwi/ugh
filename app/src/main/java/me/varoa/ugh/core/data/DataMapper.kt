package me.varoa.ugh.core.data

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

fun User.toJson() =
    UserJson(
        id = id,
        username = username,
        name = name,
        avatar = avatar,
        followersCount = followersCount,
        followingCount = followingCount
    )
