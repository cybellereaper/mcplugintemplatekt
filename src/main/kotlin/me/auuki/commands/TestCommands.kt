package me.auuki.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Listener

class TestCommands: Listener {
    @CommandMapping(cmd ="fly", permission = "command.flight")
    fun flyCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) {
        if (sender !is Player) return
        sender.allowFlight = !sender.isFlying
        val content = "You're now setting fly to ${sender.allowFlight}"
        sender.sendMessage(content)
    }
}