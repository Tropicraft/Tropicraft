package net.tropicraft.core.mixin.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Holder.Reference.class)
public class HolderReferenceMixin<T> {
    @Shadow
    @Final
    private Registry<T> registry;

    /**
     * FIXME: Please remove this awful hack! This fixes our data generation, because we do not generate into the builtin
     *  registry. By the point of datagen, the builtin registries are frozen, so we use a copied instance of the dyanmic
     *  registries. This means that any reference to vanilla builtin registry entries (e.g. Carver.CAVE) will return
     *  a Holder with the builtin registry instance, which does not match the dynamic registry instance.
     *  This hack resolves this issue by allowing the "same same but different" registry to match
     *
     * @author Gegy
     */
    @Overwrite
    public boolean isValidInRegistry(Registry<T> registry) {
        return this.registry.key() == registry.key();
    }
}
