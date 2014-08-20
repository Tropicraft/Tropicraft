package net.tropicraft.entity.koa;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;

public class EntityKoaHunter extends EntityKoaBase {

	public EntityKoaHunter(World par1World) {
		super(par1World);
		//texture = "/mods/TropicraftMod/textures/entities/koa/KoaManHunter.png";
		
	}

	@Override
	public int getCooldownRanged() {
		return 10;
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

	/*@Override
	public void postInitFakePlayer() {
		super.postInitFakePlayer();
		if (agent.entInv.inventory.mainInventory[0] == null) {
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.dagger));
			agent.entInv.inventory.addItemStackToInventory(new ItemStack(TropicraftItems.leafBall));
		}
	}
	
	@Override
	public String getLocalizedName() {
		return "a Koa Hunter";
	}*/

}
