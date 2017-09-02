package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;
import net.tropicraft.core.common.block.tileentity.TileEntityTropicraftFlowerPot;
import net.tropicraft.core.common.enums.TropicraftFlowerType;
import net.tropicraft.core.common.enums.TropicraftFlowers;
import net.tropicraft.core.common.enums.TropicraftSaplings;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockTropicraftFlowerPot extends BlockTropicraft implements ITileEntityProvider {

	protected static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);
	public static final PropertyEnum<TropicraftFlowerType> CONTENTS = PropertyEnum.<TropicraftFlowerType>create("contents", TropicraftFlowerType.class);

	public BlockTropicraftFlowerPot() {
		super(Material.CIRCUITS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(CONTENTS, TropicraftFlowerType.EMPTY));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FLOWER_POT_AABB;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/**
	 * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
	 */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {		
		if (heldItem != null && heldItem.getItem() instanceof ItemBlock) {
			TileEntityTropicraftFlowerPot flowerPotTE = this.getTileEntity(worldIn, pos);
			
			if (flowerPotTE == null) {
				return false;
			} else if (flowerPotTE.getFlowerPotItem() != null) {
				return false;
			} else {
				Block block = Block.getBlockFromItem(heldItem.getItem());

				if (!this.canContain(block, heldItem.getMetadata())) {
					return false;
				} else {
					flowerPotTE.setFlowerPotData(heldItem.getItem(), heldItem.getMetadata());
					flowerPotTE.markDirty();
					worldIn.notifyBlockUpdate(pos, state, state, 3);
					playerIn.addStat(StatList.FLOWER_POTTED);

					if (!playerIn.capabilities.isCreativeMode) {
						--heldItem.stackSize;
					}

					return true;
				}
			}
		}
		else
		{
			return false;
		}
	}

	private boolean canContain(@Nullable Block block, int meta) {
		return block == BlockRegistry.flowers ||
				block == BlockRegistry.iris ||
				block == BlockRegistry.pineapple ||
				block == BlockRegistry.saplings;
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityTropicraftFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);

		if (tileentityflowerpot != null)
		{
			ItemStack itemstack = tileentityflowerpot.getFlowerItemStack();

			if (itemstack != null)
			{
				return itemstack;
			}
		}

		return new ItemStack(Items.FLOWER_POT);
	}

	@Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.getBlockState(pos.down()).isFullyOpaque();
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (!worldIn.getBlockState(pos.down()).isFullyOpaque()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }
	
	@Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);

        if (player.capabilities.isCreativeMode) {
            TileEntityTropicraftFlowerPot tileentityflowerpot = this.getTileEntity(worldIn, pos);

            if (tileentityflowerpot != null) {
                tileentityflowerpot.setFlowerPotData((Item)null, 0);
            }
        }
    }
    
    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemRegistry.flowerPot;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {CONTENTS});
    }

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((TropicraftFlowerType) state.getValue(CONTENTS)).ordinal();
	}

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(CONTENTS, TropicraftFlowerType.VALUES[meta]);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TropicraftFlowerType flowerType = TropicraftFlowerType.EMPTY;
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityTropicraftFlowerPot) {
            TileEntityTropicraftFlowerPot tileentityflowerpot = (TileEntityTropicraftFlowerPot)tileentity;
            Item item = tileentityflowerpot.getFlowerPotItem();

            if (item instanceof ItemBlock) {
                int i = tileentityflowerpot.getFlowerPotData();
                Block block = Block.getBlockFromItem(item);

                if (block == BlockRegistry.saplings) {
                    switch (TropicraftSaplings.byMetadata(i))
                    {
//                        case PALM:
//                            flowerType = TropicraftFlowerType.PALM_SAPLING;
//                            break;
//                        case MAHOGANY:
//                            flowerType = TropicraftFlowerType.MAHOGANY_SAPLING;
//                            break;
//                        case GRAPEFRUIT:
//                            flowerType = TropicraftFlowerType.GRAPEFRUIT_SAPLING;
//                            break;
//                        case LEMON:
//                            flowerType = TropicraftFlowerType.LEMON_SAPLING;
//                            break;
//                        case LIME:
//                            flowerType = TropicraftFlowerType.LIME_SAPLING;
//                            break;
//                        case ORANGE:
//                            flowerType = TropicraftFlowerType.ORANGE_SAPLING;
//                            break;
                        default:
                            flowerType = TropicraftFlowerType.EMPTY;
                    }
                }
                else if (block == BlockRegistry.flowers)
                {
                    switch (TropicraftFlowers.VALUES[i])
                    {
                    case COMMELINA_DIFFUSA:
                    	flowerType = TropicraftFlowerType.COMMELINA_DIFFUSA;
                    	break;
                    case CROCOSMIA:
                    	flowerType = TropicraftFlowerType.CROCOSMIA;
                    	break;
                    case ORCHID:
                    	flowerType = TropicraftFlowerType.ORCHID;
                    	break;
                    case CANNA:
                    	flowerType = TropicraftFlowerType.CANNA;
                    	break;
                    case ANEMONE:
                    	flowerType = TropicraftFlowerType.ANEMONE;
                    	break;
                    case ORANGE_ANTHURIUM:
                    	flowerType = TropicraftFlowerType.ORANGE_ANTHURIUM;
                    	break;
                    case RED_ANTHURIUM:
                    	flowerType = TropicraftFlowerType.RED_ANTHURIUM;
                    	break;
                    case MAGIC_MUSHROOM:
                    	flowerType = TropicraftFlowerType.MAGIC_MUSHROOM;
                    	break;
                    case PATHOS:
                    	flowerType = TropicraftFlowerType.PATHOS;
                    	break;
                    case ACAI_VINE:
                    	flowerType = TropicraftFlowerType.ACAI_VINE;
                    	break;
                    case CROTON:
                    	flowerType = TropicraftFlowerType.CROTON;
                    	break;
                    case DRACAENA:
                    	flowerType = TropicraftFlowerType.DRACAENA;
                    	break;
                    case FERN:
                    	flowerType = TropicraftFlowerType.FERN;
                    	break;
                    case FOLIAGE:
                    	flowerType = TropicraftFlowerType.FOILAGE;
                    	break;
                    case BROMELIAD:
                    	flowerType = TropicraftFlowerType.BROMELIAD;
                    	break;
                    default:
                        flowerType = TropicraftFlowerType.EMPTY;
                    }
                }
            }
        }

        return state.withProperty(CONTENTS, flowerType);
    }
    
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return TileEntityFactory.getFlowerPotTE();
    }
    
    @Nullable
    private TileEntityTropicraftFlowerPot getTileEntity(World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityTropicraftFlowerPot ? (TileEntityTropicraftFlowerPot)tileentity : null;
    }

}
