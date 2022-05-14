package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;

public final class TropicraftProcessorTypes {
    public static final DeferredRegister<StructureProcessorType<?>> REGISTER = DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, Constants.MODID);

    public static final RegistryObject<StructureProcessorType<?>> ADJUST_BUILDING_HEIGHT = register("adjust_building_height", AdjustBuildingHeightProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<?>> AIR_TO_CAVE_AIR = register("air_to_cave_air", AirToCaveAirProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<?>> SINK_IN_GROUND = register("sink_in_ground", SinkInGroundProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<?>> SMOOTHING_GRAVITY = register("smoothing_gravity", SmoothingGravityProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<?>> SPAWNER = register("spawner", SpawnerProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<?>> STEEP_PATH = register("steep_path", SteepPathProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<?>> STRUCTURE_SUPPORTS = register("structure_supports", StructureSupportsProcessor.CODEC);
    public static final RegistryObject<StructureProcessorType<?>> STRUCTURE_VOID = register("structure_void", StructureVoidProcessor.CODEC);

    private static <P extends StructureProcessor> RegistryObject<StructureProcessorType<?>> register(String name, Codec<P> codec) {
        return REGISTER.register(name, () -> (StructureProcessorType<P>) () -> codec);
    }
}
