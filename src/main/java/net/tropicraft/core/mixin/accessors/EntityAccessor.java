package net.tropicraft.core.mixin.accessors;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("positionRider")
    void tropicraft$callPositionRider(Entity pEntity, Entity.MoveFunction pCallback);
}
