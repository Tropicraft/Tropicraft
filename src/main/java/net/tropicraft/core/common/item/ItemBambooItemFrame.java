package net.tropicraft.core.common.item;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.placeable.EntityBambooItemFrame;

public class ItemBambooItemFrame extends ItemHangingEntity {

	public ItemBambooItemFrame(Class<? extends EntityHanging> entityClass) {
		super(entityClass);
	}
	
    /**
     * Called when a Block is right-clicked with this Item
     */
	@Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos blockpos = pos.offset(facing);

        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && playerIn.canPlayerEdit(blockpos, facing, stack)) {
            EntityBambooItemFrame entityhanging = this.createEntity(worldIn, blockpos, facing);
            entityhanging.setFacing(facing);
            
            if (entityhanging != null && entityhanging.onValidSurface()) {
                if (!worldIn.isRemote) {
                    entityhanging.playPlaceSound();
                    worldIn.spawnEntity(entityhanging);
                }

                --stack.stackSize;
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Nullable
    private EntityBambooItemFrame createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide) {
        return new EntityBambooItemFrame(worldIn, pos, clickedSide);
    }

}
