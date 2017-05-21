package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0).withProperty(TELEPORTABLE, Integer.valueOf(0)));
	}
	
    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] { LEVEL, TELEPORTABLE }, FLUID_RENDER_PROPS.toArray(new IUnlistedProperty<?>[0]));
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

					if (messageTick % 100 == 0) {
						player.addChatMessage(new TextComponentTranslation("You should drink a pi\u00f1a colada before teleporting!"));
					}
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {

	}

	/**
	 * Called when a player removes a block.  This is responsible for
	 * actually destroying the block, and the block is intact at time of call.
	 * This is called regardless of whether the player can harvest the block or
	 * not.
	 *
	 * Return true if the block is actually destroyed.
	 *
	 * Note: When used in multiplayer, this is called on both client and
	 * server sides!
	 *
	 * @param state The current state.
	 * @param world The current world
	 * @param player The player damaging the block, may be null
	 * @param pos Block position in world
	 * @param willHarvest True if Block.harvestBlock will be called after this, if the return in true.
	 *        Can be useful to delay the destruction of tile entities till after harvestBlock
	 * @return True if the block is actually destroyed.
	 */
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		return false;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity entity, Explosion explosion) {
		return Float.MAX_VALUE;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (world.isRemote) {
			sparkle(state, world, pos);
		}
	}

    /**
     * Renders particle effects in the water to give it a shimmer
     * @param world
     * @param i
     * @param j
     * @param k
     */
    @SideOnly(Side.CLIENT)
    private void sparkle(IBlockState state, World world, BlockPos pos) {
        Random random = world.rand;

        int maxCount = 2;

        if (state.getValue(TELEPORTABLE) == 0 && world.isRemote) {
            for (int count = 0; count < maxCount; count++) {
            	double gx = pos.getX() + random.nextDouble();
            	double gy = pos.getY() + random.nextDouble();
            	double gz = pos.getZ() + random.nextDouble();
                world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, gx, gy, gz, 0D, 0D, 0D);
            }
        }

        if (world.isAirBlock(pos.up()) && world.isRemote) {
            for (int count = 0; count < maxCount; count++) {
            	double gx = pos.getX() + random.nextDouble();
            	double gy = pos.getY() + 0.9;
            	double gz = pos.getZ() + random.nextDouble();
            	world.spawnParticle(EnumParticleTypes.WATER_SPLASH, gx, gy, gz, 0D, 0D, 0D);
            }
        }
    }
}
