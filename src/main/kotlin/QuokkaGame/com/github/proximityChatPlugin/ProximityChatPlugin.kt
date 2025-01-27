package QuokkaGame.com.github.proximityChatPlugin


import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class ProximityChatPlugin : JavaPlugin(), Listener {

    private lateinit var luckPerms: LuckPerms
    private lateinit var chatFormat: String

    override fun onEnable() {
        // Plugin startup logic
        CommandAPI.onEnable()
        logger.info("ProximityChatPlugin enabled!")

        reloadCommand()
        saveDefaultConfig()
        server.pluginManager.registerEvents(this, this)

        chatFormat = config.getString("format", "<{prefix}{name}{suffix}> {message}").toString()

        try {
            luckPerms = LuckPermsProvider.get()
        } catch (e: Exception) {
            logger.info("§cFailed to get LuckPerms API. Please make sure LuckPerms is installed correctly.")
            e.printStackTrace()
            server.pluginManager.disablePlugins()
            return
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
        CommandAPI.onDisable()
        logger.info("ProximityChatPlugin disabled!")
    }

    @EventHandler
    fun onPlayerChat(event: AsyncChatEvent) {
        val sender = event.player
        val chatRadius = server.viewDistance * 16
        val chatRadiusSquared = chatRadius * chatRadius

        val nearbyPlayers = sender.world.players.filter {
            it != sender && it.location.distanceSquared(sender.location) <= chatRadiusSquared
        }.toMutableList()

        nearbyPlayers.add(sender)

        val user = luckPerms.userManager.getUser(sender.uniqueId)

        val prefix = user?.cachedData?.metaData?.prefix ?: ""
        val suffix = user?.cachedData?.metaData?.suffix ?: ""

        val message = PlainTextComponentSerializer.plainText().serialize(event.message())
        val formattedMessage = formatChat(sender, prefix, suffix, message)

        server.consoleSender.sendMessage(formattedMessage)

        event.renderer{ _, _, _, _ -> Component.text(formattedMessage) }

//        event.viewers().clear()
//        event.viewers().addAll(nearbyPlayers)
        event.viewers().retainAll(nearbyPlayers)
    }

    private fun formatChat(player: Player, prefix: String, suffix: String, message: String): String {
        val displayName = LegacyComponentSerializer.legacySection().serialize(player.displayName())
        return chatFormat
            .replace("{prefix}", prefix)
            .replace("{suffix}", suffix)
            .replace("{name}", player.name)
            .replace("{displayname}", displayName)
            .replace("{message}", message)
    }

    private fun reloadCommand() {
        CommandAPICommand("proximitychat")
            .withPermission("proximitychat.reload")
            .withAliases("procha")
            .withUsage("§3/proximitychat reload")
            .withShortDescription("ProximityChat reload command")
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                player.sendMessage("§3/proximitychat reload")
            })
            .withSubcommand(
                CommandAPICommand("reload")
                    .executesPlayer(PlayerCommandExecutor { player, _ ->
                        reloadConfig()
                        chatFormat = config.getString("format", "<{prefix}{name}{suffix}> {message}").toString()
                        player.sendMessage("§aProximityChat config reloaded!")
                    })
            )
            .register()
    }
}