package net.tropicraft.core.common.block;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorSimplex;

public class BlockSeaweed extends BlockTropicraft {

	public static class TileSeaweed extends TileEntity {

		private static final Random rand = new Random();
		static {
			rand.setSeed(439875L); // Random but constant seed
		}
		private static final NoiseGeneratorSimplex angleNoise = new NoiseGeneratorSimplex(rand);
		private static final NoiseGeneratorSimplex delayNoise = new NoiseGeneratorSimplex(rand);

		private int height = -1;
		private AxisAlignedBB cachedBB;
		private Vec3d offset;
		private double swayAngle;
		private double swayDelay;

		@Override
		public AxisAlignedBB getRenderBoundingBox() {
			if (height < 0) {
				rand.setSeed(MathHelper.getPositionRandom(getPos()));
				height = rand.nextInt(10) + 5;
				while (height > 0 && !getWorld().getBlockState(getPos().up(height)).getMaterial().isLiquid()) {
					height--;
				}
				cachedBB = new AxisAlignedBB(getPos()).expand(1.1, height / 2f, 1.1).offset(0, height / 2f, 0);
				
				offset = new Vec3d((rand.nextFloat() - 0.5f) * 0.75f, 0, (rand.nextFloat() - 0.5f) * 0.75f);
				
				Vec3d centerPos = new Vec3d(getPos()).addVector(0.5, 0.5, 0.5).add(offset);
				
				swayAngle = angleNoise.getValue(centerPos.xCoord / 50, centerPos.zCoord / 50);
				swayAngle += 1; // convert to 0..2
				swayAngle *= Math.PI; // convert to 0..2PI
				
				swayDelay = delayNoise.getValue(centerPos.xCoord / 25, centerPos.zCoord / 25);
				swayDelay *= 20;
			}
			return cachedBB;
		}
		
		@Override
		public double getMaxRenderDistanceSquared() {
			return super.getMaxRenderDistanceSquared() * 4;
		}

		public int getHeight() {
			return height;
		}
		
		public Vec3d getOffset() {
			return offset;
		}
		
		/** The angle of sway, in radians, on 0..2PI */
		public double getSwayAngle() {
			return swayAngle;
		}
		
		/** The amount of delay (offset from tick count) for the sway animation, in ticks */
		public double getSwayDelay() {
			return swayDelay;
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
