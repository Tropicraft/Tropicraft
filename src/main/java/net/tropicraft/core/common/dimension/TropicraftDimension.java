package net.tropicraft.core.common.dimension;

import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalLong;

public class TropicraftDimension {
    private static final Logger LOGGER = LogManager.getLogger(TropicraftDimension.class);

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "tropics");
    public static final ResourceLocation EFFECTS_ID = ID;

    public static final ResourceKey<Level> WORLD = ResourceKey.create(Registries.DIMENSION, ID);
    public static final ResourceKey<LevelStem> DIMENSION = ResourceKey.create(Registries.LEVEL_STEM, ID);
    public static final ResourceKey<DimensionType> DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ID);

    public static final int SEA_LEVEL = 127;

    public static void bootstrapDimensionType(BootstrapContext<DimensionType> context) {
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

    public static void bootstrapLevelStem(BootstrapContext<LevelStem> context) {
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
        ServerLevel targetLevel = getTeleportDestination(player.serverLevel(), dimensionType);
        if (targetLevel == null) {
            return;
        }

        int x = player.getBlockX();
        int z = player.getBlockZ();

        LevelChunk chunk = targetLevel.getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));
        int topY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, SectionPos.sectionRelative(x), SectionPos.sectionRelative(z));
        Vec3 pos = new Vec3(x + 0.5, topY + 1.0, z + 0.5);

        player.unRide();
        player.changeDimension(new DimensionTransition(
                targetLevel,
                pos,
                Vec3.ZERO,
                player.getYRot(),
                player.getXRot(),
                DimensionTransition.DO_NOTHING
        ));
    }

    @Nullable
    public static DimensionTransition getPortalTransition(ServerLevel level, Entity entity, ResourceKey<Level> targetDimension) {
        ServerLevel targetLevel = getTeleportDestination(level, targetDimension);
        if (targetLevel == null) {
            return null;
        }
        TropicsPortalLinker linker = new TropicsPortalLinker(targetLevel);
        TropicsPortalLinker.PortalInfo portal = linker.findOrCreatePortal(entity);
        if (portal == null) {
            return null;
        }
        return new DimensionTransition(
                targetLevel,
                portal.position(),
                Vec3.ZERO,
                portal.yRot(),
                portal.xRot(),
                DimensionTransition.PLACE_PORTAL_TICKET.then(DimensionTransition.PLAY_PORTAL_SOUND)
        );
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
        player.unRide();
        DimensionTransition portalTransition = getPortalTransition(player.serverLevel(), player, dimensionType);
        if (portalTransition != null) {
            player.changeDimension(portalTransition);
        }
    }

    @Nullable
    private static ServerLevel getTeleportDestination(Level sourceLevel, ResourceKey<Level> targetDimension) {
        ResourceKey<Level> destination;
        if (sourceLevel.dimension() == targetDimension) {
            destination = Level.OVERWORLD;
        } else {
            destination = targetDimension;
        }

        ServerLevel destLevel = sourceLevel.getServer().getLevel(destination);
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
