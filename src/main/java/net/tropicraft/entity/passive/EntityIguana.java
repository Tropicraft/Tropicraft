package net.tropicraft.entity.passive;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityLand.EnumTropiMobType;
import net.tropicraft.entity.EntityTropicraftAnimal;
import net.tropicraft.registry.TCItemRegistry;

public class EntityIguana extends EntityTropicraftAnimal {

    /** Timer for how much longer the iggy will be enraged */
    private int angerLevel;

    public EntityIguana(World world) {
        super(world);
        this.isImmuneToFire = true;
        setSize(1.0F, 0.4F);
    }

    /**
     * Get all iggys in range to fire at will
     */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float range) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else {
            Entity entity = damagesource.getEntity();

            if (entity instanceof EntityPlayer)
            {
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0D, 32.0D, 32.0D));

                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity1 = (Entity)list.get(i);

                    if (entity1 instanceof EntityIguana)
                    {
                        EntityIguana iggy = (EntityIguana)entity1;
                        iggy.becomeAngryAt(entity);
                    }
                }

                this.becomeAngryAt(entity);
            }

            return super.attackEntityFrom(damagesource, range);
        }
    }

    /**
     * Taken from EntityPigZombie
     * @param entity Entity to become angry at
     */
    private void becomeAngryAt(Entity entity)
    {
        this.entityToAttack = entity;
        this.angerLevel = 400 + this.rand.nextInt(400);
    }

    @Override
    public void attackEntity(Entity entity, float range) {
        if(range > 2.0F && range < 6F && rand.nextInt(10) == 0) {
            if(onGround) {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = (d / (double)f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
                motionZ = (d1 / (double)f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
                motionY = 0.40000000596046448D;
            }
        } else
            if(range < 1.5F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
                attackTime = 20;
                byte byte0 = 2;
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), byte0);
            }
    }

    @Override
    public boolean isAIEnabled() {
        return false;
    }

    @Override
    protected void fall(float f) {
        // No falling damage, mwahahaha
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        int numDrops = 3 + this.rand.nextInt(1 + looting);

        for (int i = 0; i < numDrops; i++)
            this.dropItem(TCItemRegistry.scale, 1);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.0D);
    }

    @Override
    public Entity findPlayerToAttack() {
        if (angerLevel == 0) return null;

        EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
        return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
    }

    @Override
    public float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected String getLivingSound() {
        return tcSound("iggyliving");
    }

    @Override
    protected String getHurtSound() {
        return tcSound("iggyattack");
    }

    @Override
    protected String getDeathSound() {
        return tcSound("iggydeath");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setShort("Anger", (short)this.angerLevel);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.angerLevel = nbt.getShort("Anger");
    }

    @Override
    public EntityAgeable createChild(EntityAgeable var1) {
        return new EntityIguana(worldObj);
    }
    
    @Override
    public int getMaxSpawnedInChunk() {
        return 6;
    }
}
