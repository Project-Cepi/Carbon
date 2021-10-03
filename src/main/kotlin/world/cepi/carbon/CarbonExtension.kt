package world.cepi.carbon

import net.minestom.server.extensions.Extension
import world.cepi.carbon.command.GameModeCommand
import world.cepi.carbon.command.SimpleGameModeCommand
import world.cepi.carbon.command.SpawnCommand
import world.cepi.carbon.command.TpCommand
import world.cepi.carbon.warp.Warp
import world.cepi.carbon.warp.commands.WarpCommand
import world.cepi.carbon.warp.commands.WarpsCommand
import world.cepi.carbon.whitelist.WhitelistCommand
import world.cepi.carbon.whitelist.whitelistListener
import world.cepi.kstom.command.register
import world.cepi.kstom.command.unregister
import world.cepi.kstom.event.listenOnly

class CarbonExtension : Extension() {

    override fun initialize() {
        GameModeCommand.register()
        SimpleGameModeCommand.commandList.forEach { it.register() }

        TpCommand.register()

        WhitelistCommand.register()

        WarpCommand.register()
        WarpsCommand.register()

        SpawnCommand.register()

        eventNode.listenOnly(::whitelistListener)

        logger.info("[CarbonExtension] has been enabled!")
    }

    override fun terminate() {

        GameModeCommand.unregister()
        SimpleGameModeCommand.commandList.forEach { it.unregister() }

        TpCommand.unregister()

        WhitelistCommand.unregister()

        WarpCommand.unregister()
        WarpsCommand.unregister()
        Warp.saveWarps()

        SpawnCommand.unregister()

        logger.info("[CarbonExtension] has been disabled!")
    }

}