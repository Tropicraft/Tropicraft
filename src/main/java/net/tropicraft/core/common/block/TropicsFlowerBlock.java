package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TropicsFlowerBlock extends FlowerBlock {

    private final TropicraftFlower flower;
    private final VoxelShape shape;

    public TropicsFlowerBlock(TropicraftFlower flower, Holder<MobEffect> effect, int effectDuration, VoxelShape shape, Properties properties) {
        super(effect, effectDuration, properties);
        this.flower = flower;
        this.shape = shape;
    }

    @Override
    public MapCodec<TropicsFlowerBlock> codec() {
        throw new UnsupportedOperationException();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(pos);
        return shape.move(offset.x, offset.y, offset.z);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (this.flower == TropicraftFlower.MAGIC_MUSHROOM) {
            return 10;
        }

        return super.getLightEmission(state, level, pos);
    }
}
