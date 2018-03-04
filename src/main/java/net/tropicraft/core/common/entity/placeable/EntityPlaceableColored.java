package net.tropicraft.core.common.entity.placeable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.tropicraft.ColorHelper;

public abstract class EntityPlaceableColored extends EntityPlaceableBase {

	protected static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityBeachFloat.class, DataSerializers.VARINT);

	public EntityPlaceableColored(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(COLOR, Integer.valueOf(ColorHelper.DEFAULT_VALUE));
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.setColor(Integer.valueOf(nbt.getInteger("COLOR")));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger("COLOR", Integer.valueOf(this.getColor()));
	}

	/**
	 * @return Returns the damage value associated with the color of this chair
	 */
	public int getDamageFromColor() {
		return ColorHelper.getDamageFromColor(this.getColor());
	}

	public void setColor(int color) {
		this.dataManager.set(COLOR, Integer.valueOf(color));
	}

	public void setColor(float red, float green, float blue) {
		this.dataManager.set(COLOR, Integer.valueOf(ColorHelper.getColor(red, green, blue)));
	}

	public int getColor() {
		return ((Integer)this.dataManager.get(COLOR)).intValue();
	}

}
