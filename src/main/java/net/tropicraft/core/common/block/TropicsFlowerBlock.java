package net.tropicraft.core.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import net.minecraft.block.AbstractBlock.Properties;

public class TropicsFlowerBlock extends FlowerBlock {

    private final VoxelShape shape;

    public TropicsFlowerBlock(Effect effect, int effectDuration, VoxelShape shape, Properties properties) {
        super(effect, effectDuration, properties);
        this.shape = shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        Vector3d offset = state.getOffset(world, pos);
        return shape.move(offset.x, offset.y, offset.z);
    }
}
