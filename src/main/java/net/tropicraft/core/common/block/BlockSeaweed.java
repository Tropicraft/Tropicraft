package net.tropicraft.core.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockSeaweed extends BlockTropicraft {

    public static class TileSeaweed extends TileEntity {

        private static final Random rand = new Random(439875L);

        private static final NoiseGeneratorPerlin angleNoise = new NoiseGeneratorPerlin(rand, 1);
        private static final NoiseGeneratorPerlin delayNoise = new NoiseGeneratorPerlin(rand, 3);

        private int height = -1;
        private AxisAlignedBB cachedBB;
        // Optifine can somehow cause TESR to be called before getRenderBoundingBox,
        // so this needs to be nonnull otherwise there is a potential NPE
        private Vec3d offset = Vec3d.ZERO;
        private double swayAngle;
        private double swayDelay;
        private int maxHeight;

        public TileSeaweed() {
            this.maxHeight = rand.nextInt(10) + 5;
        }

        public TileSeaweed(int startingHeight) {
            this.maxHeight = startingHeight;
        }

        /**
         * Called when this is first added to the world (by {@link World#addTileEntity(TileEntity)}).
         * Override instead of adding {@code if (firstTick)} stuff in update.
         */
        public void onLoad() {
            recalculateBB();
        }

	    public void recalculateBB() {
	        rand.setSeed(MathHelper.getCoordinateRandom(getPos().getX(), getPos().getY(), getPos().getZ()));
	        height = 0;
	        while (height <= maxHeight && getWorld().getBlockState(getPos().up(height + 1)).getMaterial().isLiquid()) {
	            height++;
	        }
	        cachedBB = new AxisAlignedBB(getPos()).grow(1.1, height / 2f, 1.1).offset(0, height / 2f, 0);

	        offset = new Vec3d((rand.nextFloat() - 0.5f) * 0.25f, 0, (rand.nextFloat() - 0.5f) * 0.25f);

	        Vec3d centerPos = new Vec3d(getPos()).addVector(0.5, 0.5, 0.5).add(offset);

	        swayAngle = angleNoise.getValue(centerPos.x / 200, centerPos.z / 200);
	        swayAngle += 1; // convert to 0..2
	        swayAngle *= Math.PI; // convert to 0..2PI

	        swayDelay = delayNoise.getValue(centerPos.x / 100, centerPos.z / 100);
	        swayDelay *= 20;
	    }

		@Override
		public AxisAlignedBB getRenderBoundingBox() {
			if (height < 0 || cachedBB == null) {
				recalculateBB();
			}
			return cachedBB;
		}
		
		@Override
		public double getMaxRenderDistanceSquared() {
			// Show more seaweed when the player is higher up
//			return MathHelper.clamp(((Minecraft.getMinecraft().getRenderViewEntity().posY - 40) / 12), 0.2, 4) * super.getMaxRenderDistanceSquared();
			return super.getMaxRenderDistanceSquared() * 2;
		}
		
		@Override
		public boolean hasFastRenderer() {
			return true;
		}
		
		public void setMaxHeight(int newMax) {
		    this.maxHeight = newMax;
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
		this.setTickRandomly(true);
	}
	
    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
     */
	@Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        grow(world, pos);
    }
	
	private void grow(IBlockAccess world, BlockPos pos) {
	    TileSeaweed tileEnt = (TileSeaweed)world.getTileEntity(pos);
	    int trueMax = TileSeaweed.rand.nextInt(10) + 5;
	    if (tileEnt.getHeight() < trueMax) {
	        tileEnt.setMaxHeight(tileEnt.getHeight() + 1);
	        tileEnt.recalculateBB();
	    }
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileSeaweed(0);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
	    return Item.getItemFromBlock(BlockRegistry.sands);
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return TropicraftSands.FOAMY.getMeta();
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
	}
	
    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
	@Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
	    TileSeaweed tileEnt = (TileSeaweed)world.getTileEntity(pos);
        int height = tileEnt.getHeight();
        
        for (int i = height; i > 0; i--) {
            ItemStack drop = new ItemStack(ItemRegistry.rawSeaweed);
            Block.spawnAsEntity(world, pos.up(i), drop);
        }
	    
	    super.breakBlock(world, pos, state);
    }
}
