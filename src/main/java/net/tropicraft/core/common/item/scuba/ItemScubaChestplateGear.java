package net.tropicraft.core.common.item.scuba;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.tropicraft.ColorHelper;
import net.tropicraft.Tropicraft;

public class ItemScubaChestplateGear extends ItemScubaGear {

    /** Number of ticks between updates - every 0.5 seconds ideally */
    public static final int UPDATE_RATE = 10;

    /** Number of ticks until the next update */
    public int ticksUntilUpdate = UPDATE_RATE;

    public ItemScubaChestplateGear(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, EntityEquipmentSlot slot) {
        super(material, scubaMaterial, renderIndex, slot);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
        AirType airType = itemstack.getItemDamage() >= 2 ? AirType.TRIMIX : AirType.REGULAR;
        String airRemaining = getTagCompound(itemstack).getFloat("AirContained") + " psi";
        String numTanks = String.valueOf(itemstack.getItemDamage() % 2 != 0 ? 2 : 1);
        String suitType = this.scubaMaterial.getDisplayName();

        list.add(ColorHelper.color(9) + I18n.translateToLocal("gui.tropicraft:suitType") + ": " + ColorHelper.color(7) + suitType);
        list.add(ColorHelper.color(9) + I18n.translateToLocal("gui.tropicraft:airType") + ": " + ColorHelper.color(7) + airType.getDisplayName());
        list.add(ColorHelper.color(9) + I18n.translateToLocal("gui.tropicraft:numTanks") + ": " + ColorHelper.color(7) + numTanks);
        list.add(ColorHelper.color(9) + I18n.translateToLocal("gui.tropicraft:maxAirCapacity") + ": " + ColorHelper.color(7) + airType.getMaxCapacity() + " psi");
        list.add(ColorHelper.color(9) + I18n.translateToLocal("gui.tropicraft:airRemaining") + ": " + ColorHelper.color(7) + airRemaining);
        list.add(ColorHelper.color(9) + String.format("%s: %s%.3f psi/sec", I18n.translateToLocal("gui.tropicraft:useEfficiency"),  ColorHelper.color(7), (airType.getUsageRate() * 20)));
    }

    /**
     * Gets the type of air this gear uses
     * @param itemstack An ItemStack containing this item
     * @return
     */
    public AirType getAirType(ItemStack itemstack) {
        return itemstack.getItemDamage() >= 2 ? AirType.TRIMIX : AirType.REGULAR;
    }
    
    /**
     * Called from ItemStack.setItem, will hold extra data for the life of this ItemStack.
     * Can be retrieved from stack.getCapabilities()
     * The NBT can be null if this is not called from readNBT or if the item the stack is
     * changing FROM is different then this item, or the previous item had no capabilities.
     *
     * This is called BEFORE the stacks item is set so you can use stack.getItem() to see the OLD item.
     * Remember that getItem CAN return null.
     *
     * @param stack The ItemStack
     * @param nbt NBT of this item serialized, or null.
     * @return A holder instance associated with this ItemStack where you can hold capabilities for the life of this item.
     */
    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new ICapabilityProvider() {
            
            @Override
            public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
            }
            
            @Override
            public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? 
                        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemStackHandler(2)): null;
            }
        };
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack noTank = new ItemStack(item, 1, 0);
        
        ItemStack singleTankRegular = new ItemStack(item, 1, 1);
        getTagCompound(singleTankRegular).setFloat("AirContained", ItemScubaGear.AirType.REGULAR.getMaxCapacity());
        getTagCompound(singleTankRegular).setByte("AirType", ItemScubaGear.AirType.REGULAR.getNbtValue());
        list.add(singleTankRegular);

        ItemStack doubleTankRegular = new ItemStack(item, 1, 2);
        getTagCompound(doubleTankRegular).setFloat("AirContained", ItemScubaGear.AirType.REGULAR.getMaxCapacity() * 2);
        getTagCompound(doubleTankRegular).setByte("AirType", ItemScubaGear.AirType.REGULAR.getNbtValue());
        list.add(doubleTankRegular);

        ItemStack singleTankTrimix = new ItemStack(item, 1, 1);
        getTagCompound(singleTankTrimix).setFloat("AirContained", ItemScubaGear.AirType.TRIMIX.getMaxCapacity());
        getTagCompound(singleTankTrimix).setByte("AirType", ItemScubaGear.AirType.TRIMIX.getNbtValue());
        list.add(singleTankTrimix);

        ItemStack doubleTankTrimix = new ItemStack(item, 1, 2);
        getTagCompound(doubleTankTrimix).setFloat("AirContained", ItemScubaGear.AirType.TRIMIX.getMaxCapacity() * 2);
        getTagCompound(doubleTankTrimix).setByte("AirType", ItemScubaGear.AirType.TRIMIX.getNbtValue());
        list.add(doubleTankTrimix);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer, EnumHand hand) {
        if (world.isRemote) {
            System.err.println("Gui");
            entityplayer.openGui(Tropicraft.instance, 0, world, hand.ordinal(), 0, 0);
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public void onScubaTick(World world, EntityPlayer player, ItemStack itemstack) {        
        if (player.capabilities.isCreativeMode)
            return;
        
        ticksUntilUpdate--;

        if (ticksUntilUpdate <= 0) {
            ItemStack helmetStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            // Ensure the player doesn't drown if they have the proper tanks / air in tanks
            if (itemstack != null && helmetStack != null && helmetStack.getItem() instanceof ItemScubaHelmet) {
                float air = getTagCompound(itemstack).getInteger("AirContained");

                if (air > 0) {
                    AirType airType = itemstack.getItemDamage() >= 2 ? AirType.TRIMIX : AirType.REGULAR;
                    getTagCompound(itemstack).setFloat("AirContained", air - airType.getUsageRate());
                    player.setAir(300);   
                }
            }
            ticksUntilUpdate = UPDATE_RATE;
        }
    }

    @Override
    protected void onRemovedFromArmorInventory(World world, EntityPlayer player, ItemStack itemstack) {
        
    }
}
