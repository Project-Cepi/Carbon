package world.cepi.carbon

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.GameMode
import net.minestom.server.extensions.Extension
import world.cepi.carbon.command.GameModeCommand
import world.cepi.carbon.command.SimpleGameModeCommand
import world.cepi.carbon.command.TpCommand
import world.cepi.carbon.command.WhitelistCommand
import world.cepi.kstom.command.register
import world.cepi.kstom.command.unregister

class CarbonExtension : Extension() {

    override fun initialize() {
        GameModeCommand.register()
        SimpleGameModeCommand.commandList.forEach { it.register() }

        TpCommand.register()

        WhitelistCommand.register()

        logger.info("[CarbonExtension] has been enabled!")
    }

    override fun terminate() {

        GameModeCommand.unregister()
        SimpleGameModeCommand.commandList.forEach { it.unregister() }

        TpCommand.unregister()

        WhitelistCommand.unregister()

        logger.info("[CarbonExtension] has been disabled!")
    }

}