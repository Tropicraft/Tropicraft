package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.tropicraft.core.common.entity.TropicraftEntities;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TropibeehiveDecorator extends TreeDecorator {
    public static final MapCodec<TropibeehiveDecorator> CODEC = Codec.floatRange(0.0F, 1.0F)
            .fieldOf("probability")
            .xmap(TropibeehiveDecorator::new, beehive -> beehive.probability);
    private static final Direction WORLDGEN_FACING = Direction.SOUTH;
    private static final Direction[] SPAWN_DIRECTIONS = Direction.Plane.HORIZONTAL
            .stream()
            .filter(p_202307_ -> p_202307_ != WORLDGEN_FACING.getOpposite())
            .toArray(Direction[]::new);
    private final float probability;

    public TropibeehiveDecorator(final float probability) {
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TropicraftTreeDecorators.TROPIBEEHIVE.get();
    }

    @Override
    public void place(TreeDecorator.Context pContext) {
        RandomSource randomsource = pContext.random();
        if (!(randomsource.nextFloat() >= this.probability)) {
            List<BlockPos> list = pContext.leaves();
            List<BlockPos> list1 = pContext.logs();
            int i = !list.isEmpty()
                    ? Math.max(list.get(0).getY() - 1, list1.get(0).getY() + 1)
                    : Math.min(list1.get(0).getY() + 1 + randomsource.nextInt(3), list1.get(list1.size() - 1).getY());
            List<BlockPos> list2 = list1.stream()
                    .filter(p_202300_ -> p_202300_.getY() == i)
                    .flatMap(p_202305_ -> Stream.of(SPAWN_DIRECTIONS).map(p_202305_::relative))
                    .collect(Collectors.toList());
            if (!list2.isEmpty()) {
                Collections.shuffle(list2);
                Optional<BlockPos> optional = list2.stream()
                        .filter(p_226022_ -> pContext.isAir(p_226022_) && pContext.isAir(p_226022_.relative(WORLDGEN_FACING)))
                        .findFirst();
                if (!optional.isEmpty()) {
                    pContext.setBlock(optional.get(), Blocks.BEE_NEST.defaultBlockState().setValue(BeehiveBlock.FACING, WORLDGEN_FACING));
                    pContext.level().getBlockEntity(optional.get(), BlockEntityType.BEEHIVE).ifPresent(p_330149_ -> {
                        int j = 2 + randomsource.nextInt(2);

                        for (int k = 0; k < j; k++) {
                            p_330149_.storeBee(tropibeeOccupant(599));
                        }
                    });
                }
            }
        }
    }

    public static BeehiveBlockEntity.Occupant tropibeeOccupant(int pTicksInHive) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(TropicraftEntities.TROPI_BEE.get()).toString());
        return new BeehiveBlockEntity.Occupant(CustomData.of(compoundtag), pTicksInHive, 600);
    }
}
