package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLand;
import net.tropicraft.core.common.entity.EntityLandHostile;

public class EntityEIH extends EntityLandHostile implements IMob {

	/**
	 * TODO: mob needs spider like leap attack AI task
	 */
	
	//0 = sleep, 1 = aware, 2 = angry
	private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityEIH.class, DataSerializers.VARINT);
	public byte STATE_SLEEP = 0;
	public byte STATE_AWARE = 1;
	public byte STATE_ANGRY = 2;

    public EntityEIH(World world) {
        super(world);
        this.isImmuneToFire = true;
        setSize(1.4F, 4.0F);
        this.experienceValue = 10;
        
    }
    
    @Override
    protected void applyEntityAttributes() {
    	super.applyEntityAttributes();
    	
    	this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }
    
    @Override
    protected void initEntityAI() {
    	super.initEntityAI();
    	
    	//this.tasks.addTask(1, new EntityAISwimming(this));
    	this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false) {
    		public boolean shouldExecute() {
    			if (getState() != STATE_ANGRY) return false;
    			return super.shouldExecute();
    		}
    	});
    	
    	EntityAILeapAtTarget leap = new EntityAILeapAtTarget(this, 0.6F);
    	leap.setMutexBits(0);
        this.tasks.addTask(3, leap);
        //this.tasks.addTask(4, new EntitySpider.AISpiderAttack(this));
        //this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        //this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        //this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        //this.targetTasks.addTask(2, new EntitySpider.AISpiderTarget(this, EntityPlayer.class));
        //this.targetTasks.addTask(3, new EntitySpider.AISpiderTarget(this, EntityIronGolem.class));
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
    	
    	if (source.getSourceOfDamage() instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
    		ItemStack heldItem = player.getHeldItemMainhand();
    		if (heldItem != null && heldItem.getItem() instanceof ItemPickaxe) {
    			this.getDataManager().set(STATE, Integer.valueOf(2));
    			return super.attackEntityFrom(source, amount);
    		}
    	}
    	
    	return true;
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
    	super.writeEntityToNBT(compound);
    	
    	compound.setByte("state", (byte)getState());
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
    	super.readEntityFromNBT(compound);
    	
    	this.getDataManager().set(STATE, (int)compound.getByte("state"));
    }
}
