package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public void stepOn(final World world, final BlockPos pos, final Entity entity) {
		final BlockState state = world.getBlockState(pos);
		if (state.getValue(HOT)) {
			if (entity instanceof LivingEntity) {
				final LivingEntity living = (LivingEntity) entity;
				final ItemStack stack = living.getItemBySlot(EquipmentSlotType.FEET);
	
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
