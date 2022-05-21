package world.cepi.carbon.block

import net.minestom.server.coordinate.Point
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockFace
import net.minestom.server.instance.block.rule.BlockPlacementRule

class HorizontalBlockPlacementRule(block: Block): BlockPlacementRule(block) {
    override fun blockUpdate(instance: Instance, blockPosition: Point, currentBlock: Block): Block {
        return block
    }

    override fun blockPlace(
        instance: Instance,
        block: Block,
        blockFace: BlockFace,
        blockPosition: Point,
        pl: Player
    ): Block {
        val yaw = (pl.position.yaw % 360 + 360) % 360

        return block.withProperty("facing", if (yaw > 135 || yaw < -135) {
            "north"
        } else if (yaw < -45) {
            "east"
        } else if (yaw > 45) {
            "west"
        } else {
            "south"
        })
    }
}