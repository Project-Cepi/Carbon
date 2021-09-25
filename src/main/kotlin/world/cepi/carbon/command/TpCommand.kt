package world.cepi.carbon.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import world.cepi.carbon.command.TpCommand.teleportTo
import world.cepi.kstom.command.kommand.Kommand

object TpCommand : Kommand({
    argumentCallback(CommandArguments.argEntities) {
        sender.sendMessage(Component.text("Player ${exception.input} not found", NamedTextColor.RED))
    }

    argumentCallback(CommandArguments.argTarget) {
        sender.sendMessage(Component.text("Target ${exception.input} not found", NamedTextColor.RED))
    }

    argumentCallback(CommandArguments.argCoordinates) {
        sender.sendMessage(Component.text("Invalid coordinates: ${exception.input}", NamedTextColor.RED))
    }

    argumentCallback(CommandArguments.argYaw) {
        sender.sendMessage(Component.text("Please specified a decimal value between ${CommandArguments.argYaw.min} and ${CommandArguments.argYaw.max} as yaw.", NamedTextColor.RED))
    }

    argumentCallback(CommandArguments.argPitch) {
        sender.sendMessage(Component.text("Please specified a decimal value between ${CommandArguments.argPitch.min} and ${CommandArguments.argPitch.max} as pitch.", NamedTextColor.RED))
    }

    default {
        if (sender is Entity) {
            sender.sendMessage(Component.text(
                "Usage: /$commandName <target>\n " +
                        "or /$commandName <player> <target>\n " +
                        "or /$commandName <x> <y> <z> [<yaw> <pitch>]\n " +
                        "or /$commandName <target> <x> <y> <z> [<yaw> <pitch>]"
            ))
        }
        else {
            sender.sendMessage(Component.text(
                "Usage: /$commandName <player> <target>\n " +
                        "or /$commandName <target> <x> <y> <z> [<yaw> <pitch>]"
            ))
        }
    }

    syntax(CommandArguments.argTarget) {

        if (sender !is Player) {
            sender.sendMessage(Component.text("Did you mean?: /$commandName <player> <target>"))
            return@syntax
        }

        val target = context.get(CommandArguments.argTarget).findFirstPlayer(sender) ?: return@syntax

        (sender as Player).teleportTo(target)

    }

    syntax(CommandArguments.argEntities, CommandArguments.argTarget) {

        val target = context.get(CommandArguments.argTarget).find(sender)
        val entities = context.get(CommandArguments.argEntities).find(sender)

        entities.forEach { it.teleportTo(target[0]) }

    }

    syntax(CommandArguments.argCoordinates) {

        if (sender !is Entity) {
            sender.sendMessage(Component.text("Did you mean?: /$commandName <target> <x> <y> <z> [<yaw> <pitch>]"))
            return@syntax
        }

        val entity = sender as Entity

        entity.teleport(context.get(CommandArguments.argCoordinates).from(entity).asPosition())

    }

    syntax(CommandArguments.argCoordinates, CommandArguments.argYaw, CommandArguments.argPitch) {

        if (sender !is Entity) {
            sender.sendMessage(Component.text("Did you mean?: /$commandName <target> <x> <y> <z> [<yaw> <pitch>]"))
            return@syntax
        }

        val entity = sender as Entity

        val position = context.get(CommandArguments.argCoordinates).from(entity).asPosition()
            .withYaw(context.get(CommandArguments.argYaw))
            .withPitch(context.get(CommandArguments.argPitch))

        entity.teleport(position)

    }

    syntax(CommandArguments.argTarget, CommandArguments.argCoordinates) {

        val target = context.get(CommandArguments.argTarget).findFirstPlayer(sender) ?: return@syntax

        if (sender is Entity) { // Relative to the sender
            target.teleport(context.get(CommandArguments.argCoordinates).from(sender as Entity).asPosition())
        } else { // Relative to the target
            target.teleport(context.get(CommandArguments.argCoordinates).from(target).asPosition())
        }

    }

    syntax(CommandArguments.argTarget, CommandArguments.argCoordinates, CommandArguments.argYaw, CommandArguments.argPitch) {

        val target = context.get(CommandArguments.argTarget).findFirstPlayer(sender) ?: return@syntax

        val position = if (sender is Entity) { // Relative to the sender
            context.get(CommandArguments.argCoordinates).from(sender as Entity).asPosition()
        } else { // Relative to the target
            context.get(CommandArguments.argCoordinates).from(target).asPosition()
        }.withYaw(context.get(CommandArguments.argYaw)).withPitch(context.get(CommandArguments.argPitch))

        target.teleport(position)

    }
}, "teleport", "tp") {
    fun Entity.teleportTo(target: Entity) {

        if (target.instance == null) return

        if (this.instance != target.instance) {
            this.setInstance(target.instance!!, target.position)
            return
        }

        this.teleport(target.position)
    }
}