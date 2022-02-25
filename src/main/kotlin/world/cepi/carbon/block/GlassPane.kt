package world.cepi.carbon.block

import net.minestom.server.coordinate.Point
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockFace
import net.minestom.server.instance.block.rule.BlockPlacementRule
import net.minestom.server.tag.Tag
import net.minestom.server.utils.Direction

class GlassPane(val pane: Block) : BlockPlacementRule(pane) {
    override fun blockUpdate(instance: Instance, blockPosition: Point, block: Block): Block {
        return update(instance, block, blockPosition)
    }

    val directions = listOf(Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH)

    fun update(instance: Instance, block: Block, blockPosition: Point): Block {
        return directions.fold(block) { loopBlock, dir ->

            val position = blockPosition.add(dir.normalX().toDouble(), dir.normalY().toDouble(), dir.normalZ().toDouble())
            val foundBlock = instance.getBlock(position)

            if (foundBlock.isSolid) {

                return@fold loopBlock.withProperty(dir.name.lowercase(), "true")
            }

            return@fold loopBlock.withProperty(dir.name.lowercase(), "false")
        }
    }

    override fun blockPlace(
        instance: Instance,
        block: Block, blockFace: BlockFace, blockPosition: Point,
        pl: Player
    ) = update(instance, block, blockPosition)

    companion object {
        val handlers = listOf(
            GlassPane(Block.GLASS_PANE),
            GlassPane(Block.BLACK_STAINED_GLASS_PANE),
            GlassPane(Block.BLUE_STAINED_GLASS_PANE),
            GlassPane(Block.BROWN_STAINED_GLASS_PANE),
            GlassPane(Block.CYAN_STAINED_GLASS_PANE),
            GlassPane(Block.GRAY_STAINED_GLASS_PANE),
            GlassPane(Block.GREEN_STAINED_GLASS_PANE),
            GlassPane(Block.LIGHT_BLUE_STAINED_GLASS_PANE),
            GlassPane(Block.LIGHT_GRAY_STAINED_GLASS_PANE),
            GlassPane(Block.LIME_STAINED_GLASS_PANE),
            GlassPane(Block.MAGENTA_STAINED_GLASS_PANE),
            GlassPane(Block.ORANGE_STAINED_GLASS_PANE),
            GlassPane(Block.YELLOW_STAINED_GLASS_PANE),
            GlassPane(Block.WHITE_STAINED_GLASS_PANE),
            GlassPane(Block.PURPLE_STAINED_GLASS_PANE),
        )
    }
}