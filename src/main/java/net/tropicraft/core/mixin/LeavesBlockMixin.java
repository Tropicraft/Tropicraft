package net.tropicraft.core.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.tropicraft.core.common.block.experimental.TropicraftExperimentalLeaveBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {

    public LeavesBlockMixin(Properties p_54422_) {
        super(p_54422_);
    }

    /**
     * This mixin targets the first setValue for leaves which is the {@link LeavesBlock#DISTANCE} to
     * replace it with {@link TropicraftExperimentalLeaveBlock#CUSTOM_DISTANCE} to prevent crashing
     * and keep the block inheritance of LeavesBlock class
     */
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;setValue(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;", ordinal = 0))
    private <T extends Comparable<T>> Property<T> preventDistanceRegistrationOnTropicraftLeaves(Property<T> par1){
        if(!(((LeavesBlock)(Object) this) instanceof TropicraftExperimentalLeaveBlock)){
            return par1;
        } else {
            return (Property<T>) TropicraftExperimentalLeaveBlock.CUSTOM_DISTANCE;
        }
    }

    /**
     * Prevents crashing when another LeaveBlock not part of TropicraftExperimentalLeaveBlock attempt
     * to get {@link LeavesBlock#DISTANCE} Data Value from an TropicraftExperimentalLeaveBlock that doesn't
     * have such but {@link TropicraftExperimentalLeaveBlock#CUSTOM_DISTANCE} Data Value
     */
    @Inject(method = "getDistanceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"), cancellable = true)
    private static void redirectDistanceValueForTropicraftCustomLeaves(BlockState pNeighbor, CallbackInfoReturnable<Integer> cir){
        if(pNeighbor.getBlock() instanceof TropicraftExperimentalLeaveBlock){
            cir.setReturnValue(Math.max(pNeighbor.getValue(TropicraftExperimentalLeaveBlock.CUSTOM_DISTANCE), 7));
        }
    }
}
