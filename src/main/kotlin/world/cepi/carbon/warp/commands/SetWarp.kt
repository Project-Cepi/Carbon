package world.cepi.carbon.warp.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.util.RGBLike
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.utils.Position
import net.minestom.server.utils.Vector
import world.cepi.carbon.command.CommandArguments
import world.cepi.carbon.warp.Warp
import world.cepi.carbon.warp.Warps
import world.cepi.kstom.command.addSyntax

object SetWarp : Command("set") {
    val name = ArgumentType.Word("name")

    init {
        addSyntax(name) {
            if (sender.isConsole) return@addSyntax
            val player = sender as Player

            if (checkName(context["name"], sender)) Warps.add(Warp(context["name"], player.position))
        }

        addSyntax(name, CommandArguments.argCoordinates) {
            val position: Vector = context["coordinates"]

            if (checkName(context["name"], sender)) Warps.add(Warp(context["name"], position.toPosition()))
        }

        addSyntax(name, CommandArguments.argCoordinates, CommandArguments.argPitch, CommandArguments.argYaw) {
            val coordinateVector: Vector = context["coordinates"]
            val pitch: Float = context["pitch"]
            val yaw: Float = context["yaw"]

            val position = coordinateVector.toPosition()
            position.pitch = pitch
            position.yaw = yaw
            if (checkName(context["name"], sender)) Warps.add(Warp(context["name"], position))

        }
    }

    private fun checkName(name: String, `for`: CommandSender): Boolean {
        val result = Warps.none { it.name == name }

        `for`.sendMessage(Component.text("A warp with that name already exists", TextColor.color(0xcd1818)))
        return result
    }
}