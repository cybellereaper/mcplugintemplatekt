package me.auuki

import org.bukkit.plugin.java.JavaPlugin

class MinecraftPlugin : JavaPlugin() {
    private val reflectionScanners: ReflectionScanners = ReflectionScanners(this)

    override fun onEnable() {
        reflectionScanners.allListeners()
        reflectionScanners.defineCommands()
    }
}