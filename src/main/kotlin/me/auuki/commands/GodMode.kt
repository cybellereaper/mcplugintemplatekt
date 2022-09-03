package me.auuki.commands

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

class GodMode : Listener {
    private val devMode: Boolean = true

    @EventHandler
    fun onDamage(e: EntityDamageByEntityEvent) {
        if (!devMode) return
        if (e.entity !is Player) return
        e.isCancelled = true
    }

    @EventHandler
    fun onHealthChange(e: EntityRegainHealthEvent) {
        if (!devMode) return
        if (e.entity !is Player) return

        (e.entity as Player).health = 20.0
        e.isCancelled = true
    }

    @EventHandler
    fun onFoodChange(e: FoodLevelChangeEvent) {
        if (!devMode) return
        if (e.entity !is Player) return
        e.entity.foodLevel = 20
        e.isCancelled = true
    }
}