package world.cepi.carbon

import net.minestom.server.extensions.Extension
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.rule.BlockPlacementRule
import world.cepi.carbon.block.ChainBlockPlacementRule
import world.cepi.carbon.block.GlassPaneBlockPlacementRule
import world.cepi.carbon.block.HorizontalBlockPlacementRule
import world.cepi.carbon.command.*
import world.cepi.carbon.command.GameModeCommand
import world.cepi.carbon.command.SimpleGameModeCommand
import world.cepi.carbon.command.SpawnCommand
import world.cepi.carbon.warp.Warp
import world.cepi.carbon.warp.commands.WarpCommand
import world.cepi.carbon.warp.commands.WarpsCommand
import world.cepi.carbon.whitelist.WhitelistCommand
import world.cepi.carbon.whitelist.whitelistListener
import world.cepi.kstom.Manager
import world.cepi.kstom.command.register
import world.cepi.kstom.command.unregister
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.util.log
import world.cepi.kstom.util.node

class CarbonExtension : Extension() {

    override fun initialize(): LoadStatus {
        GameModeCommand.register()
        SimpleGameModeCommand.commandList.forEach { it.register() }

        TpCommand.register()

        WhitelistCommand.register()

        WarpCommand.register()
        WarpsCommand.register()

        SpawnCommand.register()

        NightVisionCommand.register()

        Manager.block.registerBlockPlacementRule(ChainBlockPlacementRule)
        Manager.block.registerBlockPlacementRule(HorizontalBlockPlacementRule(Block.REPEATER))
        Manager.block.registerBlockPlacementRule(HorizontalBlockPlacementRule(Block.COMPARATOR))
        Manager.block.registerBlockPlacementRule(HorizontalBlockPlacementRule(Block.LECTERN))
        GlassPaneBlockPlacementRule.handlers.forEach { Manager.block.registerBlockPlacementRule(it) }

        node.listenOnly(::whitelistListener)

        log.info("[CarbonExtension] has been enabled!")

        return LoadStatus.SUCCESS
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

        NightVisionCommand.unregister()

        log.info("[CarbonExtension] has been disabled!")
    }

}