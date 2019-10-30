package net.tropicraft.core.common.item;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;

public class UmbrellaItem extends Item implements IColoredItem {

    private final DyeColor color;

    public UmbrellaItem(final Properties properties, final DyeColor color) {
        super(properties);
        this.color = color;
    }

    @Override
    public int getColor(ItemStack stack, int pass) {
        return (pass == 0 ? 16777215 : color.getColorValue());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity placer, Hand hand) {
        ItemStack heldItem = placer.getHeldItem(hand);
        RayTraceResult rayTraceResult = rayTrace(world, placer, RayTraceContext.FluidMode.ANY);
        if (rayTraceResult.getType() == net.minecraft.util.math.RayTraceResult.Type.MISS) {
            return new ActionResult<>(ActionResultType.PASS, heldItem);
        } else {
            Vec3d lookvec = placer.getLook(1.0F);
            List<Entity> nearbyEntities = world.getEntitiesInAABBexcluding(placer, placer.getBoundingBox().expand(lookvec.scale(5.0D)).grow(1.0D), EntityPredicates.NOT_SPECTATING);
            if (!nearbyEntities.isEmpty()) {
                Vec3d eyePosition = placer.getEyePosition(1.0F);
                Iterator<Entity> nearbyEntityIterator = nearbyEntities.iterator();

                while (nearbyEntityIterator.hasNext()) {
                    Entity nearbyEnt = nearbyEntityIterator.next();
                    AxisAlignedBB nearbyBB = nearbyEnt.getBoundingBox().grow((double)nearbyEnt.getCollisionBorderSize());
                    if (nearbyBB.contains(eyePosition)) {
                        return new ActionResult<>(ActionResultType.PASS, heldItem);
                    }
                }
            }

            if (rayTraceResult.getType() == net.minecraft.util.math.RayTraceResult.Type.BLOCK) {
                Vec3d hitVec = rayTraceResult.getHitVec();

                if (!world.isRemote) {
                    System.out.println("place");
                }

                final FurnitureEntity umbrella = TropicraftEntities.UMBRELLA.get().create(world);
                umbrella.moveToBlockPosAndAngles(new BlockPos(hitVec.x, hitVec.y, hitVec.z), 0, 0);
                umbrella.setMotion(Vec3d.ZERO);
                umbrella.rotationYaw = placer.rotationYaw;
                umbrella.setColor(this.color);

                if (!world.isCollisionBoxesEmpty(umbrella, umbrella.getBoundingBox().grow(-0.1D))) {
                    return new ActionResult<>(ActionResultType.FAIL, heldItem);
                } else {
                    if (!world.isRemote) {
                        world.addEntity(umbrella);
                    }

                    if (!placer.abilities.isCreativeMode) {
                        heldItem.shrink(1);
                    }

                    placer.addStat(Stats.ITEM_USED.get(this));
                    return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
                }
            } else {
                return new ActionResult<>(ActionResultType.PASS, heldItem);
            }
        }
    }
}
