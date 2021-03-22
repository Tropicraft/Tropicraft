package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.tropicraft.Constants;
import net.tropicraft.core.common.entity.TropicraftEntities;

import javax.annotation.Nullable;
import java.util.List;

public class SpawnerProcessor extends StructureProcessor {
    public static final SpawnerProcessor IGUANA = new SpawnerProcessor(ImmutableList.of(TropicraftEntities.IGUANA.get()));
    public static final SpawnerProcessor ASHEN = new SpawnerProcessor(ImmutableList.of(TropicraftEntities.ASHEN.get()));
    public static final SpawnerProcessor EIH = new SpawnerProcessor(ImmutableList.of(TropicraftEntities.EIH.get()));
    public static final SpawnerProcessor IGUANA_AND_ASHEN = new SpawnerProcessor(ImmutableList.of(TropicraftEntities.ASHEN.get(), TropicraftEntities.IGUANA.get()));

    public static final Codec<SpawnerProcessor> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Registry.ENTITY_TYPE.listOf().fieldOf("entity_types").forGetter(p -> p.entityTypes)
        ).apply(instance, SpawnerProcessor::new);
    });

    public static final IStructureProcessorType<SpawnerProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":spawner_processor", () -> CODEC);

    private final List<EntityType<?>> entityTypes;

    public SpawnerProcessor(final List<EntityType<?>> entityTypes) {
        this.entityTypes = entityTypes;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return TYPE;
    }

    @Override
    @Nullable
    public Template.BlockInfo process(IWorldReader world, BlockPos pos, BlockPos pos2, Template.BlockInfo originalBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, @Nullable Template template) {
        final Block block = blockInfo.state.getBlock();

        if (block != Blocks.SPAWNER) {
            return blockInfo;
        } else {
            final CompoundNBT tag = new CompoundNBT();
            String typeName = Registry.ENTITY_TYPE.getKey(entityTypes.get(0)).toString();

            tag.putString("id", entityTypes.get(0).getRegistryName().getPath());

            blockInfo.nbt.getCompound("SpawnData").putString("id", typeName);
            // TODO not working
            final ListNBT list = blockInfo.nbt.getList("SpawnPotentials", 9);
            for (int i = 0; i < list.size(); i++) {
                final CompoundNBT nbt = list.getCompound(i);
                nbt.getCompound("Entity").putString("id", typeName);
            }

            return blockInfo;
        }
    }

}
