package me.auuki.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestCommands {

    @CommandMapping(cmd = "fly", permission = "command.flight")
    fun flyCommandexecuteCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) {
        if (sender !is Player) return
        sender.allowFlight = !sender.isFlying
        val flying = if (sender.allowFlight) "&aYou are now currently flying!" else "&4You aren't able to fly anymore!"
        sender.sendMessage(flying)
    }
}