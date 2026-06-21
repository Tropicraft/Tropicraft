package net.tropicraft.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.passive.FiddlerCrabEntity;
import net.tropicraft.core.common.item.component.TropicraftDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract RandomSource getRandom();

    // TODO: Can we just replace this with the bounciness attributes?
    @Inject(method = "restituteMovementAfterCollisions(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;ZZLnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
    private void fallOnBlock(BlockPos effectPos, BlockState effectState, boolean xCollision, boolean zCollision, Vec3 movement, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof FiddlerCrabEntity crab && crab.bounce()) {
            ci.cancel();
        }
    }

    @Inject(method = "lavaIgnite", at = @At("HEAD"), cancellable = true)
    private void lavaIgnite(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof ItemEntity itemEntity) {
            if (itemEntity.getItem().has(TropicraftDataComponents.CONVERT_WITH_LAVA)) {
                Holder<Item> convertedItem = itemEntity.getItem().get(TropicraftDataComponents.CONVERT_WITH_LAVA);
                ItemStack convertedStack = new ItemStack(convertedItem, 1, itemEntity.getItem().getComponentsPatch());
                itemEntity.setItem(convertedStack);
                if (itemEntity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), SoundEvents.GENERIC_BURN, itemEntity.getSoundSource(), 0.4F, 2.0F + itemEntity.getRandom().nextFloat() * 0.4F);
                    serverLevel.sendParticles(ParticleTypes.FLAME, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 8, 0.2F, 0.2F, 0.2F, 0.01);
                }
                ci.cancel();
            }
        }
    }
}
