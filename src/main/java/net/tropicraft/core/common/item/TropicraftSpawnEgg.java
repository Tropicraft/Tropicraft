package net.tropicraft.core.common.item;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class TropicraftSpawnEgg<T extends Entity> extends Item {

    private final RegistryEntry<EntityType<T>> typeIn;

    public TropicraftSpawnEgg(final RegistryEntry<EntityType<T>> type, Properties properties) {
        super(properties);
        this.typeIn = type;
    }

    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemStack = context.getItemInHand();
            BlockPos pos = context.getClickedPos();
            Direction dir = context.getClickedFace();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (block == Blocks.SPAWNER) {
                BlockEntity te = world.getBlockEntity(pos);
                if (te instanceof SpawnerBlockEntity) {
                    BaseSpawner spawner = ((SpawnerBlockEntity)te).getSpawner();
                    EntityType<?> spawnType = typeIn.get();
                    spawner.setEntityId(spawnType);
                    te.setChanged();
                    world.sendBlockUpdated(pos, state, state, 3);
                    itemStack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }

            BlockPos spawnPos;
            if (state.getCollisionShape(world, pos).isEmpty()) {
                spawnPos = pos;
            } else {
                spawnPos = pos.relative(dir);
            }

            EntityType<?> type3 = typeIn.get();
            if (type3.spawn((ServerLevel) world, itemStack, context.getPlayer(), spawnPos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(pos, spawnPos) && dir == Direction.UP) != null) {
                itemStack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (world.isClientSide) {
            return InteractionResultHolder.pass(heldItem);
        } else {
            HitResult rayTraceResult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
            if (rayTraceResult.getType() != HitResult.Type.BLOCK) {
                return InteractionResultHolder.pass(heldItem);
            } else {
                BlockHitResult traceResult = (BlockHitResult) rayTraceResult;
                BlockPos tracePos = traceResult.getBlockPos();
                if (!(world.getBlockState(tracePos).getBlock() instanceof LiquidBlock)) {
                    return InteractionResultHolder.pass(heldItem);
                } else if (world.mayInteract(player, tracePos) && player.mayUseItemAt(tracePos, traceResult.getDirection(), heldItem)) {
                    EntityType<?> type = typeIn.get();
                    if (type.spawn((ServerLevel) world, heldItem, player, tracePos, MobSpawnType.SPAWN_EGG, false, false) == null) {
                        return InteractionResultHolder.pass(heldItem);
                    } else {
                        if (!player.getAbilities().instabuild) {
                            heldItem.shrink(1);
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.success(heldItem);
                    }
                } else {
                    return InteractionResultHolder.fail(heldItem);
                }
            }
        }
    }
}
