package world.cepi.carbon.whitelist

import net.minestom.server.command.ConsoleSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.utils.mojang.MojangUtils
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.default
import java.lang.Long
import java.util.*

internal object WhitelistCommand : Command("whitelist") {

    private fun toValidUuid(string: String) = UUID(
        Long.parseUnsignedLong(string.substring(0, 16), 16),
        Long.parseUnsignedLong(string.substring(16), 16)
    )

    init {
        default { source, _ ->
            source.sendMessage("Usage: /whitelist <add|remove> <player>")
        }

        val remove = ArgumentType.Word("remove").from("remove")
        val add = ArgumentType.Word("add").from("add")
        val playerArg = ArgumentType.Word("player").map { input ->
            MojangUtils.fromUsername(input)
                ?.get("id")?.asString
                ?.let { toValidUuid(it) }
                ?: throw ArgumentSyntaxException("That user does not exist!", input, 1)
        }

        addSyntax(add, playerArg) { source, args ->

            if (source !is ConsoleSender) return@addSyntax

            val uuid = args.get(playerArg)

            if (uuid.whitelisted()) {
                source.sendMessage("${args.get(playerArg)} is already on the whitelist.")
                return@addSyntax
            }

            WhitelistManager.add(uuid)
            source.sendMessage("Added ${args.get(playerArg)} to the whitelist!")
        }

        addSyntax(remove, playerArg) { source, args ->

            if (source !is ConsoleSender) return@addSyntax

            val uuid = args.get(playerArg)

            if (!uuid.whitelisted()) {
                source.sendMessage("Player is not on the whitelist")
                return@addSyntax
            }

            WhitelistManager.remove(uuid)
            source.sendMessage("Removed ${args.getRaw(playerArg.id)} from the whitelist!")
        }
    }
}