package net.tropicraft.core.common.entity.placeable;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityBambooItemFrame extends EntityItemFrame {

    /** Chance for this item frame's item to drop from the frame. */
    private float itemDropChance = 1.0F;
    
    private static final DataParameter<EnumFacing> FACING = EntityDataManager.<EnumFacing>createKey(EntityBambooItemFrame.class, DataSerializers.FACING);
	
	public EntityBambooItemFrame(World world) {
		super(world);
	}

	public EntityBambooItemFrame(World world, BlockPos pos, EnumFacing facing) {
		super(world, pos, facing);
		this.updateFacingWithBoundingBox(facing);
	}
	
	@Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (FACING.equals(key)) {
            this.facingDirection = this.getFacing();
            this.updateFacingWithBoundingBox(this.facingDirection);
        }
        super.notifyDataManagerChange(key);
    }
	
	@Override
	protected void entityInit() {
		this.getDataManager().register(FACING, EnumFacing.SOUTH);
		super.entityInit();
	}
	
	public void setFacing(EnumFacing facing) {
		this.dataManager.set(FACING, facing);
		this.facingDirection = getFacing();
		this.updateFacingWithBoundingBox(facing);
	}

	public EnumFacing getFacing() {
		return this.dataManager.get(FACING);
	}
	
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
    public void dropItemOrSelf(@Nullable Entity entityIn, boolean shouldDropItem) {
        if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = this.getDisplayedItem();

            if (entityIn instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;

                if (entityplayer.capabilities.isCreativeMode) {
                    this.removeFrameFromMap(itemstack);
                    return;
                }
            }

            if (shouldDropItem) {
            	System.out.println("dropping");
                this.entityDropItem(new ItemStack(ItemRegistry.bambooItemFrame), 0.0F);
            }

            if (itemstack != null && this.rand.nextFloat() < this.itemDropChance) {
            	System.out.println("bye");
                itemstack = itemstack.copy();
                this.removeFrameFromMap(itemstack);
                this.entityDropItem(itemstack, 0.0F);
            }
        }
    }
	
    /**
     * Removes the dot representing this frame's position from the map when the item frame is broken.
     */
    private void removeFrameFromMap(ItemStack stack) {
        if (stack != null) {
            if (stack.getItem() instanceof net.minecraft.item.ItemMap) {
                MapData mapdata = ((ItemMap)stack.getItem()).getMapData(stack, this.worldObj);
                mapdata.mapDecorations.remove("frame-" + this.getEntityId());
            }

            stack.setItemFrame((EntityBambooItemFrame)null);
        }
    }
}
