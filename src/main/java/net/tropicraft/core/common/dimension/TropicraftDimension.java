package net.tropicraft.core.common.dimension;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeProvider;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class TropicraftDimension {
    private static final Logger LOGGER = LogManager.getLogger(TropicraftDimension.class);

    public static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "tropics");

    public static final RegistryKey<World> WORLD = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, ID);
    public static final RegistryKey<Dimension> DIMENSION = RegistryKey.getOrCreateKey(Registry.DIMENSION_KEY, ID);
    public static final RegistryKey<DimensionType> DIMENSION_TYPE = RegistryKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, ID);
    public static final RegistryKey<DimensionSettings> DIMENSION_SETTINGS = RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, ID);

    @SuppressWarnings("unchecked")
    public static void addDefaultDimensionKey() {
        try {
            Field dimensionKeysField = ObfuscationReflectionHelper.findField(Dimension.class, "field_236056_e_");
            LinkedHashSet<RegistryKey<Dimension>> keys = (LinkedHashSet<RegistryKey<Dimension>>) dimensionKeysField.get(null);
            keys.add(DIMENSION);
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Failed to add tropics as a default dimension key", e);
        }
    }

    public static ChunkGenerator createGenerator(Registry<Biome> biomeRegistry, Registry<DimensionSettings> dimensionSettingsRegistry, long seed) {
        Supplier<DimensionSettings> dimensionSettings = () -> {
            // fallback to overworld so that we don't crash before our datapack is loaded (horrible workaround)
            DimensionSettings settings = dimensionSettingsRegistry.getValueForKey(DIMENSION_SETTINGS);
            return settings != null ? settings : dimensionSettingsRegistry.getOrThrow(DimensionSettings.OVERWORLD);
        };
        TropicraftBiomeProvider biomeSource = new TropicraftBiomeProvider(seed, biomeRegistry);
        return new TropicraftChunkGenerator(biomeSource, seed, dimensionSettings);
    }

    public static void teleportPlayer(ServerPlayerEntity player, RegistryKey<World> dimensionType) {
        if (player.world.getDimensionKey() == dimensionType) {
            teleportPlayerNoPortal(player, World.OVERWORLD);
        } else {
            teleportPlayerNoPortal(player, dimensionType);
        }
    }

    /**
     * Finds the top Y position relative to the dimension the player is teleporting to and places
     * the entity at that position. Avoids portal generation by using player.teleport() instead of
     * player.changeDimension()
     *
     * @param player The player that will be teleported
     * @param destination The target dimension to teleport to
     */
    public static void teleportPlayerNoPortal(ServerPlayerEntity player, RegistryKey<World> destination) {
        ServerWorld world = player.server.getWorld(destination);
        if (world == null) {
            LOGGER.error("Cannot teleport player to dimension {} as it does not exist!", destination.getLocation());
            return;
        }

        if (!ForgeHooks.onTravelToDimension(player, destination)) return;

        int x = MathHelper.floor(player.getPosX());
        int z = MathHelper.floor(player.getPosZ());

        Chunk chunk = world.getChunk(x >> 4, z >> 4);
        int topY = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x & 15, z & 15);
        player.teleport(world, x + 0.5, topY + 1.0, z + 0.5, player.rotationYaw, player.rotationPitch);

        BasicEventHooks.firePlayerChangedDimensionEvent(player, destination, destination);
    }
}
