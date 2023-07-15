package net.tropicraft.core.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.passive.FiddlerCrabEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;updateEntityAfterFallOn(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;)V"), cancellable = true)
    private void fallOnBlock(MoverType type, Vec3 vector, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof FiddlerCrabEntity crab && crab.bounce()) {
            ci.cancel();
        }
    }
}
