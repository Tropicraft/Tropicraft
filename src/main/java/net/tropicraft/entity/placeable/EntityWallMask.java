package net.tropicraft.entity.placeable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.tropicraft.entity.underdasea.StarfishType;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * Represents an ashen mask hanging on the wall.
 * @maskType: determines which mask. Can be derived from item metadata.
 * 
 * @author CBaakman
 */
public class EntityWallMask extends EntityHanging implements IEntityAdditionalSpawnData {

	private int maskType;

	public EntityWallMask(World par1World) {
		super(par1World);
		
		this.setMaskType(0);
	}

	public EntityWallMask(World world, int xCoord, int yCoord, int zCoord, int direction, int maskType) {
		super(world, xCoord, yCoord, zCoord, direction);
		setDirection(direction);
		this.maskType = maskType;
	}

	@Override
	public int getWidthPixels() {
		return 12;
	}

	@Override
	public int getHeightPixels() {
		return 12;
	}

//	@Override
	public void dropItemStack() {
		this.entityDropItem(new ItemStack(TCItemRegistry.ashenMask, 1, this.getMaskType ()), 0.0F);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setByte("maskType", (byte)getMaskType());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setMaskType(nbt.getByte("maskType"));
	}

	public int getMaskType() {
		return maskType;
	}
	
	public void setMaskType(int maskType) {
		this.maskType = maskType;
	}

	/**
	 * Called when this entity is broken. Entity parameter may be null.
	 */
	@Override
	public void onBroken(Entity entity) {
		this.dropItemStack();
	}

	@Override
	public void writeSpawnData(ByteBuf data) {
		data.writeInt(field_146063_b);
		data.writeInt(field_146064_c);
		data.writeInt(field_146062_d);
		data.writeByte(maskType);
		data.writeByte(hangingDirection);
	}

	@Override
	public void readSpawnData(ByteBuf data) {
		field_146063_b = data.readInt();
		field_146064_c = data.readInt();
		field_146062_d = data.readInt();
		maskType = data.readByte();
		setDirection(data.readByte());		
	}
}