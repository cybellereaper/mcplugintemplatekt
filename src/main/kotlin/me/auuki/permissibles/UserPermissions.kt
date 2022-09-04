package me.auuki.permissibles

import me.auuki.database.MongoStorage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.litote.kmongo.id.StringId

class UserPermissions : Listener {
    private val permissionStorage = MongoStorage(Permissions::class.java, "test", "user-permissions")
    private fun create(id: String): Permissions {
        val userStringId = StringId<Permissions>(id)
        return permissionStorage.get(userStringId) ?: run {
            val permissions = Permissions(userStringId.id)
            permissionStorage.insertOrUpdate(userStringId, permissions)
            return permissions
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val permissions = create(e.player.uniqueId.toString())
        println(permissions)
    }
}