package net.tropicraft.core.common.entity.passive;

import com.google.common.base.Suppliers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.entity.TropicraftEntities;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SlenderHarvestMouseEntity extends Animal {
    private static final Supplier<Ingredient> BREEDING_ITEMS = Suppliers.memoize(() -> Ingredient.of(TropicraftTags.Items.FRUITS));

    public SlenderHarvestMouseEntity(EntityType<? extends SlenderHarvestMouseEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, BREEDING_ITEMS.get(), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_ITEMS.get().test(stack);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return TropicraftEntities.SLENDER_HARVEST_MOUSE.get().create(this.level());
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return super.calculateFallDamage(fallDistance, damageMultiplier) / 4;
    }
}
