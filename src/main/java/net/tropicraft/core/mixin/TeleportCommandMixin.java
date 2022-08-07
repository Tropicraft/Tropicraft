package net.tropicraft.core.mixin;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.tropicraft.core.mixin.accessors.EntityAccessor;
import net.tropicraft.core.mixin.accessors.ServerGamePacketListenerImplAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(TeleportCommand.class)
public class TeleportCommandMixin {

    // Make Sure we also move any passengers if this entity is a vehicle and update the players vehicle stuff within its connections
    @Inject(method = "performTeleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;moveTo(DDDFF)V", shift = At.Shift.BY, by = 2, ordinal = 0))
    private static void movePossiblePassengers(CommandSourceStack chunkpos, Entity entity, ServerLevel f2, double f, double f1, double pSource, Set<ClientboundPlayerPositionPacket.RelativeArgument> pEntity, float pLevel, float pX, TeleportCommand.LookAt pY, CallbackInfo ci){
        entity.getSelfAndPassengers().forEach((vehicle) -> {
            for(Entity passanger : vehicle.getPassengers()) {
                ((EntityAccessor)vehicle).tropicraft$callPositionRider(passanger, Entity::moveTo);

                if(passanger instanceof ServerPlayer serverPlayer){
                    ServerGamePacketListenerImplAccessor accessor = (ServerGamePacketListenerImplAccessor)serverPlayer.connection;

                    accessor.tropicraft$setVehicleLastGoodX(vehicle.getX());
                    accessor.tropicraft$setVehicleLastGoodY(vehicle.getY());
                    accessor.tropicraft$setVehicleLastGoodZ(vehicle.getZ());

                    serverPlayer.connection.send(new ClientboundMoveVehiclePacket(vehicle));
                }
            }
        });
    }
}
