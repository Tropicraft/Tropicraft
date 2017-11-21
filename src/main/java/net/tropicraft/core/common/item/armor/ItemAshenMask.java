package net.tropicraft.core.common.item.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.entity.model.RenderArmorMask;
import net.tropicraft.core.common.entity.placeable.EntityWallItem;
import net.tropicraft.core.common.enums.AshenMasks;
import net.tropicraft.core.registry.ItemRegistry;


public class ItemAshenMask extends ItemTropicraftArmor {

    private AshenMasks maskType;

    public ItemAshenMask(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, AshenMasks maskType) {
        super(material, renderIndex, slot);
        this.maskType = maskType;
        ItemRegistry.maskMap.put(maskType, this);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing.getAxis().isVertical()) {
            return EnumActionResult.FAIL;
        } else {
            // It's a wall, place the shell on it.
            pos = pos.offset(facing);

            // Must set the world coordinates here, or onValidSurface will be false.
            EntityHanging entityhanging = new EntityWallItem(worldIn, pos, facing, stack);

            if (!playerIn.canPlayerEdit(pos, facing, stack)) {
                return EnumActionResult.FAIL;
            } else {
                if (entityhanging != null && entityhanging.onValidSurface()) {
                    if (!worldIn.isRemote) {
                        worldIn.spawnEntity(entityhanging);
                    }

                    --stack.stackSize;
                }

                return EnumActionResult.SUCCESS;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (armorSlot == EntityEquipmentSlot.HEAD) // head
            return new RenderArmorMask(maskType.getMeta());
        else
            return null;
    }
}
