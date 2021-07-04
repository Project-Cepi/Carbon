package world.cepi.carbon.warp.commands

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.carbon.warp.Warps
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.suggest

object DeleteWarp : Command("delete") {

    val warpName = ArgumentType.Word("warp").suggest { Warps.map { it.name } }

    init {
        addSyntax(warpName) {
            Warps.removeIf { it.name == context[warpName] }
            sender.sendMessage("Warp \"${context.get(warpName)}\" removed!")
        }
    }
}