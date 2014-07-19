package net.tropicraft.entity.hostile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

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
        return entityToAttack == null ? null : tcSound("ashenLaugh");
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float range) {

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
