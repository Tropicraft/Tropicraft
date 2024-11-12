package net.tropicraft.core.mixin.fix;

import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureTemplatePool.Projection.class)
public class StructureTemplateProjectionMixin {
	// We used to have a custom projection type for koa paths - but this no longer exists, and the Vanilla parsing logic just passes null even though this cannot be serialized back again
	@Inject(method = "byName", at = @At("HEAD"), cancellable = true)
	private static void parseByName(String name, CallbackInfoReturnable<StructureTemplatePool.Projection> cir) {
		if ("tropicraft:koa_path".equals(name)) {
			cir.setReturnValue(StructureTemplatePool.Projection.TERRAIN_MATCHING);
		}
	}
}
