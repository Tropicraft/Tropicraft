package net.tropicraft.core.common.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Arrays;
import java.util.List;

public class LavaBallEntity extends Entity {
    private static final List<Direction> PLACEMENT_DIRECTIONS = Arrays.asList(Direction.NORTH, Direction.SOUTH,
            Direction.EAST, Direction.WEST);
    public final boolean held;
    public float size;
    public int lifeTimer;

    public LavaBallEntity(EntityType<? extends Entity> type, Level world) {
        super(type, world);
        held = false;
        size = 1;
        lifeTimer = 0;
    }

    public LavaBallEntity(EntityType<? extends LavaBallEntity> type, Level world, double x, double y, double z,
                          double motX, double motY, double motZ) {
        this(type, world);
        moveTo(x, y, z, 0, 0);
        setDeltaMovement(motX, motY, motZ);
    }

    public LavaBallEntity(EntityType<? extends LavaBallEntity> type, Level world, Vec3 pos, Vec3 deltaMovement) {
        this(type, world, pos.x, pos.y, pos.z, deltaMovement.x, deltaMovement.y, deltaMovement.z);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void supahDrip() {
        float x = (float) getX();
        float y = (float) getY();
        float z = (float) getZ();

        if (level().isClientSide) {
            level().addParticle(ParticleTypes.LAVA, x, y, z, -getDeltaMovement().x, -1.5f, -getDeltaMovement().z);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        if (lifeTimer < 500)
            lifeTimer++;
        else
            remove(RemovalReason.DISCARDED);


        if (size < 1)
            size += 0.025F;

        if (!onGround() && level().isClientSide) {
            for (int i = 0; i < 1 + random.nextInt(3); i++)
                supahDrip();
        }

        var delta = getDeltaMovement();
        var deltaX = delta.x;
        var deltaY = delta.y;
        var deltaZ = delta.z;


        double newX = getX() + deltaX;
        double newY = getY() + deltaY;
        double newZ = getZ() + deltaZ;

        if (horizontalCollision) {
            deltaX = 0;
            deltaZ = 0;
        }
        if (verticalCollision) {
            deltaY = 0;
        }

        this.moveTo(new Vec3(newX, newY, newZ));
        setDeltaMovement(new Vec3(deltaX, deltaY, deltaZ).scale(0.999));
        this.applyGravity();
        this.tryCheckInsideBlocks();

        if (!level().isClientSide) {
            BlockPos posCurrent = this.blockPosition();
            if (maybeReplace(posCurrent)) {
                playSound(SoundEvents.BUCKET_EMPTY_LAVA, 5.0F, random.nextFloat() / 4 + 0.425f);
                for (var dir : PLACEMENT_DIRECTIONS)
                    maybeReplace(posCurrent.relative(dir));
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void onInsideBlock(BlockState blockstate) {
        if (!blockstate.isEmpty() && !blockstate.is(TropicraftBlocks.COOLING_LAVA)) {
            setDeltaMovement(Vec3.ZERO);
            setPos(blockPosition().above().getBottomCenter());
        }
    }

    private boolean maybeReplace(BlockPos pos) {
        BlockState stateCurrent = level().getBlockState(pos);
        BlockPos posBelow = pos.below();
        BlockState stateBelow = level().getBlockState(posBelow);
        if (!stateBelow.isEmpty() && !held) {
            if (stateCurrent.isEmpty() || stateCurrent.is(BlockTags.REPLACEABLE)) {
                level().setBlock(pos, TropicraftBlocks.COOLING_LAVA.get().defaultBlockState(), Block.UPDATE_ALL);
                return true;
            }
        }
        return false;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.1;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        lifeTimer = nbt.getInt("lifeTimer");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("lifeTimer", lifeTimer);
    }
}
