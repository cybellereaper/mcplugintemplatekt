package framework

import framework.database.MongoStorage
import framework.inventories.CustomInventory
import org.bukkit.plugin.java.JavaPlugin
import org.litote.kmongo.id.StringId

class MinecraftPlugin : JavaPlugin() {
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
        val reflectionScanners = ReflectionScanners(this)
        reflectionScanners.allListeners()
        reflectionScanners.defineCommands()
    }
}
