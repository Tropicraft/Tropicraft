package net.tropicraft.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.passive.FiddlerCrabEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    // TODO: Can we just replace this with the bounciness attributes?
    @Inject(method = "restituteMovementAfterCollisions(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;ZZLnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
    private void fallOnBlock(BlockPos effectPos, BlockState effectState, boolean xCollision, boolean zCollision, Vec3 movement, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof FiddlerCrabEntity crab && crab.bounce()) {
            ci.cancel();
        }
    }
}
