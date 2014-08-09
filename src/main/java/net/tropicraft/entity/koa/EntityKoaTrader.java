package net.tropicraft.entity.koa;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.ai.jobs.JobTrade;

public class EntityKoaTrader extends EntityKoaBase {

	public EntityKoaTrader(World par1World) {
		super(par1World);
		//texture = "/mods/TropicraftMod/textures/entities/koa/KoaManTrader.png";
		
		agent.jobMan.clearJobs();
		agent.jobMan.addPrimaryJob(new JobTrade(agent.jobMan));
		//agent.jobMan.addJob(new JobHunt(agent.jobMan));
		agent.scanForHomeChest = false;
		//agent.setMoveSpeed(0.25F);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		//agent.setSpeedFleeAdditive(0.1F); //additive
		//agent.setSpeedNormalBase(0.6F);
		agent.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
	}

	@Override
	public int getCooldownRanged() {
		return 5;
	}

	/*@Override
	public void postInitFakePlayer() {
		super.postInitFakePlayer();
		if (agent.entInv.inventory.mainInventory[0] == null) {
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.swordEudialyte));
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.blowGun));
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.dart, 64));
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.dart, 64));
		}
	}
	
	@Override
	public String getLocalizedName() {
		return "a Koa Trader";
	}*/

}
