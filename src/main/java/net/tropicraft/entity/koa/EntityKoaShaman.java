package net.tropicraft.entity.koa;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.ai.jobs.JobQuestGiver;
import net.tropicraft.registry.TCItemRegistry;

public class EntityKoaShaman extends EntityKoaBase {

	public EntityKoaShaman(World par1World) {
		super(par1World);
		//texture = "/mods/TropicraftMod/textures/entities/koa/KoaShaman.png";
		agent.jobMan.addJob(new JobQuestGiver(agent.jobMan));
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		//agent.setSpeedFleeAdditive(0.1F); //additive
		//agent.setSpeedNormalBase(0.6F);
		agent.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(60.0D);
	}

	@Override
	public int getCooldownRanged() {
		return 10;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(
			IEntityLivingData par1EntityLivingData) {
		
		agent.entInv.inventory.setInventorySlotContents(0, new ItemStack(TCItemRegistry.bambooSpear));
		agent.entInv.inventory.setInventorySlotContents(1, new ItemStack(TCItemRegistry.swordZirconium));
		
		//sync to vanilla system
		agent.entInv.syncToClient();
		
		return super.onSpawnWithEgg(par1EntityLivingData);
	}
	
	/*@Override
	public void postInitFakePlayer() {
		super.postInitFakePlayer();
		if (agent.entInv.inventory.mainInventory[0] == null) {
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.spearBamboo));
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.staffFire));
		}
	}
	
	@Override
	public String getLocalizedName() {
		return "a Koa Shaman";
	}*/

}
