package net.tropicraft.core.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;

public class BambooChestBlock extends ChestBlock {

    public BambooChestBlock(final Properties props) {
        super(props);
    }

    public TileEntity createNewTileEntity(IBlockReader world) {
        return new BambooChestTileEntity();
    }

    /**
     * Get the hardness of this Block relative to the ability of the given player
     * @deprecated call via {@link BlockState#getPlayerRelativeBlockHardness(PlayerEntity, IBlockReader, BlockPos)} whenever
     * possible. Implementing/overriding is fine.
     */
    @Deprecated
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader world, BlockPos pos) {
        final BambooChestTileEntity tileEntity = (BambooChestTileEntity) world.getTileEntity(pos);
        if (tileEntity != null && tileEntity.isUnbreakable()) {
            return 0.0f;
        }
        return super.getPlayerRelativeBlockHardness(state, player, world, pos);
    }

//    private static final MethodHandle _upperChest, _lowerChest;
//    static {
//        MethodHandle uc = null, lc = null;
//        try {
//            MethodHandles.Lookup lookup = MethodHandles.lookup();
//            uc = lookup.unreflectGetter(ObfuscationReflectionHelper.findField(DoubleSidedInventory.class, "field_70478_c"));
//            lc = lookup.unreflectGetter(ObfuscationReflectionHelper.findField(DoubleSidedInventory.class, "field_70477_b"));
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        _upperChest = uc;
//        _lowerChest = lc;
//    }
//
//    @Override
//    @Nullable
//    public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos) {
//        INamedContainerProvider ret = super.getContainer(state, world, pos);
//        if (_upperChest != null && _lowerChest != null && ret instanceof DoubleSidedInventory) {
//            DoubleSidedInventory invLC = (DoubleSidedInventory) ret;
//            try {
//                // Replace the name of the large inventory with our own, without copying all the code from super :D
//                return new DoubleSidedInventory((IInventory) _upperChest.invokeExact(invLC), (IInventory) _lowerChest.invokeExact(invLC));
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        }
//        return ret;
//    }
}
