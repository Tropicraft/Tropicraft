package net.tropicraft.event;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.tropicraft.item.tool.IUnderwaterTool;
import net.tropicraft.item.tool.ItemUnderwaterShovel;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCFluidRegistry;
import net.tropicraft.registry.TCItemRegistry;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TCItemEvents {
	
    public Map<Block, Item> buckets = new HashMap<Block, Item>();
    
    public TCItemEvents() {
    	buckets.put(TCBlockRegistry.tropicsWater, TCItemRegistry.bucketTropicsWater);
    }
    
    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {

            ItemStack result = fillCustomBucket(event.world, event.target);

            if (result != null) {
                event.result = result;
                event.setResult(Result.ALLOW);
            }
    }
    
    private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) {
        Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
        Item bucket = buckets.get(block);

        if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) {
            world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
            return new ItemStack(bucket);
        } else return null;
    }
    
    /*@SubscribeEvent
    public void handleBucketFillEvent(FillBucketEvent event) {
        ItemStack bucket = new ItemStack(TCItemRegistry.bucketTropicsWater);

        World world = event.world;

        int x = event.target.blockX;
        int y = event.target.blockY;
        int z = event.target.blockZ;
        int meta = world.getBlockMetadata(x, y, z);

        Fluid fluid = FluidRegistry.lookupFluidForBlock(world.getBlock(x, y, z));

        if (fluid != null) {
            if (fluid == TCFluidRegistry.tropicsWater && meta == 0) {
                TCItemRegistry.bucketTropicsWater.fill(bucket, new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME), true);

                world.setBlockToAir(x, y, z);

                event.result = bucket;
                event.setResult(Result.ALLOW);
            }
        }
    }*/

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

        if (itemstack != null && itemstack.getItem() != null) {
            if (itemstack.getItem() instanceof IUnderwaterTool) {
                if (isFullyUnderwater(player.worldObj, player)) {
                    event.newSpeed = event.originalSpeed * (player.onGround ? 5F : 10F);
                } else { // Nerf underwater tools above water
                    event.newSpeed = event.originalSpeed / 14F;
                    if (itemstack.getItem() instanceof ItemUnderwaterShovel)
                        event.newSpeed /= 5F;
                }
            }
        }
    }

    private boolean isFullyUnderwater(World world, EntityPlayer player) {
        return player.isInsideOfMaterial(Material.water);
    }
}
