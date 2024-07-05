package net.tropicraft.core.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BlowGunItem extends ProjectileWeaponItem {
    private static final PotionContents POTION_CONTENTS = new PotionContents(Optional.empty(), Optional.empty(), List.of(
            new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3 * 20, 20)
    ));

    public BlowGunItem(final Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (itemStack) -> {
            if (itemStack.getItem() == Items.TIPPED_ARROW) {
                final PotionContents contents = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
                for (final MobEffectInstance effectInstance : contents.getAllEffects()) {
                    if (effectInstance.getEffect() == MobEffects.MOVEMENT_SLOWDOWN) {
                        return true;
                    }
                }
            }
            return false;
        };
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }

    @Override
    protected void shootProjectile(final LivingEntity pShooter, final Projectile pProjectile, final int pIndex, final float pVelocity, final float pInaccuracy, final float pAngle, @Nullable final LivingEntity pTarget) {
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        ItemStack ammo = getAmmo(player, heldStack);
        if (!ammo.isEmpty()) {
            fireProjectile(world, player, hand, heldStack, ammo, 1.0F, player.getAbilities().instabuild, 10, 0);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldStack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, heldStack);
        }
    }

    private static ItemStack getAmmo(LivingEntity entityIn, ItemStack stack) {
        final boolean isCreativeMode = entityIn instanceof Player && ((Player) entityIn).getAbilities().instabuild;
        final ItemStack ammo = entityIn.getProjectile(stack);
        if (isCreativeMode) {
            return getProjectile();
        }
        if (!ammo.isEmpty()) {
            return ammo;
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getProjectile() {
        ItemStack itemStack = new ItemStack(Items.TIPPED_ARROW);
        itemStack.set(DataComponents.POTION_CONTENTS, POTION_CONTENTS);
        return itemStack;
    }

    public static void fireProjectile(Level world, LivingEntity shooter, InteractionHand hand, ItemStack heldItem, ItemStack projectile, float soundPitch, boolean isCreativeMode, float dmg, float pitch) {
        if (!world.isClientSide) {
            AbstractArrow arrowEntity = createArrow(world, shooter, projectile, heldItem);
            if (isCreativeMode) {
                arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            Vector3f look = shooter.getViewVector(1.0F).toVector3f();
            arrowEntity.shoot(look.x(), look.y(), look.z(), dmg, pitch);

            heldItem.hurtAndBreak(1, shooter, LivingEntity.getSlotForHand(hand));

            projectile.split(1);
            if (projectile.isEmpty() && shooter instanceof Player) {
                ((Player) shooter).getInventory().removeItem(projectile);
            }

            world.addFreshEntity(arrowEntity);
            world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, soundPitch);
        }
    }

    public static Arrow createArrow(Level world, LivingEntity shooter, ItemStack projectile, ItemStack weapon) {
        final ItemStack projectileWithEffects = projectile.copy();
        projectileWithEffects.set(DataComponents.POTION_CONTENTS, POTION_CONTENTS);
        ArrowItem arrowItem = (ArrowItem) (projectile.getItem() instanceof ArrowItem ? projectile.getItem() : Items.ARROW);
        Arrow arrowEntity = (Arrow) arrowItem.createArrow(world, projectileWithEffects, shooter, weapon);
        arrowEntity.setBaseDamage(0);
        arrowEntity.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        arrowEntity.setCritArrow(false);
        return arrowEntity;
    }

}
