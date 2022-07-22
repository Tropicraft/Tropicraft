package net.tropicraft.core.common.block.jigarbov;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public final class JigarbovTorchPlacement {
    @SubscribeEvent
    public static void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        BlockState placedState = event.getPlacedBlock();
        Block placedBlock = placedState.getBlock();
        if (placedBlock == Blocks.REDSTONE_WALL_TORCH) {
            RegistryObject<RedstoneWallTorchBlock> jigarbovTorchBlock = getJigarbovTorchFor(event.getPlacedAgainst().getBlock());
            if (jigarbovTorchBlock != null) {
                BlockState jigarbovTorch = jigarbovTorchBlock.get().defaultBlockState();
                jigarbovTorch = copyPropertiesTo(jigarbovTorch, placedState);

                event.getWorld().setBlock(event.getPos(), jigarbovTorch, Block.UPDATE_ALL);
            }
        }
    }

    @Nullable
    private static RegistryObject<RedstoneWallTorchBlock> getJigarbovTorchFor(Block placedAgainst) {
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
