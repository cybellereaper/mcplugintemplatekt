package framework

import framework.database.MongoStorage
import framework.inventories.CustomInventory
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.litote.kmongo.id.StringId

class MinecraftPlugin : JavaPlugin(), KoinComponent {

    private val appModule = module {
        single<JavaPlugin> { this@MinecraftPlugin }
        single { ReflectionScanners(get()) }
    }

    private val reflectionScanners: ReflectionScanners by inject()

    override fun onEnable() {
        System.setProperty(
            "org.litote.mongo.test.mapping.service",
            "org.litote.kmongo.jackson.JacksonClassMappingTypeService"
        )

        startKoin {
            modules(appModule)
        }

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

    override fun onDisable() {
        stopKoin()
    }
}
