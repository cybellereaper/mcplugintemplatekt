package me.auuki

import me.auuki.database.MongoStorage
import me.auuki.inventories.CustomInventory
import org.bukkit.plugin.java.JavaPlugin
import org.litote.kmongo.id.StringId

class MinecraftPlugin : JavaPlugin() {
    private val reflectionScanners: ReflectionScanners = ReflectionScanners(this)

    override fun onEnable() {
        System.setProperty(
            "org.litote.mongo.test.mapping.service",
            "org.litote.kmongo.jackson.JacksonClassMappingTypeService"
        )
        val inventoryStorage = MongoStorage(CustomInventory::class.java, "test", "inventories")
        val inventoryStringId = StringId<CustomInventory>("testInventory")
        inventoryStorage.get(inventoryStringId) ?: run {
            val inventoryEntity = CustomInventory("testInventory")
            inventoryStorage.insertOrUpdate(inventoryStringId, inventoryEntity)
            inventoryEntity
        }
        reflectionScanners.allListeners()
        reflectionScanners.defineCommands()
    }
}