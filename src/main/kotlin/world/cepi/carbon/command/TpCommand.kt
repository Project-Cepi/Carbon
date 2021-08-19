package world.cepi.carbon.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.setArgumentCallback

object TpCommand : Command("teleport", "tp") {

    init {
        setArgumentCallback(CommandArguments.argEntities) {
            sender.sendMessage(Component.text("Player ${exception.input} not found", NamedTextColor.RED))
        }

        setArgumentCallback(CommandArguments.argTarget) {
            sender.sendMessage(Component.text("Target ${exception.input} not found", NamedTextColor.RED))
        }

        setArgumentCallback(CommandArguments.argCoordinates) {
            sender.sendMessage(Component.text("Invalid coordinates: ${exception.input}", NamedTextColor.RED))
        }

        setArgumentCallback(CommandArguments.argYaw) {
            sender.sendMessage(Component.text("Please specified a decimal value between ${CommandArguments.argYaw.min} and ${CommandArguments.argYaw.max} as yaw.", NamedTextColor.RED))
        }

        setArgumentCallback(CommandArguments.argPitch) {
            sender.sendMessage(Component.text("Please specified a decimal value between ${CommandArguments.argPitch.min} and ${CommandArguments.argPitch.max} as pitch.", NamedTextColor.RED))
        }

        setDefaultExecutor { sender, _ ->
            if (sender is Entity) {
                sender.sendMessage(Component.text(
                    "Usage: /$name <target>\n " +
                            "or /$name <player> <target>\n " +
                            "or /$name <x> <y> <z> [<yaw> <pitch>]\n " +
                            "or /$name <target> <x> <y> <z> [<yaw> <pitch>]"
                ))
            }
            else {
                sender.sendMessage(Component.text(
                    "Usage: /$name <player> <target>\n " +
                            "or /$name <target> <x> <y> <z> [<yaw> <pitch>]"
                ))
            }
        }

        addSyntax(CommandArguments.argTarget) {

            if (sender !is Player) {
                sender.sendMessage(Component.text("Did you mean?: /$name <player> <target>"))
                return@addSyntax
            }

            val target = context.get(CommandArguments.argTarget).findFirstPlayer(sender) ?: return@addSyntax

            (sender as Player).teleport(target.position)

        }

        addSyntax(CommandArguments.argEntities, CommandArguments.argTarget) {

            val target = context.get(CommandArguments.argTarget).find(sender)
            val entities = context.get(CommandArguments.argEntities).find(sender)

            entities.forEach { it.teleport(target[0].position) }

        }

        addSyntax(CommandArguments.argCoordinates) {

            if (sender !is Entity) {
                sender.sendMessage(Component.text("Did you mean?: /$name <target> <x> <y> <z> [<yaw> <pitch>]"))
                return@addSyntax
            }

            val entity = sender as Entity

            entity.teleport(context.get(CommandArguments.argCoordinates).from(entity).asPosition())

        }

        addSyntax(CommandArguments.argCoordinates, CommandArguments.argYaw, CommandArguments.argPitch) {

            if (sender !is Entity) {
                sender.sendMessage(Component.text("Did you mean?: /$name <target> <x> <y> <z> [<yaw> <pitch>]"))
                return@addSyntax
            }

            val entity = sender as Entity

            val position = context.get(CommandArguments.argCoordinates).from(entity).asPosition()
                .withYaw(context.get(CommandArguments.argYaw))
                .withPitch(context.get(CommandArguments.argPitch))

            entity.teleport(position)

        }

        addSyntax(CommandArguments.argTarget, CommandArguments.argCoordinates) {

            val target = context.get(CommandArguments.argTarget).findFirstPlayer(sender) ?: return@addSyntax

            if (sender is Entity) { // Relative to the sender
                target.teleport(context.get(CommandArguments.argCoordinates).from(sender as Entity).asPosition())
            } else { // Relative to the target
                target.teleport(context.get(CommandArguments.argCoordinates).from(target).asPosition())
            }

        }

        addSyntax(CommandArguments.argTarget, CommandArguments.argCoordinates, CommandArguments.argYaw, CommandArguments.argPitch) {

            val target = context.get(CommandArguments.argTarget).findFirstPlayer(sender) ?: return@addSyntax

            val position = if (sender is Entity) { // Relative to the sender
                context.get(CommandArguments.argCoordinates).from(sender as Entity).asPosition()
            } else { // Relative to the target
                context.get(CommandArguments.argCoordinates).from(target).asPosition()
            }.withYaw(context.get(CommandArguments.argYaw)).withPitch(context.get(CommandArguments.argPitch))

            target.teleport(position)

        }
    }

}