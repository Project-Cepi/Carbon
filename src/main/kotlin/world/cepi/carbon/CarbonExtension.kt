package world.cepi.carbon

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.GameMode
import net.minestom.server.extensions.Extension
import world.cepi.carbon.command.GameModeCommand
import world.cepi.carbon.command.SimpleGameModeCommand
import world.cepi.carbon.command.TpCommand

class CarbonExtension : Extension() {

    override fun initialize() {
        MinecraftServer.getCommandManager().register(GameModeCommand())
        MinecraftServer.getCommandManager().register(SimpleGameModeCommand("gmc", GameMode.CREATIVE))
        MinecraftServer.getCommandManager().register(SimpleGameModeCommand("gms", GameMode.SURVIVAL))
        MinecraftServer.getCommandManager().register(SimpleGameModeCommand("gma", GameMode.ADVENTURE))
        MinecraftServer.getCommandManager().register(SimpleGameModeCommand("gmsp", GameMode.SPECTATOR))
        MinecraftServer.getCommandManager().register(TpCommand())

        logger.info("[CarbonExtension] has been enabled!")
    }

    override fun terminate() {
        logger.info("[CarbonExtension] has been disabled!")
    }

}