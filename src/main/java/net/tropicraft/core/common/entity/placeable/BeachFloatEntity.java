package net.tropicraft.core.common.entity.placeable;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BeachFloatEntity extends FurnitureEntity implements IEntityWithComplexSpawn {

    @Nonnull
    private static final RandomSource rand = RandomSource.create(298457L);
    @Nonnull
    private static final PerlinSimplexNoise windNoise = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(298457L)), ImmutableList.of(0));

    /* Wind */
    private double windModifier = 0;

    /* Is any entity laying on the float? */
    public final boolean isEmpty;

    /* Acceleration */
    public float rotationSpeed;

    /* Water checks */
    private double prevMotionY;

    public BeachFloatEntity(EntityType<BeachFloatEntity> type, Level worldIn) {
        super(type, worldIn, TropicraftItems.BEACH_FLOATS);
        isEmpty = true;
        blocksBuilding = true;
        setId(getId());
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        rand.setSeed(id);
        windModifier = (1 + (rand.nextGaussian() * 0.1)) - 0.05;
    }

    @Override
    public void tick() {
        Entity rider = getControllingPassenger();
        if (level().isClientSide && rider instanceof Player controller) {
            float move = controller.zza;
            float rot = -controller.xxa;
            rotationSpeed += rot * 0.25f;

            float ang = getYRot();
            float moveX = Mth.sin(-ang * 0.017453292f) * move * 0.0035f;
            float moveZ = Mth.cos(ang * 0.017453292f) * move * 0.0035f;
            setDeltaMovement(getDeltaMovement().add(moveX, 0, moveZ));
        }

        if (wasTouchingWater) {
            double windAng = (windNoise.getValue(getX() / 1000, getZ() / 1000, false) + 1) * Math.PI;
            double windX = Math.sin(windAng) * 0.0005 * windModifier;
            double windZ = Math.cos(windAng) * 0.0005 * windModifier;
            setDeltaMovement(getDeltaMovement().add(windX, 0, windZ));
            // Rotate towards a target yaw with some random perturbance
            double targetYaw = Math.toDegrees(windAng) + ((windModifier - 1) * 45);
            double yaw = (Mth.wrapDegrees(getYRot()) + 180 - 35) % 360;
            double angleDiff = targetYaw - yaw;
            if (angleDiff > 0) {
                rotationSpeed += Math.min(0.005 * windModifier, angleDiff);
            } else {
                rotationSpeed += Math.max(-0.005 * windModifier, angleDiff);
            }
        }

        double water = getWaterLevel();
        double center = getCenterY();
        double eps = 1 / 16;
        if (water < center - eps) { // Gravity
            setDeltaMovement(getDeltaMovement().add(0, -Mth.clamp(center - water, 0, 0.04), 0));
        } else if (water > center + eps) {
            double floatpush = Mth.clamp(water - center, 0, 0.02);
            setDeltaMovement(getDeltaMovement().add(0, floatpush, 0));
        } else if (Math.abs(getDeltaMovement().y) < 0.02) { // Close enough, just force to the correct spot
            if (getDeltaMovement().y != 0) {
                lerpY = water - 0.011;
            }
            setDeltaMovement(getDeltaMovement().multiply(1, 0, 1));
            prevMotionY = 0;
        }

        super.tick();

        setYRot(getYRot() + rotationSpeed);
        move(MoverType.PLAYER, getDeltaMovement());

        setDeltaMovement(getDeltaMovement().multiply(0.9, 0.9, 0.9));
        rotationSpeed *= 0.9f;

        if (!level().isClientSide) {
            List<Entity> list = level().getEntities(this, getBoundingBox().inflate(0.20000000298023224, 0.0, 0.20000000298023224));
            for (Entity entity : list) {
                if (entity != getControllingPassenger() && entity.isPushable()) {
                    entity.push(this);
                }
            }

            if (getControllingPassenger() != null && !getControllingPassenger().isAlive()) {
                ejectPassengers();
            }
        }
    }

    @Override
    protected boolean preventMotion() {
        return false;
    }

    private double getCenterY() {
        AABB bb = getBoundingBox();
        return bb.minY + (bb.maxY - bb.minY) * 0.5;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        prevMotionY = getDeltaMovement().y;
        super.checkFallDamage(y, onGroundIn, state, pos);
    }

    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {
        fluidHeight.clear();
        updateWaterState();
        boolean lava = updateFluidHeightAndDoFluidPushing(FluidTags.LAVA, level().dimensionType().ultraWarm() ? 0.007 : 0.0023333333333333335);
        return isInWater() || lava;
    }

    void updateWaterState() {
        AABB temp = getBoundingBox();
        setBoundingBox(temp.contract(1, 0, 1).contract(-1, 0.125, -1));

        try {
            if (updateFluidHeightAndDoFluidPushing(FluidTags.WATER, 0.014)) {
                if (!wasTouchingWater && !firstTick) {
                    doWaterSplashEffect();
                }

                fallDistance = 0.0f;
                wasTouchingWater = true;
                clearFire();
            } else {
                wasTouchingWater = false;
            }
        } finally {
            setBoundingBox(temp);
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (invulnerablityCheck(player, hand) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        } else if (!level().isClientSide && !player.isShiftKeyDown()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }

        return !player.isPassengerOfSameVehicle(this) ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    /* Following three methods copied from EntityBoat for passenger updates */

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction move) {
        super.positionRider(passenger, move);
        if (!passenger.getType().is(EntityTypeTags.CAN_TURN_IN_BOATS)) {
            passenger.setYRot(passenger.getYRot() + rotationSpeed);
            passenger.setYHeadRot(passenger.getYHeadRot() + rotationSpeed);
            applyYawToEntity(passenger);
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (passenger instanceof Player) {
            passenger.refreshDimensions();
        }
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        if (!entityToUpdate.level().isClientSide || isClientFirstPerson(entityToUpdate)) {
            entityToUpdate.setYBodyRot(getYRot());
            float yaw = Mth.wrapDegrees(entityToUpdate.getYRot() - getYRot());
            float pitch = Mth.wrapDegrees(entityToUpdate.getXRot() - getXRot());
            float clampedYaw = Mth.clamp(yaw, -105.0f, 105.0f);
            float clampedPitch = Mth.clamp(pitch, -100.0f, 0.0f);
            float yawClampDelta = clampedYaw - yaw;
            float pitchClampDelta = clampedPitch - pitch;
            entityToUpdate.yRotO += yawClampDelta;
            entityToUpdate.setYRot(entityToUpdate.getYRot() + yawClampDelta);
            entityToUpdate.xRotO += pitchClampDelta;
            entityToUpdate.setXRot(entityToUpdate.getXRot() + pitchClampDelta);
            entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
        }
    }

    @Override
    public void onPassengerTurned(@Nonnull Entity entityToUpdate) {
        applyYawToEntity(entityToUpdate);
    }

    private static boolean isClientFirstPerson(Entity entity) {
        Minecraft client = Minecraft.getInstance();
        return client.cameraEntity == entity && client.options.getCameraType() == CameraType.FIRST_PERSON;
    }

    /* Again, from entity boat, for water checks */

    private float getWaterLevel() {
        AABB axisalignedbb = getBoundingBox();
        int minX = Mth.floor(axisalignedbb.minX);
        int maxX = Mth.ceil(axisalignedbb.maxX);
        int minY = Mth.floor(axisalignedbb.minY - prevMotionY);
        int maxY = minY + 1;
        int minZ = Mth.floor(axisalignedbb.minZ);
        int maxZ = Mth.ceil(axisalignedbb.maxZ);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        float waterHeight = minY - 1;

        for (int y = maxY; y >= minY; --y) {
            for (int x = minX; x < maxX; x++) {
                for (int z = minZ; z < maxZ; ++z) {
                    pos.set(x, y, z);
                    FluidState fluidstate = level().getFluidState(pos);

                    if (fluidstate.getType().isSame(Fluids.WATER)) {
                        waterHeight = Math.max(waterHeight, pos.getY() + fluidstate.getHeight(level(), pos));
                    }
                    if (waterHeight >= maxY) {
                        return waterHeight;
                    }
                }
            }
        }

        return waterHeight;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof LivingEntity living ? living : null;
    }

    @Override
    public Direction getMotionDirection() {
        return getDirection().getClockWise();
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeDouble(lerpYaw);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        lerpYaw = Mth.wrapDegrees(additionalData.readDouble());
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.BEACH_FLOATS.get(DyeColor.byId(getColor().getId())).get());
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return getBoundingBox().inflate(0.1, 0.1, 0.1);
    }
}
