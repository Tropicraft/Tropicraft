package net.tropicraft.core.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.Item.Properties;

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
    public ActionResult<ItemStack> use(World world, PlayerEntity placer, Hand hand) {
        ItemStack heldItem = placer.getItemInHand(hand);
        RayTraceResult rayTraceResult = getPlayerPOVHitResult(world, placer, RayTraceContext.FluidMode.ANY);
        if (rayTraceResult.getType() == net.minecraft.util.math.RayTraceResult.Type.MISS) {
            return new ActionResult<>(ActionResultType.PASS, heldItem);
        } else {
            Vector3d lookvec = placer.getViewVector(1.0F);
            List<Entity> nearbyEntities = world.getEntities(placer, placer.getBoundingBox().expandTowards(lookvec.scale(5.0D)).inflate(1.0D), EntityPredicates.NO_SPECTATORS);
            if (!nearbyEntities.isEmpty()) {
                Vector3d eyePosition = placer.getEyePosition(1.0F);
                Iterator<Entity> nearbyEntityIterator = nearbyEntities.iterator();

                while (nearbyEntityIterator.hasNext()) {
                    Entity nearbyEnt = nearbyEntityIterator.next();
                    AxisAlignedBB nearbyBB = nearbyEnt.getBoundingBox().inflate((double)nearbyEnt.getPickRadius());
                    if (nearbyBB.contains(eyePosition)) {
                        return new ActionResult<>(ActionResultType.PASS, heldItem);
                    }
                }
            }

            if (rayTraceResult.getType() == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
                Vector3d hitVec = rayTraceResult.getLocation();

                final T entity = this.entityType.get().create(world);
                entity.moveTo(new BlockPos(hitVec.x, hitVec.y, hitVec.z), 0, 0);
                entity.setDeltaMovement(Vector3d.ZERO);
                entity.setRotation(placer.yRot + 180);
                entity.setColor(this.color);

                if (!world.noCollision(entity, entity.getBoundingBox().inflate(-0.1D))) {
                    return new ActionResult<>(ActionResultType.FAIL, heldItem);
                } else {
                    if (!world.isClientSide) {
                        world.addFreshEntity(entity);
                    }

                    if (!placer.abilities.instabuild) {
                        heldItem.shrink(1);
                    }

                    placer.awardStat(Stats.ITEM_USED.get(this));
                    return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
                }
            } else {
                return new ActionResult<>(ActionResultType.PASS, heldItem);
            }
        }
    }
}
