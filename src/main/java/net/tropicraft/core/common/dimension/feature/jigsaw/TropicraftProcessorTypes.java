package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Tropicraft;

public final class TropicraftProcessorTypes {
    public static final DeferredRegister<MapCodec<? extends StructureProcessor>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Tropicraft.ID);

    public static final DeferredHolder<MapCodec<? extends StructureProcessor>, ?> ADJUST_BUILDING_HEIGHT = register("adjust_building_height", AdjustBuildingHeightProcessor.CODEC);
    public static final DeferredHolder<MapCodec<? extends StructureProcessor>, ?> AIR_TO_CAVE_AIR = register("air_to_cave_air", AirToCaveAirProcessor.CODEC);
    public static final DeferredHolder<MapCodec<? extends StructureProcessor>, ?> SINK_IN_GROUND = register("sink_in_ground", SinkInGroundProcessor.CODEC);
    public static final DeferredHolder<MapCodec<? extends StructureProcessor>, ?> SMOOTHING_GRAVITY = register("smoothing_gravity", SmoothingGravityProcessor.CODEC);
    public static final DeferredHolder<MapCodec<? extends StructureProcessor>, ?> SPAWNER = register("spawner", SpawnerProcessor.CODEC);
    public static final DeferredHolder<MapCodec<? extends StructureProcessor>, ?> STEEP_PATH = register("steep_path", SteepPathProcessor.CODEC);
    public static final DeferredHolder<MapCodec<? extends StructureProcessor>, ?> STRUCTURE_SUPPORTS = register("structure_supports", StructureSupportsProcessor.CODEC);
    public static final DeferredHolder<MapCodec<? extends StructureProcessor>, ?> STRUCTURE_VOID = register("structure_void", StructureVoidProcessor.CODEC);

    private static <P extends StructureProcessor> DeferredHolder<MapCodec<? extends StructureProcessor>, MapCodec<P>> register(String name, MapCodec<P> codec) {
        return REGISTER.register(name, () -> codec);
    }
}
