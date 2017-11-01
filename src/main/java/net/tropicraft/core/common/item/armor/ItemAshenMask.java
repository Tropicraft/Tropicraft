package net.tropicraft.core.common.item.armor;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Names;
import net.tropicraft.core.common.enums.AshenMasks;


public class ItemAshenMask extends ItemTropicraftArmor {

    public ItemAshenMask(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot) {
        super(material, renderIndex, slot);
        setHasSubtypes(true);
        this.maxStackSize = 64;
    }

    /**
     * Called to tick armor in the armor slot. Override to do something
     *
     * @param world
     * @param player
     * @param itemStack
     */
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {

    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (AshenMasks type : AshenMasks.VALUES) {
            list.add(new ItemStack(item, 1, type.getMeta()));
        }
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an
     * ItemStack so different stacks can have different names based on their
     * damage or NBT.
     */
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int i = MathHelper.clamp(par1ItemStack.getItemDamage(), 0, 15);
        return super.getUnlocalizedName() + "." + Names.MASK_NAMES[i];
    }

    @Override
    public void damageArmor(EntityLivingBase player, ItemStack stack, DamageSource source, int damage, int slot) {
        super.damageArmor(player, stack, source, damage, slot);
    }

}
