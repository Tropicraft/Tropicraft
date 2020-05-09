package net.tropicraft.core.common.item.scuba;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PonyBottleItem extends Item {
    
    private static final int FILL_RATE = 6;
    
    public PonyBottleItem(Item.Properties properties) {
        super(properties);
    }
    
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.getAir() < player.getMaxAir()) {
            player.setActiveHand(hand);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        } else {
            return new ActionResult<>(ActionResultType.PASS, stack);
        }
    }
    
    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        super.onUsingTick(stack, player, count);
        int fillAmt = FILL_RATE + 1; // +1 to counteract the -1 per tick while underwater
        // Wait for drink sound to start, and don't add air that won't fit
        if (player.getItemInUseCount() <= 25 && player.getAir() < player.getMaxAir() - fillAmt) {
            player.setAir(player.getAir() + fillAmt);
            stack.damageItem(1, player, p -> p.sendBreakAnimation(p.getActiveHand()));
        }
    }
    
    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return !shouldCauseReequipAnimation(oldStack, newStack, false);
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }
}