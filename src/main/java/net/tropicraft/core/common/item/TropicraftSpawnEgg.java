package net.tropicraft.core.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.fml.RegistryObject;

import java.util.Objects;

import net.minecraft.item.Item.Properties;

public class TropicraftSpawnEgg<T extends Entity> extends Item {

    private final RegistryObject<EntityType<T>> typeIn;

    public TropicraftSpawnEgg(final RegistryObject<EntityType<T>> type, Properties properties) {
        super(properties);
        this.typeIn = type;
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemStack = context.getItem();
            BlockPos pos = context.getPos();
            Direction dir = context.getFace();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (block == Blocks.SPAWNER) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof MobSpawnerTileEntity) {
                    AbstractSpawner spawner = ((MobSpawnerTileEntity)te).getSpawnerBaseLogic();
                    EntityType<?> spawnType = typeIn.get();
                    spawner.setEntityType(spawnType);
                    te.markDirty();
                    world.notifyBlockUpdate(pos, state, state, 3);
                    itemStack.shrink(1);
                    return ActionResultType.SUCCESS;
                }
            }

            BlockPos spawnPos;
            if (state.getCollisionShapeUncached(world, pos).isEmpty()) {
                spawnPos = pos;
            } else {
                spawnPos = pos.offset(dir);
            }

            EntityType<?> type3 = typeIn.get();
            if (type3.spawn((ServerWorld) world, itemStack, context.getPlayer(), spawnPos, SpawnReason.SPAWN_EGG, true, !Objects.equals(pos, spawnPos) && dir == Direction.UP) != null) {
                itemStack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (world.isRemote) {
            return ActionResult.resultPass(heldItem);
        } else {
            RayTraceResult rayTraceResult = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
            if (rayTraceResult.getType() != RayTraceResult.Type.BLOCK) {
                return ActionResult.resultPass(heldItem);
            } else {
                BlockRayTraceResult traceResult = (BlockRayTraceResult) rayTraceResult;
                BlockPos tracePos = traceResult.getPos();
                if (!(world.getBlockState(tracePos).getBlock() instanceof FlowingFluidBlock)) {
                    return ActionResult.resultPass(heldItem);
                } else if (world.isBlockModifiable(player, tracePos) && player.canPlayerEdit(tracePos, traceResult.getFace(), heldItem)) {
                    EntityType<?> type = typeIn.get();
                    if (type.spawn((ServerWorld) world, heldItem, player, tracePos, SpawnReason.SPAWN_EGG, false, false) == null) {
                        return ActionResult.resultPass(heldItem);
                    } else {
                        if (!player.abilities.isCreativeMode) {
                            heldItem.shrink(1);
                        }

                        player.addStat(Stats.ITEM_USED.get(this));
                        return ActionResult.resultSuccess(heldItem);
                    }
                } else {
                    return ActionResult.resultFail(heldItem);
                }
            }
        }
    }
}
