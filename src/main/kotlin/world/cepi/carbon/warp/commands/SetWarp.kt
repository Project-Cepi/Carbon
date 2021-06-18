package world.cepi.carbon.warp.commands

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.utils.Position
import net.minestom.server.utils.Vector
import world.cepi.carbon.command.CommandArguments
import world.cepi.carbon.warp.Warp
import world.cepi.carbon.warp.Warps
import world.cepi.kstom.command.addSyntax

object SetWarp : Command("setwarp") {
    val name = ArgumentType.Word("name")

    init {
        addSyntax({sender, args ->
            if (sender.isConsole) return@addSyntax
            val player = sender as Player

            Warps.add(Warp(args["name"], player.position))
        }, name)

        addSyntax({_, args ->
            val position: Vector = args["coordinates"]

            Warps.add(Warp(args["name"], position.toPosition()))
        }, name, CommandArguments.argCoordinates)

        addSyntax({sender, args ->
            val coordinateVector: Vector = args["coordinates"]
            val pitch: Float = args["pitch"]
            val yaw: Float = args["yaw"]

            val position = coordinateVector.toPosition()
            position.pitch = pitch
            position.yaw = yaw

            Warps.add(Warp(args["name"], position))

        }, name, CommandArguments.argCoordinates, CommandArguments.argPitch, CommandArguments.argYaw)
    }
}