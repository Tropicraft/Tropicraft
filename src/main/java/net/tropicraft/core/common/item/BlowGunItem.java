package net.tropicraft.core.common.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.function.Predicate;

import net.minecraft.item.Item.Properties;

public class BlowGunItem extends ShootableItem {

    public BlowGunItem(final Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (itemStack) -> {
            if (itemStack.getItem() == Items.TIPPED_ARROW) {
                for (final EffectInstance effectInstance : PotionUtils.getMobEffects(itemStack)) {
                    if (effectInstance.getEffect() == Effects.MOVEMENT_SLOWDOWN) {
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
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        ItemStack ammo = getAmmo(player, heldStack);
        if (!ammo.isEmpty()) {
            fireProjectile(world, player, hand, heldStack, ammo, 1.0F, player.abilities.instabuild, 10, 0);
            return new ActionResult<>(ActionResultType.SUCCESS, heldStack);
        } else {
            return new ActionResult<>(ActionResultType.FAIL, heldStack);
        }
    }

    private static ItemStack getAmmo(LivingEntity entityIn, ItemStack stack) {
        final boolean isCreativeMode = entityIn instanceof PlayerEntity && ((PlayerEntity)entityIn).abilities.instabuild;
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
        itemStack = PotionUtils.setCustomEffects(itemStack, ImmutableList.of(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 3 * 20, 20)));
        return itemStack;
    }

    public static void fireProjectile(World world, LivingEntity shooter, Hand hand, ItemStack heldItem, ItemStack projectile, float soundPitch, boolean isCreativeMode, float dmg, float pitch) {
        if (!world.isClientSide) {
            AbstractArrowEntity arrowEntity = createArrow(world, shooter, projectile);
            if (isCreativeMode) {
                arrowEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
            }

            Vector3d lookVec = shooter.getLookAngle();
            Quaternion quaternion = new Quaternion(new Vector3f(lookVec), 0, true);
            Vector3d look = shooter.getViewVector(1.0F);
            Vector3f look3f = new Vector3f(look);
            look3f.transform(quaternion);
            arrowEntity.shoot(look3f.x(), look3f.y(), look3f.z(), dmg, pitch);

            heldItem.hurtAndBreak(1, shooter, (i) -> {
                i.broadcastBreakEvent(hand);
            });

            projectile.split(1);
            if (projectile.isEmpty() && shooter instanceof PlayerEntity) {
                ((PlayerEntity) shooter).inventory.removeItem(projectile);
            }

            world.addFreshEntity(arrowEntity);
            world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
        }
    }

    public static ArrowEntity createArrow(World world, LivingEntity shooter, ItemStack projectile) {
        ArrowItem arrowItem = (ArrowItem) (projectile.getItem() instanceof ArrowItem ? projectile.getItem() : Items.ARROW);
        ArrowEntity arrowEntity = (ArrowEntity) arrowItem.createArrow(world, projectile, shooter);
        arrowEntity.setBaseDamage(0);
        arrowEntity.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        arrowEntity.setCritArrow(false);
        arrowEntity.setEffectsFromItem(getProjectile());
        return arrowEntity;
    }

}
