package me.varoa.ugh.core.data

import me.varoa.ugh.core.data.remote.json.UserJson
import me.varoa.ugh.core.domain.model.User
import me.varoa.ugh.ui.parcelable.UserParcel

fun UserJson.toModel() =
    User(
        id = id,
        username = username,
        name = name,
        avatar = avatar,
        followersCount = followersCount,
        followingCount = followingCount,
        createdAt = createdAt
    )

fun User.parcelize() =
    UserParcel(
        id = id,
        username = username,
        name = name,
        avatar = avatar,
        followersCount = followersCount,
        followingCount = followingCount,
        createdAt = createdAt
    )
