package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.portal.DimensionTransition;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import javax.annotation.Nullable;

public final class PortalWaterBlock extends LiquidBlock implements Portal {
    public PortalWaterBlock(Properties builder) {
        super(Fluids.WATER, builder);
    }

    @Override
    public MapCodec<LiquidBlock> codec() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(2) == 0) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();
            level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + 0.5, y, z + 0.5, 0.0, 0.04, 0.0);
            level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + random.nextFloat(), y + random.nextFloat(), z + random.nextFloat(), 0.0, 0.04, 0.0);
        }
    }

    @Override
    protected void entityInside(final BlockState state, final Level level, final BlockPos pos, final Entity entity) {
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public int getPortalTransitionTime(final ServerLevel level, final Entity entity) {
        return entity instanceof Player ? SharedConstants.TICKS_PER_SECOND * 4 : 0;
    }

    @Nullable
    @Override
    public DimensionTransition getPortalDestination(final ServerLevel level, final Entity entity, final BlockPos pos) {
        return TropicraftDimension.getPortalTransition(level, entity, TropicraftDimension.WORLD);
    }
}
