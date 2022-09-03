package me.auuki.inventories

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.ItemStack

class CustomInventoryTest : Listener {
    private val customInventory = Bukkit.createInventory(null, InventoryType.CHEST, "CustomInventory")

    /** TODO: This class is terribly unfinished at the moment. **/
    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        if (e.clickedInventory == null) return
        if (e.inventory != customInventory) return
        e.isCancelled = true
    }

    @EventHandler
    fun onEntityInteract(e: PlayerInteractAtEntityEvent) {
       if (e.rightClicked.customName != "TestMerchant") return
       e.player.closeInventory()
       e.player.openInventory(customInventory)
    }


    @EventHandler
    fun onInventoryOpen(e: InventoryOpenEvent) {
        if (e.inventory != customInventory) return
        for (index in 0 until e.inventory.size) {
            val item = e.inventory.getItem(index)
            if (item != null) continue
            e.inventory.setItem(index, ItemStack(Material.BLACK_STAINED_GLASS_PANE))
        }
    }
}