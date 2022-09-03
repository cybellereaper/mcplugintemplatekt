package me.auuki

import org.bukkit.plugin.java.JavaPlugin

class MinecraftPlugin : JavaPlugin() {
    private lateinit var reflectionScanners: ReflectionScanners
    override fun onEnable() {
        reflectionScanners = ReflectionScanners(this)
        reflectionScanners.registerAllListeners()
        reflectionScanners.defineCommands()
    }
}