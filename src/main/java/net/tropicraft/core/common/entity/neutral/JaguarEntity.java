package net.tropicraft.core.common.entity.neutral;

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
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.passive.WhiteLippedPeccaryEntity;

import java.util.function.Supplier;

public class JaguarEntity extends Animal {
    private static final Supplier<Ingredient> BREEDING_ITEMS = Suppliers.memoize(() -> Ingredient.of(TropicraftTags.Items.MEATS));

    public JaguarEntity(EntityType<? extends JaguarEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.5f));
        goalSelector.addGoal(3, new OcelotAttackGoal(this));
        goalSelector.addGoal(4, new TemptGoal(this, 1.25, BREEDING_ITEMS.get(), false));
        goalSelector.addGoal(5, new BreedGoal(this, 0.8));
        goalSelector.addGoal(6, new FollowParentGoal(this, 1.25));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.8, 1e-5f));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0f));

        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, WhiteLippedPeccaryEntity.class, 20, true, true, null));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return BREEDING_ITEMS.get().test(stack);
    }

    @Override
    public JaguarEntity getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return TropicraftEntities.JAGUAR.get().create(level());
    }
}
