package net.tropicraft.core.common.dimension;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.tropicraft.core.common.block.BlockTropicraftSand;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;

import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public final class TropicraftSurfaces {
    private static final RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
    private static final RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
    private static final RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final RuleSource WATER = makeStateRule(Blocks.WATER);
    private static final RuleSource STONE = makeStateRule(Blocks.STONE);

    private static final RuleSource PURIFIED_SAND = makeStateRule(TropicraftBlocks.PURIFIED_SAND.get());
    private static final RuleSource UNDERWATER_PURIFIED_SAND = makeStateRule(TropicraftBlocks.PURIFIED_SAND.get().defaultBlockState().setValue(BlockTropicraftSand.UNDERWATER, true));
    private static final RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
    private static final RuleSource MUD = makeStateRule(TropicraftBlocks.MUD.get());

    private static RuleSource makeStateRule(Block block) {
        return state(block.defaultBlockState());
    }

    private static RuleSource makeStateRule(BlockState state) {
        return state(state);
    }

    public static RuleSource tropics() {
        ConditionSource atOrAboveSeaLevel = yBlockCheck(VerticalAnchor.absolute(TropicraftDimension.SEA_LEVEL - 1), 0);
        ConditionSource aboveSeaLevel = yBlockCheck(VerticalAnchor.absolute(TropicraftDimension.SEA_LEVEL), 0);
        ConditionSource notUnderWater = waterBlockCheck(-1, 0);
        ConditionSource underWater = not(notUnderWater);

        ConditionSource isMangrovey = isBiome(TropicraftBiomes.MANGROVES, TropicraftBiomes.OVERGROWN_MANGROVES);
        ConditionSource notUnderDeepWater = waterStartCheck(-6, -1);

        RuleSource grassRule = sequence(ifTrue(notUnderWater, GRASS_BLOCK), DIRT);
        RuleSource sandRule = sequence(ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), sequence(ifTrue(underWater, UNDERWATER_PURIFIED_SAND), PURIFIED_SAND));

        ConditionSource isSandy = isBiome(TropicraftBiomes.OCEAN, TropicraftBiomes.KELP_FOREST, TropicraftBiomes.RIVER, TropicraftBiomes.BEACH);
        ConditionSource isStony = isBiome(TropicraftBiomes.TROPICAL_PEAKS);

        // Applies to both top surface and under
        RuleSource surfaceRule = sequence(
                ifTrue(isMangrovey, ifTrue(noiseCondition(Noises.CALCITE, -0.0125, 0.0125), MUD)),
                ifTrue(isMangrovey, ifTrue(surfaceNoiseAbove(2.25), MUD)),
                ifTrue(isBiome(TropicraftBiomes.TROPICS), ifTrue(surfaceNoiseAbove(1.35), sandRule)),
                ifTrue(isSandy, sandRule),
                ifTrue(isStony, sequence(
                        ifTrue(steep(), STONE),
                        ifTrue(noiseCondition(Noises.CALCITE, -0.0125, 0.0125), STONE)
                ))
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
                        ifTrue(isMangrovey, ifTrue(atOrAboveSeaLevel,
                                ifTrue(not(aboveSeaLevel), ifTrue(noiseCondition(Noises.SWAMP, 0.0), WATER)))),
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
