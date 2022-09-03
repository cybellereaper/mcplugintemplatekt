package me.auuki.mobs

import org.bukkit.entity.EntityType
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class CustomMobTest : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val entity = e.player.world.spawnEntity(e.player.location, EntityType.ZOMBIE)
        (entity as Zombie).apply {
            setAI(false)
            isCustomNameVisible = true
            customName = "TestMerchant"
        }
    }
}