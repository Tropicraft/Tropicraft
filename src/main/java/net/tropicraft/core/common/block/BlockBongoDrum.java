package net.tropicraft.core.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.core.registry.SoundRegistry;

public class BlockBongoDrum extends BlockTropicraft {

    public static final float SMALL_DRUM_SIZE = 0.5f;
    public static final float MEDIUM_DRUM_SIZE = 0.6f;
    public static final float BIG_DRUM_SIZE = 0.7f;

    public static final float SMALL_DRUM_OFFSET = (1.0f - SMALL_DRUM_SIZE)/2.0f;
    public static final float MEDIUM_DRUM_OFFSET = (1.0f - MEDIUM_DRUM_SIZE)/2.0f;
    public static final float BIG_DRUM_OFFSET = (1.0f - BIG_DRUM_SIZE)/2.0f;
    public static final float DRUM_HEIGHT = 1.0f;

    protected final AxisAlignedBB BONGO_SMALL_AABB = new AxisAlignedBB(SMALL_DRUM_OFFSET, 0.0f, SMALL_DRUM_OFFSET, 1 - SMALL_DRUM_OFFSET, DRUM_HEIGHT, 1 - SMALL_DRUM_OFFSET);
    protected final AxisAlignedBB BONGO_MEDIUM_AABB = new AxisAlignedBB(MEDIUM_DRUM_OFFSET, 0.0f, MEDIUM_DRUM_OFFSET, 1 - MEDIUM_DRUM_OFFSET, DRUM_HEIGHT, 1 - MEDIUM_DRUM_OFFSET);
    protected final AxisAlignedBB BONGO_LARGE_AABB = new AxisAlignedBB(BIG_DRUM_OFFSET, 0.0f, BIG_DRUM_OFFSET, 1-BIG_DRUM_OFFSET, DRUM_HEIGHT, 1-BIG_DRUM_OFFSET);

    private BongoSize size;

    public BlockBongoDrum(BongoSize size) {
        super(Material.CIRCUITS);
        this.setHardness(1.0F);
        this.size = size;
        this.setLightOpacity(255);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (size) {
        case LARGE:
            return BONGO_LARGE_AABB;
        case MEDIUM:
            return BONGO_MEDIUM_AABB;
        case SMALL:
        default:
            return BONGO_SMALL_AABB;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        // Only play drum sound if player hits the top
        if (side != EnumFacing.UP) {
            return false;
        }

        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        playBongoSound(world, x,y, z);

        return true;
    }

    /**
     * Play the bongo sound in game. Sound played determined by the {@link #size} attribute
     */
    private void playBongoSound(World world, int x, int y, int z) {
        world.playSound((EntityPlayer)null, x, y + 0.5D, z, SoundRegistry.get(size.getSoundRegistryName()), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return false;
        }

        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    /**
     * Location aware and overrideable version of the lightOpacity array,
     * return the number to subtract from the light value when it passes through this block.
     *
     * This is not guaranteed to have the tile entity in place before this is called, so it is
     * Recommended that you have your tile entity call relight after being placed if you
     * rely on it for light info.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @return The amount of light to block, 0 for air, 255 for fully opaque.
     */
    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 255;
    }

    public static enum BongoSize {
        SMALL("bongohigh"), MEDIUM("bongomedium"), LARGE("bongolow");

        private String soundRegistryName;

        BongoSize(String soundRegistryName) {
            this.soundRegistryName = soundRegistryName;
        }

        public String getSoundRegistryName() {
            return this.soundRegistryName;
        }
    }
}
