package world.cepi.carbon.command

import net.minestom.server.MinecraftServer
import net.minestom.server.chat.ChatColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Arguments
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import world.cepi.kstom.addSyntax
import world.cepi.kstom.setArgumentCallback

class GameModeCommand : Command("gamemode", "gm") {

    init {
        setArgumentCallback(CommandArguments.argPlayer) {sender, arg ->
            sender.sendMessage("${ChatColor.RED}Player $arg not found")
        }

        setArgumentCallback(CommandArguments.argGameMode) {sender, arg ->
            sender.sendMessage("${ChatColor.RED}$arg is not a valid gamemode, use <survival/creative/adventure/spectator> or <0-3>.")
        }

        setDefaultExecutor { sender, _ ->
            if (sender is Player) {
                sender.sendMessage("Usage: /$name <survival/creative/adventure/spectator> or <0-3> [<player>]")
            } else {
                sender.sendMessage("Usage: /$name <survival/creative/adventure/spectator> or <0-3> <player>")
            }
        }

        addSyntax(CommandArguments.argGameMode) { sender, args ->

            subcommandPlayerNotSelected(sender, GameMode.valueOf(args.getWord("gameMode").toUpperCase()))

        }

        addSyntax(CommandArguments.argGameModeId) { sender, args ->

            subcommandPlayerNotSelected(sender, GameMode.values()[args.getInteger("gameModeId")])

        }

        addSyntax(CommandArguments.argGameMode, CommandArguments.argPlayer) { sender, args ->

            subcommandPlayerSelected(sender, args, GameMode.valueOf(args.getWord("gameMode").toUpperCase()))

        }

        addSyntax(CommandArguments.argGameModeId, CommandArguments.argPlayer) { sender, args ->

            subcommandPlayerSelected(sender, args, GameMode.values()[args.getInteger("gameModeId")])

        }

    }

    override fun onDynamicWrite(sender: CommandSender, text: String): Array<out String> {
        return MinecraftServer.getConnectionManager().onlinePlayers.map { it.username }.toTypedArray()
    }

    private fun subcommandPlayerNotSelected(sender: CommandSender, gameMode: GameMode) {
        if (sender is Player) {
            sender.gameMode = gameMode
        }
        else {
            sender.sendMessage("Usage: /$name <survival/creative/adventure/spectator> or <0-3> <player>")
        }
    }

    companion object {
        fun subcommandPlayerSelected(sender: CommandSender, args: Arguments, gameMode: GameMode) {
            val player = MinecraftServer.getConnectionManager().getPlayer(args.getWord("player"))

            // Basically never happens since there is a check for that in the command argument callback.
            if (player == null) {
                sender.sendMessage("${ChatColor.RED}Player ${args.getWord("player")} not found")
                return
            }

            player.gameMode = gameMode
        }
    }

}