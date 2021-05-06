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
        setArgumentCallback(CommandArguments.argEntities) { sender, exception ->
            sender.sendMessage(Component.text("Player ${exception.input} not found", NamedTextColor.RED))
        }

        setArgumentCallback(CommandArguments.argTarget) { sender, exception ->
            sender.sendMessage(Component.text("Target ${exception.input} not found", NamedTextColor.RED))
        }

        setArgumentCallback(CommandArguments.argCoordinates) { sender, exception ->
            sender.sendMessage(Component.text("Invalid coordinates: ${exception.input}", NamedTextColor.RED))
        }

        setArgumentCallback(CommandArguments.argYaw) { sender, _ ->
            sender.sendMessage(Component.text("Please specified a decimal value between ${CommandArguments.argYaw.min} and ${CommandArguments.argYaw.max} as yaw.", NamedTextColor.RED))
        }

        setArgumentCallback(CommandArguments.argPitch) { sender, _ ->
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

        addSyntax(CommandArguments.argTarget) { sender, args ->

            if (sender !is Player) {
                sender.sendMessage(Component.text("Did you mean?: /$name <player> <target>"))
                return@addSyntax
            }

            val target = args.get(CommandArguments.argTarget).findFirstPlayer(sender) ?: return@addSyntax

            sender.teleport(target.position)

        }

        addSyntax(CommandArguments.argEntities, CommandArguments.argTarget) { sender, args ->

            val target = args.get(CommandArguments.argTarget).find(sender)
            val entities = args.get(CommandArguments.argEntities).find(sender)

            entities.forEach { it.teleport(target[0].position) }

        }

        addSyntax(CommandArguments.argCoordinates) { sender, args ->

            if (sender !is Entity) {
                sender.sendMessage(Component.text("Did you mean?: /$name <target> <x> <y> <z> [<yaw> <pitch>]"))
                return@addSyntax
            }

            sender.teleport(args.get(CommandArguments.argCoordinates).from(sender).toPosition())

        }

        addSyntax(CommandArguments.argCoordinates, CommandArguments.argYaw, CommandArguments.argPitch) { sender, args ->

            if (sender !is Entity) {
                sender.sendMessage(Component.text("Did you mean?: /$name <target> <x> <y> <z> [<yaw> <pitch>]"))
                return@addSyntax
            }

            val position = args.get(CommandArguments.argCoordinates).from(sender).toPosition()
            position.yaw = args.get(CommandArguments.argYaw)
            position.pitch = args.get(CommandArguments.argPitch)

            sender.teleport(args.get(CommandArguments.argCoordinates).from(sender).toPosition())

        }

        addSyntax(CommandArguments.argTarget, CommandArguments.argCoordinates) {sender, args ->

            val target = args.get(CommandArguments.argTarget).findFirstPlayer(sender) ?: return@addSyntax

            if (sender is Entity) { // Relative to the sender
                target.teleport(args.get(CommandArguments.argCoordinates).from(sender).toPosition())
            } else { // Relative to the target
                target.teleport(args.get(CommandArguments.argCoordinates).from(target).toPosition())
            }

        }

        addSyntax(CommandArguments.argTarget, CommandArguments.argCoordinates, CommandArguments.argYaw, CommandArguments.argPitch) { sender, args ->

            val target = args.get(CommandArguments.argTarget).findFirstPlayer(sender) ?: return@addSyntax

            val position = if (sender is Entity) { // Relative to the sender
                args.get(CommandArguments.argCoordinates).from(sender).toPosition()
            } else { // Relative to the target
                args.get(CommandArguments.argCoordinates).from(target).toPosition()
            }

            position.yaw = args.get(CommandArguments.argYaw)
            position.pitch = args.get(CommandArguments.argPitch)

            target.teleport(position)

        }
    }

}