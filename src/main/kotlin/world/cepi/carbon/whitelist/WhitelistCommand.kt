package world.cepi.carbon.whitelist

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.CommandSender
import net.minestom.server.command.ConsoleSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.utils.mojang.MojangUtils
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.default
import java.lang.Long
import java.util.*

internal object WhitelistCommand : Command("whitelist") {

    fun consoleOnly(sender: CommandSender) = sender.sendMessage(Component.text("You must be console to run this command!", NamedTextColor.RED))

    private fun toValidUuid(string: String) = UUID(
        Long.parseUnsignedLong(string.substring(0, 16), 16),
        Long.parseUnsignedLong(string.substring(16), 16)
    )

    init {
        default { source, _ ->
            source.sendMessage("Usage: /whitelist <add|remove> <player>")
        }

        val remove = "remove".literal()
        val add = "add".literal()
        val list = "list".literal()

        val playerArg = ArgumentType.Word("player").map { input ->
            MojangUtils.fromUsername(input)
                ?.get("id")?.asString
                ?.let { toValidUuid(it) }
                ?: throw ArgumentSyntaxException("That user does not exist!", input, 1)
        }

        addSyntax(add, playerArg) {

            if (sender !is ConsoleSender) {
                consoleOnly(sender)
                return@addSyntax
            }

            val uuid = context.get(playerArg)

            if (uuid.whitelisted()) {
                sender.sendMessage("${context.get(playerArg)} is already on the whitelist.")
                return@addSyntax
            }

            WhitelistManager.add(uuid)
            sender.sendMessage("Added ${context.get(playerArg)} to the whitelist!")
        }

        addSyntax(list) {
            if (sender !is ConsoleSender) {
                consoleOnly(sender)
                return@addSyntax
            }

            WhitelistManager.list().forEach {
                sender.sendMessage(
                    Component.text("- ", NamedTextColor.DARK_GRAY)
                        .append(Component.text(MojangUtils.fromUuid(it.toString())!!.get("name").asString, NamedTextColor.WHITE))
                        .append(Component.text("($it)", NamedTextColor.GRAY))
                )
            }
        }

        addSyntax(remove, playerArg) {

            if (sender !is ConsoleSender) {
                consoleOnly(sender)
                return@addSyntax
            }

            val uuid = context.get(playerArg)

            if (!uuid.whitelisted()) {
                sender.sendMessage("Player is not on the whitelist")
                return@addSyntax
            }

            WhitelistManager.remove(uuid)
            sender.sendMessage("Removed ${context.getRaw(playerArg.id)} from the whitelist!")
        }
    }
}