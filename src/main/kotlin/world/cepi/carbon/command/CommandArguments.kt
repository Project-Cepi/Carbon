package world.cepi.carbon.command

import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.GameMode

internal object CommandArguments {

    val argGameMode = ArgumentType.Word("gameMode").from("survival", "creative", "adventure", "spectator").map { input ->
        GameMode.valueOf(input.uppercase())
    }

    val argGameModeId = ArgumentType.Integer("gameModeId").between(0, 3).map {
        GameMode.values()[it]
    }

    val argEntities = ArgumentType.Entity("entities")
    val argPlayer = ArgumentType.Entity("player").onlyPlayers(true)
    val argTarget = ArgumentType.Entity("target").singleEntity(true)
    val argCoordinates = ArgumentType.RelativeVec3("coordinates")
    val argYaw = ArgumentType.Float("yaw").between(-180f, 180f)
    val argPitch = ArgumentType.Float("pitch").between(-90f, 90f)

}