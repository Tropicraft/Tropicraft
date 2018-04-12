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

	public ItemEncyclopediaTropica(TropicalBook book) {
		super();
		maxStackSize = 1;
	}

	public ItemEncyclopediaTropica() {
		this(null);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityplayer, EnumHand hand) {
	    ItemStack stack = entityplayer.getHeldItem(hand);
		if (world.isRemote && getTropBook() != null) {
			getTropBook().discoverPages(world, entityplayer);
			FMLCommonHandler.instance().showGuiScreen(new GuiTropicalBook(getTropBook()));     
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@SideOnly(Side.CLIENT)
	private TropicalBook getTropBook() {
		return Tropicraft.encyclopedia;
	}
}
