package net.tropicraft.core.common.item.scuba;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Info;
import net.tropicraft.core.common.item.armor.ItemTropicraftArmor;

public abstract class ItemScubaGear extends ItemTropicraftArmor {

    protected ScubaMaterial scubaMaterial;

    public ItemScubaGear(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, EntityEquipmentSlot slot) {
        super(material, renderIndex, slot);
        this.scubaMaterial = scubaMaterial;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Info.ARMOR_LOCATION + "scubaGearPink.png";
    }

    /**
     * Override this method to have an item handle its own armor rendering.
     * 
     * @param  entityLiving  The entity wearing the armor 
     * @param  itemStack  The itemStack to render the model of 
     * @param  armorSlot  0=head, 1=torso, 2=legs, 3=feet
     * 
     * @return  A ModelBiped to render instead of the default
     */
//    @SideOnly(Side.CLIENT)
//    @Override
//    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemstack, EntityEquipmentSlot slot) {
//        if (itemstack == null)
//            return null;
//        //TODO this is weird <_<
//       /* ModelBiped armorModel = Tropicraft.instance.proxy.getArmorModel(0);
//
//        if(armorModel != null){
//            armorModel.bipedHead.showModel = armorSlot == 0;
//            armorModel.bipedHeadwear.showModel = armorSlot == 0;
//            armorModel.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
//            armorModel.bipedRightArm.showModel = armorSlot == 1;
//            armorModel.bipedLeftArm.showModel = armorSlot == 1;
//            armorModel.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
//            armorModel.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
//
//            armorModel.isSneak = entityLiving.isSneaking();
//            armorModel.isRiding = entityLiving.isRiding();
//            armorModel.isChild = entityLiving.isChild();
//            armorModel.heldItemRight = entityLiving.getEquipmentInSlot(0) != null ? 1 :0;
//            if(entityLiving instanceof EntityPlayer){
//                armorModel.aimedBow =((EntityPlayer)entityLiving).getItemInUseDuration() > 2;
//            }
//
//            return armorModel;
//        }*/
//
//        return null;
//    }

    /**
     * Retrives an existing nbt tag compound or creates a new one if it is null
     * @param stack
     * @return
     */
    public NBTTagCompound getTagCompound(ItemStack stack) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        return stack.getTagCompound();
    }

    @Override
    public abstract void onArmorTick(World world, EntityPlayer player, ItemStack itemStack);

    public static enum ScubaMaterial {

        DRY(0, "dry", "Dry"),
        WET(35, "wet", "Wet");

        /** The y-level that a player can safely dive to while wearing this gear material */
        private int maxDepth;

        /** The image prefix of this material type */
        private String imagePrefix;

        /** The name to be displayed when figuring out what type of gear this is */
        private String displayName;

        private ScubaMaterial(int maxDepth, String imagePrefix, String displayName) {
            this.maxDepth = maxDepth;
            this.imagePrefix = imagePrefix;
            this.displayName = displayName;
        }

        public int getMaxDepth() {
            return this.maxDepth;
        }

        public String getImagePrefix() {
            return this.imagePrefix;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }

    public static enum AirType {

        REGULAR(3200, 0.005F, "Regular"),
        TRIMIX(3200, 1.185F, "Trimix");

        /** The max amount of psi one tank of this air type can hold */
        private int maxCapacity;

        /** The average amount of air that escapes one tank of this air per second */
        private float usageRate;

        /** The name that shows up in the GUI when this air type is used */
        private String displayName;

        private AirType(int maxCapacity, float usageRate, String displayName) {
            this.maxCapacity = maxCapacity;
            this.usageRate = usageRate;
            this.displayName = displayName;
        }

        public int getMaxCapacity() {
            return this.maxCapacity;
        }

        public float getUsageRate() {
            return this.usageRate;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }

}
