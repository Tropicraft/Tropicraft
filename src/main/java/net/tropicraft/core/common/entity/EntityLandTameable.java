package net.tropicraft.core.common.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.world.World;
import net.tropicraft.Info;

public abstract class EntityLandTameable extends EntityTameable {

    public EntityLandTameable(World world) {
        super(world);
        setSize(.7F, 1.95F);
    }
    
    @Override
    protected void applyEntityAttributes() {
    	super.applyEntityAttributes();

    	//give a default that isnt 1 since thats crazy fast
    	this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }
}
