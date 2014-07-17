package net.tropicraft.entity.hostile;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityTropicraftAnimal;
import net.tropicraft.entity.projectile.EntityPoisonBlot;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class EntityTreeFrog extends EntityTropicraftAnimal implements IEntityAdditionalSpawnData, IRangedAttackMob {

    /** 0 = peaceful green, 1 = red, 2 = blue, 3 = yellow */
    public int type = 0;
    public int jumpDelay = 0;
    public int jumpDelayMax = 10;
    public boolean wasOnGround = false;

    public double leapVecX;
    public double leapVecZ;

    public EntityTreeFrog(World world) {
        super(world);
        setSize(0.8F, 0.8F);
        this.entityCollisionReduction = 0.8F;
        this.experienceValue = 5;
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    public EntityTreeFrog(World world, int parType) {
        this(world);
        type = parType;
    }
    
    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        
        if (!getNavigator().noPath() || this.getAttackTarget() != null) {
            if (onGround || isInWater()) {
                if (jumpDelay > 0) jumpDelay--;
                if (jumpDelay <= 0) {
                    jumpDelay = rand.nextInt(4);
                    
                    getNavigator().onUpdateNavigation();
                    
                    this.jump();
                    this.motionY += -0.01D + rand.nextDouble() * 0.1D;

                    double speed = 0.4D;
                    if (getNavigator().getPath() != null) {
                        try {
                            if (getNavigator().getPath().getCurrentPathIndex() > getNavigator().getPath().getCurrentPathLength()) {
                                //System.out.println("frog hopathing: path index: " + getNavigator().getPath().getCurrentPathIndex() + " > path size: " + getNavigator().getPath().getCurrentPathLength() + ", resetting path");
                                getNavigator().clearPathEntity();
                                return;
                                
                            }
                            Vec3 pos = getNavigator().getPath().getPosition(this);
                            leapVecX = pos.xCoord - this.posX;
                            leapVecZ = pos.zCoord - this.posZ;
                            speed = 0.2D;
                            //leapVecX = 0;
                            //leapVecZ = 0;
                        } catch (Exception ex) {
                            //Why the hell does it crash when i touch the path data this way? is the cur path index invalid? error happens no matter the placing of above path code
                            //System.out.println("TreeFrog getPosition crash");
                            //ex.printStackTrace();
                            return;
                        }
                    } else if (this.getAttackTarget() != null) {
                        leapVecX = this.getAttackTarget().posX - this.posX;
                        leapVecZ = this.getAttackTarget().posZ - this.posZ;
                        //jumpDelay = 1;
                    }

                    if (leapVecX != 0) {
                        
                        double dist2 = (double)Math.sqrt(leapVecX * leapVecX + leapVecZ * leapVecZ);
                        motionX += leapVecX / dist2 * speed;
                        //motionY += vecY / dist2 * speed;
                        motionZ += leapVecZ / dist2 * speed;
                    }
                    
                }
            } else {
                if (leapVecX != 0 /*&& this.curSpeed < 0.02*/) {
                    double speed = 0.1D;
                    if (isInWater()) speed = 0.2D;
                    double dist2 = (double)Math.sqrt(leapVecX * leapVecX + leapVecZ * leapVecZ);
                    motionX += leapVecX / dist2 * speed;
                    //motionY += vecY / dist2 * speed;
                    motionZ += leapVecZ / dist2 * speed;
                }
            }
            
            if (isInWater()) {
                motionY += 0.07D;
            }
        }
        
        wasOnGround = onGround;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(5.0D);
    }

    @Override
    protected void dropFewItems(boolean par1, int par2) {
        int j = 2;
        int k;

        for (k = 0; k < j; ++k) {
            this.dropItem(TCItemRegistry.frogLeg, 1);
        }
    }

    @Override
    public EntityAgeable createChild(EntityAgeable var1) {
        //TODO
        return null;
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        buf.writeInt(type);
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        type = buf.readInt();
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float f) {
        if (type == 0) return;  // Make sure green tree frogs don't attack

        if(f < 4F && !worldObj.isRemote && attackTime == 0 && worldObj.difficultySetting != EnumDifficulty.PEACEFUL)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;

            EntityPoisonBlot entitypoisonblot = new EntityPoisonBlot(worldObj, this);
            entitypoisonblot.posY += 1.3999999761581421D;
            double d2 = (entity.posY + (double)entity.getEyeHeight()) - 0.20000000298023224D - entitypoisonblot.posY;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
            worldObj.playSoundAtEntity(this, "frogspit", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
            worldObj.spawnEntityInWorld(entitypoisonblot);
            entitypoisonblot.setThrowableHeading(d, d2 + (double)f1, d1, 0.6F, 12F);
            attackTime = 50;

            rotationYaw = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
            hasAttacked = true;
        }
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }

}
