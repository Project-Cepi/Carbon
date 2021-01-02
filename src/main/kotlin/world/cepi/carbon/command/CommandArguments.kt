package world.cepi.carbon.command

import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.arguments.ArgumentType

object CommandArguments {

    val playerRestriction: (String) -> Boolean = { arg -> MinecraftServer.getConnectionManager().onlinePlayers.any { it.username.equals(arg, true) } }

    val argGameMode = ArgumentType.Word("gameMode").from("survival", "creative", "adventure", "spectator")
    val argGameModeId = ArgumentType.Integer("gameModeId").between(0, 3)
    val argPlayer = ArgumentType.DynamicWord("player").fromRestrictions(playerRestriction)
    val argTarget = ArgumentType.DynamicWord("target").fromRestrictions(playerRestriction)
    val argCoordinates = ArgumentType.RelativeBlockPosition("coordinates")
    val argYaw = ArgumentType.Float("yaw").between(-180f, 180f)
    val argPitch = ArgumentType.Float("pitch").between(-90f, 90f)

}