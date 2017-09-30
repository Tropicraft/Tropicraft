package net.tropicraft.core.common.entity.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.projectile.EntityDart;
import net.tropicraft.core.registry.ItemRegistry;
import net.tropicraft.core.registry.SoundRegistry;

public class EntityAshenHunter extends EntityAshen {

    public boolean hasGTFO;
    
    public EntityAshenHunter(World par1World) {
        super(par1World);
        setActionState(2);
        actionPicker = 2;
        hasGTFO = false;
        this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ItemRegistry.dagger));
       // lostSight = 0;
    }
    
    @Override
    public void onLivingUpdate() { 
        super.onLivingUpdate();

        if ((world.getDifficulty() == EnumDifficulty.PEACEFUL) && getActionState() != 1) {
            setActionState(0);
        } else if (getActionState() != 1) {
            setActionState(actionPicker);
        }
    }
    
	protected SoundEvent getAmbientSound() {
		return getAttackTarget() == null ? null : SoundRegistry.get("ashenLaugh");
	}

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float range) {
    	if (this.getAttackTarget() != null) {
    		this.faceEntity(getAttackTarget(), 180, 180);
    		EntityDart entitydart = new EntityDart(world, this, 3.0F, (short)(0));
        	world.spawnEntity(entitydart);
        	//System.out.println("shoot!");
    	}
    }
    
    @Override
    public boolean attackEntityAsMob(Entity p_70652_1_) {
    	this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(getAttackStrength());
    	return super.attackEntityAsMob(p_70652_1_);
    }

    @Override
    protected double getAttackStrength() {
    	if (world == null) return 0;
        switch (world.getDifficulty()) {
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
