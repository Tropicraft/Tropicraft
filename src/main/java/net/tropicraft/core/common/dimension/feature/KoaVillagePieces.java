package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern.PlacementBehaviour;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.tropicraft.Info;

public class KoaVillagePieces {

    public static void init() {
    }
    
    static {
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Info.MODID, "koa_village/town_centers"), 
                new ResourceLocation("empty"), 
                ImmutableList.of(new Pair<>(
                        new SingleJigsawPiece(Info.MODID + ":koa_village/town_centers/firepit_01"),
                        1)), PlacementBehaviour.RIGID));
        
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Info.MODID, "koa_village/streets"), 
                new ResourceLocation(Info.MODID, "koa_village/terminators"), 
                ImmutableList.of(new Pair<>(
                        new SingleJigsawPiece(Info.MODID + ":koa_village/streets/straight_01"),
                        1)), PlacementBehaviour.TERRAIN_MATCHING));
        
        JigsawManager.REGISTRY.register(new JigsawPattern(new ResourceLocation(Info.MODID, "koa_village/terminators"), 
                new ResourceLocation("empty"), 
                ImmutableList.of(new Pair<>(
                        new SingleJigsawPiece(Info.MODID + ":koa_village/terminators/terminator_01"),
                        1)), PlacementBehaviour.TERRAIN_MATCHING));
    }
}
