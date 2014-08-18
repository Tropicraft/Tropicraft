package tropicraft.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tropicraft.EnumToolMaterialTropics;
import tropicraft.creative.TropiCreativeTabs;

public class ItemDagger extends ItemTropicraftImpl {

    private int weaponDamage;
	
	public ItemDagger(int par1, String imageName, EnumToolMaterialTropics enumtoolmaterial) {
		super(par1, imageName, TropiCreativeTabs.tabCombat);
        maxStackSize = 1;
        setMaxDamage(enumtoolmaterial.getMaxUses());
        weaponDamage = 4 + enumtoolmaterial.getDamageVsEntity();
	}
	

    @Override
    public float getStrVsBlock(ItemStack itemstack, Block block) {
        return block.blockID != Block.web.blockID ? 1.5F : 15F;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        itemstack.damageItem(1, entityliving1);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving)
    {
        return true;
    }

  //  @Override
 //   public int getDamageVsEntity(Entity entity) {
  //  public float func_82803_g() {
   //     return weaponDamage;
        //TODO
 //  }
    
    public float func_82803_g()
    {
        return weaponDamage;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 0x11940;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    @Override
    public boolean canHarvestBlock(Block block) {
        return block.blockID == Block.web.blockID;
    }

}
