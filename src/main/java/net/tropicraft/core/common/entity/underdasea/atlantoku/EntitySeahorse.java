package net.tropicraft.core.common.entity.underdasea.atlantoku;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntitySeahorse extends EntityTropicraftWaterBase {

	private static final DataParameter<Byte> TEXTURE_COLOR = EntityDataManager.<Byte>createKey(EntitySeahorse.class, DataSerializers.BYTE);

	public EntitySeahorse(World world) {
		super(world);
		this.setSize(0.75F, 0.75F);
		this.setSwimSpeeds(0.2f, 0.4f, 0.5f);
	}
	
	@Override
	public void entityInit() {
		super.entityInit();
		byte color = (byte)this.world.rand.nextInt(6);
		this.getDataManager().register(TEXTURE_COLOR, Byte.valueOf(color));
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SQUID_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        return false;
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
		return this.dataManager.get(TEXTURE_COLOR);
	}

	public void setColor(byte color) {
		this.dataManager.set(TEXTURE_COLOR, Byte.valueOf(color));
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

}
