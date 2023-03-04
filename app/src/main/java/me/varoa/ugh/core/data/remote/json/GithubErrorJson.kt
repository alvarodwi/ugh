package me.varoa.ugh.core.data.remote.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubErrorJson(
    val message: String,
    @SerialName("documentation_url") val docsUrl: String
)
