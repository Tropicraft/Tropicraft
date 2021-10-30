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

    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemStack = context.getItemInHand();
            BlockPos pos = context.getClickedPos();
            Direction dir = context.getClickedFace();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (block == Blocks.SPAWNER) {
                TileEntity te = world.getBlockEntity(pos);
                if (te instanceof MobSpawnerTileEntity) {
                    AbstractSpawner spawner = ((MobSpawnerTileEntity)te).getSpawner();
                    EntityType<?> spawnType = typeIn.get();
                    spawner.setEntityId(spawnType);
                    te.setChanged();
                    world.sendBlockUpdated(pos, state, state, 3);
                    itemStack.shrink(1);
                    return ActionResultType.SUCCESS;
                }
            }

            BlockPos spawnPos;
            if (state.getCollisionShape(world, pos).isEmpty()) {
                spawnPos = pos;
            } else {
                spawnPos = pos.relative(dir);
            }

            EntityType<?> type3 = typeIn.get();
            if (type3.spawn((ServerWorld) world, itemStack, context.getPlayer(), spawnPos, SpawnReason.SPAWN_EGG, true, !Objects.equals(pos, spawnPos) && dir == Direction.UP) != null) {
                itemStack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (world.isClientSide) {
            return ActionResult.pass(heldItem);
        } else {
            RayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
            if (rayTraceResult.getType() != RayTraceResult.Type.BLOCK) {
                return ActionResult.pass(heldItem);
            } else {
                BlockRayTraceResult traceResult = (BlockRayTraceResult) rayTraceResult;
                BlockPos tracePos = traceResult.getBlockPos();
                if (!(world.getBlockState(tracePos).getBlock() instanceof FlowingFluidBlock)) {
                    return ActionResult.pass(heldItem);
                } else if (world.mayInteract(player, tracePos) && player.mayUseItemAt(tracePos, traceResult.getDirection(), heldItem)) {
                    EntityType<?> type = typeIn.get();
                    if (type.spawn((ServerWorld) world, heldItem, player, tracePos, SpawnReason.SPAWN_EGG, false, false) == null) {
                        return ActionResult.pass(heldItem);
                    } else {
                        if (!player.abilities.instabuild) {
                            heldItem.shrink(1);
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));
                        return ActionResult.success(heldItem);
                    }
                } else {
                    return ActionResult.fail(heldItem);
                }
            }
        }
    }
}
