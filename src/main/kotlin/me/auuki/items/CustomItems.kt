package me.auuki.items

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CustomItems(
    @SerialName("_id") val _id: String = UUID.randomUUID().toString(),
    val display: String = "example",
)



