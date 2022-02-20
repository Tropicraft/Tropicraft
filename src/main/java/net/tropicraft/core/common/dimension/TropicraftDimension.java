package net.tropicraft.core.common.dimension;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeSource;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGenerator;
import net.tropicraft.core.mixin.worldgen.LevelStemAccessor;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class TropicraftDimension {
    private static final Logger LOGGER = LogManager.getLogger(TropicraftDimension.class);

    public static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "tropics");

    public static final ResourceKey<Level> WORLD = ResourceKey.create(Registry.DIMENSION_REGISTRY, ID);
    public static final ResourceKey<LevelStem> DIMENSION = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, ID);
    public static final ResourceKey<DimensionType> DIMENSION_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, ID);
    public static final ResourceKey<NoiseGeneratorSettings> DIMENSION_SETTINGS = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, ID);

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getWorld();
            if (world.dimension() == Level.OVERWORLD) {
                upgradeTropicraftDimension(world.getServer());
            }
        }
    }

    private static void upgradeTropicraftDimension(MinecraftServer server) {
        // forge put dimensions in a different place to where vanilla does with its custom dimension support
        // we need to move our old data to the correct place if it exists

        LevelStorageSource.LevelStorageAccess save = server.storageSource;

        File oldDimension = save.getLevelPath(new LevelResource("tropicraft/tropics")).toFile();
        Path newDimension = save.getDimensionPath(WORLD);
        if (oldDimension.exists() && !newDimension.toFile().exists()) {
            try {
                FileUtils.moveDirectory(oldDimension, newDimension.toFile());
            } catch (IOException e) {
                LOGGER.error("Failed to move old tropicraft dimension to new location!", e);
            }
        }
    }

    public static void addDefaultDimensionKey() {
        Set<ResourceKey<LevelStem>> order = ImmutableSet.<ResourceKey<LevelStem>>builder()
                .addAll(LevelStemAccessor.getBuiltinOrder())
                .add(DIMENSION)
                .build();
        LevelStemAccessor.setBuiltinOrder(order);
    }

    public static LevelStem createDimension(
            Registry<DimensionType> dimensionTypeRegistry,
            Registry<StructureSet> structureSetRegistry,
            Registry<Biome> biomeRegistry,
            Registry<NoiseGeneratorSettings> dimensionSettingsRegistry,
            Registry<NormalNoise.NoiseParameters> paramRegistry,
            long seed
    ) {
        ChunkGenerator generator = TropicraftDimension.createGenerator(structureSetRegistry, paramRegistry, biomeRegistry, dimensionSettingsRegistry, seed);

        return new LevelStem(dimensionTypeRegistry.getHolderOrThrow(TropicraftDimension.DIMENSION_TYPE), generator);
    }

    public static ChunkGenerator createGenerator(Registry<StructureSet> structureSetRegistry, Registry<NormalNoise.NoiseParameters> params, Registry<Biome> biomeRegistry, Registry<NoiseGeneratorSettings> dimensionSettingsRegistry, long seed) {
        Supplier<NoiseGeneratorSettings> dimensionSettings = () -> {
            // fallback to overworld so that we don't crash before our datapack is loaded (horrible workaround)
            NoiseGeneratorSettings settings = dimensionSettingsRegistry.get(DIMENSION_SETTINGS);
            return settings != null ? settings : dimensionSettingsRegistry.getOrThrow(NoiseGeneratorSettings.OVERWORLD);
        };
        TropicraftBiomeSource biomeSource = new TropicraftBiomeSource(seed, biomeRegistry);
        return new TropicraftChunkGenerator(structureSetRegistry, params, biomeSource, seed,
                dimensionSettingsRegistry.getHolderOrThrow(dimensionSettingsRegistry.getResourceKey(dimensionSettings.get()).get())
        );
    }

    /**
     * Method that handles teleporting the player to and from the tropics depending on certain parameters.
     * Finds the top Y position relative to the dimension the player is teleporting to and places the entity at that position.
     * <p>
     * The position will be based on finding the top Y position relative
     * to the dimension the player is teleporting to and places the entity at that position. Avoids portal generation
     * by using player.teleport() instead of player.changeDimension()
     *
     * @param player The player that will be teleported
     * @param dimensionType The Tropicraft Dimension Type for reference
     */
    public static void teleportPlayer(ServerPlayer player, ResourceKey<Level> dimensionType) {
        ServerLevel destLevel = getTeleportDestination(player, dimensionType);
        if (destLevel == null) return;

        ResourceKey<Level> destDimension = destLevel.dimension();
        if (!ForgeHooks.onTravelToDimension(player, destDimension)) return;

        int x = Mth.floor(player.getX());
        int z = Mth.floor(player.getZ());

        LevelChunk chunk = destLevel.getChunk(x >> 4, z >> 4);
        int topY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x & 15, z & 15);
        player.teleportTo(destLevel, x + 0.5, topY + 1.0, z + 0.5, player.getYRot(), player.getXRot());

        ForgeEventFactory.firePlayerChangedDimensionEvent(player, destDimension, destDimension);
    }

    /**
     * Method that handles teleporting the player to and from the tropics depending on certain parameters.
     * Finds the top Y position relative to the dimension the player is teleporting to and places the entity at that position.
     * <p>
     * A portal will be generated on the players teleport with such position based on the portal's info position.
     *
     * @param player The player that will be teleported
     * @param dimensionType The Tropicraft Dimension Type for reference
     */
    public static void teleportPlayerWithPortal(ServerPlayer player, ResourceKey<Level> dimensionType) {
        ServerLevel destLevel = getTeleportDestination(player, dimensionType);
        if (destLevel == null) return;

        if (!player.isOnPortalCooldown()) {
            player.unRide();
            //player.changeDimension(destLevel, new TropicsTeleporter(destLevel));
            player.changeDimension(destLevel, new PortalTropics(destLevel));

            //Note: Stops the player from teleporting right after going through the portal
            player.portalCooldown = 160;
        }
    }

    @Nullable
    private static ServerLevel getTeleportDestination(ServerPlayer player, ResourceKey<Level> dimensionType) {
        ResourceKey<Level> destination;
        if (player.level.dimension() == dimensionType) {
            destination = Level.OVERWORLD;
        } else {
            destination = dimensionType;
        }

        ServerLevel destLevel = player.server.getLevel(destination);
        if (destLevel == null) {
            LOGGER.error("Cannot teleport player to dimension {} as it does not exist!", destination.location());
            return null;
        }
        return destLevel;
    }

    // hack to get the correct sea level given a world: the vanilla IWorldReader.getSeaLevel() is deprecated and always returns 63 despite the chunk generator
    public static int getSeaLevel(LevelReader world) {
        if (world instanceof ServerLevel) {
            ServerChunkCache chunkProvider = ((ServerLevel) world).getChunkSource();
            return chunkProvider.getGenerator().getSeaLevel();
        } else if (world instanceof Level) {
            ResourceKey<Level> dimensionKey = ((Level) world).dimension();
            if (dimensionKey == WORLD) {
                return 127;
            }
        }
        return world.getSeaLevel();
    }
}
