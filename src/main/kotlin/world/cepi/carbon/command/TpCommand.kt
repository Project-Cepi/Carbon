package world.cepi.carbon.command

import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player

class TpCommand : Command("teleport", "tp") {

    private fun getPlayer(name: String) : Player? {
        return MinecraftServer.getConnectionManager().getPlayer(name)
    }

    init {
        setDefaultExecutor { sender, _ ->
            if (sender is Entity) {
                sender.sendMessage(
                    "Usage: /$name <target>\n " +
                            "or /$name <player> <target>\n " +
                            "or /$name <x> <y> <z> [<yaw>] [<pitch>]\n " +
                            "or /$name <target> <x> <y> <z> [<yaw>] [<pitch>]"
                )
            }
            else {
                sender.sendMessage(
                    "Usage: /$name <player> <target>\n " +
                            "or /$name <target> <x> <y> <z> [<yaw>] [<pitch>]"
                )
            }
        }

        addSyntax({ sender, args ->

            if (sender !is Entity) {
                sender.sendMessage("Did you mean?: /$name <player> <target>")
                return@addSyntax
            }

            val target = getPlayer(args.getWord("target"))

            if (target == null) {
                sender.sendMessage("§cPlayer ${args.getWord("target")} not found")
                return@addSyntax
            }

            sender.teleport(target.position)

        }, CommandArguments.argTarget)

        addSyntax({ sender, args ->

            val player =getPlayer(args.getWord("player"))
            val target = getPlayer(args.getWord("target"))

            if (player == null) {
                sender.sendMessage("§cPlayer ${args.getWord("player")} not found")
                return@addSyntax
            }
            if (target == null) {
                sender.sendMessage("§cPlayer ${args.getWord("target")} not found")
                return@addSyntax
            }

            player.teleport(target.position)

        }, CommandArguments.argPlayer, CommandArguments.argTarget)

        addSyntax({sender, args ->

            if (sender !is Entity) {
                sender.sendMessage("Did you mean?: /$name <target> <x> <y> <z> [<yaw>] [<pitch>]")
                return@addSyntax
            }

            sender.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition())

        }, CommandArguments.argCoordinates)

        addSyntax({sender, args ->

            val target = getPlayer(args.getWord("target"))

            if (target == null) {
                sender.sendMessage("§cPlayer ${args.getWord("target")} not found")
                return@addSyntax
            }

            if (sender is Entity) { // Relative to the sender
                target.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(sender).toPosition())
            }
            else { // Relative to the target
                target.teleport(args.getRelativeBlockPosition("coordinates").fromRelativePosition(target).toPosition())
            }

        }, CommandArguments.argTarget, CommandArguments.argCoordinates)
    }

    override fun onDynamicWrite(text: String): Array<String> {
        return MinecraftServer.getConnectionManager().onlinePlayers.map { it.username }.toTypedArray()
    }

}