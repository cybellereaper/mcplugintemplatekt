package me.auuki

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.scanners.Scanners

object ListenerReflect {
    fun defineListeners(javaPlugin: JavaPlugin) {
        val pluginManager = javaPlugin.server.pluginManager
        val reflects = Reflections("me", Scanners.MethodsAnnotated, Scanners.SubTypes)
        reflects.getSubTypesOf(Listener::class.java).forEach {
            try {
                javaPlugin.logger.info(it.toString())
                val listener = it.getDeclaredConstructor().newInstance()
                pluginManager.registerEvents(listener, javaPlugin)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}