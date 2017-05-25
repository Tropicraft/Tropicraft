package net.tropicraft.core.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Tropicraft;
import net.tropicraft.client.gui.GuiTropicalBook;
import net.tropicraft.core.encyclopedia.TropicalBook;

public class ItemEncyclopediaTropica extends ItemTropicraft {

	private String bookName;

	public ItemEncyclopediaTropica(TropicalBook book, String name) {
		super();
		this.bookName = name;
		maxStackSize = 1;
	}

	public ItemEncyclopediaTropica(String name) {
		this(null, name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer, EnumHand hand) {
		if (world.isRemote && getTropBook() != null) {
			System.err.println("Gui");
			getTropBook().updatePagesFromInventory(entityplayer.inventory);
			FMLCommonHandler.instance().showGuiScreen(new GuiTropicalBook(getTropBook()));     
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	@SideOnly(Side.CLIENT)
	private TropicalBook getTropBook() {
		return Tropicraft.encyclopedia;
	}
}
