package net.tropicraft.core.common.entity.passive.monkey;

import com.google.common.base.Suppliers;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.Easings;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.function.Supplier;

public class SpiderMonkeyEntity extends AnimalEntity {
    private static final Supplier<Ingredient> BREEDING_ITEMS = Suppliers.memoize(() -> Ingredient.fromTag(TropicraftTags.Items.FRUITS));

    private static final int STAND_ANIMATION_LENGTH = 15;

    private static final DataParameter<Boolean> STANDING = EntityDataManager.createKey(SpiderMonkeyEntity.class, DataSerializers.BOOLEAN);

    private int standAnimation;

    public SpiderMonkeyEntity(EntityType<? extends SpiderMonkeyEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, BREEDING_ITEMS.get(), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 10.0)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(STANDING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isRemote) {
            this.tickStandingState();
        } else {
            this.tickStandingAnimation();
        }
    }

    private void tickStandingState() {
        if (this.getRevengeTarget() != null) {
            this.setStanding(false);
            return;
        }

        if (this.world.rand.nextInt(100) == 0) {
            boolean standing = this.world.rand.nextInt(3) == 0;
            this.setStanding(standing);
        }
    }

    private void tickStandingAnimation() {
        if (this.isStanding()) {
            if (this.standAnimation < STAND_ANIMATION_LENGTH) {
                this.standAnimation++;
            }
        } else {
            if (this.standAnimation > 0) {
                this.standAnimation--;
            }
        }
    }

    public void setStanding(boolean standing) {
        this.dataManager.set(STANDING, standing);
    }

    public boolean isStanding() {
        return this.dataManager.get(STANDING);
    }

    public float getStandAnimation(float partialTicks) {
        float animation = (this.standAnimation + (this.isStanding() ? partialTicks : -partialTicks)) / STAND_ANIMATION_LENGTH;
        return Easings.inOutSine(MathHelper.clamp(animation, 0.0F, 1.0F));
    }

    @Override
    public void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        this.setStanding(nbt.getBoolean("standing"));
    }

    @Override
    public void writeAdditional(final CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putBoolean("standing", this.isStanding());
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public SpiderMonkeyEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.SPIDER_MONKEY_SPAWN_EGG.get());
    }

    @Override
    public int getMaxFallHeight() {
        return 5;
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return super.calculateFallDamage(distance, damageMultiplier) / 2;
    }
}
