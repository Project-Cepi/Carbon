package world.cepi.carbon.command

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player

internal object SpawnCommand : Command("spawn") {

    init {
        setDefaultExecutor { sender, context ->

            val player = sender as? Player ?: return@setDefaultExecutor

            player.teleport(player.respawnPoint)
        }
    }

}