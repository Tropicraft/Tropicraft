package net.tropicraft.core.common.entity.passive;

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
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.function.Supplier;

public class WhiteLippedPeccaryEntity extends AnimalEntity {
    private static final Supplier<Ingredient> BREEDING_ITEMS = Suppliers.memoize(() -> Ingredient.fromTag(TropicraftTags.Items.FRUITS));

    public WhiteLippedPeccaryEntity(EntityType<? extends WhiteLippedPeccaryEntity> type, World world) {
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
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_ITEMS.get().test(stack);
    }

    @Override
    public WhiteLippedPeccaryEntity createChild(ServerWorld world, AgeableEntity mate) {
        return TropicraftEntities.WHITE_LIPPED_PECCARY.get().create(this.world);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.WHITE_LIPPED_PECCARY_SPAWN_EGG.get());
    }
}
