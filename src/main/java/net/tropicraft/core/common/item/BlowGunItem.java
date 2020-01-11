package net.tropicraft.core.common.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FireworkRocketEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class BlowGunItem extends ShootableItem {

    public static final String LOADED = "Loaded";

    public BlowGunItem(final Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return ARROWS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        // TODO figure out how to do crafting or hand-loading
        if (isLoaded(heldStack) || true) {
            fireProjectile(world, player, hand, heldStack, getProjectile(), 1.0F, player.abilities.isCreativeMode, 10, 0);
            setLoaded(heldStack, false);
            return new ActionResult<>(ActionResultType.SUCCESS, heldStack);
        } else {
            return new ActionResult<>(ActionResultType.FAIL, heldStack);
        }
    }

    public static ItemStack getProjectile() {
        ItemStack itemStack = new ItemStack(Items.TIPPED_ARROW);
        itemStack = PotionUtils.appendEffects(itemStack, ImmutableList.of(new EffectInstance(Effects.SLOWNESS, 3 * 20, 20)));
        return itemStack;
    }

    public static boolean isLoaded(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        return nbt != null && nbt.getBoolean(LOADED);
    }

    public static void setLoaded(ItemStack stack, boolean isLoaded) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putBoolean(LOADED, isLoaded);
    }

    public static void fireProjectile(World world, LivingEntity shooter, Hand hand, ItemStack heldItem, ItemStack projectile, float soundPitch, boolean isCreativeMode, float dmg, float pitch) {
        if (!world.isRemote) {
            AbstractArrowEntity arrowEntity = createArrow(world, shooter, projectile);
            if (isCreativeMode) {
                arrowEntity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
            }

            Vec3d lookVec = shooter.func_213286_i(1.0F);
            Quaternion quaternion = new Quaternion(new Vector3f(lookVec), 0, true);
            Vec3d look = shooter.getLook(1.0F);
            Vector3f look3f = new Vector3f(look);
            look3f.func_214905_a(quaternion);
            arrowEntity.shoot(look3f.getX(), look3f.getY(), look3f.getZ(), dmg, pitch);

            heldItem.damageItem(1, shooter, (i) -> {
                i.sendBreakAnimation(hand);
            });
            world.addEntity(arrowEntity);
            world.playSound(null, shooter.posX, shooter.posY, shooter.posZ, SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
        }
    }

    public static ArrowEntity createArrow(World world, LivingEntity shooter, ItemStack projectile) {
        ArrowItem arrowItem = (ArrowItem) (projectile.getItem() instanceof ArrowItem ? projectile.getItem() : Items.ARROW);
        ArrowEntity arrowEntity = (ArrowEntity) arrowItem.createArrow(world, projectile, shooter);
        arrowEntity.setDamage(0);
        arrowEntity.setHitSound(SoundEvents.ITEM_CROSSBOW_HIT);
        arrowEntity.func_213865_o(false);

        return arrowEntity;
    }

}
