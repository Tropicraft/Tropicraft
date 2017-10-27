package net.tropicraft.core.common.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.enums.TropicraftBongos;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.SoundRegistry;

@EventBusSubscriber
public class BlockBongoDrum extends BlockTropicraft implements ITropicraftBlock {

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

    public static final PropertyEnum<TropicraftBongos> VARIANT = PropertyEnum.create("variant", TropicraftBongos.class);

    public BlockBongoDrum() {
        super(Material.CIRCUITS);
        this.setHardness(1.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, TropicraftBongos.SMALL));
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {        
        for (int i = 0; i < TropicraftBongos.VALUES.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        TropicraftBongos bongo = ((TropicraftBongos) state.getValue(VARIANT));
        switch (bongo) {
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

        playBongoSound(world, playerIn, pos, state);
        return true;
    }
    
    @SubscribeEvent
    public static void onBlockLeftClick(LeftClickBlock event) {
        World world = event.getWorld();
        IBlockState state = world.getBlockState(event.getPos());
        if (state.getBlock() == BlockRegistry.bongo && event.getFace() == EnumFacing.UP) {
            ((BlockBongoDrum)BlockRegistry.bongo).playBongoSound(world, event.getEntityPlayer(), event.getPos(), state);
        }
    }

    /**
     * Play the bongo sound in game. Sound played determined by the {@link #size} attribute
     */
    private void playBongoSound(World world, EntityPlayer entity, BlockPos pos, IBlockState state) {
        TropicraftBongos bongo = ((TropicraftBongos) state.getValue(VARIANT));
        world.playSound(entity, pos.getX(), pos.getY() + 0.5D, pos.getZ(), SoundRegistry.get(bongo.getSoundRegistryName()), SoundCategory.BLOCKS, 1.0F, 1.0F);
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

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, TropicraftBongos.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((TropicraftBongos) state.getValue(VARIANT)).ordinal();
    }

    @Override
    public String getStateName(IBlockState state) {
        return ((TropicraftBongos) state.getValue(VARIANT)).getName();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }
}
