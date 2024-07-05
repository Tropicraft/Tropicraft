package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.tileentity.BambooChestBlockEntity;

import javax.annotation.Nullable;
import java.util.Optional;

public final class BambooChestBlock extends ChestBlock {
    public static final MapCodec<BambooChestBlock> CODEC = simpleCodec(BambooChestBlock::new);

    public static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>() {
        @Override
        public Optional<MenuProvider> acceptDouble(final ChestBlockEntity left, final ChestBlockEntity right) {
            final Container inventory = new CompoundContainer(left, right);
            return Optional.of(new MenuProvider() {
                @Override
                @Nullable
                public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
                    if (left.canOpen(player) && right.canOpen(player)) {
                        left.unpackLootTable(playerInventory.player);
                        right.unpackLootTable(playerInventory.player);
                        return ChestMenu.sixRows(id, playerInventory, inventory);
                    } else {
                        return null;
                    }
                }

                @Override
                public Component getDisplayName() {
                    if (left.hasCustomName()) {
                        return left.getDisplayName();
                    } else {
                        return right.hasCustomName() ? right.getDisplayName() : Component.translatable(Constants.MODID + ".container.bambooChestDouble");
                    }
                }
            });
        }

        @Override
        public Optional<MenuProvider> acceptSingle(ChestBlockEntity chest) {
            return Optional.of(chest);
        }

        @Override
        public Optional<MenuProvider> acceptNone() {
            return Optional.empty();
        }
    };

    protected BambooChestBlock(Block.Properties props) {
        super(props, () -> TropicraftBlocks.BAMBOO_CHEST_ENTITY.get());
    }

    @Override
    public MapCodec<BambooChestBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new BambooChestBlockEntity(TropicraftBlocks.BAMBOO_CHEST_ENTITY.get(), pos, state);
    }

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return combine(state, worldIn, pos, false).apply(MENU_PROVIDER_COMBINER).orElse(null);
    }

    @Override
    @Deprecated
    public float getDestroyProgress(BlockState state, Player player, BlockGetter world, BlockPos pos) {
        final BambooChestBlockEntity tileEntity = (BambooChestBlockEntity) world.getBlockEntity(pos);
        if (tileEntity != null && tileEntity.isUnbreakable()) {
            return 0.0f;
        }
        return super.getDestroyProgress(state, player, world, pos);
    }
}
