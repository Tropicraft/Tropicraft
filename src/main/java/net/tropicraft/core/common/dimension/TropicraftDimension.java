package net.tropicraft.core.common.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalLong;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class TropicraftDimension {
    private static final Logger LOGGER = LogManager.getLogger(TropicraftDimension.class);

    public static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "tropics");
    public static final ResourceLocation EFFECTS_ID = ID;

    public static final ResourceKey<Level> WORLD = ResourceKey.create(Registries.DIMENSION, ID);
    public static final ResourceKey<LevelStem> DIMENSION = ResourceKey.create(Registries.LEVEL_STEM, ID);
    public static final ResourceKey<DimensionType> DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ID);

    public static final int SEA_LEVEL = 127;

    public static void bootstrapDimensionType(final BootstapContext<DimensionType> context) {
        context.register(DIMENSION_TYPE, new DimensionType(
                OptionalLong.empty(),
                true,
                false,
                false,
                true,
                1.0,
                true,
                false,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                EFFECTS_ID,
                0.0f,
                new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0)
        ));
    }

    public static void bootstrapLevelStem(final BootstapContext<LevelStem> context) {
        context.register(DIMENSION, new LevelStem(
                context.lookup(Registries.DIMENSION_TYPE).getOrThrow(DIMENSION_TYPE),
                new NoiseBasedChunkGenerator(
                        MultiNoiseBiomeSource.createFromPreset(context.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST).getOrThrow(TropicraftBiomeBuilder.PARAMETER_LIST)),
                        context.lookup(Registries.NOISE_SETTINGS).getOrThrow(TropicraftNoiseGenSettings.TROPICS)
                )
        ));
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
            player.changeDimension(destLevel, new TropicsTeleporter(destLevel));

            //Note: Stops the player from teleporting right after going through the portal
            player.portalCooldown = 160;
        }
    }

    @Nullable
    private static ServerLevel getTeleportDestination(ServerPlayer player, ResourceKey<Level> dimensionType) {
        ResourceKey<Level> destination;
        if (player.level().dimension() == dimensionType) {
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
    public static int getSeaLevel(LevelReader reader) {
        if (reader instanceof ServerLevel serverLevel) {
            ServerChunkCache chunkProvider = serverLevel.getChunkSource();
            return chunkProvider.getGenerator().getSeaLevel();
        } else if (reader instanceof Level level) {
            if (level.dimension() == WORLD) {
                return SEA_LEVEL;
            }
        }
        return reader.getSeaLevel();
    }
}
