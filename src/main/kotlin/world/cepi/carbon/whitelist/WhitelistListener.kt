package world.cepi.carbon.whitelist

import net.kyori.adventure.text.Component
import net.minestom.server.event.player.PlayerLoginEvent

fun whitelistListener(event: PlayerLoginEvent) {
    if (WhitelistManager.size() != 0 && !event.player.uuid.whitelisted())
        event.player.kick(Component.text("You are not whitelisted!"))
}