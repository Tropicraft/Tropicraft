package net.tropicraft.core.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.item.Item.Properties;

public class FurnitureItem<T extends FurnitureEntity> extends Item implements IColoredItem {

    private final Supplier<? extends EntityType<T>> entityType;
    private final DyeColor color;

    public FurnitureItem(final Properties properties, final Supplier<? extends EntityType<T>> entityType, final DyeColor color) {
        super(properties);
        this.entityType = entityType;
        this.color = color;
    }

    @Override
    public int getColor(ItemStack stack, int pass) {
        return (pass == 0 ? 16777215 : color.getColorValue());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player placer, InteractionHand hand) {
        ItemStack heldItem = placer.getItemInHand(hand);
        HitResult rayTraceResult = getPlayerPOVHitResult(world, placer, ClipContext.Fluid.ANY);
        if (rayTraceResult.getType() == net.minecraft.world.phys.HitResult.Type.MISS) {
            return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
        } else {
            Vec3 lookvec = placer.getViewVector(1.0F);
            List<Entity> nearbyEntities = world.getEntities(placer, placer.getBoundingBox().expandTowards(lookvec.scale(5.0D)).inflate(1.0D), EntitySelector.NO_SPECTATORS);
            if (!nearbyEntities.isEmpty()) {
                Vec3 eyePosition = placer.getEyePosition(1.0F);
                Iterator<Entity> nearbyEntityIterator = nearbyEntities.iterator();

                while (nearbyEntityIterator.hasNext()) {
                    Entity nearbyEnt = nearbyEntityIterator.next();
                    AABB nearbyBB = nearbyEnt.getBoundingBox().inflate((double)nearbyEnt.getPickRadius());
                    if (nearbyBB.contains(eyePosition)) {
                        return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
                    }
                }
            }

            if (rayTraceResult.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
                Vec3 hitVec = rayTraceResult.getLocation();

                final T entity = this.entityType.get().create(world);
                entity.moveTo(new BlockPos(hitVec.x, hitVec.y, hitVec.z), 0, 0);
                entity.setDeltaMovement(Vec3.ZERO);
                entity.setRotation(placer.yRot + 180);
                entity.setColor(this.color);

                if (!world.noCollision(entity, entity.getBoundingBox().inflate(-0.1D))) {
                    return new InteractionResultHolder<>(InteractionResult.FAIL, heldItem);
                } else {
                    if (!world.isClientSide) {
                        world.addFreshEntity(entity);
                    }

                    if (!placer.abilities.instabuild) {
                        heldItem.shrink(1);
                    }

                    placer.awardStat(Stats.ITEM_USED.get(this));
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
                }
            } else {
                return new InteractionResultHolder<>(InteractionResult.PASS, heldItem);
            }
        }
    }
}
