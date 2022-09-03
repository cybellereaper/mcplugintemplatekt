package me.auuki

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.auuki.commands.CommandMapping
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.scanners.Scanners.MethodsAnnotated
import org.reflections.scanners.Scanners.SubTypes
import java.lang.reflect.Method

class ReflectionScanners(private val javaPlugin: JavaPlugin) {
    private val pluginManager: PluginManager = javaPlugin.server.pluginManager
    private val packageName: String = MinecraftPlugin::class.java.packageName

    @OptIn(DelicateCoroutinesApi::class)
    private fun executeCommand(
        method: Method?,
        instance: Any?,
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<String>,
    ) = GlobalScope.launch { method?.invoke(instance, sender, cmd, label, args) }

    fun defineCommands() {
        val commands: MutableMap<String, MutableMap<String, Pair<String, Method>>> = mutableMapOf()
        val reflections = Reflections(MinecraftPlugin::class.java, MethodsAnnotated)
        val annotation = reflections.getMethodsAnnotatedWith(CommandMapping::class.java)
        annotation.forEach {
            val command = it.getAnnotation(CommandMapping::class.java).cmd
            val subCommand = it.getAnnotation(CommandMapping::class.java).sub
            val permission = it.getAnnotation(CommandMapping::class.java).permission
            when (commands[command]) {
                null -> commands[command] = mutableMapOf(subCommand to Pair(permission, it))
                else -> commands[command]?.set(subCommand, Pair(permission, it))
            }
            commands.forEach { (t, u) ->
                javaPlugin.getCommand(t)?.setExecutor { sender, command, label, args ->
                    val arguments = when (args.size) {
                        in Int.MIN_VALUE..0 -> ""
                        else -> args[0]
                    }
                    val method = when (u.containsKey(arguments)) {
                        true -> u[arguments]
                        else -> u[""]
                    } as Pair<String, Method>
                    val instance = method.second.declaringClass?.getDeclaredConstructor()?.newInstance()
                    when (method.first.isBlank() ||
                            sender.hasPermission(method.first)
                    ) {
                        true -> executeCommand(method.second, instance, sender, command, label, args)
                        else -> sender.sendMessage("Error: Unable to complete your request!")
                    }
                    true
                }
            }
        }
    }

    fun registerAllListeners() {
        val reflections = Reflections(packageName, MethodsAnnotated, SubTypes)
        val subtypes = reflections.getSubTypesOf(Listener::class.java)
        subtypes.forEach {
            try {
                val listener = it.getDeclaredConstructor().newInstance()
                pluginManager.registerEvents(listener, javaPlugin)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}