package world.cepi.carbon.whitelist

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.event.player.PlayerLoginEvent

internal fun whitelistListener(event: PlayerLoginEvent) {
    if (WhitelistManager.size() != 0 && !event.player.uuid.whitelisted())
        event.player.kick(Component.text("You are not whitelisted!", NamedTextColor.RED))
}