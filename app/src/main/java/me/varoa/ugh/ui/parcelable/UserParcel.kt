package me.varoa.ugh.ui.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserParcel(
    val id: Long,
    val username: String,
    val name: String,
    val avatar: String,
    val followersCount: Int,
    val followingCount: Int,
    val createdAt: String
) : Parcelable
