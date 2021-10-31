package net.tropicraft.core.common.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.util.*;
import com.mojang.math.Quaternion;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

import net.minecraft.world.item.Item.Properties;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

public class BlowGunItem extends ProjectileWeaponItem {

    public BlowGunItem(final Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (itemStack) -> {
            if (itemStack.getItem() == Items.TIPPED_ARROW) {
                for (final MobEffectInstance effectInstance : PotionUtils.getMobEffects(itemStack)) {
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
        final boolean isCreativeMode = entityIn instanceof Player && ((Player)entityIn).getAbilities().instabuild;
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
        itemStack = PotionUtils.setCustomEffects(itemStack, ImmutableList.of(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3 * 20, 20)));
        return itemStack;
    }

    public static void fireProjectile(Level world, LivingEntity shooter, InteractionHand hand, ItemStack heldItem, ItemStack projectile, float soundPitch, boolean isCreativeMode, float dmg, float pitch) {
        if (!world.isClientSide) {
            AbstractArrow arrowEntity = createArrow(world, shooter, projectile);
            if (isCreativeMode) {
                arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            Vec3 lookVec = shooter.getLookAngle();
            Quaternion quaternion = new Quaternion(new Vector3f(lookVec), 0, true);
            Vec3 look = shooter.getViewVector(1.0F);
            Vector3f look3f = new Vector3f(look);
            look3f.transform(quaternion);
            arrowEntity.shoot(look3f.x(), look3f.y(), look3f.z(), dmg, pitch);

            heldItem.hurtAndBreak(1, shooter, (i) -> {
                i.broadcastBreakEvent(hand);
            });

            projectile.split(1);
            if (projectile.isEmpty() && shooter instanceof Player) {
                ((Player) shooter).getInventory().removeItem(projectile);
            }

            world.addFreshEntity(arrowEntity);
            world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, soundPitch);
        }
    }

    public static Arrow createArrow(Level world, LivingEntity shooter, ItemStack projectile) {
        ArrowItem arrowItem = (ArrowItem) (projectile.getItem() instanceof ArrowItem ? projectile.getItem() : Items.ARROW);
        Arrow arrowEntity = (Arrow) arrowItem.createArrow(world, projectile, shooter);
        arrowEntity.setBaseDamage(0);
        arrowEntity.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        arrowEntity.setCritArrow(false);
        arrowEntity.setEffectsFromItem(getProjectile());
        return arrowEntity;
    }

}
