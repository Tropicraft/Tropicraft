package net.tropicraft.item.placeable;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.tropicraft.entity.placeable.EntityChair;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.item.ItemTropicraft;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemChair extends ItemTropicraft {

	@SideOnly(Side.CLIENT)
	private IIcon overlayIcon;

	public ItemChair() {
		super();
		this.setTextureName(TCNames.chair);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		this.itemIcon = par1IconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getIconString());
		this.overlayIcon = par1IconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getIconString() + "Inverted");
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
		return pass > 0 ? this.overlayIcon : super.getIconFromDamageForRenderPass(damage, pass);
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int pass) {
		Integer color = EntityChair.woolValues.inverse().get(par1ItemStack.getItemDamage());
		return (pass == 0 ? 16777215 : color.intValue());
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < EntityChair.woolValues.keySet().size(); i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName().replace("item.", String.format("item.%s:", TCInfo.MODID)).split(":")[0]
				+ ":" + TCNames.chair + "_" + ItemDye.field_150923_a[itemstack.getItemDamage()] + ".name")).trim();

		return s;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		int color = EntityChair.woolValues.inverse().get(itemstack.getItemDamage());
		float f = 1.0F;
		float f1 = entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * f;
		float f2 = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f;
		double d = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double) f;
		double d1 = (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double) f + 1.6200000000000001D) - (double) entityplayer.yOffset;
		double d2 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double) f;
		Vec3 vec3d = Vec3.createVectorHelper(d, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
		float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
		float f5 = -MathHelper.cos(-f1 * 0.01745329F);
		float f6 = MathHelper.sin(-f1 * 0.01745329F);
		float f7 = f4 * f5;
		float f8 = f6;
		float f9 = f3 * f5;
		double d3 = 5D;


		Vec3 vec3d1 = vec3d.addVector((double) f7 * d3, (double) f8 * d3, (double) f9 * d3);
		MovingObjectPosition movingobjectposition = world.rayTraceBlocks(vec3d, vec3d1, true);
		if (movingobjectposition == null) {
			return itemstack;
		}
		if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK && movingobjectposition.sideHit == 1) {
			int i = movingobjectposition.blockX;
			int j = movingobjectposition.blockY;
			int k = movingobjectposition.blockZ;
			if (!world.isRemote) {
				if (world.getBlock(i, j, k) == Blocks.snow) {
					j--;
				}

				world.spawnEntityInWorld(new EntityChair(world, i, j + 1.01, k, color, entityplayer));

			}

			if(!entityplayer.capabilities.isCreativeMode)
				itemstack.stackSize--;
		}
		return itemstack;
	}

}
