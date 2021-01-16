package world.cepi.carbon.command

import net.minestom.server.MinecraftServer
import net.minestom.server.chat.ChatColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import world.cepi.kstom.addSyntax
import world.cepi.kstom.setArgumentCallback

class TpCommand : Command("teleport", "tp") {

    private fun getPlayer(name: String) : Player? {
        return MinecraftServer.getConnectionManager().getPlayer(name)
    }

    init {
        setArgumentCallback(CommandArguments.argPlayer) { sender, exception ->
            sender.sendMessage("${ChatColor.RED}Player ${exception.input} not found")
        }

        setArgumentCallback(CommandArguments.argTarget) { sender, exception ->
            sender.sendMessage("${ChatColor.RED}Target ${exception.input} not found")
        }

        setArgumentCallback(CommandArguments.argCoordinates) { sender, exception ->
            sender.sendMessage("${ChatColor.RED}Invalid coordinates: ${exception.input}")
        }

        setArgumentCallback(CommandArguments.argYaw) { sender, _ ->
            sender.sendMessage("${ChatColor.RED}Please specified a decimal value between ${CommandArguments.argYaw.min} and ${CommandArguments.argYaw.max} as yaw.")
        }

        setArgumentCallback(CommandArguments.argPitch) { sender, _ ->
            sender.sendMessage("${ChatColor.RED}Please specified a decimal value between ${CommandArguments.argPitch.min} and ${CommandArguments.argPitch.max} as pitch.")
        }

        setDefaultExecutor { sender, _ ->
            if (sender is Entity) {
                sender.sendMessage(
                    "Usage: /$name <target>\n " +
                            "or /$name <player> <target>\n " +
                            "or /$name <x> <y> <z> [<yaw> <pitch>]\n " +
                            "or /$name <target> <x> <y> <z> [<yaw> <pitch>]"
                )
            }
            else {
                sender.sendMessage(
                    "Usage: /$name <player> <target>\n " +
                            "or /$name <target> <x> <y> <z> [<yaw> <pitch>]"
                )
            }
        }

        addSyntax(CommandArguments.argTarget) { sender, args ->

            if (sender !is Entity) {
                sender.sendMessage("Did you mean?: /$name <player> <target>")
                return@addSyntax
            }

            val target = getPlayer(args.getWord("target")) ?: return@addSyntax

            sender.teleport(target.position)

        }

        addSyntax(CommandArguments.argPlayer, CommandArguments.argTarget) { _, args ->

            val player =getPlayer(args.getWord("player"))
            val target = getPlayer(args.getWord("target"))

            if (player == null) {
                return@addSyntax
            }

            if (target == null) {
                return@addSyntax
            }

            player.teleport(target.position)

        }

        addSyntax(CommandArguments.argCoordinates) { sender, args ->

            if (sender !is Entity) {
                sender.sendMessage("Did you mean?: /$name <target> <x> <y> <z> [<yaw> <pitch>]")
                return@addSyntax
            }

            sender.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition())

        }

        addSyntax(CommandArguments.argCoordinates, CommandArguments.argYaw, CommandArguments.argPitch) { sender, args ->

            if (sender !is Entity) {
                sender.sendMessage("Did you mean?: /$name <target> <x> <y> <z> [<yaw> <pitch>]")
                return@addSyntax
            }

            val position = args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition()
            position.yaw = args.getFloat("yaw")
            position.pitch = args.getFloat("pitch")

            sender.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition())

        }

        addSyntax(CommandArguments.argTarget, CommandArguments.argCoordinates) {sender, args ->

            val target = getPlayer(args.getWord("target")) ?: return@addSyntax

            if (sender is Entity) { // Relative to the sender
                target.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition())
            }
            else { // Relative to the target
                target.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(target).toPosition())
            }

        }

        addSyntax(CommandArguments.argTarget, CommandArguments.argCoordinates, CommandArguments.argYaw, CommandArguments.argPitch) { sender, args ->

            val target = getPlayer(args.getWord("target")) ?: return@addSyntax

            /* Kotlin black magic right here*/
            val position = if (sender is Entity) { // Relative to the sender
                args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition()
            } else { // Relative to the target
                args.getRelativeBlockPosition("coordinates").fromRelativePosition(target).toPosition()
            }

            position.yaw = args.getFloat("yaw")
            position.pitch = args.getFloat("pitch")

            target.teleport(position)

        }
    }

    override fun onDynamicWrite(sender: CommandSender, text: String): Array<out String> {
        return MinecraftServer.getConnectionManager().onlinePlayers.map { it.username }.toTypedArray()
    }

}