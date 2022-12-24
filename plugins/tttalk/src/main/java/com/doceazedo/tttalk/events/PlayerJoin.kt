package com.doceazedo.tttalk.events

import com.doceazedo.tttalk.Tttalk
import com.doceazedo.tttalk.utils.IgnoredManager
import com.doceazedo.tttalk.utils.ChatColorManager
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoin : Listener {
    private val motd = Tttalk.instance.config.getStringList("motd")

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        // Load player chat data into memory
        IgnoredManager.loadIgnoredPlayers(e.player.uniqueId)
        ChatColorManager.loadNameColor(e.player)

        // Show custom join message
        e.joinMessage = "§8[§a+§8] ${e.player.displayName} entrou"

        // Show MOTD
        if (motd.isEmpty()) return
        e.player.playSound(e.player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
        for (message in motd) {
            e.player.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
        }
    }
}