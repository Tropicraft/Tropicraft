package net.tropicraft.core.mixin;

import net.minecraft.world.entity.Mob;
import net.tropicraft.core.common.entity.passive.FiddlerCrabEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    private void serverAiStep(CallbackInfo ci) {
        Mob self = (Mob) (Object) this;
        if (self instanceof FiddlerCrabEntity crab && crab.isRollingDownTown()) {
            ci.cancel();
        }
    }
}
