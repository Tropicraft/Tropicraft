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
		this.setDefaultState(getDefaultState().with(HOT, false));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(HOT);
	}

	@Override
	public void onEntityWalk(final World world, final BlockPos pos, final Entity entity) {
		final BlockState state = world.getBlockState(pos);
		if (state.get(HOT)) {
			if (entity instanceof LivingEntity) {
				final LivingEntity living = (LivingEntity) entity;
				final ItemStack stack = living.getItemStackFromSlot(EquipmentSlotType.FEET);
	
				// If entity isn't wearing anything on their feetsies
				if (stack.isEmpty()) {
					living.attackEntityFrom(DamageSource.LAVA, 0.5F);
				}
			} else {
				entity.attackEntityFrom(DamageSource.LAVA, 0.5F);
			}
		}
	}
}
