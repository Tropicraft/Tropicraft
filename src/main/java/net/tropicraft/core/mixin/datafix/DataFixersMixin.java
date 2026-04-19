package net.tropicraft.core.mixin.datafix;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.filefix.FileFixerUpper;
import net.tropicraft.core.common.datafix.TropicraftDataFixers;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(DataFixers.class)
public class DataFixersMixin {
    @Unique
    private static final Logger tropicraft$LOGGER = LogUtils.getLogger();

    @SuppressWarnings("unchecked")
    @Inject(method = "addFixers", at = @At("RETURN"))
    private static void addFixers(DataFixerBuilder builder, FileFixerUpper.Builder fileFixerBuilder, CallbackInfo ci) {
        Int2ObjectSortedMap<Schema> schemas;
        try {
            Field schemasField = DataFixerBuilder.class.getDeclaredField("schemas");
            schemasField.setAccessible(true);
            schemas = (Int2ObjectSortedMap<Schema>) schemasField.get(builder);
        } catch (ReflectiveOperationException e) {
            tropicraft$LOGGER.error("Unable to inject Tropicraft data fixers", e);
            return;
        }
        TropicraftDataFixers.injectFixers(new TropicraftDataFixers.Builder() {
            @Override
            public void injectFixer(DataFix fix) {
                builder.addFixer(fix);
            }

            @Override
            public Schema getSchema(int version, int subVersion) {
                return schemas.get(DataFixUtils.makeKey(version, subVersion));
            }
        });
    }
}
