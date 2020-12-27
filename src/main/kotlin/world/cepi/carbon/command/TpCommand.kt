package world.cepi.carbon.command

import net.minestom.server.MinecraftServer
import net.minestom.server.chat.ChatColor
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player

class TpCommand : Command("teleport", "tp") {

    private fun getPlayer(name: String) : Player? {
        return MinecraftServer.getConnectionManager().getPlayer(name)
    }

    init {
        setArgumentCallback({sender, arg, _ ->

            sender.sendMessage("${ChatColor.RED}Player $arg not found")

        }, CommandArguments.argPlayer)

        setArgumentCallback({sender, arg, _ ->

            sender.sendMessage("${ChatColor.RED}Target $arg not found")

        }, CommandArguments.argTarget)

        setArgumentCallback({sender, arg, _ ->

            sender.sendMessage("${ChatColor.RED}Invalid coordinates: $arg")

        }, CommandArguments.argCoordinates)

        setArgumentCallback({ sender, _, _ ->

            sender.sendMessage("${ChatColor.RED}Please specified a decimal value between ${CommandArguments.argYaw.min} and ${CommandArguments.argYaw.max} as yaw.")

        }, CommandArguments.argYaw)

        setArgumentCallback({ sender, _, _ ->

            sender.sendMessage("${ChatColor.RED}Please specified a decimal value between ${CommandArguments.argPitch.min} and ${CommandArguments.argPitch.max} as pitch.")

        }, CommandArguments.argPitch)

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

        addSyntax({ sender, args ->

            if (sender !is Entity) {
                sender.sendMessage("Did you mean?: /$name <player> <target>")
                return@addSyntax
            }

            val target = getPlayer(args.getWord("target")) ?: return@addSyntax

            sender.teleport(target.position)

        }, CommandArguments.argTarget)

        addSyntax({ _, args ->

            val player =getPlayer(args.getWord("player"))
            val target = getPlayer(args.getWord("target"))

            if (player == null) {
                return@addSyntax
            }

            if (target == null) {
                return@addSyntax
            }

            player.teleport(target.position)

        }, CommandArguments.argPlayer, CommandArguments.argTarget)

        addSyntax({sender, args ->

            if (sender !is Entity) {
                sender.sendMessage("Did you mean?: /$name <target> <x> <y> <z> [<yaw> <pitch>]")
                return@addSyntax
            }

            sender.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition())

        }, CommandArguments.argCoordinates)

        addSyntax({sender, args ->

            if (sender !is Entity) {
                sender.sendMessage("Did you mean?: /$name <target> <x> <y> <z> [<yaw> <pitch>]")
                return@addSyntax
            }

            val position = args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition()
            position.yaw = args.getFloat("yaw")
            position.pitch = args.getFloat("pitch")

            sender.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition())

        }, CommandArguments.argCoordinates, CommandArguments.argYaw, CommandArguments.argPitch)

        addSyntax({sender, args ->

            val target = getPlayer(args.getWord("target")) ?: return@addSyntax

            if (sender is Entity) { // Relative to the sender
                target.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition())
            }
            else { // Relative to the target
                target.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(target).toPosition())
            }

        }, CommandArguments.argTarget, CommandArguments.argCoordinates)

        addSyntax({sender, args ->

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

        }, CommandArguments.argTarget, CommandArguments.argCoordinates, CommandArguments.argYaw, CommandArguments.argPitch)
    }

    override fun onDynamicWrite(text: String): Array<String> {
        return MinecraftServer.getConnectionManager().onlinePlayers.map { it.username }.toTypedArray()
    }

}