package world.cepi.carbon

import net.minestom.server.extensions.Extension
import world.cepi.carbon.command.GameModeCommand
import world.cepi.carbon.command.SimpleGameModeCommand
import world.cepi.carbon.command.TpCommand
import world.cepi.carbon.whitelist.WhitelistCommand
import world.cepi.carbon.whitelist.whitelistListener
import world.cepi.kstom.Manager
import world.cepi.kstom.addEventCallback
import world.cepi.kstom.command.register
import world.cepi.kstom.command.unregister
import world.cepi.kstom.removeEventCallback

class CarbonExtension : Extension() {

    override fun initialize() {
        GameModeCommand.register()
        SimpleGameModeCommand.commandList.forEach { it.register() }

        TpCommand.register()

        WhitelistCommand.register()

        Manager.globalEvent.addEventCallback(::whitelistListener)

        logger.info("[CarbonExtension] has been enabled!")
    }

    override fun terminate() {

        GameModeCommand.unregister()
        SimpleGameModeCommand.commandList.forEach { it.unregister() }

        TpCommand.unregister()

        WhitelistCommand.unregister()

        Manager.globalEvent.removeEventCallback(::whitelistListener)

        logger.info("[CarbonExtension] has been disabled!")
    }

}