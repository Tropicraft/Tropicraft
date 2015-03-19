package net.tropicraft.entity.hostile;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;

public abstract class EntityAshen extends EntityMob implements IRangedAttackMob {

    public static final int DATAWATCHER_MASK_TYPE = 16;
    public static final int DATAWATCHER_ACTION_STATE = 17;
    
    public float bobber;
    public int bobberHelper;
    
    /* 0 = peaceful, 1 = lost mask, 2 = hostile */
    public int actionPicker;
    
    public EntityLostMask maskToTrack;
    public Entity itemToTrack;
   /* protected int[] dropItems = new int[]{TropicraftMod.poisonSkin.shiftedIndex, Item.bone.shiftedIndex,  Item.rottenFlesh.shiftedIndex};
    protected List items = Arrays.asList(TropicraftMod.ashenMask.shiftedIndex, TropicraftMod.poisonSkin.shiftedIndex, TropicraftMod.paraDart.shiftedIndex,
            TropicraftMod.blowGun.shiftedIndex, TropicraftMod.bambooSpear.shiftedIndex, Item.beefRaw.shiftedIndex, Item.porkRaw.shiftedIndex,
            Item.chickenRaw.shiftedIndex, Item.rottenFlesh);*/

    public EntityAshen(World par1World) {
        super(par1World);
        setSize(0.5F, 1.3F);      
        setMaskType(new Random().nextInt(7));
        actionPicker = 0;
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new AIAshenChaseAndPickupLostMask(this, 1.0D));
        this.tasks.addTask(3, new EntityAIMeleeAndRangedAttack(this, 1.0D, 60, 5F));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();        
        dataWatcher.addObject(DATAWATCHER_MASK_TYPE, new Integer(0));
        dataWatcher.addObject(DATAWATCHER_ACTION_STATE, new Integer(0));  
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(getAttackStrength());
    }
    
    protected abstract double getAttackStrength();

    @Override
    public boolean isAIEnabled() {
        return true;
    }
    
    public void setMaskType(int type) {
        this.dataWatcher.updateObject(DATAWATCHER_MASK_TYPE, new Integer(type));
    }
    
    public int getMaskType() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_MASK_TYPE);
    }
    
    public void setActionState(int state) {
        this.dataWatcher.updateObject(DATAWATCHER_ACTION_STATE, new Integer(state));
    }
    
    public int getActionState() {
        return this.dataWatcher.getWatchableObjectInt(DATAWATCHER_ACTION_STATE);
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("MaskType", (short)getMaskType());
        nbttagcompound.setShort("ActionState", (short)getActionState());
    }
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        setMaskType(nbttagcompound.getShort("MaskType"));
        setActionState(nbttagcompound.getShort("ActionState"));
    }
    
    public boolean hasMask() {
    	int ac = getActionState();
        return getActionState() != 1;
    }
    
    public void dropMask() {
    	//System.out.println("drop");
    	setActionState(1);
    	maskToTrack = new EntityLostMask(worldObj, getMaskType(), posX, posY, posZ, rotationYaw);
    	worldObj.spawnEntityInWorld(maskToTrack);
    }
    
    public void pickupMask(EntityLostMask mask) {
    	//System.out.println("pickup");
    	setActionState(2);
    	maskToTrack = null;
    	setMaskType(mask.type);
    	mask.setDead();
    }
    
    /**
     * Appends a sound name to the necessary mod prefix so the sound file can be located correctly
     * @param postfix Actual file name (sans file type)
     * @return Corrected path to mod sound file
     */
    protected String tcSound(String postfix) {
        return String.format("%s:%s", TCInfo.MODID, postfix);
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
    	boolean wasHit = super.attackEntityFrom(p_70097_1_, p_70097_2_);
    	
    	if (!worldObj.isRemote) {
	    	if (hasMask() && wasHit) {
	    		dropMask();
	    	}
    	}
    	
    	return wasHit;
    }
}
