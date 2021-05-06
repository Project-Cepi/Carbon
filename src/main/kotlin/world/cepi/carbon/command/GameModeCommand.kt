package world.cepi.carbon.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.setArgumentCallback

internal object GameModeCommand : Command("gamemode", "gm") {

    init {
        setArgumentCallback(CommandArguments.argPlayer) { sender, arg ->
            sender.sendMessage(Component.text("Player $arg not found", NamedTextColor.RED))
        }

        setArgumentCallback(CommandArguments.argGameMode) { sender, arg ->
            sender.sendMessage(Component.text("$arg is not a valid gamemode, use <survival/creative/adventure/spectator> or <0-3>.", NamedTextColor.RED))
        }

        setDefaultExecutor { sender, _ ->
            if (sender is Player) {
                sender.sendMessage(Component.text("Usage: /$name <survival/creative/adventure/spectator> or <0-3> [<player>]"))
            } else {
                sender.sendMessage(Component.text("Usage: /$name <survival/creative/adventure/spectator> or <0-3> <player>"))
            }
        }

        addSyntax(CommandArguments.argGameMode) { sender, args ->

            subcommandPlayerNotSelected(sender, args.get(CommandArguments.argGameMode))

        }

        addSyntax(CommandArguments.argGameModeId) { sender, args ->

            subcommandPlayerNotSelected(sender, args.get(CommandArguments.argGameModeId))

        }

        addSyntax(CommandArguments.argGameMode, CommandArguments.argPlayer) { sender, args ->

            subcommandPlayerSelected(sender, args, args.get(CommandArguments.argGameMode))

        }

        addSyntax(CommandArguments.argGameModeId, CommandArguments.argPlayer) { sender, args ->

            subcommandPlayerSelected(sender, args, args.get(CommandArguments.argGameModeId))

        }

    }

    private fun subcommandPlayerNotSelected(sender: CommandSender, gameMode: GameMode) {
        if (sender is Player) {
            sender.gameMode = gameMode
        }
        else {
            sender.sendMessage(Component.text("Usage: /$name <survival/creative/adventure/spectator> or <0-3> <player>"))
        }
    }

    fun subcommandPlayerSelected(sender: CommandSender, context: CommandContext, gameMode: GameMode) {
        context.get(CommandArguments.argPlayer).find(sender).forEach { (it as? Player)?.gameMode = gameMode }
    }


}