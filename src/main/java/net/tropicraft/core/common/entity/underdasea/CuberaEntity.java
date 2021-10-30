package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;

public final class CuberaEntity extends AbstractFishEntity {
    public CuberaEntity(EntityType<? extends CuberaEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return AbstractFishEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    public void calculateEntityAnimation(LivingEntity entity, boolean verticalMovement) {
        // apply swimming animation when moving vertically
        super.calculateEntityAnimation(entity, true);
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        return ActionResultType.PASS;
    }

    @Override
    protected ItemStack getBucketItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.CUBERA_SPAWN_EGG.get());
    }
}
