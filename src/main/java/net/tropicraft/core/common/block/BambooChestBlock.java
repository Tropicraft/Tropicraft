package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class BambooChestBlock extends ChestBlock {
    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>> CHEST_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>>() {
        @Override
        public Optional<Container> acceptDouble(ChestBlockEntity p_225539_1_, ChestBlockEntity p_225539_2_) {
            return Optional.of(new CompoundContainer(p_225539_1_, p_225539_2_));
        }

        @Override
        public Optional<Container> acceptSingle(ChestBlockEntity p_225538_1_) {
            return Optional.of(p_225538_1_);
        }

        @Override
        public Optional<Container> acceptNone() {
            return Optional.empty();
        }
    };
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
                        return right.hasCustomName() ? right.getDisplayName() : new TranslatableComponent(Constants.MODID + ".container.bambooChestDouble");
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


///////////////////////////////////////////////////////////////////////////////////

    protected BambooChestBlock(Block.Properties props, Supplier<BlockEntityType<? extends ChestBlockEntity>> tileEntityTypeIn) {
        super(props, tileEntityTypeIn);
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter world) {
        return new BambooChestTileEntity();
    }

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return combine(state, worldIn, pos, false).apply(MENU_PROVIDER_COMBINER).orElse(null);
    }

    /**
     * Get the hardness of this Block relative to the ability of the given player
     * 
     * @deprecated call via {@link BlockState#getPlayerRelativeBlockHardness(PlayerEntity, IBlockReader, BlockPos)} whenever possible. Implementing/overriding is fine.
     */
    @Override
    @Deprecated
    public float getDestroyProgress(BlockState state, Player player, BlockGetter world, BlockPos pos) {
        final BambooChestTileEntity tileEntity = (BambooChestTileEntity) world.getBlockEntity(pos);
        if (tileEntity != null && tileEntity.isUnbreakable()) {
            return 0.0f;
        }
        return super.getDestroyProgress(state, player, world, pos);
    }

    // private static final MethodHandle _upperChest, _lowerChest;
    // static {
    // MethodHandle uc = null, lc = null;
    // try {
    // MethodHandles.Lookup lookup = MethodHandles.lookup();
    // uc = lookup.unreflectGetter(ObfuscationReflectionHelper.findField(DoubleSidedInventory.class, "container2"));
    // lc = lookup.unreflectGetter(ObfuscationReflectionHelper.findField(DoubleSidedInventory.class, "container1"));
    // } catch (IllegalAccessException e) {
    // e.printStackTrace();
    // }
    // _upperChest = uc;
    // _lowerChest = lc;
    // }
    //
    // @Override
    // @Nullable
    // public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos) {
    // INamedContainerProvider ret = super.getContainer(state, world, pos);
    // if (_upperChest != null && _lowerChest != null && ret instanceof DoubleSidedInventory) {
    // DoubleSidedInventory invLC = (DoubleSidedInventory) ret;
    // try {
    // // Replace the name of the large inventory with our own, without copying all the code from super :D
    // return new DoubleSidedInventory((IInventory) _upperChest.invokeExact(invLC), (IInventory) _lowerChest.invokeExact(invLC));
    // } catch (Throwable e) {
    // e.printStackTrace();
    // }
    // }
    // return ret;
    // }
}
