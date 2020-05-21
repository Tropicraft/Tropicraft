package net.tropicraft.core.common.block;

import javax.annotation.Nullable;

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

import java.util.Optional;
import java.util.function.Supplier;

public class BambooChestBlock extends ChestBlock {
    private static final TileEntityMerger.ICallback<ChestTileEntity, Optional<IInventory>> field_220109_i = new TileEntityMerger.ICallback<ChestTileEntity, Optional<IInventory>>() {
        public Optional<IInventory> func_225539_a_(ChestTileEntity p_225539_1_, ChestTileEntity p_225539_2_) {
            return Optional.of(new DoubleSidedInventory(p_225539_1_, p_225539_2_));
        }

        public Optional<IInventory> func_225538_a_(ChestTileEntity p_225538_1_) {
            return Optional.of(p_225538_1_);
        }

        public Optional<IInventory> func_225537_b_() {
            return Optional.empty();
        }
    };
    public static final TileEntityMerger.ICallback<ChestTileEntity, Optional<INamedContainerProvider>> field_220110_j = new TileEntityMerger.ICallback<ChestTileEntity, Optional<INamedContainerProvider>>() {
        public Optional<INamedContainerProvider> func_225539_a_(final ChestTileEntity p_225539_1_, final ChestTileEntity p_225539_2_) {
            final IInventory iinventory = new DoubleSidedInventory(p_225539_1_, p_225539_2_);
            return Optional.of(new INamedContainerProvider() {
                @Nullable
                public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                    if (p_225539_1_.canOpen(p_createMenu_3_) && p_225539_2_.canOpen(p_createMenu_3_)) {
                        p_225539_1_.fillWithLoot(p_createMenu_2_.player);
                        p_225539_2_.fillWithLoot(p_createMenu_2_.player);
                        return ChestContainer.createGeneric9X6(p_createMenu_1_, p_createMenu_2_, iinventory);
                    } else {
                        return null;
                    }
                }

                public ITextComponent getDisplayName() {
                    if (p_225539_1_.hasCustomName()) {
                        return p_225539_1_.getDisplayName();
                    } else {
                        return (ITextComponent)(p_225539_2_.hasCustomName() ? p_225539_2_.getDisplayName() : new TranslationTextComponent(Constants.MODID + ".container.bambooChestDouble"));
                    }
                }
            });
        }

        public Optional<INamedContainerProvider> func_225538_a_(ChestTileEntity p_225538_1_) {
            return Optional.of(p_225538_1_);
        }

        public Optional<INamedContainerProvider> func_225537_b_() {
            return Optional.empty();
        }
    };


///////////////////////////////////////////////////////////////////////////////////

    protected BambooChestBlock(Block.Properties props, Supplier<TileEntityType<? extends ChestTileEntity>> tileEntityTypeIn) {
        super(props, tileEntityTypeIn);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new BambooChestTileEntity();
    }

    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        return func_225536_a_(state, worldIn, pos, false).apply(field_220110_j).orElse(null);
    }

    /**
     * Get the hardness of this Block relative to the ability of the given player
     * 
     * @deprecated call via {@link BlockState#getPlayerRelativeBlockHardness(PlayerEntity, IBlockReader, BlockPos)} whenever possible. Implementing/overriding is fine.
     */
    @Deprecated
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader world, BlockPos pos) {
        final BambooChestTileEntity tileEntity = (BambooChestTileEntity) world.getTileEntity(pos);
        if (tileEntity != null && tileEntity.isUnbreakable()) {
            return 0.0f;
        }
        return super.getPlayerRelativeBlockHardness(state, player, world, pos);
    }

    // private static final MethodHandle _upperChest, _lowerChest;
    // static {
    // MethodHandle uc = null, lc = null;
    // try {
    // MethodHandles.Lookup lookup = MethodHandles.lookup();
    // uc = lookup.unreflectGetter(ObfuscationReflectionHelper.findField(DoubleSidedInventory.class, "field_70478_c"));
    // lc = lookup.unreflectGetter(ObfuscationReflectionHelper.findField(DoubleSidedInventory.class, "field_70477_b"));
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
