package net.tropicraft.entity.underdasea;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.registry.TCItemRegistry;

public class EntityTropicalFish extends EntityTropicraftWaterMob {

    //public boolean shouldSpawnSchool = false;
    //public int color;
    public boolean isLeader;
    public boolean inSchool;
    public EntityTropicalFish leader; 
    public boolean targetHook;
    public Entity hook;
    public boolean hasBeenPlaced;
    public static final String[] names = {"Clownfish", "Queen Angelfish", "Yellow Tang", "Butterflyfish", "Geophagus Surinamensis", "Betta Fish"
        , "Regal Tang", "Royal Gamma"};

    /**
     * Constant used in the datawatcher so this fish knows whether it should spawn a school or not
     */
    private static final int SHOULD_SPAWN_DATAWATCHER = 17, DATA_COLOR = 16;

    public EntityTropicalFish(World world) {
        super(world);
        //this.texture = "/mods/TropicraftMod/textures/entities/tropicalFish.png";
        targetHook = false;
        isLeader = true;
        inSchool = false;
        leader = null;      
        setSize(.6F, .6F);
        //  if (!world.isRemote) {
        setColor(worldObj.rand.nextInt(names.length));
        setShouldSpawnSchool(true);
        //  shouldSpawnSchool = true;
        //  }
        isCatchable = true;
        this.experienceValue = 3;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(5.0D);
    }

    public void setColor(int col) {
        this.dataWatcher.updateObject(DATA_COLOR, Integer.valueOf(col));
    }

    public int getColor() {
        return this.dataWatcher.getWatchableObjectInt(DATA_COLOR);
    }

    public void setShouldSpawnSchool(boolean spawnStatus) {
        this.dataWatcher.updateObject(SHOULD_SPAWN_DATAWATCHER, spawnStatus ? Integer.valueOf(1) : Integer.valueOf(-1));
    }

    public boolean getShouldSpawnSchool() {
        return this.dataWatcher.getWatchableObjectInt(SHOULD_SPAWN_DATAWATCHER) == 1;
    }

    public EntityTropicalFish(World world, EntityLiving entityliving, int i)
    {
        super(world);
        //shouldSpawnSchool = false;
        setShouldSpawnSchool(false);
        //this.texture = "/mods/TropicraftMod/textures/entities/tropicalFish.png";
        targetHook = false;
        isLeader = true;
        inSchool = false;
        leader = null;
        setColor(i);
        setSize(.4F, .85F);         
        setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
        motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);

    }

    /**
     * Will spawn a new fish of the same type that follows the original fish
     */
    public EntityTropicalFish(EntityTropicalFish original) {
        this(original.worldObj);
        //  shouldSpawnSchool = false;
        setShouldSpawnSchool(false);
        //this.texture = "/mods/TropicraftMod/textures/entities/tropicalFish.png";
        targetHook = false;
        isLeader = false;
        inSchool = true;
        leader = original;
        setColor(original.getColor());
        yOffset = 0.0F;
        setSize(original.width, original.height);
        do {
            double offsetX = (new Random()).nextDouble() * 3 - 1.5D;
            double offsetY = (new Random()).nextDouble() * 2 + 1.0D;
            double offsetZ = (new Random()).nextDouble() * 3 - 1.5D;
            setLocationAndAngles(original.posX + offsetX, original.posY + offsetY, original.posZ + offsetZ, original.rotationYaw, original.rotationPitch);
        } while (!getCanSpawnHere());
        motionX = original.motionX;
        motionY = original.motionY;
        motionZ = original.motionZ;
    }

    @Override
    public void entityInit()
    {
        super.entityInit(); 

        dataWatcher.addObject(16, new Integer(0));   
        dataWatcher.addObject(SHOULD_SPAWN_DATAWATCHER, Integer.valueOf(-1));
    }

    public void checkForLeader(){
        List list = worldObj.getEntitiesWithinAABB(EntityTropicalFish.class, this.boundingBox.expand(10D, 10D, 10D));
        for(Object ent : list){
            //System.out.println("Checking for leader");
            if(((EntityTropicalFish)ent).getColor() == this.getColor()){
                if(getEntityId() > ((Entity)ent).getEntityId()){
                    leader = (EntityTropicalFish)ent;
                    isLeader = false;
                }
                else{
                    isLeader = true;

                }
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        //   nbttagcompound.setShort("Color", (short)color);     
        nbttagcompound.setBoolean("Placed", hasBeenPlaced);
        nbttagcompound.setInteger("Color", Integer.valueOf(getColor()));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        //  color = nbttagcompound.getShort("Color");
        // Following conditional is to prevent fish despawning if the world
        // was created prior to introduction of the placed flag
        if (nbttagcompound.hasKey("Placed")) {
            hasBeenPlaced = nbttagcompound.getBoolean("Placed");
        } else {
            hasBeenPlaced = true;
        }

        setShouldSpawnSchool(false);

        //shouldSpawnSchool = false;

        setColor(Integer.valueOf(nbttagcompound.getInteger("Color")));
    }

    @Override
    public EntityLivingBase getAttackTarget() {
        if(leader != null && !inSchool && this.canEntityBeSeen(leader)){
            return leader;
        }       

        return null;
    }

    public void checkForHook(){
        List list = worldObj.getEntitiesWithinAABB(EntityFishHook.class, this.boundingBox.expand(10, 10, 10));
        if(list.isEmpty()){
            targetHook = false;
            hook = null;
            return;
        }
        hook = (EntityFishHook)(list.get(0));
        targetHook = true;
    }

    @Override
    public void applyEntityCollision(Entity entity) {        
        super.applyEntityCollision(entity);
        if(targetEntity != null && entity instanceof EntityTropicalFish) {
            targetEntity = null;
            inSchool = true;
        }       
    }

    @Override
    public boolean interact(EntityPlayer entityplayer) {
        if(entityplayer.getCurrentEquippedItem() == null || entityplayer.getCurrentEquippedItem().getItem() != TCItemRegistry.fishingNet) {
            return false;
        }
        
        if(!entityplayer.inventory.hasItem(TCItemRegistry.bucketTropicsWater)) {
            return false;
        } else {
            for (int i = 0 ; i < entityplayer.inventory.mainInventory.length; i ++ ) {
                if (entityplayer.inventory.getStackInSlot(i) != null) {
                    if (entityplayer.inventory.getStackInSlot(i).getItem() == TCItemRegistry.bucketTropicsWater) {
                        entityplayer.inventory.mainInventory[i] = new ItemStack(TCItemRegistry.fishBucket, 1, getColor());
                        this.setDead();
                        entityplayer.swingItem();
                        return true;                        
                    }
                }
            }
        }
        
        return false;
    }

    @Override
    protected void updateEntityActionState()
    {
        //setSize(.4f,.4f);
        //  System.out.println("this" + this.posX + " " + this.posY + " " + this.posZ);

        if (getShouldSpawnSchool()/*shouldSpawnSchool*/) {
            // Note: min/max values include this fish
            int maxInSchool = 7;
            int minInSchool = 4;
            int numToSpawn = (new Random()).nextInt(1 + maxInSchool - minInSchool) + minInSchool - 1;
            for (int i = 0; i < numToSpawn; i++) {
                if (!worldObj.isRemote) {
                    continue;
                    //EntityTropicalFish fish = new EntityTropicalFish(this);
                    //worldObj.spawnEntityInWorld(fish);
                }
            }
            setShouldSpawnSchool(false);
            //shouldSpawnSchool = false;
        }


        if(leader != null){         
            if(getDistanceToEntity(leader) < 1.5F){
                inSchool = true;                
            }           
        }
        if(leader != null && leader.isDead){
            leader = null;
        }
        if(leader == null || isLeader){
            checkForLeader();
        }


        if(!inSchool || isLeader){
            super.updateEntityActionState();
        }
        else if(inSchool && leader != null){

            if(getDistanceToEntity(leader)>= 2.25F && ticksExisted % 40 == 0){
                inSchool = false;
            }

            if(!leader.isLeader && leader.leader != null){
                leader = leader.leader;
            }
            randomMotionVecX = leader.randomMotionVecX;         
            randomMotionVecY = leader.randomMotionVecY;         
            randomMotionVecZ = leader.randomMotionVecZ;
        }
    }

    @Override
    protected int attackStrength() {
        return 0;
    }

    @Override
    public boolean canDespawn() {
        return !hasBeenPlaced;
    }

    public void disableDespawning() {
        hasBeenPlaced = true;
    }
}