package net.tropicraft.core.common.item;

import javax.annotation.Nullable;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityHook;
import net.tropicraft.core.common.entity.underdasea.atlantoku.RodLink;

public class ItemFishingRod extends Item {

	public ItemFishingRod() {
		this.setMaxDamage(64);
		this.setMaxStackSize(1);
		this.addPropertyOverride(TropicraftRenderUtils.getTexture("items/fishing_rod_cast"), new IItemPropertyGetter() {
			@Override
            @SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return entityIn == null ? 0.0F
						: (entityIn.getHeldItemMainhand() == stack && entityIn instanceof EntityPlayer
								&& RodLink.playerHasFloat((EntityPlayer)entityIn) ? 1.0F : 0.0F);
			}
		});
	}

	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	@Override
    @SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Returns true if this item should be rotated by 180 degrees around the Y axis
	 * when being held in an entities hands.
	 */
	@Override
    @SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (RodLink.playerHasFloat(playerIn)) {
			// int i = playerIn.fishEntity.handleHookRetraction();
			// itemStackIn.damageItem(i, playerIn);
			playerIn.swingArm(hand);
			if(!worldIn.isRemote)
				RodLink.destroyLink(RodLink.getLinkedHook(playerIn), playerIn);
		} else {
			worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ,
					SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F,
					0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if (!worldIn.isRemote) {
				EntityHook hook = new EntityHook(worldIn, playerIn);
				worldIn.spawnEntity(hook);
				hook.setAngler(playerIn);
				RodLink.createLink(hook, playerIn);
			}

			playerIn.swingArm(hand);
		}

		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	/**
	 * Checks isDamagable and if it cannot be stacked
	 */
	@Override
    public boolean isEnchantable(ItemStack stack) {
		return super.isEnchantable(stack);
	}

	/**
	 * Return the enchantability factor of the item, most of the time is based on
	 * material.
	 */
	@Override
    public int getItemEnchantability() {
		return 1;
	}
}