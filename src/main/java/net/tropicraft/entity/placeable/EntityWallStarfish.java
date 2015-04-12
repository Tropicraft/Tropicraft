package net.tropicraft.entity.placeable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.entity.underdasea.StarfishType;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityWallStarfish extends EntityHanging implements IEntityAdditionalSpawnData {
	private StarfishType starfishType;

	public EntityWallStarfish(World par1World) {
		super(par1World);
		setStarfishType(StarfishType.values()[0]);
	}

	public EntityWallStarfish(World world, int xCoord, int yCoord, int zCoord, int direction, StarfishType starfishType) {
		super(world, xCoord, yCoord, zCoord, direction);
		setDirection(direction);
		setStarfishType(starfishType);
	}

	@Override
	public int getWidthPixels() {
		return 9;
	}

	@Override
	public int getHeightPixels() {
		return 9;
	}

//	@Override
	public void dropItemStack() {
		//this.entityDropItem(new ItemStack(TropicraftItems.shells, 1, getShellType()), 0.0F);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setByte("StarfishType", (byte)getStarfishType().ordinal());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setStarfishType(StarfishType.values()[nbt.getByte("StarfishType")]);
	}

	public StarfishType getStarfishType() {
		return starfishType;
	}
	
	public void setStarfishType(StarfishType starfishType) {
		this.starfishType = starfishType;
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
		data.writeByte(starfishType.ordinal());
		data.writeByte(hangingDirection);
	}

	@Override
	public void readSpawnData(ByteBuf data) {
		field_146063_b = data.readInt();
		field_146064_c = data.readInt();
		field_146062_d = data.readInt();
		starfishType = StarfishType.values()[data.readByte()];
		setDirection(data.readByte());		
	}
}