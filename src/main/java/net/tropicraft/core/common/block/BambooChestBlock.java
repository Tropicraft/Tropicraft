package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleSidedInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class BambooChestBlock extends ChestBlock {
    private static final TileEntityMerger.ICallback<ChestTileEntity, Optional<IInventory>> CHEST_COMBINER = new TileEntityMerger.ICallback<ChestTileEntity, Optional<IInventory>>() {
        @Override
        public Optional<IInventory> acceptDouble(ChestTileEntity p_225539_1_, ChestTileEntity p_225539_2_) {
            return Optional.of(new DoubleSidedInventory(p_225539_1_, p_225539_2_));
        }

        @Override
        public Optional<IInventory> acceptSingle(ChestTileEntity p_225538_1_) {
            return Optional.of(p_225538_1_);
        }

        @Override
        public Optional<IInventory> acceptNone() {
            return Optional.empty();
        }
    };
    public static final TileEntityMerger.ICallback<ChestTileEntity, Optional<INamedContainerProvider>> MENU_PROVIDER_COMBINER = new TileEntityMerger.ICallback<ChestTileEntity, Optional<INamedContainerProvider>>() {
        @Override
        public Optional<INamedContainerProvider> acceptDouble(final ChestTileEntity left, final ChestTileEntity right) {
            final IInventory inventory = new DoubleSidedInventory(left, right);
            return Optional.of(new INamedContainerProvider() {
                @Override
                @Nullable
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    if (left.canOpen(player) && right.canOpen(player)) {
                        left.unpackLootTable(playerInventory.player);
                        right.unpackLootTable(playerInventory.player);
                        return ChestContainer.sixRows(id, playerInventory, inventory);
                    } else {
                        return null;
                    }
                }

                @Override
                public ITextComponent getDisplayName() {
                    if (left.hasCustomName()) {
                        return left.getDisplayName();
                    } else {
                        return right.hasCustomName() ? right.getDisplayName() : new TranslationTextComponent(Constants.MODID + ".container.bambooChestDouble");
                    }
                }
            });
        }

        @Override
        public Optional<INamedContainerProvider> acceptSingle(ChestTileEntity chest) {
            return Optional.of(chest);
        }

        @Override
        public Optional<INamedContainerProvider> acceptNone() {
            return Optional.empty();
        }
    };


///////////////////////////////////////////////////////////////////////////////////

    protected BambooChestBlock(Block.Properties props, Supplier<TileEntityType<? extends ChestTileEntity>> tileEntityTypeIn) {
        super(props, tileEntityTypeIn);
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new BambooChestTileEntity();
    }

    @Override
    @Nullable
    public INamedContainerProvider getMenuProvider(BlockState state, World worldIn, BlockPos pos) {
        return combine(state, worldIn, pos, false).apply(MENU_PROVIDER_COMBINER).orElse(null);
    }

    /**
     * Get the hardness of this Block relative to the ability of the given player
     * 
     * @deprecated call via {@link BlockState#getPlayerRelativeBlockHardness(PlayerEntity, IBlockReader, BlockPos)} whenever possible. Implementing/overriding is fine.
     */
    @Override
    @Deprecated
    public float getDestroyProgress(BlockState state, PlayerEntity player, IBlockReader world, BlockPos pos) {
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
