package net.tropicraft.core.common.item.scuba;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.scuba.ItemScubaGear.AirType;

public class ItemDiveComputer extends ItemMap {

    /** Number of ticks between updates */
    public static final int UPDATE_RATE = 20;

    /** Number of ticks until the next update */
    public int ticksUntilUpdate = UPDATE_RATE;

    public ItemDiveComputer() {
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
        if (world.isRemote)
            return;

        if (ticksUntilUpdate <= 0) {
            EntityPlayer player;

            if (entity instanceof EntityPlayer)
                player = (EntityPlayer)entity;
            else
                return;

            if (!isFullyUnderwater(world, player)) {
            //    System.out.println("Not fully underwater");
                return;
            }

            ItemStack helmetStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            ItemStack chestplateStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            ItemStack leggingsStack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            ItemStack flippersStack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

            if (!armorCheck(world, player, helmetStack, chestplateStack, leggingsStack, flippersStack))
                return;

            player.setAir(300);

            float air = getTagCompound(chestplateStack).getFloat("AirContained");
            AirType airType = chestplateStack.getItemDamage() >= 2 ? AirType.TRIMIX : AirType.REGULAR;

            chestplateStack.getTagCompound().setFloat("AirContained", air - airType.getUsageRate());

            int currentDepth = MathHelper.floor(player.posY);

            if (currentDepth < chestplateStack.getTagCompound().getInteger("MaxDepth") || chestplateStack.getTagCompound().getInteger("MaxDepth") == 0)
                chestplateStack.getTagCompound().setInteger("MaxDepth", currentDepth);

            int waterBlocksAbove = 0, waterBlocksBelow = 0;
            int x = MathHelper.floor(player.posX);
            int y = MathHelper.floor(player.posY + player.height - 0.5);
            int z = MathHelper.floor(player.posZ);

            while (world.getBlockState(new BlockPos(x, y + waterBlocksAbove + 1, z)).getMaterial().isLiquid()) {
                waterBlocksAbove++;
            }

            while (world.getBlockState(new BlockPos(x, y - waterBlocksBelow - 1, z)).getMaterial().isLiquid()) {
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
        int x = MathHelper.ceil(player.posX);
        int y = MathHelper.ceil(player.posY + player.height - 0.5F);
        int z = MathHelper.ceil(player.posZ);

       // return world.getBlock(x, y, z).getMaterial().isLiquid();
        return player.isInsideOfMaterial(Material.WATER);
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
