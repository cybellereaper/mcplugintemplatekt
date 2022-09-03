package me.auuki.items

import kotlinx.serialization.Serializable

@Serializable
data class CustomItems(
    val Id: String,
    val display: String
)

@Serializable
data class CustomItemProperties(val Id: String, val map: Map<String, String> = mapOf())

