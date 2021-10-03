package world.cepi.carbon.warp.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Player
import world.cepi.carbon.command.CommandArguments
import world.cepi.carbon.warp.Warp
import world.cepi.carbon.warp.Warps
import world.cepi.kstom.command.kommand.Kommand

object SetWarp : Kommand({
    val name = ArgumentType.Word("name")
        .filter { warpName -> Warps.none { it.name == warpName } }

    argumentCallback(name) {
        sender.sendMessage(Component.text("A warp with that name already exists", TextColor.color(0xcd1818)))
    }

    syntax(name) {
        Warps.add(Warp(context["name"], player.position))
    }

    syntax(name, CommandArguments.argCoordinates) {
        val position: Vec = context["coordinates"]

        Warps.add(Warp(context["name"], position.asPosition()))
    }

    syntax(name, CommandArguments.argCoordinates, CommandArguments.argPitch, CommandArguments.argYaw) {
        val coordinateVector: Vec = context["coordinates"]
        val pitch: Float = context["pitch"]
        val yaw: Float = context["yaw"]

        val position = coordinateVector.asPosition().withPitch(pitch).withYaw(yaw)
        Warps.add(Warp(context["name"], position))

    }
}, "set")