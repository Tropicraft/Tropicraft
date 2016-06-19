package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLand;

public class EntityTreeFrogGreen extends EntityTreeFrogBase implements IMob {
	
    public EntityTreeFrogGreen(World world) {
        super(world, 0);
    }
    
}
