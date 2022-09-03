package me.auuki.inventories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.event.inventory.InventoryType


@Serializable
data class CustomInventory(
    @SerialName("_id") val _id: String,
    val size: Int = InventoryType.CHEST.defaultSize
)
