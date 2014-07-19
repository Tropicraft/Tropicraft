package net.tropicraft.event;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.tropicraft.item.tool.IUnderwaterTool;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCFluidRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TCItemEvents {

    @SubscribeEvent
    public void handleBucketFillEvent(FillBucketEvent event) {
        ItemStack iHazBucket = new ItemStack(TCItemRegistry.bucketTropicsWater);

        World world = event.world;

        int x = event.target.blockX;
        int y = event.target.blockY;
        int z = event.target.blockZ;
        int meta = world.getBlockMetadata(x, y, z);

        Fluid fluid = FluidRegistry.lookupFluidForBlock(world.getBlock(x, y, z));

        if (fluid != null) {
            if (fluid == TCFluidRegistry.tropicsWater && meta == 0) {
                TCItemRegistry.bucketTropicsWater.fill(iHazBucket, new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME), true);

                world.setBlockToAir(x, y, z);

                event.result = iHazBucket;
                event.setResult(Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void changeTropicsWaterFogDensity(EntityViewRenderEvent.FogDensity event) {
        int x = MathHelper.floor_double(event.entity.posX);
        int y = MathHelper.ceiling_double_int(event.entity.posY + event.entity.height - 0.5F);
        int z = MathHelper.floor_double(event.entity.posZ);

        if (event.block.getMaterial().isLiquid() && event.block.getUnlocalizedName().equals(TCBlockRegistry.tropicsWater.getUnlocalizedName())) {
            //  System.out.println("hello?");
            event.density = 0.0115F;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void changeTropicsWaterFogColor(EntityViewRenderEvent.FogColors event) {
        int x = MathHelper.floor_double(event.entity.posX);
        int y = MathHelper.ceiling_double_int(event.entity.posY + event.entity.height - 0.5F);
        int z = MathHelper.floor_double(event.entity.posZ);

        if (event.block.getMaterial().isLiquid() && event.block.getUnlocalizedName().equals(TCBlockRegistry.tropicsWater.getUnlocalizedName())) {
            event.red = 0.2F;
            event.green = 0.8F;
            event.blue = 0.5F;
        }
    }

    @SubscribeEvent
    public void handleUnderwaterTools(PlayerEvent.BreakSpeed event) {
        EntityPlayer player = (EntityPlayer)event.entityPlayer;
        ItemStack itemstack = player.getHeldItem();
        
        if (isFullyUnderwater(player.worldObj, player)) {
            if (itemstack != null && itemstack.getItem() != null) {
                if (itemstack.getItem() instanceof IUnderwaterTool) {
                    event.newSpeed = event.originalSpeed * (player.onGround ? 5F : 10F);
                }
            }
        }
    }

    private boolean isFullyUnderwater(World world, EntityPlayer player) {
        int x = MathHelper.ceiling_double_int(player.posX);
        int y = MathHelper.ceiling_double_int(player.posY + player.height - 0.5F);
        int z = MathHelper.ceiling_double_int(player.posZ);

        //return world.getBlock(x, y, z).getMaterial().isLiquid();
        return player.isInsideOfMaterial(Material.water);
    }
}
