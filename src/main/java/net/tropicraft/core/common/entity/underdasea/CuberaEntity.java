package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class CuberaEntity extends AbstractFish {
    public CuberaEntity(EntityType<? extends CuberaEntity> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractFish.createAttributes()
                .add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    public void calculateEntityAnimation(boolean verticalMovement) {
        // apply swimming animation when moving vertically
        super.calculateEntityAnimation(true);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }
}
