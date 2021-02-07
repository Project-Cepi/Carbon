package world.cepi.carbon.command

import net.minestom.server.MinecraftServer
import net.minestom.server.chat.ChatColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.setArgumentCallback

class SimpleGameModeCommand(name: String, gameMode: GameMode) : Command(name) {

    init {

        setArgumentCallback(CommandArguments.argPlayer) {sender, arg ->
            sender.sendMessage("${ChatColor.RED}Player $arg not found")
        }

        setDefaultExecutor { sender, _ ->
            if (sender is Player)
                sender.gameMode = gameMode
            else
                sender.sendMessage("Usage: /$name <player>")

        }

        addSyntax(CommandArguments.argPlayer) {sender, args ->
            GameModeCommand.subcommandPlayerSelected(sender, args, gameMode)
        }

    }

}