package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.tropicraft.core.common.entity.TropicraftEntities;

import javax.annotation.Nullable;
import java.util.List;

public class SpawnerProcessor extends StructureProcessor {
    public static final SpawnerProcessor IGUANA = new SpawnerProcessor(ImmutableList.of(TropicraftEntities.IGUANA.getId()));
    public static final SpawnerProcessor ASHEN = new SpawnerProcessor(ImmutableList.of(TropicraftEntities.ASHEN.getId()));
    public static final SpawnerProcessor EIH = new SpawnerProcessor(ImmutableList.of(TropicraftEntities.EIH.getId()));
    public static final SpawnerProcessor IGUANA_AND_ASHEN = new SpawnerProcessor(ImmutableList.of(TropicraftEntities.ASHEN.getId(), TropicraftEntities.IGUANA.getId()));

    public static final MapCodec<SpawnerProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            ResourceLocation.CODEC.listOf().fieldOf("entity_types").forGetter(p -> p.entityTypes)
    ).apply(i, SpawnerProcessor::new));

    private final List<ResourceLocation> entityTypes;

    public SpawnerProcessor(final List<ResourceLocation> entityTypes) {
        this.entityTypes = entityTypes;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TropicraftProcessorTypes.SPAWNER.get();
    }

    @Override
    @Nullable
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        final Block block = blockInfo.state().getBlock();

        if (block != Blocks.SPAWNER) {
            return blockInfo;
        } else {
            final CompoundTag tag = new CompoundTag();

            String typeName = entityTypes.get(0).toString();
            tag.putString("id", typeName);

            blockInfo.nbt().getCompound("SpawnData").putString("id", typeName);
            // TODO not working
            final ListTag list = blockInfo.nbt().getList("SpawnPotentials", 9);
            for (int i = 0; i < list.size(); i++) {
                final CompoundTag nbt = list.getCompound(i);
                nbt.getCompound("Entity").putString("id", typeName);
            }

            return blockInfo;
        }
    }
}
