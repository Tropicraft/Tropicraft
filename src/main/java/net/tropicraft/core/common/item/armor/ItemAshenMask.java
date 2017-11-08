package net.tropicraft.core.common.item.armor;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
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
import net.tropicraft.core.registry.ItemRegistry;


public class ItemAshenMask extends ItemTropicraftArmor {

    private AshenMasks maskType;

    public ItemAshenMask(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, AshenMasks maskType) {
        super(material, renderIndex, slot);
        setHasSubtypes(true);
        this.maxStackSize = 64;
        this.maskType = maskType;
        ItemRegistry.maskMap.put(maskType, this);
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

    @Override
    public void damageArmor(EntityLivingBase player, ItemStack stack, DamageSource source, int damage, int slot) {
        super.damageArmor(player, stack, source, damage, slot);
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (armorSlot == EntityEquipmentSlot.HEAD) // head
            return new RenderArmorMask (maskType.getMeta());
        else
            return null;
    }
}
