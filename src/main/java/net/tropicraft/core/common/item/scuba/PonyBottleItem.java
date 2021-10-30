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
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.DRINK;
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }
    
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getAirSupply() < player.getMaxAirSupply()) {
            player.startUsingItem(hand);
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
        if (player.getUseItemRemainingTicks() <= 25 && player.getAirSupply() < player.getMaxAirSupply() - fillAmt) {
            player.setAirSupply(player.getAirSupply() + fillAmt);
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
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