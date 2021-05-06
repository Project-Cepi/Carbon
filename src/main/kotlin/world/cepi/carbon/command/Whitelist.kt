package world.cepi.carbon.command

import world.cepi.kstom.command.default
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.utils.mojang.MojangUtils
import java.io.File
import java.lang.Long.parseUnsignedLong
import java.util.*

internal object WhitelistCommand : Command("whitelist") {

    fun toValidUuid(string: String) = UUID(
        parseUnsignedLong(string.substring(0, 16), 16),
        parseUnsignedLong(string.substring(16), 16)
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

        addSyntax({ source, args ->

            val uuid = args.get(playerArg)

            if (uuid.isWhitelisted()) {
                source.sendMessage("${args.get(playerArg)} is already on the whitelist.")
                return@addSyntax
            }

            Whitelist.add(uuid)
            source.sendMessage("Added ${args.get(playerArg)} to the whitelist!")
        }, add, playerArg)

        addSyntax({ source, args ->
            val uuid = args.get(playerArg)

            if (!uuid.isWhitelisted()) {
                source.sendMessage("Player is not on the whitelist")
                return@addSyntax
            }

            Whitelist.remove(uuid)
            source.sendMessage("Removed ${args.getRaw(playerArg.id)} from the whitelist!")
        }, remove, playerArg)
    }
}

object Whitelist {
    private val whitelistFile = File("./whitelist.json")
    var whitelist: MutableList<UUID>

    val serilalizer: KSerializer<List<String>> = ListSerializer(String.serializer())

    init {
        whitelist = try {
            Json.decodeFromString(serilalizer, whitelistFile.readText()).map { UUID.fromString(it) }.toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    fun add(id: UUID) {
        whitelist.add(id)
        save()
    }

    fun remove(id: UUID) {
        whitelist.remove(id)
        save()
    }

    private fun save() {
        if (!whitelistFile.exists())
            whitelistFile.createNewFile()
        whitelistFile.writeText(Json.encodeToString(serilalizer, whitelist.map { it.toString() }.toList()))
    }
}

fun UUID.isWhitelisted(): Boolean = this in Whitelist.whitelist