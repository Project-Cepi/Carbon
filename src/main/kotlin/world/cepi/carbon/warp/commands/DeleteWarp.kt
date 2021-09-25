package world.cepi.carbon.warp.commands

import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.carbon.warp.Warps
import world.cepi.kstom.command.arguments.suggest
import world.cepi.kstom.command.kommand.Kommand

object DeleteWarp : Kommand({

    val warpName = ArgumentType.Word("warp").suggest { Warps.map { it.name } }

    syntax(warpName) {
        Warps.removeIf { it.name == !warpName }
        sender.sendMessage("Warp \"${context.get(warpName)}\" removed!")
    }

}, "delete")