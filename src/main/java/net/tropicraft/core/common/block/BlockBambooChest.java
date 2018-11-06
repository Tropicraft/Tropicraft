package net.tropicraft.core.common.block;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import javax.annotation.Nullable;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.tropicraft.core.common.block.tileentity.TileEntityBambooChest;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;

public class BlockBambooChest extends BlockChest {
    
    private static final BlockChest.Type TYPE;
    static {
        TYPE = EnumHelper.addEnum(BlockChest.Type.class, "BAMBOO", new Class<?>[0]);
    }

	public BlockBambooChest() {
		super(TYPE);
		this.setHardness(2.5F);
		this.disableStats();
	}
	
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return TileEntityFactory.getBambooChestTE();
    }

    /**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * EntityPlayer.
     */
    @SuppressWarnings("deprecation")
    @Override
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
        TileEntityBambooChest tile = (TileEntityBambooChest) world.getTileEntity(pos);
        if (tile != null && tile.isUnbreakable()) {
            return 0.0F;
        }
        return super.getPlayerRelativeBlockHardness(state, player, world, pos);
    }
    
    private static final MethodHandle _upperChest, _lowerChest;
    static {
        MethodHandle uc = null, lc = null;
        try {
            Lookup lookup = MethodHandles.lookup();
            uc = lookup.unreflectGetter(ReflectionHelper.findField(InventoryLargeChest.class, "field_70478_c", "lowerChest"));
            lc = lookup.unreflectGetter(ReflectionHelper.findField(InventoryLargeChest.class, "field_70477_b", "upperChest"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        _upperChest = uc;
        _lowerChest = lc;
    }
    
    @Override
    @Nullable
    public ILockableContainer getContainer(World worldIn, BlockPos pos, boolean allowBlocking) {
        ILockableContainer ret = super.getContainer(worldIn, pos, allowBlocking);
        if (_upperChest != null && _lowerChest != null && ret instanceof InventoryLargeChest) {
            InventoryLargeChest invLC = (InventoryLargeChest) ret;
            try {
                // Replace the name of the large inventory with our own, without copying all the code from super :D
                return new InventoryLargeChest("tile.tropicraft.bamboo_chest_large.name", (ILockableContainer) _upperChest.invokeExact(invLC), (ILockableContainer) _lowerChest.invokeExact(invLC));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
