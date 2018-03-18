package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.ChatUtil;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;

public class BlockTropicsPortal extends BlockFluidClassic {

	public static final PropertyBool TELEPORTABLE = PropertyBool.create("teleportable");

	/** Amount of time player must spend in teleport block to teleport */
	private static final int TIME_UNTIL_TELEPORT = 20;
	
	public BlockTropicsPortal(Fluid fluid, Material material, boolean canTeleport) {
		super(fluid, material);
		this.setCreativeTab(null);
		setTickRandomly(true);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0).withProperty(TELEPORTABLE, canTeleport));
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
					state.getValue(TELEPORTABLE)) {
				if (player.isPotionActive(MobEffects.NAUSEA)) {
					player.timeUntilPortal = 0;
					player.removePotionEffect(MobEffects.NAUSEA);
					TropicraftWorldUtils.teleportPlayer(player);
				} else {
                    player.timeUntilPortal = 0;
                    if (world.getTotalWorldTime() % 20 == 0) {
                        ChatUtil.sendNoSpamUnloc(player, "tropicraft.chat.teleport.unable");
                    }
                }
            }
        }
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
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
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
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

        if (!state.getValue(TELEPORTABLE) && world.isRemote) {
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

	/** These 5 methods below are a copypasta from BlockLiquid to patch the issue of tropics water pulling your down too much **/

	@Override
	@Nonnull
	public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion)
	{
		return motion.add(this.getFlow(worldIn, pos, worldIn.getBlockState(pos)));
	}

	protected Vec3d getFlow(IBlockAccess worldIn, BlockPos pos, IBlockState state)
	{
		double d0 = 0.0D;
		double d1 = 0.0D;
		double d2 = 0.0D;
		int i = this.getRenderedDepth(state);
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
		{
			blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing);
			int j = this.getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos));

			if (j < 0)
			{
				if (!worldIn.getBlockState(blockpos$pooledmutableblockpos).getMaterial().blocksMovement())
				{
					j = this.getRenderedDepth(worldIn.getBlockState(blockpos$pooledmutableblockpos.down()));

					if (j >= 0)
					{
						int k = j - (i - 8);
						d0 += (double)(enumfacing.getFrontOffsetX() * k);
						d1 += (double)(enumfacing.getFrontOffsetY() * k);
						d2 += (double)(enumfacing.getFrontOffsetZ() * k);
					}
				}
			}
			else if (j >= 0)
			{
				int l = j - i;
				d0 += (double)(enumfacing.getFrontOffsetX() * l);
				d1 += (double)(enumfacing.getFrontOffsetY() * l);
				d2 += (double)(enumfacing.getFrontOffsetZ() * l);
			}
		}

		Vec3d vec3d = new Vec3d(d0, d1, d2);

		if (((Integer)state.getValue(LEVEL)).intValue() >= 8)
		{
			for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
			{
				blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing1);

				if (this.causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos, enumfacing1) || this.causesDownwardCurrent(worldIn, blockpos$pooledmutableblockpos.up(), enumfacing1))
				{
					vec3d = vec3d.normalize().addVector(0.0D, -6.0D, 0.0D);
					break;
				}
			}
		}

		blockpos$pooledmutableblockpos.release();
		return vec3d.normalize();
	}

	protected int getDepth(IBlockState state)
	{
		return state.getMaterial() == this.blockMaterial ? ((Integer)state.getValue(LEVEL)).intValue() : -1;
	}

	protected int getRenderedDepth(IBlockState state)
	{
		int i = this.getDepth(state);
		return i >= 8 ? 0 : i;
	}

	/**
	 * Checks if an additional {@code -6} vertical drag should be applied to the entity. See {#link
	 * net.minecraft.block.BlockLiquid#getFlow()}
	 */
	private boolean causesDownwardCurrent(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();
		Material material = iblockstate.getMaterial();

		if (material == this.blockMaterial)
		{
			return false;
		}
		else if (side == EnumFacing.UP)
		{
			return true;
		}
		else if (material == Material.ICE)
		{
			return false;
		}
		else
		{
			boolean flag = isExceptBlockForAttachWithPiston(block) || block instanceof BlockStairs;
			return !flag && iblockstate.getBlockFaceShape(worldIn, pos, side) == BlockFaceShape.SOLID;
		}
	}
}
