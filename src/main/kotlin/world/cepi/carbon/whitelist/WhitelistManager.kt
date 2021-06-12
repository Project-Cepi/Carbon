package world.cepi.carbon.whitelist

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

object WhitelistManager {
    private val whitelistFile = File("./whitelist.json")
    var whitelist: MutableList<UUID>

    private val serilalizer: KSerializer<List<String>> = ListSerializer(String.serializer())

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

    fun list(): List<UUID> {
        return whitelist
    }

    fun size() = whitelist.size

    operator fun contains(id: UUID): Boolean = id in whitelist

    private fun save() {
        if (!whitelistFile.exists())
            whitelistFile.createNewFile()
        whitelistFile.writeText(Json.encodeToString(serilalizer, whitelist.map { it.toString() }.toList()))
    }
}

fun UUID.whitelisted(): Boolean = this in WhitelistManager