package net.tropicraft.core.common.entity;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.world.World;
import net.tropicraft.Info;

public abstract class EntityLandTameable extends EntityTameable {

    public EntityLandTameable(World world) {
        super(world);
        setSize(.7F, 1.95F);
        this.enablePersistence();
    }
    
    @Override
    protected void applyEntityAttributes() {
    	super.applyEntityAttributes();

    	//give a default that isnt 1 since thats crazy fast
    	this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    /**
     * This is required if spawning an IMob based entity in the creature list that is also marked persistant
     *
     * @param type
     * @param forSpawnCount
     * @return
     */
    @Override
    public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
        return type == EnumCreatureType.CREATURE;
    }
}
