package net.tropicraft.core.common.entity.passive;

import com.mojang.serialization.Codec;
import net.minecraft.SharedConstants;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.BinaryAnimation;
import net.tropicraft.core.common.entity.IkWalker;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ShoebillStorkEntity extends Animal {
    public static final float BASE_FOOT_Z = 0.0152f;
    private static final int KICK_SHOES_TICKS = SharedConstants.TICKS_PER_SECOND;
    private static final int KICK_SHOES_DROP_TICKS = KICK_SHOES_TICKS * 2 / 3;

    private static final byte EVENT_KICK_SHOES = EntityEvent.FOX_EAT;

    private static final Codec<HolderSet<Item>> ITEM_SET_CODEC = RegistryCodecs.homogeneousList(Registries.ITEM);

    private final IkWalker walker = new IkWalker(
            6,
            4,
            SharedConstants.TICKS_PER_SECOND,
            0.25f
    );
    // Positions from actual model coordinates of the feet
    private final IkWalker.Foot leftFoot = walker.addFoot(BASE_FOOT_Z, -0.1168f);
    private final IkWalker.Foot rightFoot = walker.addFoot(BASE_FOOT_Z, 0.1168f);

    private final BinaryAnimation flightAnimation = new BinaryAnimation(3, Mth::easeInOutSine);

    @Nullable
    private HolderSet<Item> wantedShoes;
    private boolean pickyAboutShoes;

    private boolean kickingShoes;
    private int kickingShoesTicks;

    public ShoebillStorkEntity(EntityType<? extends ShoebillStorkEntity> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.08);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new RejectShoesGoal());
        goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(2, new TemptGoal(this, 1.25, item -> item.is(ItemTags.FISHES), false));
        goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            Vec3 deltaMovement = getDeltaMovement();
            if (!moveControl.hasWanted() && !onGround() && deltaMovement.y < 0.0) {
                setDeltaMovement(deltaMovement.multiply(1.0, 0.8, 1.0));
            }
        }

        if (kickingShoes) {
            kickingShoesTicks++;
            if (kickingShoesTicks > KICK_SHOES_TICKS) {
                kickingShoes = false;
                kickingShoesTicks = 0;
            } else if (kickingShoesTicks == KICK_SHOES_DROP_TICKS && !level().isClientSide()) {
                ItemStack itemStack = getItemBySlot(EquipmentSlot.FEET);
                setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
                drop(itemStack, false, true);
            }
        }
    }

    @Override
    public void calculateEntityAnimation(boolean includeHeight) {
        super.calculateEntityAnimation(includeHeight);
        if (level().isClientSide()) {
            flightAnimation.tick(!onGround());
            walker.update(this);
        }
    }

    @Override
    public void absSnapTo(double x, double y, double z) {
        super.absSnapTo(x, y, z);
        walker.reset(this);
    }

    @Override
    public void snapTo(double x, double y, double z, float yRot, float xRot) {
        super.snapTo(x, y, z, yRot, xRot);
        walker.reset(this);
    }

    public IkWalker.Foot leftFoot() {
        return leftFoot;
    }

    public IkWalker.Foot rightFoot() {
        return rightFoot;
    }

    public float getFlightAnimation(float partialTicks) {
        return flightAnimation.get(partialTicks);
    }

    @Override
    protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
        return super.canDispenserEquipIntoSlot(slot) || slot == EquipmentSlot.FEET;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public ShoebillStorkEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == EVENT_KICK_SHOES) {
            startKickingShoes();
        }
    }

    private void startKickingShoes() {
        kickingShoes = true;
        level().broadcastEntityEvent(this, EVENT_KICK_SHOES);
    }

    private boolean wantsShoes(ItemStack itemBySlot) {
        return wantedShoes == null || itemBySlot.is(wantedShoes);
    }

    public float getKickAnimation(float partialTicks) {
        if (!kickingShoes) {
            return 0.0f;
        }
        return Math.min(kickingShoesTicks + partialTicks, KICK_SHOES_TICKS) / KICK_SHOES_TICKS;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.storeNullable("wanted_shoes", ITEM_SET_CODEC, wantedShoes);
        output.putBoolean("picky_about_shoes", pickyAboutShoes);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        wantedShoes = input.read("wanted_shoes", ITEM_SET_CODEC).orElse(null);
        pickyAboutShoes = input.getBooleanOr("picky_about_shoes", false);
    }

    private class RejectShoesGoal extends Goal {
        private static final int CHANCE_PER_TICK = SharedConstants.TICKS_PER_SECOND * 5;

        private RejectShoesGoal() {
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            ItemStack feetItem = getItemBySlot(EquipmentSlot.FEET);
            if (feetItem.isEmpty() || wantsShoes(feetItem)) {
                return false;
            }
            return pickyAboutShoes || random.nextInt(adjustedTickDelay(CHANCE_PER_TICK)) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return kickingShoes;
        }

        @Override
        public void start() {
            super.start();
            startKickingShoes();
        }
    }
}
