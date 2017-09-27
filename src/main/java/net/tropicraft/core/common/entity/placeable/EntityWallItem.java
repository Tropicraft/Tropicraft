package net.tropicraft.core.common.entity.placeable;

import javax.annotation.Nullable;

import com.google.common.base.Optional;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

	public static final DataParameter<Optional<ItemStack>> ITEM = EntityDataManager.<Optional<ItemStack>> createKey(EntityItemFrame.class, DataSerializers.OPTIONAL_ITEM_STACK);

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
		this.dataManager.register(ITEM, Optional.absent());
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

			this.dataManager.get(ITEM).transform(s -> this.entityDropItem(s, 0.0F));
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

	@Nullable
	public ItemStack getDisplayedItem() {
		return (ItemStack) ((Optional) this.getDataManager().get(ITEM)).orNull();
	}

	public void setDisplayedItem(@Nullable ItemStack stack) {
		this.setDisplayedItemWithUpdate(stack, true);
	}

	private void setDisplayedItemWithUpdate(@Nullable ItemStack stack, boolean p_174864_2_) {
		if (stack != null) {
			stack = stack.copy();
			stack.stackSize = 1;
		}

		this.getDataManager().set(ITEM, Optional.fromNullable(stack));
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
			this.setDisplayedItemWithUpdate(ItemStack.loadItemStackFromNBT(nbttagcompound), false);
		}

		super.readEntityFromNBT(compound);
	}
}
