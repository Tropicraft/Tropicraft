package net.tropicraft.core.common.data;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.tropicraft.Constants;
import net.tropicraft.core.common.entity.TropicraftEntities;

public class TropicraftEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public TropicraftEntityTypeTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        appendToTag(EntityTypeTags.BEEHIVE_INHABITORS, TropicraftEntities.TROPI_BEE);
    }

    @SafeVarargs
    private final <T> T[] resolveAll(IntFunction<T[]> creator, Supplier<? extends T>... suppliers) {
        return Arrays.stream(suppliers).map(Supplier::get).toArray(creator);
    }

    @SafeVarargs
    private final void appendToTag(INamedTag<EntityType<?>> tag, Supplier<? extends EntityType<?>>... types) {
        getOrCreateBuilder(tag).add(resolveAll(EntityType<?>[]::new, types));
    }

    @Override
    public String getName() {
        return "Tropicraft Entity Type Tags";
    }
}
