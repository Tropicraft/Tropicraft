package net.tropicraft.core.common.item.scuba;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.item.scuba.api.IAirType;
import net.tropicraft.core.common.item.scuba.api.IScubaGear;
import net.tropicraft.core.common.item.scuba.api.IScubaTank;
import net.tropicraft.core.common.item.scuba.api.ScubaMaterial;
import net.tropicraft.core.registry.CreativeTabRegistry;

public class ItemScubaChestplateGear extends ItemScubaGear {

    /** Number of ticks between updates - every second ideally */
    public static final int UPDATE_RATE = 20;

    public ItemScubaChestplateGear(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, EntityEquipmentSlot slot) {
        super(material, scubaMaterial, renderIndex, slot);
        this.setHasSubtypes(true);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
        IScubaGear cap = itemstack.getCapability(ScubaCapabilities.getGearCapability(), null);
        ItemStack leftTank = cap.getStackInSlot(0);
        ItemStack rightTank = cap.getStackInSlot(1);
        
        String suitType = scubaMaterial.getDisplayName();

        tooltip.add(TextFormatting.BLUE + I18n.format("tropicraft.gui.suit.type", TextFormatting.GRAY + suitType));
        tooltip.add("");
        
        if (leftTank == null) {
            tooltip.add(I18n.format("tropicraft.gui.gear.tank.left.none"));
        } else {
            tooltip.add(I18n.format("tropicraft.gui.gear.tank.left.info"));
//            IScubaTank tank = leftTank.getCapability(ScubaCapabilities.getTankCapability(), null);
//            IAirType airType = tank.getAirType();
//            String airRemaining = tank.getPressure() + " psi";
            leftTank.getItem().addInformation(leftTank, world, tooltip, flagIn);
//            list.add(TextFormatting.BLUE + I18n.format("gui.tropicraft:airType", TextFormatting.GRAY + airType.getDisplayName()));
//            list.add(TextFormatting.BLUE + I18n.format("gui.tropicraft:maxAirCapacity", TextFormatting.GRAY.toString() + airType.getMaxCapacity() + " psi"));
//            list.add(TextFormatting.BLUE + I18n.format("gui.tropicraft:airRemaining", TextFormatting.GRAY + airRemaining));
//            list.add(TextFormatting.BLUE + I18n.format("gui.tropicraft:useEfficiency", TextFormatting.GRAY, (airType.getUsageRate() * 20)));
            tooltip.add("");
        }
        if (rightTank == null) {
            tooltip.add(I18n.format("tropicraft.gui.gear.tank.right.none"));
        } else {
            tooltip.add(I18n.format("tropicraft.gui.gear.tank.right.info"));
//            IScubaTank tank = rightTank.getCapability(ScubaCapabilities.getTankCapability(), null);
//            IAirType airType = tank.getAirType();
//            String airRemaining = tank.getPressure() + " psi";
            rightTank.getItem().addInformation(rightTank, world, tooltip, flagIn);
//            list.add(TextFormatting.BLUE + I18n.format("gui.tropicraft:airType", TextFormatting.GRAY + airType.getDisplayName()));
//            list.add(TextFormatting.BLUE + I18n.format("gui.tropicraft:maxAirCapacity", TextFormatting.GRAY.toString() + airType.getMaxCapacity() + " psi"));
//            list.add(TextFormatting.BLUE + I18n.format("gui.tropicraft:airRemaining", TextFormatting.GRAY + airRemaining));
//            list.add(TextFormatting.BLUE + I18n.format("gui.tropicraft:useEfficiency", TextFormatting.GRAY, (airType.getUsageRate() * 20)));
        }
//        list.add(TextFormatting.BLUE + I18n.format("gui.tropicraft:numTanks", TextFormatting.GRAY + numTanks));
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
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return ScubaCapabilities.getProvider(ScubaCapabilities.getGearCapability(), () -> {
            IScubaGear ret = new IScubaGear.ScubaGear() {
                @Override
                public void markDirty() {
                    stack.setTagCompound(serializeNBT());
                }
            };
            if (stack.hasTagCompound()) {
                ret.deserializeNBT(stack.getTagCompound());
            }
            return ret;
        });
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (tab != CreativeTabRegistry.tropicraftTab) return;
        ItemStack noTank = new ItemStack(this, 1, 0);
        list.add(noTank);
        
//        ItemStack singleTankRegular = new ItemStack(item, 1, 1);
//        getTagCompound(singleTankRegular).setFloat("AirContained", AirType.REGULAR.getMaxCapacity());
//        getTagCompound(singleTankRegular).setByte("AirType", AirType.REGULAR.getNbtValue());
//        list.add(singleTankRegular);
//
//        ItemStack doubleTankRegular = new ItemStack(item, 1, 2);
//        getTagCompound(doubleTankRegular).setFloat("AirContained", AirType.REGULAR.getMaxCapacity() * 2);
//        getTagCompound(doubleTankRegular).setByte("AirType", AirType.REGULAR.getNbtValue());
//        list.add(doubleTankRegular);
//
//        ItemStack singleTankTrimix = new ItemStack(item, 1, 1);
//        getTagCompound(singleTankTrimix).setFloat("AirContained", AirType.TRIMIX.getMaxCapacity());
//        getTagCompound(singleTankTrimix).setByte("AirType", AirType.TRIMIX.getNbtValue());
//        list.add(singleTankTrimix);
//
//        ItemStack doubleTankTrimix = new ItemStack(item, 1, 2);
//        getTagCompound(doubleTankTrimix).setFloat("AirContained", AirType.TRIMIX.getMaxCapacity() * 2);
//        getTagCompound(doubleTankTrimix).setByte("AirType", AirType.TRIMIX.getNbtValue());
//        list.add(doubleTankTrimix);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityplayer, EnumHand hand) {
        if (entityplayer.isSneaking()) {
            ItemStack itemstack = entityplayer.getHeldItem(hand);
            entityplayer.openGui(Tropicraft.instance, 0, world, hand.ordinal(), 0, 0);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return super.onItemRightClick(world, entityplayer, hand);
    }

    @Override
    public void onScubaTick(World world, EntityPlayer player, ItemStack itemstack) {        
        if (player.capabilities.isCreativeMode)
            return;

        if (!world.isRemote && world.getTotalWorldTime() % UPDATE_RATE == 0) {
            ItemStack helmetStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            // Ensure the player doesn't drown if they have the proper tanks / air in tanks
            if (!itemstack.isEmpty() && !helmetStack.isEmpty() && helmetStack.getItem() instanceof ItemScubaHelmet) {
                IScubaGear gear = itemstack.getCapability(ScubaCapabilities.getGearCapability(), null);
                IScubaTank tankToEmpty = gear.getFirstNonEmptyTank();

                if (tankToEmpty != null) {
                    float air = tankToEmpty.getPressure();
                    if (air > 0) {
                        IAirType airType = tankToEmpty.getAirType();
                        tankToEmpty.setPressure(Math.max(0, air - airType.getUsageRate()));
                        gear.markDirty();
                        player.setAir(300);
                    }
                }
            }
        }
    }

    @Override
    protected void onRemovedFromArmorInventory(World world, EntityPlayer player, ItemStack itemstack) {
        
    }
}
