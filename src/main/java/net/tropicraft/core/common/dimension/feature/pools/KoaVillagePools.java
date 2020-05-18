package net.tropicraft.core.common.dimension.feature.pools;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern.PlacementBehaviour;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.feature.jigsaw.AdjustBuildingHeightProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.StructureSupportsProcessor;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.StructureVoidProcessor;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;

@SuppressWarnings("deprecation")
public class KoaVillagePools {

    public static void init() {
    }
        
    static {
        StructureSupportsProcessor fenceExtender = new StructureSupportsProcessor(false, TropicraftBlocks.BAMBOO_FENCE.getId());
        ImmutableList<StructureProcessor> townCenterProcessors = ImmutableList.of(fenceExtender, new StructureVoidProcessor());
        ImmutableList<StructureProcessor> buildingProcessors = ImmutableList.of(new AdjustBuildingHeightProcessor(126), fenceExtender, new StructureVoidProcessor());

        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Constants.MODID, "koa_village/town_centers"), 
                new ResourceLocation("empty"), 
                ImmutableList.of(new Pair<>(
                        new SingleJigsawPiece(Constants.MODID + ":koa_village/town_centers/firepit_01", townCenterProcessors),
                        1)), PlacementBehaviour.RIGID));
        
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Constants.MODID, "koa_village/huts"), 
                new ResourceLocation("empty"),
                ImmutableList.of(
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/huts/hut_01", buildingProcessors),
                                5),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/huts/hut_02", buildingProcessors),
                                2),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/huts/hut_03", buildingProcessors),
                                3),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/huts/hut_04", buildingProcessors),
                                4),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/huts/hut_05", buildingProcessors),
                                10),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/huts/bongo_hut_01", buildingProcessors),
                                2),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/huts/trade_hut_01", buildingProcessors),
                                2)),
                PlacementBehaviour.RIGID));
        
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Constants.MODID, "koa_village/streets"), 
                new ResourceLocation(Constants.MODID, "koa_village/terminators"), 
                ImmutableList.of(
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/straight_01"),
                                3),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/straight_02"),
                                4),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/straight_03"),
                                10),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/straight_04"),
                                2),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/straight_05"),
                                3),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/straight_06"),
                                2),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/corner_01"),
                                2),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/corner_02"),
                                4),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/corner_03"),
                                6),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/corner_04"),
                                2),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/crossroad_01"),
                                5),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/crossroad_02"),
                                2),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/crossroad_03"),
                                1),
                        new Pair<>(
                                new SingleJigsawPiece(Constants.MODID + ":koa_village/streets/crossroad_04"),
                                2)
                        ),
                TropicraftFeatures.KOA_PATH));
        
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Constants.MODID, "koa_village/terminators"), 
                new ResourceLocation("empty"),
                ImmutableList.of(new Pair<>(
                        new SingleJigsawPiece(Constants.MODID + ":koa_village/terminators/terminator_01"),
                        1)), TropicraftFeatures.KOA_PATH));
        
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Constants.MODID, "koa_village/villagers"), 
                new ResourceLocation("empty"),
                ImmutableList.of(new Pair<>(
                        new SingleJigsawPiece(Constants.MODID + ":koa_village/villagers/unemployed"),
                        1)), PlacementBehaviour.RIGID));

        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Constants.MODID, "koa_village/fish"),
                new ResourceLocation("empty"),
                ImmutableList.of(new Pair<>(
                        new SingleJigsawPiece(Constants.MODID + ":koa_village/fish/fish_01"),
                        1)), PlacementBehaviour.RIGID));
    }
}
