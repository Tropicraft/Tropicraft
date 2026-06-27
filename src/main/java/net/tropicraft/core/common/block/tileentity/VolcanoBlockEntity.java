package net.tropicraft.core.common.block.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.TropicsConfigs;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.volcano.VolcanoStructurePiece;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.projectile.LavaBallEntity;
import net.tropicraft.core.common.particle.TropicraftParticles;
import net.tropicraft.core.common.volcano.VolcanoState;

import javax.annotation.Nullable;

public class VolcanoBlockEntity extends BlockEntity {
    private static final int RAND_DORMANT_DURATION = 4000;
    private static final int MAX_LAVA_LEVEL_DURING_RISE = VolcanoStructurePiece.VOLCANO_CRUST - 1;
    private static final int MAX_LAVA_LEVEL_DURING_ERUPTION = VolcanoStructurePiece.VOLCANO_CRUST + 1;
    private static final int LAVA_BASE_LEVEL = VolcanoStructurePiece.LAVA_LEVEL;

    private int ticksUntilEruption = VolcanoState.getTimeBefore(VolcanoState.ERUPTING);
    private int ticksUntilSmoking = VolcanoState.getTimeBefore(VolcanoState.SMOKING);
    private int ticksUntilRetreating = VolcanoState.getTimeBefore(VolcanoState.RETREATING);
    private int ticksUntilDormant = VolcanoState.getTimeBefore(VolcanoState.DORMANT);
    private int ticksUntilRising = VolcanoState.getTimeBefore(VolcanoState.RISING);

    /**
     * How high the lava is during the rising phase
     */
    private int lavaLevel = -1;

    /**
     * Volcano radius
     */
    private int radius = -1;

    private VolcanoState state = VolcanoState.DORMANT;
    private int heightOffset = Integer.MIN_VALUE;

    public VolcanoBlockEntity(BlockEntityType<VolcanoBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void volcanoTick(Level level, BlockPos pos, BlockState state, VolcanoBlockEntity volcano) {
        volcano.tick();
    }

    private void tick() {
        if (!TropicsConfigs.allowVolcanoEruption) {
            return;
        }

        // If radius needs to be initialized
        if (radius == -1) {
            radius = findRadius();
        }

        if (lavaLevel == -1) {
            setLavaLevel();
        }

        if (radius == -1 || lavaLevel == -1) {
            return;
        }

        updateStates();

        switch (state) {
            case DORMANT:
                break;

            case ERUPTING:
                if (!level.isClientSide) {
                    if (level.random.nextInt(15) == 0)
                        throwLavaFromCaldera(4);
                }
                break;

            case RETREATING:
                if (ticksUntilDormant % 30 == 0)
                    lowerLavaLevels();
                break;

            case RISING:
                if (level.isClientSide) {
                    spewSmoke();
                }

                if (ticksUntilEruption % 20 == 0) {
                    if (lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION + heightOffset) {
                        raiseLavaLevels();
                    } else {
                        ticksUntilEruption = 0;
                        level.playLocalSound(worldPosition.getX(), lavaLevel, worldPosition.getY(),
                                SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 10000.0F,
                                level.random.nextFloat() / 4 + 0.825f, false);

                        int balls = level.random.nextInt(25) + 15;
                        for (int i = 0; i < balls; i++) {
                            throwLavaFromCaldera(4);
                        }
                        break;
                    }
                }
                break;

            case SMOKING:
                if (level.isClientSide)
                    spewSmoke();
                break;

            default:
                break;
        }
    }

    public void cleanUpFromEruption() {
        int xPos = worldPosition.getX();
        int zPos = worldPosition.getZ();

        for (int x = xPos - (radius * 2); x < xPos + (radius * 2); x++) {
            for (int z = zPos - (radius * 2); z < zPos + (radius * 2); z++) {
                for (int y = LAVA_BASE_LEVEL + heightOffset; y < 140; y++) {
                    BlockPos outBlockPos = new BlockPos(x, y, z);
                    if (level.getBlockState(outBlockPos).is(Blocks.LAVA))
                        level.setBlockAndUpdate(outBlockPos, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    public void throwLavaFromCaldera(double force) {
        // Get a random angle from 0 to 2PI (radians)
        float facingAngle = level.random.nextFloat() * (float) Math.TAU;
        // Get a random angle between 5 and 30 degrees
        float pitchAngle = (5 + (level.random.nextFloat() * 25)) * Mth.DEG_TO_RAD;

        Vec3 vec = new Vec3(0, 1, 0) // Create a unit vector facing up
                .xRot(pitchAngle) // lean it by the pitchAngle
                .yRot(facingAngle); // and rotate by the facingAngle

        // Get the ball's deltaMovement by scaling the unit vector by force
        Vec3 motion = vec.normalize().scale(force);

        var center = worldPosition.getCenter();
        var volcanoCenter = new Vec3(center.x, lavaLevel + 2, center.z);
        Vec3 offset = vec.multiply(1, 0, 1).normalize() // flatten unit vector
                .scale(radius / 2f) // scale it by size of volcano
                .add(volcanoCenter); // offset the volcanoCenter by it

        throwLava(offset, motion);
    }


    public void throwLava(Vec3 pos, Vec3 deltaMovement) {
        throwLava(pos.x, pos.y, pos.z, deltaMovement.x, deltaMovement.y, deltaMovement.z);
    }

    public void throwLava(double posX, double posY, double posZ, double xMot, double yMot, double zMot) {
        if (!level.isClientSide)
            level.addFreshEntity(new LavaBallEntity(TropicraftEntities.LAVA_BALL.get(), level, posX, posY, posZ, xMot
                    , yMot, zMot));
    }

    private void raiseLavaLevels() {
        if (lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION + heightOffset) {
            lavaLevel++;
            setBlocksOnLavaLevel(Blocks.LAVA.defaultBlockState(), 3);
        }
    }

    private void lowerLavaLevels() {
        if (lavaLevel > LAVA_BASE_LEVEL + heightOffset) {
            setBlocksOnLavaLevel(Blocks.AIR.defaultBlockState(), 3);
            lavaLevel--;
        }
    }

    private void setBlocksOnLavaLevel(BlockState state, int updateFlag) {
        int xPos = worldPosition.getX();
        int zPos = worldPosition.getZ();

        for (int x = xPos - radius; x < xPos + radius; x++) {
            for (int z = zPos - radius; z < zPos + radius; z++) {
                if (Math.sqrt(Math.pow(x - xPos, 2) + Math.pow(z - zPos, 2)) < radius + 3) {
                    BlockPos botPos = new BlockPos(x, 10, z);
                    if (level.getBlockState(botPos).is(Blocks.LAVA)) {
                        BlockPos pos2 = new BlockPos(x, lavaLevel, z);

                        if (lavaLevel >= MAX_LAVA_LEVEL_DURING_RISE + heightOffset && lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION + heightOffset) {
                            if (level.getBlockState(pos2).getBlock() != TropicraftBlocks.CHUNK.get()) {
                                level.setBlock(pos2, state, updateFlag);
                            }
                        } else {
                            level.setBlock(pos2, state, updateFlag);
                        }
                    }
                }
            }
        }
    }

    public void spewSmoke() {
        int n = level.random.nextInt(100) + 4;
        for (int i = 0; i < n; i++) {
            double x = worldPosition.getX() + level.random.nextInt(radius) * (level.random.nextBoolean() ? -1 : 1);
            double y = lavaLevel + level.random.nextInt(6);
            double z = worldPosition.getZ() + level.random.nextInt(radius) * (level.random.nextBoolean() ? -1 : 1);
            level.addParticle(TropicraftParticles.VOLCANO_SMOKE_PARTICLE.get(), true, x, y, z, 0.0, 0.7, 0.0);
        }
    }

    private void updateStates() {
        switch (state) {
            // If volcano is dormant, count down to smoking
            case DORMANT:
                ticksUntilSmoking--;

                // If it's time to smoke, then let's smoke
                if (ticksUntilSmoking <= 0) {
                    state = VolcanoState.SMOKING;
                    ticksUntilSmoking = VolcanoState.getTimeBefore(VolcanoState.SMOKING);
                }
                break;
            // If volcano is smoking, count down to raising lava
            case SMOKING:
                ticksUntilRising--;

                // If it's time to raise lava, then raise the roof...of lava!
                if (ticksUntilRising <= 0) {
                    state = VolcanoState.RISING;
                    ticksUntilRising = VolcanoState.getTimeBefore(VolcanoState.RISING);
                }
                break;
            // If volcano is done smoking, raise up lava levels
            case RISING:
                ticksUntilEruption--;

                // If it's time to erupt, then LET'S ERUPT
                if (ticksUntilEruption <= 0) {
                    state = VolcanoState.ERUPTING;
                    ticksUntilEruption = VolcanoState.getTimeBefore(VolcanoState.ERUPTING);
                }
                break;
            // If lava is done rising, ERUPT
            case ERUPTING:
                ticksUntilRetreating--;

                // If it's time to become dormant, then let's chill, yo
                if (ticksUntilRetreating <= 0) {
                    state = VolcanoState.RETREATING;
                    ticksUntilRetreating = VolcanoState.getTimeBefore(VolcanoState.RETREATING);
                }
                break;
            // If eruption is complete, lower lava levels
            case RETREATING:
                ticksUntilDormant--;

                // If it's time to go to sleep, then let's do it
                if (ticksUntilDormant <= 0) {
                    state = VolcanoState.DORMANT;
                    ticksUntilDormant =
                            VolcanoState.getTimeBefore(VolcanoState.DORMANT) + level.random.nextInt(RAND_DORMANT_DURATION);
                }
                break;
            default:
                break;
        }
    }

    private void setLavaLevel() {
        int x = worldPosition.getX();
        int z = worldPosition.getZ();
        int minY = LAVA_BASE_LEVEL + heightOffset;
        int maxY = level.getMaxBuildHeight();
        for (BlockPos pos : BlockPos.betweenClosed(x, minY, z, x, maxY, z)) {
            if (!level.getFluidState(pos).is(FluidTags.LAVA)) {
                lavaLevel = pos.getY() - 1;
                return;
            }
        }
    }

    /**
     * Calculate the radius of this volcano
     *
     * @return the number of lava blocks going outwards in the +x direction from this block
     */
    private int findRadius() {
        int x = worldPosition.getX();
        int z = worldPosition.getZ();
        for (BlockPos pos : BlockPos.betweenClosed(x, 10, z, x + 60, 10, z)) {
            if (!level.getFluidState(pos).is(FluidTags.LAVA)) {
                return pos.getX() - worldPosition.getX();
            }
        }
        return -1;
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        heightOffset = nbt.getInt("height_offset");
        state = VolcanoState.valueOf(nbt.getString("state"));
        ticksUntilDormant = nbt.getInt("ticksUntilDormant");
        ticksUntilSmoking = nbt.getInt("ticksUntilSmoking");
        ticksUntilRising = nbt.getInt("ticksUntilRising");
        ticksUntilEruption = nbt.getInt("ticksUntilEruption");
        ticksUntilRetreating = nbt.getInt("ticksUntilRetreating");
        lavaLevel = nbt.getInt("lavaLevel");
        radius = nbt.getInt("radius");
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.putInt("height_offset", heightOffset);
        nbt.putString("state", state.name());
        nbt.putInt("ticksUntilDormant", ticksUntilDormant);
        nbt.putInt("ticksUntilSmoking", ticksUntilSmoking);
        nbt.putInt("ticksUntilRising", ticksUntilRising);
        nbt.putInt("ticksUntilEruption", ticksUntilEruption);
        nbt.putInt("ticksUntilRetreating", ticksUntilRetreating);
        nbt.putInt("lavaLevel", lavaLevel);
        nbt.putInt("radius", radius);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        loadAdditional(pkt.getTag(), registries);
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt, registries);
        return nbt;
    }

    public void setHeightOffset(int y) {
        heightOffset = y;
    }
}
