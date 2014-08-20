package net.tropicraft.entity.koa;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.ai.jobs.JobFish;
import net.tropicraft.registry.TCItemRegistry;
import CoroUtil.componentAI.jobSystem.JobHunt;
import CoroUtil.entity.EntityTropicalFishHook;

public class EntityKoaFisher extends EntityKoaBase {

	public float castingStrength = 1F;
	
	public EntityKoaFisher(World par1World) {
		super(par1World);
		//texture = "/mods/TropicraftMod/textures/entities/koa/KoaMan3.png";
		agent.jobMan.clearJobs();
		agent.jobMan.addPrimaryJob(new JobFish(agent.jobMan));
		agent.jobMan.addJob(new JobHunt(agent.jobMan));
		
		/*this.setCurrentItemOrArmor(0, new ItemStack(Item.swordIron));
		this.setCurrentItemOrArmor(4, new ItemStack(Item.helmetIron));*/
	}

	@Override
	public int getCooldownRanged() {
		return 40;
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(
			IEntityLivingData par1EntityLivingData) {
		
		agent.entInv.inventory.setInventorySlotContents(0, new ItemStack(TCItemRegistry.dagger));
		agent.entInv.inventory.setInventorySlotContents(1, new ItemStack(TCItemRegistry.leafBall));
		agent.entInv.inventory.setInventorySlotContents(2, new ItemStack(TCItemRegistry.fishingRodTropical));
		
		//sync to vanilla system
		agent.entInv.syncToClient();
		
		return super.onSpawnWithEgg(par1EntityLivingData);
	}
	
	@Override
	public void onUpdate() {
		
		if (!worldObj.isRemote) {
			agent.entInv.setSlotActive(0);
			agent.entInv.setSlotActive(2);
		} else {
			//System.out.println(this.getEquipmentInSlot(0));
		}
		
		super.onUpdate();
	}

	/*@Override
	public void postInitFakePlayer() {
		super.postInitFakePlayer();
		if (agent.entInv.inventory.mainInventory[0] == null) {
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.dagger));
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.leafBall));
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.fishingRodTropical));
		}
	}
	
	@Override
	public String getLocalizedName() {
		return koaName + ", a Koa Fisher";
	}*/

}
