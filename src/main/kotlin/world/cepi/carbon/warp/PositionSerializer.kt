package world.cepi.carbon.warp

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minestom.server.utils.Position

object PositionSerializer : KSerializer<Position> {

    override val descriptor: SerialDescriptor = SerializablePosition.serializer().descriptor

    override fun deserialize(decoder: Decoder): Position {
        return decoder.decodeSerializableValue(SerializablePosition.serializer()).toPosition()
    }

    override fun serialize(encoder: Encoder, value: Position) {
        encoder.encodeSerializableValue(SerializablePosition.serializer(), SerializablePosition.from(value))
    }

    @Serializable
    private data class SerializablePosition(
        val x: Double,
        val y: Double,
        val z: Double,

        val pitch: Float = 0F,
        val yaw: Float = 0F
    ) {

        fun toPosition() = Position(x, y, z, yaw, pitch)

        companion object {
            fun from(position: Position): SerializablePosition {
                return SerializablePosition(position.x, position.y, position.z, position.pitch, position.yaw)
            }
        }
    }
}