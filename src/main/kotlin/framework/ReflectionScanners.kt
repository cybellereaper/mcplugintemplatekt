package framework

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import framework.commands.CommandMapping
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.reflections.Reflections
import org.reflections.scanners.Scanners.MethodsAnnotated
import org.reflections.scanners.Scanners.SubTypes
import java.lang.reflect.Method

class ReflectionScanners(private val javaPlugin: JavaPlugin): KoinComponent {
    private val pluginManager: PluginManager = javaPlugin.server.pluginManager
    private val packageName: String = MinecraftPlugin::class.java.`package`.name

    private suspend fun executeCommand(
        method: Method,
        instance: Any?,
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<String>
    ) {
        withContext(Dispatchers.IO) {
            method.invoke(instance, sender, cmd, label, args)
        }
    }

    fun defineCommands() {
        val reflections = Reflections(MinecraftPlugin::class.java, MethodsAnnotated)
        val commands = reflections.getMethodsAnnotatedWith(CommandMapping::class.java)
            .groupBy(
                { it.getAnnotation(CommandMapping::class.java).cmd },
                { method ->
                    method.getAnnotation(CommandMapping::class.java).sub to Pair(
                        method.getAnnotation(CommandMapping::class.java).permission, method
                    )
                }
            )
            .mapValues { it.value.toMap() }

        commands.forEach { (command, subCommands) ->
            val executor = createExecutor(subCommands)
            javaPlugin.getCommand(command)?.setExecutor(executor)
        }
    }

    private fun createExecutor(u: Map<String, Pair<String, Method>>): CommandExecutor {
        return CommandExecutor { sender, command, label, args ->
            val arguments = args.getOrNull(0) ?: ""
            val method = u[arguments] ?: u[""]
            if (method != null) {
                val instance = method.second.declaringClass?.getDeclaredConstructor()?.newInstance()
                if (method.first.isBlank() || sender.hasPermission(method.first)) {
                    runBlocking {
                        executeCommand(method.second, instance, sender, command, label, args)
                    }
                } else {
                    sender.sendMessage("Error: Unable to complete your request!")
                }
            }
            true
        }
    }

    fun allListeners() {
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
