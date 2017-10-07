package net.tropicraft.core.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import scala.util.Random;

public class BlockSeaweed extends BlockTropicraft {

	public static class TileSeaweed extends TileEntity {

		private static final Random rand = new Random();

		private int height = -1;
		private AxisAlignedBB cachedBB;
		private Vec3d offset;

		@Override
		public AxisAlignedBB getRenderBoundingBox() {
			if (height < 0) {
				rand.setSeed(MathHelper.getPositionRandom(getPos()));
				height = rand.nextInt(10) + 5;
				while (height > 0 && !getWorld().getBlockState(getPos().up(height)).getMaterial().isLiquid()) {
					height--;
				}
				cachedBB = new AxisAlignedBB(getPos()).expand(0, height, 0);
				offset = new Vec3d((rand.nextFloat() - 0.5f) * 0.75f, 0, (rand.nextFloat() - 0.5f) * 0.75f);
			}
			return cachedBB;
		}
		
		public int getHeight() {
			return height;
		}
		
		public Vec3d getOffset() {
			return offset;
		}
	}

	public BlockSeaweed() {
		super(Material.SAND);
		this.setSoundType(SoundType.SAND);
		this.setHardness(0.5f);
		this.setHarvestLevel("shovel", 0);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileSeaweed();
	}
}
