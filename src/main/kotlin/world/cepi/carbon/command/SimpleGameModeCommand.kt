package world.cepi.carbon.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.setArgumentCallback

internal class SimpleGameModeCommand(name: String, gameMode: GameMode) : Command(name) {

    init {

        setArgumentCallback(CommandArguments.argPlayer) {sender, arg ->
            sender.sendMessage(Component.text("Player $arg not found", NamedTextColor.RED))
        }

        setDefaultExecutor { sender, _ ->
            if (sender is Player)
                sender.gameMode = gameMode
            else
                sender.sendMessage(Component.text("Usage: /$name <player>"))

        }

        addSyntax(CommandArguments.argPlayer) {sender, args ->
            GameModeCommand.subcommandPlayerSelected(sender, args, gameMode)
        }

    }

    companion object {
        val commandList = arrayOf(
            SimpleGameModeCommand("gmc", GameMode.CREATIVE),
            SimpleGameModeCommand("gms", GameMode.SURVIVAL),
            SimpleGameModeCommand("gma", GameMode.ADVENTURE),
            SimpleGameModeCommand("gmsp", GameMode.SPECTATOR)
        )
    }

}