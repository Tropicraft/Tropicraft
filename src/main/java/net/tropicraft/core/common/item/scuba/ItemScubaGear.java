package net.tropicraft.core.common.item.scuba;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Info;
import net.tropicraft.core.common.item.armor.ItemTropicraftArmor;
import net.tropicraft.core.common.item.scuba.api.ScubaMaterial;
import net.tropicraft.core.registry.CreativeTabRegistry;
import net.tropicraft.core.registry.EntityRenderRegistry;

public abstract class ItemScubaGear extends ItemTropicraftArmor {

    protected final ScubaMaterial scubaMaterial;

    /**
     * When true, {@link #onRemovedFromArmorInventory(World, EntityPlayer, ItemStack)} should get called
     */
    protected boolean outOfArmorInventory = false;
    protected boolean hasCleanedUp = false;

    public ItemScubaGear(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, EntityEquipmentSlot slot) {
        super(material, renderIndex, slot);
        this.scubaMaterial = scubaMaterial;
        this.setCreativeTab(CreativeTabRegistry.tropicraftTab);
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return new ArmorProperties(10, source == DamageSource.DROWN ? 1.0 : 1.0, Integer.MAX_VALUE);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return getArmorTexture(scubaMaterial).toString();
    }
    
    public static ResourceLocation getArmorTexture(ScubaMaterial material) {
        String color = material == ScubaMaterial.PINK ? "Pink" : "Yellow";
        return new ResourceLocation(Info.ARMOR_LOCATION + "scubaGear" + color + ".png");   
    }

    /**
     * Override this method to have an item handle its own armor rendering.
     *
     * @param  entityLiving  The entity wearing the armor
     * @param  itemStack  The itemStack to render the model of
     * @param  armorSlot  The slot the armor is in
     * @param _default Original armor model. Will have attributes set.
     * @return  A ModelBiped to render instead of the default
     */
    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemstack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
        if (itemstack.isEmpty()) {
            return null;
        }

        ModelBiped armorModel = EntityRenderRegistry.getScubaModel(armorSlot);

        if (armorModel != null){
            armorModel.isSneak = entityLiving.isSneaking();
            armorModel.isRiding = entityLiving.isRiding();
            armorModel.isChild = entityLiving.isChild();
            armorModel.rightArmPose = entityLiving.getHeldItemMainhand() != null ? ModelBiped.ArmPose.BLOCK : ModelBiped.ArmPose.EMPTY;
            return armorModel;
        }

        return null;
    }

    /**
     * Override onArmorTick to call onScubaTick only if the scuba gear is in the player's armor list
     */
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemstack) {
        //        for (ItemStack armorStack : player.getArmorInventoryList()) {
        //            if (armorstack.isEmpty()) continue;
        //            if (armorStack.isItemEqualIgnoreDurability(itemstack)) {
        //                this.outOfArmorInventory = false;
        hasCleanedUp = false;
//
//        PlayerDataInstance storage = player.getCapability(Tropicraft.PLAYER_DATA_INSTANCE, null);
//        
//        if (this instanceof ItemScubaFlippers) {
//            storage.scubaFlippers = true;
//        }
//        
//        if (this instanceof ItemScubaChestplate || this instanceof ItemScubaChestplateGear) {
//            storage.scubaChestgear = true;
//        }
//        
//        if (this instanceof ItemScubaHelmet) {
//            storage.scubaHelmet = true;
//        }
//        
        
        this.onScubaTick(world, player, itemstack);
        //                return;
        //            }
        //        }
        //        
        //        this.outOfArmorInventory = true;
    }

    protected abstract void onScubaTick(World world, EntityPlayer player, ItemStack itemstack);

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
//        if (!(entityIn instanceof EntityPlayer)) return;
//
//        EntityPlayer player = (EntityPlayer)entityIn;
//        for (ItemStack armorStack : player.getArmorInventoryList()) {
//            if (armorstack.isEmpty()) continue;
//            if (armorStack.isItemEqual(stack)) {
//                this.outOfArmorInventory = false;
//                return;
//            }
//        }
//
//        this.outOfArmorInventory = true;
//
//        if (outOfArmorInventory && !hasCleanedUp && entityIn instanceof EntityPlayer) {
//            onRemovedFromArmorInventory(worldIn, (EntityPlayer)entityIn, stack);
//            hasCleanedUp = true;
//        }
    }

    /**
     * To be called when an item is no longer in the armor inventory, but may still be in the player's inventory.
     *
     * @param world
     * @param player
     * @param itemstack
     */
    protected abstract void onRemovedFromArmorInventory(World world, EntityPlayer player, ItemStack itemstack);

}
