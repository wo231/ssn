package com.github.maizenalegal.sensor.commands

import com.github.maizenalegal.sensor.identity.Username.getUsername
import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object PlaytimeCmd : SuspendingCommandExecutor {
    override suspend fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return false
        val target = if (args.isEmpty()) sender.displayName else args[0]
        val user = getUsername(target)

        if (user?.firstJoin == null) {
            sender.sendMessage("§cEsse jogador nunca entrou nesse servidor ou não existe.")
            return true
        }

        sender.sendMessage("§e${user.name} §aentrou no servidor em §e${user.firstJoin} §ae jogou por §e${user.playedSeconds} segundos§a.")
        return true
    }
}