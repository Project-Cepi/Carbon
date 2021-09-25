package world.cepi.carbon.whitelist

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.utils.mojang.MojangUtils
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand
import java.lang.Long
import java.util.*

internal object WhitelistCommand : Kommand({

    fun consoleOnly(sender: CommandSender) = sender.sendMessage(Component.text("You must be console to run this command!", NamedTextColor.RED))

    fun toValidUuid(string: String) = UUID(
        Long.parseUnsignedLong(string.substring(0, 16), 16),
        Long.parseUnsignedLong(string.substring(16), 16)
    )

    default {
        sender.sendMessage("Usage: /whitelist <add|remove> <player>")
    }

    val remove by literal
    val add by literal
    val list by literal

    val playerArg = ArgumentType.Word("player").map { input ->
        MojangUtils.fromUsername(input)
            ?.get("id")?.asString
            ?.let { toValidUuid(it) }
    }

    syntax(add, playerArg).onlyConsole {

        val uuid = context.get(playerArg)

        if (uuid.whitelisted()) {
            sender.sendMessage("${context.get(playerArg)} is already on the whitelist.")
            return@onlyConsole
        }

        WhitelistManager.add(uuid)
        sender.sendMessage("Added ${context.get(playerArg)} to the whitelist!")
    }

    syntax(list).onlyConsole {

        WhitelistManager.list().forEach {
            sender.sendMessage(
                Component.text("- ", NamedTextColor.DARK_GRAY)
                    .append(Component.text(MojangUtils.fromUuid(it.toString())!!.get("name").asString, NamedTextColor.WHITE))
                    .append(Component.text("($it)", NamedTextColor.GRAY))
            )
        }
    }

    syntax(remove, playerArg).onlyConsole {

        val uuid = context.get(playerArg)

        if (!uuid.whitelisted()) {
            sender.sendMessage("Player is not on the whitelist")
            return@onlyConsole
        }

        WhitelistManager.remove(uuid)
        sender.sendMessage("Removed ${context.getRaw(playerArg.id)} from the whitelist!")
    }

}, "whitelist")