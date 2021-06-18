package world.cepi.carbon.warp.commands

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.carbon.warp.Warps
import world.cepi.carbon.warp.warp

object Warp : Command("warp") {
    val warpArg = ArgumentType.DynamicWord("warp").fromRestrictions { string -> Warps.any { it.name == string } }

    init {
        addSyntax({ sender, args ->
            val warp = Warps.firstOrNull { it.name == args[warpArg] } ?: return@addSyntax

            if (sender.isConsole) return@addSyntax

            sender.asPlayer().warp(warp)
        })
    }
}