package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class VolcanicSandBlock extends BlockTropicraftSand {

	public static final BooleanProperty HOT = BooleanProperty.create("hot");

	public VolcanicSandBlock(Block.Properties properties) {
		super(properties);
		this.registerDefaultState(defaultBlockState().setValue(HOT, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(HOT);
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		if (state.getValue(HOT)) {
			if (entity instanceof LivingEntity) {
				final LivingEntity living = (LivingEntity) entity;
				final ItemStack stack = living.getItemBySlot(EquipmentSlot.FEET);

				// If entity isn't wearing anything on their feetsies
				if (stack.isEmpty()) {
					living.hurt(DamageSource.LAVA, 0.5F);
				}
			} else {
				entity.hurt(DamageSource.LAVA, 0.5F);
			}
		}
	}
}
