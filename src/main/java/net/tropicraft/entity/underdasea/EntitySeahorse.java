package net.tropicraft.entity.underdasea;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntitySeahorse extends EntityTropicraftWaterMob {

	private static final int DATAWATCHER_COLOR = 30;

	public EntitySeahorse(World par1World) {
		super(par1World);
		this.type = WaterMobType.OCEAN_DWELLER;
		this.setSize(0.75F, 1.1F);
	}

	public EntitySeahorse(World par1World, int color) {
		this (par1World);
		this.setColor((byte)color);
	}

	@Override
	public void entityInit() {
		this.dataWatcher.addObject(DATAWATCHER_COLOR, Byte.valueOf((byte)0));
		super.entityInit();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setByte("Color", Byte.valueOf(getColor()));
		super.writeEntityToNBT(nbt);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		setColor(nbt.getByte("Color"));
		super.readEntityFromNBT(nbt);
	}

	public byte getColor() {
		return this.dataWatcher.getWatchableObjectByte(DATAWATCHER_COLOR);
	}

	public void setColor(byte color) {
		this.dataWatcher.updateObject(DATAWATCHER_COLOR, Byte.valueOf(color));
	}

	public String getColorName() {
		switch (getColor()) {
		case 0:
			return "razz";
		case 1:
			return "blue";
		case 2:
			return "cyan";
		case 3:
			return "yellow";
		case 4:
			return "green";
		case 5:
			return "orange";
		default:
			return "razz";
		}
	}

    @Override
    protected int attackStrength() {
        return 0;
    }
}
