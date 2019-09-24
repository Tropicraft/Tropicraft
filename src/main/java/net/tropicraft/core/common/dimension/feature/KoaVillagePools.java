package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern.PlacementBehaviour;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.tropicraft.Info;

@SuppressWarnings("deprecation")
public class KoaVillagePools {

    public static void init() {
    }
        
    static {
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Info.MODID, "koa_village/town_centers"), 
                new ResourceLocation("empty"), 
                ImmutableList.of(new Pair<>(
                        new SingleJigsawPiece(Info.MODID + ":koa_village/town_centers/firepit_01"),
                        1)), TropicraftFeatures.KOA_TOWN_CENTER));
        
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Info.MODID, "koa_village/huts"), 
                new ResourceLocation("empty"),
                ImmutableList.of(
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/huts/hut_01"),
                                10),
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/huts/bongo_hut_01"),
                                1),
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/huts/trade_hut_01"),
                                2)),
                TropicraftFeatures.KOA_BUILDING));
        
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Info.MODID, "koa_village/streets"), 
                new ResourceLocation(Info.MODID, "koa_village/terminators"), 
                ImmutableList.of(
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/streets/straight_01"),
                                10),
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/streets/straight_02"),
                                5),
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/streets/straight_03"),
                                3),
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/streets/corner_01"),
                                5),
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/streets/corner_02"),
                                4),
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/streets/corner_03"),
                                2),
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/streets/crossroad_01"),
                                2),
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/streets/crossroad_02"),
                                2),
                        new Pair<>(
                                new SingleJigsawPiece(Info.MODID + ":koa_village/streets/crossroad_03"),
                                1)
                        ),
                TropicraftFeatures.KOA_PATH));
        
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Info.MODID, "koa_village/terminators"), 
                new ResourceLocation("empty"), 
                ImmutableList.of(new Pair<>(
                        new SingleJigsawPiece(Info.MODID + ":koa_village/terminators/terminator_01"),
                        1)), TropicraftFeatures.KOA_PATH));
        
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Info.MODID, "koa_village/villagers"), 
                new ResourceLocation("empty"), 
                ImmutableList.of(new Pair<>(
                        new SingleJigsawPiece(Info.MODID + ":koa_village/villagers/unemployed"),
                        1)), PlacementBehaviour.RIGID));
    }
}
