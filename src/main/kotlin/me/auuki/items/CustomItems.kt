package me.auuki.items

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import java.util.*

@Serializable
data class CustomItems(
    @SerialName("_id") val _id: String = UUID.randomUUID().toString(),
    val material: String = "DIAMOND",
    val display: String = "example_item",
    val lores: ArrayList<String> = arrayListOf("I'm an example item!"),
    val customModel: Int = -1
)






//fun create(custom: CustomItems): ItemStack {
//    val stack = Material.getMaterial(custom.material)?.let { ItemStack(it) }
//    val stackMeta = stack.itemMeta ?: return
//    stackMeta.apply {
//        setDisplayName(custom.display)
//        setCustomModelData(custom.customModel)
//        isUnbreakable = true
//        lore = custom.lores
//    }
//    itemMeta = stackMeta
//}


