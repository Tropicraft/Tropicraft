package net.tropicraft.core.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;

import java.util.List;
import java.util.function.Supplier;

public class FurnitureItem<T extends FurnitureEntity> extends Item {

    private final Supplier<? extends EntityType<T>> entityType;
    private final DyeColor color;

    public FurnitureItem(Properties properties, Supplier<? extends EntityType<T>> entityType, DyeColor color) {
        super(properties);
        this.entityType = entityType;
        this.color = color;
    }

    @Override
    public InteractionResult use(Level level, Player placer, InteractionHand hand) {
        ItemStack heldItem = placer.getItemInHand(hand);
        HitResult rayTraceResult = getPlayerPOVHitResult(level, placer, ClipContext.Fluid.ANY);
        if (rayTraceResult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        }

        Vec3 viewVector = placer.getViewVector(1.0f);
        List<Entity> nearbyEntities = level.getEntities(placer, placer.getBoundingBox().expandTowards(viewVector.scale(5.0)).inflate(1.0), EntitySelector.NO_SPECTATORS);
        if (!nearbyEntities.isEmpty()) {
            Vec3 eyePosition = placer.getEyePosition(1.0f);
            for (Entity nearbyEnt : nearbyEntities) {
                AABB nearbyBB = nearbyEnt.getBoundingBox().inflate(nearbyEnt.getPickRadius());
                if (nearbyBB.contains(eyePosition)) {
                    return InteractionResult.PASS;
                }
            }
        }

        if (rayTraceResult.getType() == HitResult.Type.BLOCK) {
            Vec3 hitVec = rayTraceResult.getLocation();

            T entity = entityType.get().create(level, EntitySpawnReason.SPAWN_ITEM_USE);
            entity.snapTo(BlockPos.containing(hitVec), placer.getYRot() + 180.0f, 0.0f);
            if (level instanceof ServerLevel serverlevel) {
                EntityType.createDefaultStackConfig(serverlevel, heldItem, placer).accept(entity);
            }

            entity.setDeltaMovement(Vec3.ZERO);
            entity.setColor(color);

            if (!level.noCollision(entity, entity.getBoundingBox().inflate(-0.1))) {
                return InteractionResult.FAIL;
            }

            if (!level.isClientSide()) {
                level.addFreshEntity(entity);
            }

            heldItem.consume(1, placer);
            placer.awardStat(Stats.ITEM_USED.get(this));

            return InteractionResult.SUCCESS.heldItemTransformedTo(heldItem);
        } else {
            return InteractionResult.PASS;
        }
    }
}
