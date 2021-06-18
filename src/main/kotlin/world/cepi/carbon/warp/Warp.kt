package world.cepi.carbon.warp

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.minestom.server.entity.Player
import net.minestom.server.sound.SoundEvent
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

fun Player.warp(to: Warp) {
    this.playSound(Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.PLAYER, 1F, 1F))
    this.teleport(to.position)

    this.sendMessage(Component.text("Warped you to ${to.name}")
        .colorIfAbsent(TextColor.color(0x0fff3f))
    )
}
