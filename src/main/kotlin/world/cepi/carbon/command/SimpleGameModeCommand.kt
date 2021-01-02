package world.cepi.carbon.command

import net.minestom.server.MinecraftServer
import net.minestom.server.chat.ChatColor
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player

class SimpleGameModeCommand(name: String, gameMode: GameMode) : Command(name) {

    init {

        setArgumentCallback({sender, arg, _ ->

            sender.sendMessage("${ChatColor.RED}Player $arg not found")

        }, CommandArguments.argPlayer)

        setDefaultExecutor {sender, _ ->
            if (sender is Player)
                sender.gameMode = gameMode
            else
                sender.sendMessage("Usage: /$name <player>")

        }

        addSyntax({sender, args ->

            GameModeCommand.subcommandPlayerSelected(sender, args, gameMode)

        }, CommandArguments.argPlayer)

    }

    override fun onDynamicWrite(text: String): Array<String> {
        return MinecraftServer.getConnectionManager().onlinePlayers.map { it.username }.toTypedArray()
    }

}