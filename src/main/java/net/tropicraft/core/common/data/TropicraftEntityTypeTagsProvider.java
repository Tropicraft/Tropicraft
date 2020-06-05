package net.tropicraft.core.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.tropicraft.core.common.entity.TropicraftEntities;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class TropicraftEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public TropicraftEntityTypeTagsProvider(DataGenerator p_i49827_1_) {
        super(p_i49827_1_);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void registerTags() {
        appendToTag(EntityTypeTags.BEEHIVE_INHABITORS, TropicraftEntities.TROPI_BEE);
    }

    @SafeVarargs
    private final <T> T[] resolveAll(IntFunction<T[]> creator, Supplier<? extends T>... suppliers) {
        return Arrays.stream(suppliers).map(Supplier::get).toArray(creator);
    }

    @SafeVarargs
    private final void appendToTag(Tag<EntityType<?>> tag, Supplier<? extends EntityType<?>>... types) {
        getBuilder(tag).add(resolveAll(EntityType<?>[]::new, types));
    }

    @Override
    public String getName() {
        return "Tropicraft Entity Type Tags";
    }
}
