package net.tropicraft.entity.ai.jobs;

import net.minecraft.entity.player.EntityPlayer;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;
import CoroUtil.quest.EnumQuestState;
import CoroUtil.quest.PlayerQuestManager;
import CoroUtil.quest.quests.ActiveQuest;

public class JobQuestGiver extends JobBase {

	public JobQuestGiver(JobManager jm) {
		super(jm);
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public boolean shouldContinue() {
		return true;
	}
	
	@Override
	public boolean hookInteract(EntityPlayer par1EntityPlayer) {
		if (!ent.worldObj.isRemote) {
			ActiveQuest quest = PlayerQuestManager.i().getPlayerQuests(par1EntityPlayer).getFirstQuestByStatus(EnumQuestState.CONCLUDING);
			if (quest == null) {
				int randID = 0;//par1EntityPlayer.worldObj.rand.nextInt(QuestCreator.curQuestCount);
				//PlayerQuestManager.i().giveQuest(randID, CoroUtilEntity.getName(par1EntityPlayer), false);
			} else {
				quest.eventComplete();
				questComplete(par1EntityPlayer);
			}
			return true;
		}
		return super.hookInteract(par1EntityPlayer);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	public void questComplete(EntityPlayer par1EntityPlayer) {
		//givePage(par1EntityPlayer);
	}
	
	
}
