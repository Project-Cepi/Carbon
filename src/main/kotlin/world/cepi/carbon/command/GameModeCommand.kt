package world.cepi.carbon.command

import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Arguments
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import world.cepi.carbon.CommandArguments

class GameModeCommand : Command("gamemode", "gm") {

    init {
        setArgumentCallback({sender, arg, _ ->

            sender.sendMessage("§cPlayer $arg not found")

        }, CommandArguments.argPlayer)

        setArgumentCallback({sender, arg, _ ->

            sender.sendMessage("§c$arg is not a valid gamemode, use <survival/creative/adventure/spectator> or <0-3>.")

        }, CommandArguments.argGameMode)

        setDefaultExecutor { sender, _ ->
            if (sender is Player) {
                sender.sendMessage("Usage: /$name <survival/creative/adventure/spectator> or <0-3> [<player>]")
            } else {
                sender.sendMessage("Usage: /$name <survival/creative/adventure/spectator> or <0-3> <player>")
            }
        }

        addSyntax({ sender, args ->

            subcommandPlayerNotSelected(sender, GameMode.valueOf(args.getWord("gameMode").toUpperCase()))

        }, CommandArguments.argGameMode)

        addSyntax({ sender, args ->

            subcommandPlayerNotSelected(sender, GameMode.values()[args.getInteger("gameModeId")])

        }, CommandArguments.argGameModeId)

        addSyntax({ sender, args ->

            subcommandPlayerSelected(sender, args, GameMode.valueOf(args.getWord("gameMode").toUpperCase()))

        }, CommandArguments.argGameMode, CommandArguments.argPlayer)

        addSyntax({ sender, args ->

            subcommandPlayerSelected(sender, args, GameMode.values()[args.getInteger("gameModeId")])

        }, CommandArguments.argGameModeId, CommandArguments.argPlayer)

    }

    override fun onDynamicWrite(text: String): Array<String> {
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
                sender.sendMessage("§cPlayer ${args.getWord("player")} not found")
                return
            }

            player.gameMode = gameMode
        }
    }

}