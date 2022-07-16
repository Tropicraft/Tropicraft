package net.tropicraft.core.common;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;

import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public final class TropicraftSurfaces {
    private static final RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
    private static final RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
    private static final RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final RuleSource WATER = makeStateRule(Blocks.WATER);

    private static final RuleSource SAND = makeStateRule(TropicraftBlocks.PURIFIED_SAND.get());
    private static final RuleSource MUD = makeStateRule(TropicraftBlocks.MUD.get());

    private static RuleSource makeStateRule(Block block) {
        return state(block.defaultBlockState());
    }

    public static RuleSource tropics() {
        ConditionSource atOrAboveSeaLevel = yBlockCheck(VerticalAnchor.absolute(126), 0);
        ConditionSource aboveSeaLevel = yBlockCheck(VerticalAnchor.absolute(127), 0);
        ConditionSource notUnderWater = waterBlockCheck(-1, 0);
        ConditionSource notUnderDeepWater = waterStartCheck(-6, -1);

        RuleSource grassRule = sequence(ifTrue(notUnderWater, GRASS_BLOCK), DIRT);
        RuleSource sandRule = sequence(SAND);

        ConditionSource isSandy = isBiome(TropicraftBiomes.OCEAN.getKey(), TropicraftBiomes.RIVER.getKey(), TropicraftBiomes.BEACH.getKey());

        RuleSource surfaceRule = sequence(
                ifTrue(isBiome(TropicraftBiomes.MANGROVES.getKey()), ifTrue(noiseCondition(Noises.CALCITE, -0.0125, 0.0125), MUD)),
                ifTrue(isBiome(TropicraftBiomes.MANGROVES.getKey()), ifTrue(surfaceNoiseAbove(2.25), MUD)),
                ifTrue(isBiome(TropicraftBiomes.TROPICS.getKey()), ifTrue(surfaceNoiseAbove(1.35), sandRule)),
                ifTrue(isSandy, sandRule)
        );

        RuleSource underFloorRule = sequence(
                surfaceRule,
                DIRT
        );

        RuleSource floorRule = sequence(
                surfaceRule,
                grassRule
        );

        RuleSource aboveSurface = sequence(
                ifTrue(ON_FLOOR, sequence(
                        ifTrue(isBiome(TropicraftBiomes.MANGROVES.getKey()), ifTrue(atOrAboveSeaLevel, ifTrue(not(aboveSeaLevel), ifTrue(noiseCondition(Noises.SWAMP, 0.0), WATER)))),
                        ifTrue(notUnderWater, floorRule)
                )),
                ifTrue(notUnderDeepWater, sequence(
                        ifTrue(UNDER_FLOOR, underFloorRule)
                )),
                ifTrue(ON_FLOOR, sandRule)
        );

        ImmutableList.Builder<RuleSource> rules = ImmutableList.builder();
        rules.add(ifTrue(verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
        rules.add(ifTrue(abovePreliminarySurface(), aboveSurface));
        rules.add(ifTrue(verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));

        return sequence(rules.build().toArray(RuleSource[]::new));
    }

    private static ConditionSource surfaceNoiseAbove(double threshold) {
        return noiseCondition(Noises.SURFACE, threshold / 8.25, Double.MAX_VALUE);
    }
}
