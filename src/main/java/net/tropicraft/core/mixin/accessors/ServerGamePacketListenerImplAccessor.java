package net.tropicraft.core.mixin.accessors;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerGamePacketListenerImpl.class)
public interface ServerGamePacketListenerImplAccessor {

    @Accessor("vehicleLastGoodX")
    void tropicraft$setVehicleLastGoodX(double vehicleLastGoodX);

    @Accessor("vehicleLastGoodY")
    void tropicraft$setVehicleLastGoodY(double vehicleLastGoodY);

    @Accessor("vehicleLastGoodZ")
    void tropicraft$setVehicleLastGoodZ(double vehicleLastGoodZ);
}
