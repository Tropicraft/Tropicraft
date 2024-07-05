package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Constants;

public final class TropicraftProcessorTypes {
    public static final DeferredRegister<StructureProcessorType<?>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Constants.MODID);

    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<?>> ADJUST_BUILDING_HEIGHT = register("adjust_building_height", AdjustBuildingHeightProcessor.CODEC);
    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<?>> AIR_TO_CAVE_AIR = register("air_to_cave_air", AirToCaveAirProcessor.CODEC);
    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<?>> SINK_IN_GROUND = register("sink_in_ground", SinkInGroundProcessor.CODEC);
    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<?>> SMOOTHING_GRAVITY = register("smoothing_gravity", SmoothingGravityProcessor.CODEC);
    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<?>> SPAWNER = register("spawner", SpawnerProcessor.CODEC);
    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<?>> STEEP_PATH = register("steep_path", SteepPathProcessor.CODEC);
    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<?>> STRUCTURE_SUPPORTS = register("structure_supports", StructureSupportsProcessor.CODEC);
    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<?>> STRUCTURE_VOID = register("structure_void", StructureVoidProcessor.CODEC);

    private static <P extends StructureProcessor> DeferredHolder<StructureProcessorType<?>, StructureProcessorType<?>> register(String name, MapCodec<P> codec) {
        return REGISTER.register(name, () -> (StructureProcessorType<P>) () -> codec);
    }
}
