package net.tropicraft.block.scuba;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.block.BlockTropicraft;
import net.tropicraft.block.tileentity.TileEntityAirCompressor;
import net.tropicraft.factory.TileEntityFactory;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockAirCompressor extends BlockTropicraft implements ITileEntityProvider {

	public BlockAirCompressor() {
		super(Material.rock);
		this.setBlockBounds(0, 0, 0, 1, 1.8F, 1);
		this.isBlockContainer = true;
		this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
	}
	
	@Override
    public int getRenderType() {
        return TCRenderIDs.airCompressor;
    }
	
	/**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }
    
    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase e, ItemStack par6ItemStack)
    {
        int var6 = MathHelper.floor_double((double)(e.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        int meta = 0;
        if (var6 == 0) {
            meta = 2;
        } else if (var6 == 1) {
            meta = 5;
        } else if (var6 == 2) {
            meta = 3;
        } else if (var6 == 3) {
            meta = 4;
        }

        par1World.setBlockMetadataWithNotify(par2, par3, par4, meta, 2);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
        if (world.isRemote) {
            return true;
        }

        ItemStack stack = entityPlayer.getCurrentEquippedItem();

        TileEntityAirCompressor mixer = (TileEntityAirCompressor)world.getTileEntity(x, y, z);

        if (mixer.isDoneCompressing()) {
            mixer.ejectTank();
            return true;
        }

        if (stack == null) {
            mixer.ejectTank();
            return true;
        }

        ItemStack ingredientStack = stack.copy();
        ingredientStack.stackSize = 1;

        if (mixer.addTank(ingredientStack)) {
            entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
        }

        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (!world.isRemote) {
            TileEntityAirCompressor te = (TileEntityAirCompressor) world.getTileEntity(x, y, z);
            te.ejectTank();
        }

        super.breakBlock(world, x, y, z, block, meta);
    }
	
    /**
     * How many world ticks before ticking
     */
	@Override
    public int tickRate(World world) {
        return 4;
    }
	
    /**
     * Ticks the block if it's been scheduled
     */
	@Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote) {
            
        }
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return TileEntityFactory.getAirCompressorTE();
	}
	
	/**
     * Register all Icons used in this block
     */
    @Override
    public void registerBlockIcons(IIconRegister iconRegistry) {
        this.blockIcon = iconRegistry.registerIcon(TCInfo.ICON_LOCATION + "chunk");
    }

}
