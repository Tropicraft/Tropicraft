package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TropicsFlowerBlock extends FlowerBlock {

    private final VoxelShape shape;

    public TropicsFlowerBlock(MobEffect effect, int effectDuration, VoxelShape shape, Properties properties) {
        super(effect, effectDuration, properties);
        this.shape = shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(world, pos);
        return shape.move(offset.x, offset.y, offset.z);
    }
}
