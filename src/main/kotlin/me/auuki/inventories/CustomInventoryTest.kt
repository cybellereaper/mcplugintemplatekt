package me.auuki.inventories

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack



class CustomInventoryTest : Listener {
    private val customInventory = Bukkit.createInventory(null, InventoryType.CHEST, "CustomInventory")

    @EventHandler
    fun onClick(e: PlayerInteractEvent) {
        e.player.closeInventory()
        e.player.openInventory(customInventory)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        if (e.clickedInventory == null) return
        if (e.inventory != customInventory) return
        e.isCancelled = true
    }

    @EventHandler
    fun onInventoryOpen(e: InventoryOpenEvent) {
        e.inventory.setItem(5, ItemStack(Material.BARRIER))
        if (e.inventory != customInventory) return
        for (index in 0 until e.inventory.size) {
            val item = e.inventory.getItem(index)
            if (item != null) continue
            e.inventory.setItem(index, ItemStack(Material.BLACK_STAINED_GLASS_PANE))
        }
    }
}