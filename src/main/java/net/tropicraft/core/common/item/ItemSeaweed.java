package net.tropicraft.core.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.block.BlockTropicraftSands;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.registry.BlockRegistry;

public class ItemSeaweed extends ItemTropicraft {

    public ItemSeaweed() {

    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            ItemStack itemstack = player.getHeldItem(hand);
            
            // If not tropics sand, exit
            if (block != BlockRegistry.sands) {
                return EnumActionResult.FAIL;
            }
            
            // If wrong kind of tropics sand, exit
            if (iblockstate.getValue(BlockTropicraftSands.VARIANT) != TropicraftSands.FOAMY) {
                return EnumActionResult.FAIL;
            }
            
            // All is well
            if (player.canPlayerEdit(pos, facing, itemstack)) {
                IBlockState state = BlockRegistry.seaweed.getDefaultState();
                worldIn.setBlockState(pos, state);
                SoundType soundtype = SoundType.SAND;
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
                state.getBlock().onBlockPlacedBy(worldIn, pos, state, player, itemstack);
                return EnumActionResult.SUCCESS;
            }
        }
        
        return EnumActionResult.PASS;
    }
}
