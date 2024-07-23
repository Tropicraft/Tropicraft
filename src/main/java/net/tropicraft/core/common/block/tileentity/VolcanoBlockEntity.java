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
import net.tropicraft.core.common.volcano.VolcanoState;

import javax.annotation.Nullable;

public class VolcanoBlockEntity extends BlockEntity {
    private static final int RAND_DORMANT_DURATION = 4000;
    private static final int MAX_LAVA_LEVEL_DURING_RISE = VolcanoStructurePiece.VOLCANO_CRUST - 1;
    private static final int MAX_LAVA_LEVEL_DURING_ERUPTION = VolcanoStructurePiece.VOLCANO_CRUST + 1;
    private static final int LAVA_BASE_LEVEL = VolcanoStructurePiece.LAVA_LEVEL;
    private static final int LAVA_ERUPT_LEVEL = VolcanoStructurePiece.LAVA_LEVEL + 11;

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

        if (!getLevel().isClientSide) {
            //System.out.println(radius + " Volcano Update: " + pos.getX() + " " + pos.getZ() + " State:" + state + " lvl: " + lavaLevel);
            //System.out.println("smoking: " + ticksUntilSmoking + " rising: " + ticksUntilRising + " eruption: " + ticksUntilEruption + " retreating: " + ticksUntilRetreating + " dormant: " + ticksUntilDormant);
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
                if (!getLevel().isClientSide) {
                    //	if ((ticksUntilRetreating % (getWorld().rand.nextInt(40) + 10) == 0)/* && time > 800 && !falling*/) {
                    if (getLevel().random.nextInt(15) == 0) {
                        throwLavaFromCaldera(0.05 + Math.abs(getLevel().random.nextGaussian()) * (lavaLevel > 90 ? LAVA_ERUPT_LEVEL + heightOffset : 0.75));
                    }
                    //	}

                    //	if ((ticksUntilRetreating % (getWorld().rand.nextInt(40) + 10) == 0) && lavaLevel > 90) {
                    if (getLevel().random.nextInt(15) == 0) {
                        throwLavaFromCaldera(0.05 + Math.abs(getLevel().random.nextGaussian()) * (lavaLevel > LAVA_ERUPT_LEVEL + heightOffset ? 1 : 0.75));
                    }
                    //	}
                }
                break;
            case RETREATING:
                if (ticksUntilDormant % 30 == 0) {
                    lowerLavaLevels();
                }
                break;
            case RISING:
                if (getLevel().isClientSide) {
                    spewSmoke();
                }

                if (ticksUntilEruption % 20 == 0) {
                    if (lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION + heightOffset) {
                        raiseLavaLevels();
                    } else {
                        ticksUntilEruption = 0;
                        getLevel().playLocalSound(worldPosition.getX(), 73, worldPosition.getY(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.NEUTRAL, 1.0f, getLevel().random.nextFloat() / 4 + 0.825f, false);
                        int balls = getLevel().random.nextInt(25) + 15;

                        for (int i = 0; i < balls; i++) {
                            throwLavaFromCaldera((getLevel().random.nextDouble() * 0.5) + 1.25);
                        }
                        break;
                    }
                }
                break;
            case SMOKING:
                // TODO: Client only in the future if this is particles
                if (getLevel().isClientSide) {
                    //if (ticksUntilRising % 100 == 0) {
                    //if (getWorld().rand.nextInt(10) == 0)
                    spewSmoke();
                    //}
                }
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
                    if (getLevel().getBlockState(outBlockPos).is(Blocks.LAVA)) {
                        getLevel().setBlockAndUpdate(outBlockPos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    public void throwLavaFromCaldera(double force) {
        // Create vector at center facing in the +x direction
        Vec3 pos = new Vec3(((getLevel().random.nextDouble() / 2) + 0.3) * radius, lavaLevel + 2, 0);
        // Get a random angle from 0 to 2PI (radians)
        float angle = getLevel().random.nextFloat() * (float) Math.PI * 2;
        // Rotate the center vector to this angle, and offset it to the volcano's position
        pos = pos.yRot(angle).add(Vec3.atCenterOf(getBlockPos()));
        // Compute x/y components of angle
        double motX = force * Math.cos(angle);
        double motZ = force * Math.sin(-angle);
        throwLava(pos.x, pos.y, pos.z, motX, 0.86, motZ);
    }

    public void throwLava(double i, double j, double k, double xMot, double yMot, double zMot) {
        if (!getLevel().isClientSide) {
            getLevel().addFreshEntity(new LavaBallEntity(TropicraftEntities.LAVA_BALL.get(), getLevel(), i, j, k, xMot, yMot, zMot));
        }
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
                    if (getLevel().getBlockState(botPos).is(Blocks.LAVA)) {
                        BlockPos pos2 = new BlockPos(x, lavaLevel, z);

                        if (lavaLevel >= MAX_LAVA_LEVEL_DURING_RISE + heightOffset && lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION + heightOffset) {
                            if (getLevel().getBlockState(pos2).getBlock() != TropicraftBlocks.CHUNK.get()) {
                                getLevel().setBlock(pos2, state, updateFlag);
                            }
                        } else {
                            getLevel().setBlock(pos2, state, updateFlag);
                            // System.out.println("Setting block " + x + " " + lavaLevel + " " + z + " to whatever");
                        }
                        //getWorld().setBlockWithNotify(x, lavaLevel, z, falling ? 0 : lavaLevel >= 95 ? TropicraftMod.tempLavaMoving.blockID : Block.lavaStill.blockID);
                    }
                }
            }
        }

        //		if (updateFlag == 0) {
        //			getWorld().markBlockRangeForRenderUpdate(xPos - radius, lavaLevel, zPos - radius, xPos + radius, lavaLevel, zPos + radius);
        //		}
    }

    public void spewSmoke() {
        // System.out.println("Spewing smoke");
        int n = getLevel().random.nextInt(100) + 4;
        for (int i = 0; i < n; i++) {
            // getWorld().spawnEntity(new EntitySmoke(getWorld(), xPos + rand.nextInt(10) - 5, lavaLevel + rand.nextInt(4), zPos + rand.nextInt(10) - 5));
            double x = worldPosition.getX() + getLevel().random.nextInt(radius) * (getLevel().random.nextBoolean() ? -1 : 1);
            double y = lavaLevel + getLevel().random.nextInt(6);
            double z = worldPosition.getZ() + getLevel().random.nextInt(radius) * (getLevel().random.nextBoolean() ? -1 : 1);
            getLevel().addParticle(ParticleTypes.LARGE_SMOKE, true, x, y, z, 0.0, 0.7, 0.0);
            //System.out.println("Spewing smoke " + x + " " + z);
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
                    // cleanUpFromEruption();
                    state = VolcanoState.DORMANT;
                    ticksUntilDormant = VolcanoState.getTimeBefore(VolcanoState.DORMANT) + getLevel().random.nextInt(RAND_DORMANT_DURATION);
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
