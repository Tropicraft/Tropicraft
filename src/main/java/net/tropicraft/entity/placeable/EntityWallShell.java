package net.tropicraft.entity.placeable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.tropicraft.entity.underdasea.StarfishType;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * Represents a shell (or starfish) hanging on the wall.
 * @shellType: determines which shell. Can be derived from item metadata.
 * 
 * @author CBaakman
 */
public class EntityWallShell extends EntityHanging implements IEntityAdditionalSpawnData {

	private int shellType;

	public EntityWallShell(World par1World) {
		super(par1World);
		setShellType(0);
	}

	public EntityWallShell(World world, int xCoord, int yCoord, int zCoord, int direction, int shellType) {
		super(world, xCoord, yCoord, zCoord, direction);
		setDirection(direction);
		this.shellType = shellType;
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
		this.entityDropItem(new ItemStack(TCItemRegistry.shells, 1, this.getShellType ()), 0.0F);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setByte("shellType", (byte)getShellType());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setShellType(nbt.getByte("shellType"));
	}

	public int getShellType() {
		return shellType;
	}
	
	public void setShellType(int shellType) {
		this.shellType = shellType;
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
		data.writeByte(shellType);
		data.writeByte(hangingDirection);
	}

	@Override
	public void readSpawnData(ByteBuf data) {
		field_146063_b = data.readInt();
		field_146064_c = data.readInt();
		field_146062_d = data.readInt();
		shellType = data.readByte();
		setDirection(data.readByte());		
	}
}