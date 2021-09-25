package world.cepi.carbon.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import world.cepi.carbon.command.GameModeCommand.subcommandPlayerNotSelected
import world.cepi.kstom.command.kommand.Kommand

internal object GameModeCommand : Kommand({
    fun subcommandPlayerSelected(sender: CommandSender, context: CommandContext, gameMode: GameMode) {
        context.get(CommandArguments.argPlayer).find(sender).forEach { (it as? Player)?.gameMode = gameMode }
    }

    argumentCallback(CommandArguments.argPlayer) {
        sender.sendMessage(Component.text("Player ${exception.input} not found", NamedTextColor.RED))
    }

    argumentCallback(CommandArguments.argGameMode) {
        sender.sendMessage(Component.text("${exception.input} is not a valid gamemode, use <survival/creative/adventure/spectator> or <0-3>.", NamedTextColor.RED))
    }

    default {
        if (sender is Player) {
            sender.sendMessage(Component.text("Usage: /$commandName <survival/creative/adventure/spectator> or <0-3> [<player>]"))
        } else {
            sender.sendMessage(Component.text("Usage: /$commandName <survival/creative/adventure/spectator> or <0-3> <player>"))
        }
    }

    syntax(CommandArguments.argGameMode) {

        subcommandPlayerNotSelected(sender, context.get(CommandArguments.argGameMode), commandName)

    }

    syntax(CommandArguments.argGameModeId) {

        subcommandPlayerNotSelected(sender, context.get(CommandArguments.argGameModeId), commandName)

    }

    syntax(CommandArguments.argGameMode, CommandArguments.argPlayer) {

        subcommandPlayerSelected(sender, context, context.get(CommandArguments.argGameMode))

    }

    syntax(CommandArguments.argGameModeId, CommandArguments.argPlayer) {

        subcommandPlayerSelected(sender, context, context.get(CommandArguments.argGameModeId))

    }


}, "gamemode", "gm") {
    fun subcommandPlayerNotSelected(sender: CommandSender, gameMode: GameMode, commandName: String) {
        if (sender is Player) {
            sender.gameMode = gameMode
        }
        else {
            sender.sendMessage(Component.text("Usage: /$commandName <survival/creative/adventure/spectator> or <0-3> <player>"))
        }
    }
}