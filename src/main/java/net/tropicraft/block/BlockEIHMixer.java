package net.tropicraft.block;

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
import net.tropicraft.block.tileentity.TileEntityEIHMixer;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEIHMixer extends BlockTropicraft implements ITileEntityProvider {

	/**
	 * @param par1 block id
	 */
	public BlockEIHMixer() {
		super(Material.rock);
		this.setBlockBounds(0, 0, 0, 1, 1.8F, 1);
		this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
	}

	@Override
	public int getRenderType() {
		return TCRenderIDs.eihMixer;
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

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
		if (world.isRemote) {
			return true;
		}

		ItemStack stack = entityPlayer.getCurrentEquippedItem();

		TileEntityEIHMixer mixer = (TileEntityEIHMixer)world.getTileEntity(x, y, z);

		if (mixer.isDoneMixing()) {
			mixer.retrieveResult();
			return true;
		}

		if (stack == null) {
			mixer.emptyMixer();
			return true;
		}	

		ItemStack ingredientStack = stack.copy();
		ingredientStack.stackSize = 1;

		if (/*MixerRecipeRegistry.getInstance().isRegisteredIngredient(ingredientStack) &&*/ mixer.addToMixer(ingredientStack)) {
			entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
		}

		if (stack.getItem() == TCItemRegistry.bambooMug && mixer.canMix()) {
			mixer.startMixing();
			entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
		}

		return true;    	
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
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
		if (!world.isRemote) {
			TileEntityEIHMixer te = (TileEntityEIHMixer) world.getTileEntity(x, y, z);
			if (te.isDoneMixing()) {
				te.retrieveResult();
			} else {
				te.emptyMixer();
			}
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	/**
	 * Register all the icons of the block
	 * @param iconRegister Icon registry
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + "chunk");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityEIHMixer();
	}

}