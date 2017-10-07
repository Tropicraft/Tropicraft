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
import net.tropicraft.core.client.entity.model.ModelScubaGear;
import net.tropicraft.core.common.item.armor.ItemTropicraftArmor;
import net.tropicraft.core.registry.CreativeTabRegistry;
import net.tropicraft.core.registry.EntityRenderRegistry;

public abstract class ItemScubaGear extends ItemTropicraftArmor {

    protected ScubaMaterial scubaMaterial;

    public ItemScubaGear(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, EntityEquipmentSlot slot) {
        super(material, renderIndex, slot);
        this.scubaMaterial = scubaMaterial;
        this.setCreativeTab(CreativeTabRegistry.tropicraftTab);
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
     * @param  armorSlot  The slot the armor is in
     * @param _default Original armor model. Will have attributes set.
     * @return  A ModelBiped to render instead of the default
     */
    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemstack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
        if (itemstack == null) {
            return null;
        }

        //TODO this is weird <_<
        ModelBiped armorModel = EntityRenderRegistry.scubaGearModel;

        if (armorModel != null){
//            armorModel.bipedHead.showModel = armorSlot == EntityEquipmentSlot.HEAD;
//            armorModel.bipedHeadwear.showModel = armorSlot == EntityEquipmentSlot.HEAD;
//            armorModel.bipedBody.showModel = armorSlot == EntityEquipmentSlot.CHEST || armorSlot == EntityEquipmentSlot.LEGS;
//            armorModel.bipedRightArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
//            armorModel.bipedLeftArm.showModel = armorSlot == EntityEquipmentSlot.CHEST;
//            armorModel.bipedRightLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET;
//            armorModel.bipedLeftLeg.showModel = armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET;

            armorModel.isSneak = entityLiving.isSneaking();
            armorModel.isRiding = entityLiving.isRiding();
            armorModel.isChild = entityLiving.isChild();
            armorModel.rightArmPose = entityLiving.getHeldItemMainhand() != null ? ModelBiped.ArmPose.BLOCK : ModelBiped.ArmPose.EMPTY;
//            if (entityLiving instanceof EntityPlayer){
//                armorModel.aimedBow =((EntityPlayer)entityLiving).getItemInUseDuration() > 2;
//            }

            return armorModel;
        }

        return null;
    }

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
