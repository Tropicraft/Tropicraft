package net.tropicraft.item.scuba;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.item.scuba.ItemScubaGear.AirType;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemDiveComputer extends ItemMap {

    /** Number of ticks between updates */
    public static final int UPDATE_RATE = 20;

    /** Number of ticks until the next update */
    public int ticksUntilUpdate = UPDATE_RATE;

    public ItemDiveComputer() {
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
        if (world.isRemote)
            return;

        if (ticksUntilUpdate <= 0) {
            EntityPlayer player;

            if (entity instanceof EntityPlayer)
                player = (EntityPlayer)entity;
            else
                return;

            if (!isFullyUnderwater(world, player))
                return;

            ItemStack helmetStack = player.getEquipmentInSlot(4);
            ItemStack chestplateStack = player.getEquipmentInSlot(3);
            ItemStack leggingsStack = player.getEquipmentInSlot(2);
            ItemStack flippersStack = player.getEquipmentInSlot(1);

            if (!armorCheck(world, player, helmetStack, chestplateStack, leggingsStack, flippersStack))
                return;

            player.setAir(300);

            float air = getTagCompound(chestplateStack).getFloat("AirContained");
            AirType airType = chestplateStack.getItemDamage() >= 2 ? AirType.TRIMIX : AirType.REGULAR;

            chestplateStack.getTagCompound().setFloat("AirContained", air - airType.getUsageRate());

            int currentDepth = MathHelper.floor_double(player.posY);

            if (currentDepth < chestplateStack.getTagCompound().getInteger("MaxDepth") || chestplateStack.getTagCompound().getInteger("MaxDepth") == 0)
                chestplateStack.getTagCompound().setInteger("MaxDepth", currentDepth);

            int waterBlocksAbove = 0, waterBlocksBelow = 0;
            int x = MathHelper.floor_double(player.posX);
            int y = MathHelper.floor_double(player.posY);
            int z = MathHelper.floor_double(player.posZ);

            while (world.getBlock(x, y + waterBlocksAbove + 1, z).getMaterial().isLiquid()) {
                waterBlocksAbove++;
            }

            while (world.getBlock(x, y - waterBlocksBelow - 1, z).getMaterial().isLiquid()) {
                waterBlocksBelow++;
            }

            chestplateStack.getTagCompound().setInteger("WaterBlocksAbove", waterBlocksAbove);
            chestplateStack.getTagCompound().setInteger("WaterBlocksBelow", waterBlocksBelow);

            ticksUntilUpdate = UPDATE_RATE;
        } else
            ticksUntilUpdate--;
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

    private boolean isFullyUnderwater(World world, EntityPlayer player) {
        int x = MathHelper.ceiling_double_int(player.posX);
        int y = MathHelper.ceiling_double_int(player.posY + player.height - 0.5F);
        int z = MathHelper.ceiling_double_int(player.posZ);

        return world.getBlock(x, y, z).getMaterial().isLiquid();
    }

    private boolean armorCheck(World world, EntityPlayer player, ItemStack helmetStack, ItemStack chestplateStack,
            ItemStack leggingsStack, ItemStack flippersStack) {

        if (helmetStack == null || chestplateStack == null || leggingsStack == null || flippersStack == null)
            return false;

        if (!(helmetStack.getItem() instanceof ItemScubaHelmet))
            return false;

        if (!(leggingsStack.getItem() instanceof ItemScubaLeggings))
            return false;

        if (!(flippersStack.getItem() instanceof ItemScubaFlippers))
            return false;

        if (!(chestplateStack.getItem() instanceof ItemScubaChestplateGear))
            return false;

        ItemScubaHelmet helmet = (ItemScubaHelmet)helmetStack.getItem();
        ItemScubaLeggings leggings = (ItemScubaLeggings)leggingsStack.getItem();
        ItemScubaFlippers flippers = (ItemScubaFlippers)flippersStack.getItem();
        ItemScubaChestplateGear chestplate = (ItemScubaChestplateGear)chestplateStack.getItem();

        if (helmet.scubaMaterial == leggings.scubaMaterial && leggings.scubaMaterial == flippers.scubaMaterial
                && flippers.scubaMaterial == chestplate.scubaMaterial)
            return true;

        return false;
    }

}
