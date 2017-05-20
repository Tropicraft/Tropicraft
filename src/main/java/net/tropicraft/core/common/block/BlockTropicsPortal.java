package net.tropicraft.core.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

public class BlockTropicsPortal extends BlockFluidClassic {

	public static final PropertyInteger TELEPORTABLE = PropertyInteger.create("teleportable", 0, 1);

	/** Amount of time player must spend in teleport block to teleport */
	private static final int TIME_UNTIL_TELEPORT = 20;

	public int messageTick;

	public BlockTropicsPortal(Fluid fluid, Material material) {
		super(fluid, material);
		this.setCreativeTab(null);
		setTickRandomly(true);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
	}

	/**
	 * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
	 */
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (!world.isRemote && entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)entity;
			entity.setAir(300);
			player.timeUntilPortal++;

			if (player.timeUntilPortal > TIME_UNTIL_TELEPORT && 
					state.getValue(TELEPORTABLE) == 1) {
				if (player.isPotionActive(MobEffects.POISON)) {
					messageTick = 0;
					player.timeUntilPortal = 0;
					player.removePotionEffect(MobEffects.POISON);
					TropicraftWorldUtils.teleportPlayer(player);
				} else {
					messageTick++;
					player.timeUntilPortal = 0;

					if (messageTick % 50 == 0)
						player.addChatMessage(new TextComponentTranslation("You should drink a pi\u00f1a colada before teleporting!"));
				}
			}
		}
	}
}
