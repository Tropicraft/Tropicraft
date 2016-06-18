package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLand;

public class EntityEIH extends EntityLand implements IMob {

	/**
	 * TODO: mob needs spider like leap attack AI task
	 */

    public EntityEIH(World world) {
        super(world);
        this.isImmuneToFire = true;
        setSize(1.4F, 4.0F);
        this.experienceValue = 10;
        
    }
}
