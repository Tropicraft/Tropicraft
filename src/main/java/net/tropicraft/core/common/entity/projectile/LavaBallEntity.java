package net.tropicraft.core.common.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class LavaBallEntity extends Entity {
    public boolean setFire;
    public float size;
    public boolean held;
    public int lifeTimer;

    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;

    public LavaBallEntity(EntityType<? extends LavaBallEntity> type, Level world) {
        super(type, world);
        setFire = false;
        held = false;
        size = 1;
        lifeTimer = 0;
    }

    public LavaBallEntity(EntityType<? extends LavaBallEntity> type,Level world, double i, double j, double k, double motX, double motY, double motZ) {
        super(type, world);
        setFire = false;
        moveTo(i, j, k, 0, 0);
        accelerationX = motX;
        accelerationY = motY;
        accelerationZ = motZ;
        size = 1;
        held = false;
        lifeTimer = 0;
    }

    public LavaBallEntity(EntityType<? extends LavaBallEntity> type, Level world, float startSize) {
        super(type, world);
        size = startSize;
        setFire = false;
        held = true;
        lifeTimer = 0;
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
            level().addParticle(ParticleTypes.LAVA, x, y, z, this.getDeltaMovement().x, -1.5F, this.getDeltaMovement().z);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        // System.out.println("laba ball: " + posX + " " + posY + " " + posZ);

        if (lifeTimer < 500) {
            lifeTimer++;
        } else {
            this.remove(RemovalReason.DISCARDED);
        }

        double motionX = this.getDeltaMovement().x;
        double motionY = this.getDeltaMovement().y;
        double motionZ = this.getDeltaMovement().z;

        if (size < 1) {
            size += .025;
        }

        if (onGround()) {
            motionZ *= .95;
            motionX *= .95;
        }

        motionY *= .99;

        if (!onGround()) {
            motionY -=.05F;
            if (level().isClientSide) {
                for (int i = 0; i < 5 + random.nextInt(3); i++){
                    supahDrip();
                }
            }
        }

        if (horizontalCollision) {
            motionZ = 0;
            motionX = 0;
        }

        //TODO: Note below, these used to be tempLavaMoving - maybe they still need to be?
        int thisX = (int)Math.floor(getX());
        int thisY = (int)Math.floor(getY());
        int thisZ = (int)Math.floor(getZ());

        BlockPos posCurrent = new BlockPos(thisX, thisY, thisZ);
        BlockPos posBelow = posCurrent.below();
        BlockState stateBelow = level().getBlockState(posBelow);

        if (!stateBelow.isAir() && !stateBelow.is(Blocks.LAVA) && !held) {
            if (setFire) {
                level().setBlock(posCurrent, Blocks.LAVA.defaultBlockState(), 3);
                this.remove(RemovalReason.DISCARDED);
            }

            if (!setFire) {
                if (level().isEmptyBlock(posCurrent.west())) {
                    level().setBlock(posCurrent.west(), Blocks.LAVA.defaultBlockState(), 2);
                }

                if (level().isEmptyBlock(posCurrent.east())) {
                    level().setBlock(posCurrent.east(), Blocks.LAVA.defaultBlockState(), 2);
                }

                if (level().isEmptyBlock(posCurrent.south())) {
                    level().setBlock(posCurrent.south(), Blocks.LAVA.defaultBlockState(), 2);
                }

                if (level().isEmptyBlock(posCurrent.north())) {
                    level().setBlock(posCurrent.north(), Blocks.LAVA.defaultBlockState(), 2);
                }

                level().setBlock(posCurrent, Blocks.LAVA.defaultBlockState(), 3);
                setFire = true;
            }
        }

        Vec3 motion = new Vec3(motionX + this.accelerationX, motionY + this.accelerationY, motionZ + this.accelerationZ);
        this.setDeltaMovement(motion);

        this.move(MoverType.SELF, motion);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        this.lifeTimer = nbt.getInt("lifeTimer");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putInt("lifeTimer", this.lifeTimer);
    }
}
