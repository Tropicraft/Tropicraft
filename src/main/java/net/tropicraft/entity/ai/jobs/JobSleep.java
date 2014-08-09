package net.tropicraft.entity.ai.jobs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.tropicraft.item.tool.ItemTropicraftPickaxe;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;

public class JobSleep extends JobBase {
	
	public boolean sleeping = true;

	public JobSleep(JobManager jm) {
		super(jm);
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public boolean shouldContinue() {
		return !sleeping;
	}
	
	@Override
	public boolean hookHit(DamageSource ds, int damage) {
		
		//System.out.println(ds.toString());
		if (ds.getEntity() instanceof EntityPlayer) {
			sleeping = false;
			ItemStack is = ((EntityPlayer)ds.getEntity()).getCurrentEquippedItem();
			if (is != null && (is.getItem() instanceof ItemPickaxe || is.getItem() instanceof ItemTropicraftPickaxe)) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	@Override
	public void onIdleTickAct() {
		if (!sleeping) super.onIdleTickAct();
	}

}
