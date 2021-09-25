package world.cepi.carbon.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import world.cepi.kstom.command.kommand.Kommand

internal class SimpleGameModeCommand(name: String, gameMode: GameMode) : Kommand({

    argumentCallback(CommandArguments.argPlayer) {
        sender.sendMessage(Component.text("Player ${exception.input} not found", NamedTextColor.RED))
    }

    default {
        if (sender is Player)
            player.gameMode = gameMode
        else
            sender.sendMessage(Component.text("Usage: /$name <player>"))

    }

    syntax(CommandArguments.argPlayer) {
        GameModeCommand.subcommandPlayerNotSelected(sender, gameMode, context.commandName)
    }

}, name) {
    companion object {
        val commandList = arrayOf(
            SimpleGameModeCommand("gmc", GameMode.CREATIVE),
            SimpleGameModeCommand("gms", GameMode.SURVIVAL),
            SimpleGameModeCommand("gma", GameMode.ADVENTURE),
            SimpleGameModeCommand("gmsp", GameMode.SPECTATOR)
        )
    }
}