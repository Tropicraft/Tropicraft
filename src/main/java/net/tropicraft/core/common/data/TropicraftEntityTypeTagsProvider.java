package net.tropicraft.core.common.data;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag.Named;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.tropicraft.Constants;
import net.tropicraft.core.common.entity.TropicraftEntities;

public class TropicraftEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public TropicraftEntityTypeTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        appendToTag(EntityTypeTags.BEEHIVE_INHABITORS, TropicraftEntities.TROPI_BEE);
    }

    @SafeVarargs
    private final <T> T[] resolveAll(IntFunction<T[]> creator, Supplier<? extends T>... suppliers) {
        return Arrays.stream(suppliers).map(Supplier::get).toArray(creator);
    }

    @SafeVarargs
    private final void appendToTag(Named<EntityType<?>> tag, Supplier<? extends EntityType<?>>... types) {
        tag(tag).add(resolveAll(EntityType<?>[]::new, types));
    }

    @Override
    public String getName() {
        return "Tropicraft Entity Type Tags";
    }
}
