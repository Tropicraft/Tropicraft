package net.tropicraft.core.common.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class LavaBallEntity extends Entity {
    public final boolean held;
    public boolean setFire;
    public float size;
    public int lifeTimer;

    public LavaBallEntity(EntityType<? extends Entity> type, Level world) {
        super(type, world);
        setFire = false;
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
        if (lifeTimer < 500) {
            lifeTimer++;
        } else {
            remove(RemovalReason.DISCARDED);
        }

        var delta = getDeltaMovement();

        double newX = getX() + delta.x;
        double newY = getY() + delta.y;
        double newZ = getZ() + delta.z;
        this.setPos(newX, newY, newZ);

        setDeltaMovement(delta.scale(0.999));
        this.applyGravity();

        if (size < 1) {
            size += 0.025;
        }

        if (!onGround()) {
            if (level().isClientSide) {
                for (int i = 0; i < 1 + random.nextInt(3); i++) {
                    supahDrip();
                }
            }
        }

        BlockPos posCurrent = this.blockPosition();
        BlockState stateCurrent = level().getBlockState(posCurrent);
        BlockPos posBelow = posCurrent.below();
        BlockState stateBelow = level().getBlockState(posBelow);

        if (!stateBelow.isEmpty() && !held) {
            if (stateCurrent.isEmpty())
                level().setBlock(posCurrent, TropicraftBlocks.COOLING_LAVA.get().defaultBlockState(), 3);
            remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.15;
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
