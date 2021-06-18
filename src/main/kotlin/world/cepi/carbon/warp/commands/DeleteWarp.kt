package world.cepi.carbon.warp.commands

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.carbon.warp.Warps

object DeleteWarp : Command("delwarp") {
    init {
        addSyntax({ sender, args ->

            Warps.removeIf { it.name == args[warpName] }
            sender.sendMessage("Warp \"${args.get(warpName)}\" removed!")
        })
    }

    val warpName = ArgumentType.DynamicWord("warp").fromRestrictions { string -> Warps.any { it.name == string } }
}