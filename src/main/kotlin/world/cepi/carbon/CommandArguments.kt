package world.cepi.carbon

import net.minestom.server.MinecraftServer
import net.minestom.server.command.builder.arguments.ArgumentType

object CommandArguments {

    val argGameMode = ArgumentType.Word("gameMode").from("survival", "creative", "adventure", "spectator")
    val argGameModeId = ArgumentType.Integer("gameModeId").between(0, 3)
    val argPlayer = ArgumentType.DynamicWord("player").fromRestrictions { arg -> MinecraftServer.getConnectionManager().onlinePlayers.any { it.username == arg } }

}