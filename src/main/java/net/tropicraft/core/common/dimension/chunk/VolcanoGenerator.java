package net.tropicraft.core.common.dimension.chunk;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.VolcanicSandBlock;
import net.tropicraft.core.common.dimension.noise.NoiseModule;
import net.tropicraft.core.common.dimension.noise.generator.Billowed;

import java.util.function.Supplier;

// TODO: This whole class really needs a rewrite
@Mod.EventBusSubscriber(modid = Constants.MODID)
public class VolcanoGenerator {
    private final static int MAX_RADIUS = 65;
    private final static int MIN_RADIUS = 45;
    private final static int CALDERA_CUTOFF = 194; //The Y level where if the height of the volcano would pass becomes the caldera
    public final static int VOLCANO_TOP = CALDERA_CUTOFF - 7; //The Y level cut off of the sides of the volcano
    public final static int VOLCANO_CRUST = VOLCANO_TOP - 3; //The Y level where the crust of the volcano generates
    public final static int LAVA_LEVEL = 149; //The Y level where the top of the lava column is
    private final static int CRUST_HOLE_CHANCE = 15; //1 / x chance a certain block of the crust will be missing
    private final static int OCEAN_HEIGHT_OFFSET = -50;

    public final static int SURFACE_BIOME = 1;
    public final static int OCEAN_BIOME = 2;

    public final static int CHUNK_SIZE_X = 16;
    public final static int CHUNK_SIZE_Z = 16;
    public final static int CHUNK_SIZE_Y = 256;

    private static final int CHUNK_RANGE = MAX_RADIUS >> 4;

    private final static Supplier<BlockState> VOLCANO_BLOCK = TropicraftBlocks.CHUNK.lazyMap(Block::defaultBlockState);
    private final static Supplier<BlockState> LAVA_BLOCK = () -> Blocks.LAVA.defaultBlockState();
    private final static Supplier<BlockState> SAND_BLOCK = TropicraftBlocks.VOLCANIC_SAND.lazyMap(b -> b.defaultBlockState().setValue(VolcanicSandBlock.HOT, true));

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        // we don't really have a structure but we fake it
        CommandDispatcher<CommandSourceStack> dispatcher = event.getServer().getCommands().getDispatcher();

        LiteralArgumentBuilder<CommandSourceStack> locate = Commands.literal("locate").requires(source -> source.hasPermission(2));
        dispatcher.register(locate.then(Commands.literal("structure").then(
                Commands.literal(Constants.MODID + ":volcano").executes(ctx -> {
                    CommandSourceStack source = ctx.getSource();
                    BlockPos pos = new BlockPos(source.getPosition());

                    ChunkGenerator generator = source.getLevel().getChunkSource().getGenerator();
                    if (!(generator instanceof TropicraftChunkGenerator tropicsGenerator)) {
                        throw new SimpleCommandExceptionType(Component.translatable("commands.locate.structure.invalid")).create();
                    }

                    VolcanoGenerator volcanoGen = tropicsGenerator.getVolcano();

                    BlockPos volcanoPos = volcanoGen.getVolcanoNear(source.getLevel(), pos.getX() >> 4, pos.getZ() >> 4, 100);
                    if (volcanoPos == null) {
                        throw new SimpleCommandExceptionType(Component.translatable("commands.locate.structure.not_found")).create();
                    }

                    int distance = Mth.floor(dist(volcanoPos.getX(), volcanoPos.getZ(), pos.getX(), pos.getZ()));
                    Component component = ComponentUtils.wrapInSquareBrackets(Component.translatable("chat.coordinates", volcanoPos.getX(), "~", volcanoPos.getZ())).withStyle((p_207527_) -> {
                        return p_207527_.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + volcanoPos.getX() + " ~ " + volcanoPos.getZ())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.coordinates.tooltip")));
                    });
                    source.sendSuccess(Component.translatable("commands.locate.structure.success", "Volcano", component, distance), false);

                    return distance;
                }))));
    }

    private static float dist(int x1, int z1, int x2, int z2) {
        int deltaX = x2 - x1;
        int deltaZ = z2 - z1;
        return Mth.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }

    public ChunkAccess generate(int chunkX, int chunkZ, ChunkAccess chunk, RandomState randomState, BiomeSource biomeSource, WorldgenRandom random) {
        BlockPos volcanoCoords = getVolcanoNear(randomState, biomeSource, chunkX, chunkZ, 0);
        if (volcanoCoords == null) {
            return chunk;
        }

        int heightOffset = VolcanoGenerator.getHeightOffsetForBiome(volcanoCoords.getY());
        int calderaCutoff = CALDERA_CUTOFF + heightOffset;
        int lavaLevel = LAVA_LEVEL + heightOffset;
        int volcanoTop = VOLCANO_TOP + heightOffset;
        int volcanoCrust = VOLCANO_CRUST + heightOffset;

        chunkX *= CHUNK_SIZE_X;
        chunkZ *= CHUNK_SIZE_Z;

        int volcCenterX = volcanoCoords.getX();
        int volcCenterZ = volcanoCoords.getZ();

        long seed = getPositionSeed(randomState.legacyLevelSeed(), volcCenterX, volcCenterZ);
        RandomSource rand = RandomSource.create(seed);

        int radiusX = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
        int radiusZ = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;

        NoiseModule volcNoise = getNoise(seed);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        // if this chunk contains the volcano center
        if (volcanoCoords.getX() <= chunkX + 15 && volcanoCoords.getX() >= chunkX && volcanoCoords.getZ() <= chunkZ + 15 && volcanoCoords.getZ() >= chunkZ) {
            BlockPos volcanoBlockPos = new BlockPos(volcanoCoords.getX() & 15, 1, volcanoCoords.getZ() & 15);
            chunk.setBlockState(volcanoBlockPos, TropicraftBlocks.VOLCANO.get().defaultBlockState(), false);
        }

        for (int x = 0; x < CHUNK_SIZE_X; x++) {
            for (int z = 0; z < CHUNK_SIZE_Z; z++) {

                int relativeX = ((x + chunkX) - volcCenterX);
                int relativeZ = ((z + chunkZ) - volcCenterZ);

                double volcanoHeight = getVolcanoHeight(relativeX, relativeZ, radiusX, radiusZ, volcNoise);
                float distanceSquared = getDistanceSq(relativeX, relativeZ, radiusX, radiusZ);

                int groundHeight = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
                groundHeight = Math.min(groundHeight, lavaLevel - 3);

                if (distanceSquared < 1) {
                    for (int y = CHUNK_SIZE_Y; y > 0; y--) {
                        pos.set(x, y, z);

                        if (volcanoHeight + groundHeight < calderaCutoff) {
                            if (volcanoHeight + groundHeight <= volcanoTop) {
                                if (y <= volcanoHeight + groundHeight) {
                                    if (y > groundHeight) {
                                        this.placeBlock(pos, VOLCANO_BLOCK, chunk);
                                    } else if (y > groundHeight - 2) {
                                        this.placeBlock(pos, SAND_BLOCK, chunk);
                                    }
                                }
                            } else if (y == volcanoCrust - 1) {
                                if (random.nextInt(3) != 0) {
                                    this.placeBlock(pos, VOLCANO_BLOCK, chunk);
                                }
                            } else if (y <= volcanoTop) {
                                placeBlock(pos, VOLCANO_BLOCK, chunk);
                            }
                        } else {
                            // Flat area on top of the volcano
                            if (y == volcanoCrust && rand.nextInt(CRUST_HOLE_CHANCE) != 0) {
                                placeBlock(pos, VOLCANO_BLOCK, chunk);
                            } else if (y <= lavaLevel) {
                                placeBlock(pos, LAVA_BLOCK, chunk);
                            } else {
                                placeBlock(pos, Blocks.AIR::defaultBlockState, chunk);
                            }
                        }
                    }
                }
            }
        }

        return chunk;
    }

    private long getPositionSeed(long worldSeed, int volcCenterX, int volcCenterZ) {
        return (long) volcCenterX * 341873128712L + (long) volcCenterZ * 132897987541L + worldSeed + (long) 4291726;
    }

    private NoiseModule getNoise(long seed) {
        NoiseModule volcNoise = new Billowed(seed, 1, 1);
        volcNoise.amplitude = 0.45;
        return volcNoise;
    }

    /**
     * Get the height of volcano generation at the given x/z coordinates, without needing
     * to generate any blocks.
     *
     * @param groundHeight The current known heightmap level at the given coordinates.
     * @param x            Block x position
     * @param z            Block z position
     * @return The height value of the volcano generating near the given coordinates, at the
     * specified x and z. If there is no nearby volcano, returns -1.
     * @apiNote This only does a somewhat rough calculation -- it ignores the caldera "edge"
     * and treats volcanoes as completely flat at the top (ignoring random holes).
     * <p>
     * The latter is actually beneficial to the main use, village gen, because otherwise
     * village pieces may generate directly on top of a hole (and thus inside the volcano).
     * <p>
     * It also duplicates a significant amount of logic from { #generate(int, int, IChunk, SharedSeedRandom)},
     * but I have yet to find a nice way to deduplicate that logic. This whole class could use
     * a rewrite at some point with these goals in mind.
     * <p>
     */
    // TODO Fix the above issues
    public int getVolcanoHeight(RandomState randomState, BiomeSource biomeSource, int groundHeight, int x, int z) {
        BlockPos volcanoCoords = getVolcanoNear(randomState, biomeSource, x >> 4, z >> 4, 0);
        if (volcanoCoords == null) {
            return -1;
        }

        int volcCenterX = volcanoCoords.getX();
        int volcCenterZ = volcanoCoords.getZ();

        long seed = getPositionSeed(randomState.legacyLevelSeed(), volcCenterX, volcCenterZ);
        RandomSource rand = RandomSource.create(seed);

        int radiusX = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
        int radiusZ = rand.nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;

        NoiseModule volcNoise = getNoise(seed);

        int relativeX = x - volcCenterX;
        int relativeZ = z - volcCenterZ;

        double ret = getVolcanoHeight(relativeX, relativeZ, radiusX, radiusZ, volcNoise);

        int heightOffset = getHeightOffsetForBiome(volcanoCoords.getY());
        int lavaLevel = LAVA_LEVEL + heightOffset;
        int volcanoCrust = VOLCANO_CRUST + heightOffset;
        groundHeight = Math.min(groundHeight, lavaLevel - 3);

        return Math.min(volcanoCrust + 1, Mth.ceil(ret + groundHeight));
    }

    private float getDistanceSq(float relativeX, float relativeZ, float radiusX, float radiusZ) {
        return ((relativeX / radiusX) * (relativeX / radiusX) + (relativeZ / radiusZ) * (relativeZ / radiusZ));
    }

    private double getVolcanoHeight(float relativeX, float relativeZ, float radiusX, float radiusZ, NoiseModule volcNoise) {
        float distanceSquared = getDistanceSq(relativeX, relativeZ, radiusX, radiusZ);

        //float perlin = (float)volcNoise.getNoise(relativeX * 0.05 + 0.0001, relativeZ * 0.05 + 0.0001) + 1;
        float perlin = (float) volcNoise.getNoise(relativeX * 0.21 + 0.01, relativeZ * 0.21 + 0.01) + 1;

        //double volcanoHeight = steepnessMod / (distanceSquared) * perlin - steepnessMod - 2;
        double steepness = 10.2;
        return steepness / distanceSquared * perlin - steepness - 2;
    }

    public void placeBlock(BlockPos pos, Supplier<BlockState> blockState, ChunkAccess chunk) {
        chunk.setBlockState(pos, blockState.get(), false);
    }

    /**
     * Method to choose spawn locations for volcanos (borrowed from village gen)
     * Rarity is determined by the numChunks/offsetChunks vars (smaller numbers
     * mean more spawning)
     */
    public int canGenVolcanoAtCoords(RandomState randomState, BiomeSource biomeSource, int chunkX, int chunkZ) {
        byte numChunks = 64; // was 32
        byte offsetChunks = 16; // was 8
        int oldi = chunkX;
        int oldj = chunkZ;

        if (chunkX < 0) {
            chunkX -= numChunks - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= numChunks - 1;
        }

        int randX = chunkX / numChunks;
        int randZ = chunkZ / numChunks;
        long seed = (long) randX * 341873128712L + (long) randZ * 132897987541L + randomState.legacyLevelSeed() + (long) 4291726;
        RandomSource rand = RandomSource.create(seed);
        randX *= numChunks;
        randZ *= numChunks;
        randX += rand.nextInt(numChunks - offsetChunks);
        randZ += rand.nextInt(numChunks - offsetChunks);

        if (oldi == randX && oldj == randZ) {
            if (hasAllBiomes(randomState, biomeSource, oldi * 16 + 8, 0, oldj * 16 + 8, TropicraftTags.Biomes.HAS_LAND_VOLCANO)) {
                return SURFACE_BIOME;
            }
            if (hasAllBiomes(randomState, biomeSource, oldi * 16 + 8, 0, oldj * 16 + 8, TropicraftTags.Biomes.HAS_OCEAN_VOLCANO)) {
                return OCEAN_BIOME;
            }

            return SURFACE_BIOME;
        }

        return 0;
    }

    /**
     * Returns the coordinates of a volcano if it should be spawned near
     * this chunk, otherwise returns null.
     * The posY of the returned object should be used as the volcano radius
     */
    public BlockPos getVolcanoNear(ServerLevel world, int chunkX, int chunkZ, int maxRadius) {
        ServerChunkCache chunkSource = world.getChunkSource();
        return getVolcanoNear(chunkSource.randomState(), chunkSource.getGenerator().getBiomeSource(), chunkX, chunkZ, maxRadius);
    }

    /**
     * Returns the coordinates of a volcano if it should be spawned near
     * this chunk, otherwise returns null.
     * The posY of the returned object should be used as the volcano radius
     */
    public BlockPos getVolcanoNear(RandomState randomState, BiomeSource biomeSource, int chunkX, int chunkZ, int maxRadius) {
        maxRadius = maxRadius + CHUNK_RANGE;

        for (int radius = 0; radius <= maxRadius; radius++) {
            for (int offsetX = -radius; offsetX <= radius; offsetX++) {
                boolean edgeX = offsetX == -radius || offsetX == radius;
                for (int offsetZ = -radius; offsetZ <= radius; offsetZ++) {
                    boolean edgeZ = offsetZ == -radius || offsetZ == radius;
                    if (edgeX || edgeZ) {
                        int x = chunkX + offsetX;
                        int z = chunkZ + offsetZ;

                        int biome = canGenVolcanoAtCoords(randomState, biomeSource, x, z);
                        if (biome != 0) {
                            return new BlockPos((x << 4) + 8, biome, (z << 4) + 8);
                        }
                    }
                }
            }
        }

        return null;
    }

    public static int getHeightOffsetForBiome(int biome) {
        return biome == SURFACE_BIOME ? 0 : OCEAN_HEIGHT_OFFSET;
    }

    private boolean hasAllBiomes(RandomState randomState, BiomeSource biomeSource, int x, int y, int z, TagKey<Biome> allowedBiomes) {
        return biomeSource.getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z), randomState.sampler()).is(allowedBiomes);
    }
}
