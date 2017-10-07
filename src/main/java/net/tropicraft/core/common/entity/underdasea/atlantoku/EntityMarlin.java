package net.tropicraft.core.common.entity.underdasea.atlantoku;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityMarlin extends EntityTropicraftWaterBase {

	public String texture = "";
	
	public EntityMarlin(World world) {
		super(world);
		//heightRange = new int[] { 58, 32 };
		/*texture = ModInfo.TEXTURE_ENTITY_LOC + "marlin.png";
		if(world.isRemote){
			if(rand.nextInt(70) == 0)
			texture = ModInfo.TEXTURE_ENTITY_LOC + "marlin2.png";
		}*/
		
		setSwimSpeeds(0.8f, 3f, 2f, 2f, 5f);
		setSize(1.4f, 0.95f);
		
		this.canAggress = true;
		this.experienceValue = 5;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		n.setString("texture", texture);
		super.writeEntityToNBT(n);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		texture = n.getString("texture");
		super.readEntityFromNBT(n);
	}



	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	
	
	protected void dropFewItems(boolean flag) {
		int i = rand.nextInt(3) + 1;
		for (int j = 0; j < i; j++) {
			if(!world.isRemote)
				entityDropItem(new ItemStack(ItemRegistry.freshMarlin), 0.0F);
		}
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		dropFewItems(true);
	}



	@Override
	public boolean isInWater() {
		return super.isInWater();
	}

	@Override
	public void applyEntityCollision(Entity entity) {
		super.applyEntityCollision(entity);
		
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}




	@Override
	protected boolean canDespawn() {
		return true;
	}

}