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

public class EntityEIH extends EntityLand implements IMob {

	/**
	 * TODO: mob needs spider like leap attack AI task
	 */
	
	//0 = sleep, 1 = aware, 2 = angry
	private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityEIH.class, DataSerializers.VARINT);

    public EntityEIH(World world) {
        super(world);
        this.isImmuneToFire = true;
        setSize(1.4F, 4.0F);
        this.experienceValue = 10;
        
    }
    
    @Override
    protected void entityInit() {
    	super.entityInit();
    	this.getDataManager().register(STATE, Integer.valueOf(0));
    }
    
    public int getState() {
    	return this.getDataManager().get(STATE);
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
    	
    	//test
    	this.getDataManager().set(STATE, Integer.valueOf(2));
    	
    	return super.attackEntityFrom(source, amount);
    }
}
