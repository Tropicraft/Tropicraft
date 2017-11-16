package net.tropicraft.core.common.entity.placeable;

import javax.annotation.Nullable;

import com.google.common.base.Optional;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityWallItem extends EntityHanging implements IEntityAdditionalSpawnData {

    private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityItemFrame.class, DataSerializers.ITEM_STACK);

	public EntityWallItem(World worldIn) {
		super(worldIn);
	}

	public EntityWallItem(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack) {
		super(worldIn, pos);
		this.updateFacingWithBoundingBox(facing);
		setDisplayedItem(stack);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ITEM, ItemStack.EMPTY);
	}

	@Override
	public int getWidthPixels() {
		return 16;
	}

	@Override
	public int getHeightPixels() {
		return 16;
	}

	@Override
	public void onBroken(@Nullable Entity brokenEntity) {
		if (this.world.getGameRules().getBoolean("doEntityDrops")) {
			if (brokenEntity instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) brokenEntity;

				if (entityplayer.capabilities.isCreativeMode) {
					return;
				}
			}

			this.playSound(SoundEvents.ENTITY_ITEMFRAME_BREAK, 1.0F, 1.0F);
			entityDropItem(dataManager.get(ITEM), 0.0F);
		}
	}

	@Override
	public void playPlaceSound() {}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeByte(this.facingDirection.getHorizontalIndex());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.updateFacingWithBoundingBox(EnumFacing.getHorizontal(additionalData.readByte()));
	}

	public ItemStack getDisplayedItem() {
	    return (ItemStack)this.getDataManager().get(ITEM);
	}

	public void setDisplayedItem(@Nullable ItemStack stack) {
		this.setDisplayedItemWithUpdate(stack, true);
	}

	private void setDisplayedItemWithUpdate(@Nullable ItemStack stack, boolean p_174864_2_) {
		if (stack != null) {
			stack = stack.copy();
			stack.setCount(1);
		}

        this.getDataManager().set(ITEM, stack);
        this.getDataManager().setDirty(ITEM);
	}

	public void writeEntityToNBT(NBTTagCompound compound) {
		if (this.getDisplayedItem() != null) {
			compound.setTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
		}

		super.writeEntityToNBT(compound);
	}

	public void readEntityFromNBT(NBTTagCompound compound) {
		NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");

		if (nbttagcompound != null && !nbttagcompound.hasNoTags()) {
			this.setDisplayedItemWithUpdate(new ItemStack(nbttagcompound), false);
		}

		super.readEntityFromNBT(compound);
	}
}
