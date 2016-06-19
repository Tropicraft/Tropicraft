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

public class EntityTreeFrogBase extends EntityLand implements IMob {

	public int type = 0;
	
    public EntityTreeFrogBase(World world) {
        super(world);
        setSize(0.8F, 0.8F);
        this.entityCollisionReduction = 0.8F;
        this.experienceValue = 5;
        
    }
    
    public EntityTreeFrogBase(World world, int type) {
    	this(world);
    	this.type = type;
	}
}
