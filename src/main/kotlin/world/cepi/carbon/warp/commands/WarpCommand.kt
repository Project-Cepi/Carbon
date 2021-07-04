package world.cepi.carbon.warp.commands

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.carbon.warp.Warps
import world.cepi.carbon.warp.warp
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.addSubcommands
import world.cepi.kstom.command.arguments.suggest

object WarpCommand : Command("warp") {
    val warpArg = ArgumentType.Word("warp").suggest { Warps.map { it.name } }

    init {
        addSyntax {
            val warp = Warps.firstOrNull { it.name == context[warpArg] } ?: return@addSyntax

            if (sender.isConsole) return@addSyntax

            sender.asPlayer().warp(warp)
        }

        addSubcommands(SetWarp, ListWarps, DeleteWarp)
    }
}