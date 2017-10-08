package net.tropicraft.core.common.item.scuba;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Info;
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
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return new ArmorProperties(10, source == DamageSource.drown ? 1.0 : 1.0, Integer.MAX_VALUE);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        String color = scubaMaterial == ScubaMaterial.PINK ? "Pink" : "Yellow";
        return Info.ARMOR_LOCATION + "scubaGear" + color + ".png";   
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

        PINK(35, "pink", "Pink"),
        YELLOW(35, "yellow", "Yellow");

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
