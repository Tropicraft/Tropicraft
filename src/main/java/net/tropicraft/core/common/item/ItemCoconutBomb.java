package net.tropicraft.core.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.tropicraft.configuration.ConfigMisc;
import net.tropicraft.core.common.entity.projectile.EntityCoconutGrenade;

public class ItemCoconutBomb extends ItemTropicraft {

	public ItemCoconutBomb() {
		super();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemstack, World world, EntityPlayer player, EnumHand hand) {
		itemstack.stackSize--;
		world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
		if (!world.isRemote && ConfigMisc.coconutBombWhitelistedUsers.contains(player.getGameProfile().getName())) {
			world.spawnEntityInWorld(new EntityCoconutGrenade(world, player));
		} else {
			if (!world.isRemote && !ConfigMisc.coconutBombWhitelistedUsers.contains(player.getGameProfile().getName()))
				player.addChatMessage(new TextComponentTranslation(I18n.translateToLocal("tropicraft.coconutBombWarning")));
		}


		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	private String getName(EntityPlayer player) {
		return player.getCommandSenderEntity().getName();
	}
}
