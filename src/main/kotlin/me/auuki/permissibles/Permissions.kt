package me.auuki.permissibles

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Permissions(
    @SerialName("_id") val _id: String,
    val permissions: Set<String> = setOf(),
    val inheritance: Set<String> = setOf()
)

