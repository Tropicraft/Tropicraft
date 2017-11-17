package net.tropicraft.core.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDagger extends ItemTropicraft {

    private float weaponDamage;

    public ItemDagger(ToolMaterial enumtoolmaterial) {
        super();
        maxStackSize = 1;
        setMaxDamage(enumtoolmaterial.getMaxUses());
        weaponDamage = 4 + enumtoolmaterial.getAttackDamage();
    }

    /**
     * Metadata-sensitive version of getStrVsBlock
     * @param itemstack The Item Stack
     * @param block The block the item is trying to break
     * @param metadata The items current metadata
     * @return The damage strength
     */
    public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
        return block != Blocks.WEB ? 1.5F : 15F;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        itemstack.damageItem(1, entityliving1);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, IBlockState state, BlockPos pos, EntityLivingBase par7EntityLiving) {
        return true;
    }

    //  @Override
    //   public int getDamageVsEntity(Entity entity) {
    //  public float func_82803_g() {
    //     return weaponDamage;
    //TODO
    //  }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.BLOCK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 0x11940;
    }
    
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemstack, World worldIn, EntityPlayer playerIn, EnumHand hand) {
    	playerIn.setActiveHand(hand);
        return new ActionResult(EnumActionResult.PASS, itemstack);
    }

    @Override
    public boolean canHarvestBlock(IBlockState block) {
        return block == Blocks.WEB;
    }

}
