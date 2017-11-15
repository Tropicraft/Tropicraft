package net.tropicraft.core.common.entity.underdasea.atlantoku.ai;

import net.minecraft.entity.ai.EntityAIBase;

public abstract class EntityAISwimBase extends EntityAIBase{

	@Override
	public void startExecuting() {
		System.out.println("Started executing task "+this.getClass().getSimpleName());
		super.startExecuting();
	}

}
