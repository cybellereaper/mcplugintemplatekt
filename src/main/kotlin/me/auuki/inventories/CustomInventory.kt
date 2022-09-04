package me.auuki.inventories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack


@Serializable
data class CustomInventory(
    @SerialName("_id") var _id: String,
    val display: String = "Example Inventory",
    val size: Int = InventoryType.CHEST.defaultSize,

)


