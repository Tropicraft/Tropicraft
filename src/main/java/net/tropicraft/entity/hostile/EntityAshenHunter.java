package net.tropicraft.entity.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.tropicraft.entity.projectile.EntityDart;

public class EntityAshenHunter extends EntityAshen {

    public boolean hasGTFO;
    
    public EntityAshenHunter(World par1World) {
        super(par1World);
        setActionState(2);
        actionPicker = 2;
        hasGTFO = false;
       // lostSight = 0;
    }
    
    @Override
    public void onLivingUpdate() { 
        super.onLivingUpdate();

        if ((worldObj.difficultySetting == EnumDifficulty.PEACEFUL) && getActionState() != 1) {
            setActionState(0);
        } else if (getActionState() != 1) {
            setActionState(actionPicker);
        }
    }
    
    @Override
    protected String getLivingSound() {
        return getAttackTarget() == null ? null : tcSound("ashenLaugh");
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float range) {
    	if (this.getAttackTarget() != null) {
    		this.faceEntity(getAttackTarget(), 180, 180);
    		EntityDart entitydart = new EntityDart(worldObj, this, 3.0F, (short)(6));
        	worldObj.spawnEntityInWorld(entitydart);
        	//System.out.println("shoot!");
    	}
    }
    
    @Override
    public boolean attackEntityAsMob(Entity p_70652_1_) {
    	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(getAttackStrength());
    	return super.attackEntityAsMob(p_70652_1_);
    }

    @Override
    protected double getAttackStrength() {
        switch (worldObj.difficultySetting) {
        case EASY:
            return 1;
        case NORMAL:
            return 2;
        case HARD:
            return 3;
        default:
            return 0;
        }
    }

}
