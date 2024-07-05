package net.tropicraft.core.common.block.jigarbov;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = Constants.MODID)
public final class JigarbovTorchPlacement {
    @SubscribeEvent
    public static void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        BlockState placedState = event.getPlacedBlock();
        Block placedBlock = placedState.getBlock();
        if (placedBlock == Blocks.REDSTONE_WALL_TORCH) {
            BlockEntry<? extends RedstoneWallTorchBlock> jigarbovTorchBlock = getJigarbovTorchFor(event.getPlacedAgainst().getBlock());
            if (jigarbovTorchBlock != null) {
                BlockState jigarbovTorch = jigarbovTorchBlock.get().defaultBlockState();
                jigarbovTorch = copyPropertiesTo(jigarbovTorch, placedState);

                event.getLevel().setBlock(event.getPos(), jigarbovTorch, Block.UPDATE_ALL);
            }
        }
    }

    @Nullable
    private static BlockEntry<? extends RedstoneWallTorchBlock> getJigarbovTorchFor(Block placedAgainst) {
        JigarbovTorchType type = JigarbovTorchType.byBlock(placedAgainst);
        return type != null ? TropicraftBlocks.JIGARBOV_WALL_TORCHES.get(type) : null;
    }

    private static BlockState copyPropertiesTo(BlockState to, BlockState from) {
        for (Property<?> property : from.getProperties()) {
            to = copyPropertyTo(to, from, property);
        }
        return to;
    }

    private static <T extends Comparable<T>> BlockState copyPropertyTo(BlockState to, BlockState from, Property<T> property) {
        if (to.hasProperty(property)) {
            return to.setValue(property, from.getValue(property));
        } else {
            return to;
        }
    }
}
