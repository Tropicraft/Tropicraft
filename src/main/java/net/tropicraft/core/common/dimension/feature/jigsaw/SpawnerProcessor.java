package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
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
                Registry.ENTITY_TYPE.byNameCodec().listOf().fieldOf("entity_types").forGetter(p -> p.entityTypes)
        ).apply(instance, SpawnerProcessor::new);
    });

    public static final StructureProcessorType<SpawnerProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":spawner_processor", () -> CODEC);

    private final List<EntityType<?>> entityTypes;

    public SpawnerProcessor(final List<EntityType<?>> entityTypes) {
        this.entityTypes = entityTypes;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }

    @Override
    @Nullable
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        final Block block = blockInfo.state.getBlock();

        if (block != Blocks.SPAWNER) {
            return blockInfo;
        } else {
            final CompoundTag tag = new CompoundTag();
            String typeName = Registry.ENTITY_TYPE.getKey(entityTypes.get(0)).toString();

            tag.putString("id", entityTypes.get(0).getRegistryName().getPath());

            blockInfo.nbt.getCompound("SpawnData").putString("id", typeName);
            // TODO not working
            final ListTag list = blockInfo.nbt.getList("SpawnPotentials", 9);
            for (int i = 0; i < list.size(); i++) {
                final CompoundTag nbt = list.getCompound(i);
                nbt.getCompound("Entity").putString("id", typeName);
            }

            return blockInfo;
        }
    }

}
