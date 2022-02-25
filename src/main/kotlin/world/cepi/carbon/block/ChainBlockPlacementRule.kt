package world.cepi.carbon.block

import net.minestom.server.coordinate.Point
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockFace
import net.minestom.server.instance.block.rule.BlockPlacementRule

object ChainBlockPlacementRule : BlockPlacementRule(Block.CHAIN) {

    override fun blockUpdate(instance: Instance, blockPosition: Point, currentBlock: Block): Block {
        return block
    }

    override fun blockPlace(
        instance: Instance,
        block: Block,
        blockFace: BlockFace,
        blockPosition: Point,
        pl: Player
    ) = block.withProperty("axis", when(blockFace) {
        BlockFace.BOTTOM, BlockFace.TOP -> "y"
        BlockFace.NORTH, BlockFace.SOUTH -> "z"
        BlockFace.EAST, BlockFace.WEST -> "x"
    })

}