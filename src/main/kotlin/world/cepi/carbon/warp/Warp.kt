package world.cepi.carbon.warp

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.minestom.server.utils.Position
import java.io.File

@Serializable
data class Warp(
    val name: String,
    @Serializable(with = PositionSerializer::class) val position: Position
) {
    companion object {
        fun saveWarps() {
            val warpFolder = File("./warps")

            Warps.forEach {
                val warpFile = File(warpFolder, "${it.name}.json")
                warpFile.writeText(Json.encodeToString(serializer(), it))
            }
        }
    }
}

val Warps: MutableSet<Warp> by lazy {
    val warpFolder = File("./warps")
    warpFolder.mkdir()

    warpFolder.listFiles()!!.map { file ->
        Json.decodeFromString(Warp.serializer(), file.readText())
    }.toMutableSet()
}
