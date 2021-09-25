package world.cepi.carbon.warp.commands

import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.carbon.warp.Warps
import world.cepi.carbon.warp.warp
import world.cepi.kstom.command.arguments.suggest
import world.cepi.kstom.command.kommand.Kommand

object WarpCommand : Kommand({
    val warpArg = ArgumentType.Word("warp").suggest { Warps.map { it.name } }

    syntax {
        val warp = Warps.firstOrNull { it.name == context[warpArg] } ?: return@syntax

        if (sender.isConsole) return@syntax

        sender.asPlayer().warp(warp)
    }

    addSubcommands(SetWarp, ListWarps, DeleteWarp)

}, "warp")