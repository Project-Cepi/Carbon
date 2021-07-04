package world.cepi.carbon.warp.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import net.kyori.adventure.text.format.TextColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import world.cepi.carbon.warp.Warp
import world.cepi.carbon.warp.Warps

object ListWarps : Command("list") {
    init {
        setDefaultExecutor { sender, _ ->
            Warps.forEach { sender.sendMessage(warpMessage(it)) }
        }
    }

    private fun warpMessage(warp: Warp): Component {
        return Component.text { builder ->
            builder.content(warp.name)
            builder.color { 0x1be1e4 }

            builder.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,
            "warp @s ${warp.name}"))

            builder.hoverEvent(HoverEventSource<Component> { HoverEvent.showText(Component.text { builder ->
                builder.content("Click to teleport!")
                    .color { 0xc012e2 }
            }) })
        }
    }
}