package net.tropicraft.core.common.entity.neutral;

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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.passive.WhiteLippedPeccaryEntity;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.function.Supplier;

public class JaguarEntity extends AnimalEntity {
    private static final Supplier<Ingredient> BREEDING_ITEMS = Suppliers.memoize(() -> Ingredient.of(TropicraftTags.Items.MEATS));

    public JaguarEntity(EntityType<? extends JaguarEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.5F));
        this.goalSelector.addGoal(3, new OcelotAttackGoal(this));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25, BREEDING_ITEMS.get(), false));
        this.goalSelector.addGoal(5, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 0.8, 1e-5f));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 10.0F));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, WhiteLippedPeccaryEntity.class, 20, true, true, null));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_ITEMS.get().test(stack);
    }

    @Override
    public JaguarEntity getBreedOffspring(ServerWorld world, AgeableEntity mate) {
        return TropicraftEntities.JAGUAR.get().create(this.level);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.JAGUAR_SPAWN_EGG.get());
    }
}
