package net.tropicraft.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.tropicraft.entity.underdasea.EntityTropicalFish;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCFluidRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFishBucket extends ItemTropicraft {

	public ItemFishBucket() {
		super();
		maxStackSize = 1;
		hasSubtypes = true;
		setCreativeTab(TCCreativeTabRegistry.tabMisc);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (!world.isRemote) {
			EntityTropicalFish fish = new EntityTropicalFish(world); //1.6.2 edited to adapt to new constructor, + 2 lines below
			fish.setPosition(entityplayer.posX, entityplayer.posY, entityplayer.posZ);
			fish.setColor(itemstack.getItemDamage());
			fish.disableDespawning();
			world.spawnEntityInWorld(fish);
		}

		ItemStack stack = new ItemStack(TCItemRegistry.bucketTropicsWater);
		TCItemRegistry.bucketTropicsWater.fill(stack, new FluidStack(TCFluidRegistry.tropicsWater, FluidContainerRegistry.BUCKET_VOLUME), true);

		return stack;
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
		for (int index = 0; index < EntityTropicalFish.names.length; ++index) {
			par3List.add(new ItemStack(item, 1, index));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		return EntityTropicalFish.names[itemstack.getItemDamage()];
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer ent, List list, boolean wat) {
		list.clear();
		list.add(EntityTropicalFish.names[itemstack.getItemDamage()]);
	}
}