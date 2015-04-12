package net.tropicraft.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.Tropicraft;
import net.tropicraft.client.gui.GuiTropicalBook;
import net.tropicraft.encyclopedia.TropicalBook;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropBook extends ItemTropicraft {

	private String bookName;

	public ItemTropBook(TropicalBook book, String name) {
		super();
		this.bookName = name;
		maxStackSize = 1;
		this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
	}

	public ItemTropBook(String name) {
		this(null, name);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (world.isRemote && getTropBook() != null) {
			System.err.println("Gui");
			getTropBook().updatePagesFromInventory(entityplayer.inventory);
			FMLCommonHandler.instance().showGuiScreen(new GuiTropicalBook(getTropBook()));     
		}

		return itemstack;
	}

	@SideOnly(Side.CLIENT)
	private TropicalBook getTropBook() {
		return Tropicraft.encyclopedia;
	}
}