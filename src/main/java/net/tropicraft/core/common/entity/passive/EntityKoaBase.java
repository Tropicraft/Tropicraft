package net.tropicraft.core.common.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.capability.PlayerDataInstance;
import net.tropicraft.core.common.capability.WorldDataInstance;
import net.tropicraft.core.common.entity.ai.*;
import net.tropicraft.core.common.entity.hostile.EntityAshen;
import net.tropicraft.core.common.entity.hostile.EntityIguana;
import net.tropicraft.core.common.entity.hostile.EntityTropiSkeleton;
import net.tropicraft.core.common.town.ISimulationTickable;
import net.tropicraft.core.common.worldgen.village.TownKoaVillage;
import net.tropicraft.core.registry.ItemRegistry;

import java.lang.reflect.Field;
import java.util.*;

import javax.annotation.Nullable;

public class EntityKoaBase extends EntityVillager {

    //TODO: consider serializing found water sources to prevent them refinding each time, which old AI did
    public long lastTimeFished = 0;

    public BlockPos posLastFireplaceFound = null;

    public InventoryBasic inventory;

    private static final DataParameter<Integer> ROLE = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> GENDER = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ORIENTATION = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.BOOLEAN);

    private EntityAIBase taskFishing = new EntityAIGoneFishin(this);

    private float clientHealthLastTracked = 0;

    public static int MAX_HOME_DISTANCE = 64;

    private int villageID = -1;

    public static Predicate<Entity> ENEMY_PREDICATE =
            input -> input instanceof EntityMob || input instanceof EntityTropiSkeleton || input instanceof EntityIguana || input instanceof EntityAshen;

    public enum Genders {
        MALE,
        FEMALE;

        private static final Map<Integer, Genders> lookup = new HashMap<>();
        static { for(Genders e : EnumSet.allOf(Genders.class)) { lookup.put(e.ordinal(), e); } }
        public static Genders get(int intValue) { return lookup.get(intValue); }
    }

    public enum Roles {
        HUNTER,
        FISHERMAN;

        private static final Map<Integer, Roles> lookup = new HashMap<>();
        static { for(Roles e : EnumSet.allOf(Roles.class)) { lookup.put(e.ordinal(), e); } }
        public static Roles get(int intValue) { return lookup.get(intValue); }
    }

    public enum Orientations {
        STRAIT,
        GAY;

        private static final Map<Integer, Orientations> lookup = new HashMap<>();
        static { for(Orientations e : EnumSet.allOf(Orientations.class)) { lookup.put(e.ordinal(), e); } }
        public static Orientations get(int intValue) { return lookup.get(intValue); }
    }

    public EntityKoaBase(World worldIn) {
        super(worldIn);
        this.enablePersistence();

        this.isImmuneToFire = true;


        inventory = new InventoryBasic("koa.inventory", false, 9);
    }

    public Genders getGender() {
        return Genders.get(this.getDataManager().get(GENDER));
    }

    public Roles getRole() {
        return Roles.get(this.getDataManager().get(ROLE));
    }

    public Orientations getOrientation() {
        return Orientations.get(this.getDataManager().get(ORIENTATION));
    }

    public boolean isSitting() {
        return this.getDataManager().get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.getDataManager().set(SITTING, Boolean.valueOf(sitting));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(ROLE, Integer.valueOf(0));
        this.getDataManager().register(GENDER, Integer.valueOf(0));
        this.getDataManager().register(ORIENTATION, Integer.valueOf(0));
        this.getDataManager().register(SITTING, Boolean.valueOf(false));
    }

    @Override
    protected void initEntityAI()
    {

    }

    public void updateUniqueEntityAI() {
        Set<EntityAITasks.EntityAITaskEntry> executingTaskEntries = ReflectionHelper.getPrivateValue(EntityAITasks.class, this.tasks, "field_75780_b", "executingTaskEntries");
        if (executingTaskEntries != null) {
            for (EntityAITasks.EntityAITaskEntry entry : this.tasks.taskEntries) {
                entry.action.resetTask();
            }
            executingTaskEntries.clear();
        }

        Set<EntityAITasks.EntityAITaskEntry> executingTaskEntries2 = ReflectionHelper.getPrivateValue(EntityAITasks.class, this.targetTasks, "field_75780_b", "executingTaskEntries");
        if (executingTaskEntries2 != null) {
            for (EntityAITasks.EntityAITaskEntry entry : this.targetTasks.taskEntries) {
                entry.action.resetTask();
            }
            executingTaskEntries2.clear();
        }

        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();

        this.tasks.addTask(0, new EntityAISwimming(this));

        this.tasks.addTask(1, new EntityAIAvoidEntityOnLowHealth(this, EntityLivingBase.class, ENEMY_PREDICATE,
                12.0F, 1.4D, 1.4D, 15F));

        this.tasks.addTask(2, new EntityAIEatToHeal(this));

        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1F, true) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                if (this.attacker instanceof EntityKoaBase) {
                    ((EntityKoaBase) this.attacker).setFightingItem();
                }
            }
        });
        //this.tasks.addTask(1, new EntityAITradePlayer(this));
        //this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
        this.tasks.addTask(4, new EntityAIChillAtFire(this));
        //this.tasks.addTask(3, new EntityAIMoveIndoors(this));
        //this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        //this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1D));
        this.tasks.addTask(6, new EntityAIKoaMate(this));
        //this.tasks.addTask(7, new EntityAIFollowGolem(this));

        if (canFish()) {
            this.tasks.addTask(7, taskFishing);
        }

        if (isChild()) {
            this.tasks.addTask(8, new EntityAIPlayKoa(this, 1.2D));
        }

        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        //this.tasks.addTask(9, new EntityAIVillagerInteract(this));
        this.tasks.addTask(9, new EntityAIWanderNotLazy(this, 1D, 40));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        //i dont think this one works, change to predicate
        if (canHunt()) {
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, ENEMY_PREDICATE));
        }


    }

    @Override
    public EntityVillager createChild(EntityAgeable ageable) {
        //EntityVillager ent = super.createChild(ageable);
        EntityKoaHunter entityvillager = new EntityKoaHunter(this.world);
        entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null);

        //TODO: default code that uses this sets child state after this so AI will be wrong, fix when making my own EntityAIVillagerMate

        return entityvillager;
    }

    @Override
    protected void onGrowingAdult() {
        super.onGrowingAdult();

        updateUniqueEntityAI();
    }

    public boolean canFish() {
        return this.getDataManager().get(ROLE) == Roles.FISHERMAN.ordinal();
    }

    public boolean canHunt() {
        return this.getDataManager().get(ROLE) == Roles.HUNTER.ordinal() && !isChild();
    }

    public void setHunter() {
        this.getDataManager().set(ROLE, Integer.valueOf(Roles.HUNTER.ordinal()));
        this.setFightingItem();
    }

    public void setFisher() {
        this.getDataManager().set(ROLE, Integer.valueOf(Roles.FISHERMAN.ordinal()));
        this.setFishingItem();
    }

    @Override
    protected void updateAITasks() {
        //cancel villager AI that overrides our home position
        //super.updateAITasks();

        //temp until we use AT
        Util.removeTask(this, EntityAIHarvestFarmland.class);
        Util.removeTask(this, EntityAIPlay.class);

        //this.setDead();
        /*if (isChild()) {
            setGrowingAge(0);
            onGrowingAdult();
        }*/

        //adjust home position to chest right nearby for easy item spawning
        findAndSetHomeToCloseChest(false);
        findAndSetFireSource(false);

    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Override
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
    }

    /** Copied from EntityMob */
    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : null;

                if (itemstack != null && itemstack1 != null && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD)
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }
    
    private static final Field _buyingPlayer = ReflectionHelper.findField(EntityVillager.class, "field_70962_h", "buyingPlayer");
    
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {

        if (hand != EnumHand.MAIN_HAND) return false;

    	boolean ret = false;
    	try {
            boolean doTrade = true;
            if (!this.world.isRemote) {
                int swimTimeNeeded = 20 * 30;
                PlayerDataInstance storage = player.getCapability(Tropicraft.PLAYER_DATA_INSTANCE, null);
                if (storage != null) {
                    if (!storage.receivedQuestReward) {

                        //temp
                        storage.swimTimeCur = swimTimeNeeded + 1;

                        if (storage.swimTimeCur >= swimTimeNeeded) {
                            doTrade = false;
                            storage.receivedQuestReward = true;
                            player.sendMessage(new TextComponentString("you good swim, have thing, it good"));
                            player.inventory.addItemStackToInventory(new ItemStack(Items.POTATO));
                        }

                    }
                }
                if (doTrade) {
                    // Make the super method think this villager is already trading, to block the GUI from opening
                    _buyingPlayer.set(this, player);
                    ret = super.processInteract(player, hand, stack);
                    _buyingPlayer.set(this, null);
                }
            }
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    	return ret;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return null;
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.setHomePosAndDistance(this.getPosition(), MAX_HOME_DISTANCE);

        rollDiceChild();

        rollDiceRoleAndGender();

        rollDiceOrientation();

        updateUniqueEntityAI();

        findAndSetHomeToCloseChest(true);
        findAndSetFireSource(true);

        IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);

        /*VillagerRegistry.VillagerProfession koaProfession = new VillagerRegistry.VillagerProfession("koa_profession", "");
        this.setProfession(koaProfession);*/

        return data;
    }

    public void rollDiceChild() {
        int childChance = 20;
        if (childChance >= this.world.rand.nextInt(100)) {
            this.setGrowingAge(-24000);
        }
    }

    //TODO: track male/female count per village and modify chance to keep it equal and sustainable for future generations
    public void rollDiceRoleAndGender() {
        int randValRole = this.world.rand.nextInt(Roles.values().length);
        if (randValRole == Roles.FISHERMAN.ordinal()) {
            this.setFisher();
        } else if (randValRole == Roles.HUNTER.ordinal()) {
            this.setHunter();
        }
        int randValGender = this.world.rand.nextInt(Genders.values().length);
        this.getDataManager().set(GENDER, Integer.valueOf(randValGender));
    }

    public void rollDiceOrientation() {
        this.getDataManager().set(ORIENTATION, Integer.valueOf(Orientations.STRAIT.ordinal()));
        int chance = 5;
        if (chance >= this.world.rand.nextInt(100)) {
            this.getDataManager().set(ORIENTATION, Orientations.GAY.ordinal());
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("home_X", getHomePosition().getX());
        compound.setInteger("home_Y", getHomePosition().getY());
        compound.setInteger("home_Z", getHomePosition().getZ());

        if (posLastFireplaceFound != null) {
            compound.setInteger("fireplace_X", posLastFireplaceFound.getX());
            compound.setInteger("fireplace_Y", posLastFireplaceFound.getY());
            compound.setInteger("fireplace_Z", posLastFireplaceFound.getZ());
        }

        compound.setLong("lastTimeFished", lastTimeFished);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.inventory.getStackInSlot(i);

            if (itemstack != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        compound.setTag("koa_inventory", nbttaglist);

        compound.setInteger("role_id", this.getDataManager().get(ROLE));
        compound.setInteger("gender_id", this.getDataManager().get(GENDER));
        compound.setInteger("orientation_id", this.getDataManager().get(ORIENTATION));

        compound.setInteger("village_id", villageID);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("home_X")) {
            this.setHomePosAndDistance(new BlockPos(compound.getInteger("home_X"), compound.getInteger("home_Y"), compound.getInteger("home_Z")), MAX_HOME_DISTANCE);
        }

        if (compound.hasKey("fireplace_X")) {
            this.setFirelacePos(new BlockPos(compound.getInteger("fireplace_X"), compound.getInteger("fireplace_Y"), compound.getInteger("fireplace_Z")));
        }

        lastTimeFished = compound.getLong("lastTimeFished");

        if (compound.hasKey("koa_inventory", 9)) {
            NBTTagList nbttaglist = compound.getTagList("koa_inventory", 10);
            //this.initHorseChest();

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;

                this.inventory.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
        }

        this.villageID = compound.getInteger("village_id");

        this.getDataManager().set(ROLE, compound.getInteger("role_id"));
        this.getDataManager().set(GENDER, compound.getInteger("gender_id"));
        this.getDataManager().set(ORIENTATION, compound.getInteger("orientation_id"));

        updateUniqueEntityAI();
    }

    public void setFishingItem() {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
    }

    public void setFightingItem() {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.dagger));
    }

    public void findAndSetHomeToCloseChest(boolean force) {

        if (!force && (world.getTotalWorldTime()+this.getEntityId()) % (20*30) != 0) return;

        //validate home position
        boolean tryFind = false;
        if (getHomePosition() == null) {
            tryFind = true;
        } else {
            TileEntity tile = world.getTileEntity(getHomePosition());
            if (!(tile instanceof TileEntityChest)) {
                //home position isnt a chest, keep current position but find better one
                tryFind = true;
            }
        }

        if (tryFind) {
            int range = 20;
            for (int x = -range; x <= range; x++) {
                for (int y = -range / 2; y <= range / 2; y++) {
                    for (int z = -range; z <= range; z++) {
                        BlockPos pos = this.getPosition().add(x, y, z);
                        TileEntity tile = world.getTileEntity(pos);
                        if (tile instanceof TileEntityChest) {
                            System.out.println("found chest, updating home position to " + pos);
                            setHomePosAndDistance(pos, MAX_HOME_DISTANCE);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void findAndSetFireSource(boolean force) {

        if (!force && (world.getTotalWorldTime()+this.getEntityId()) % (20*30) != 0) return;

        //validate fire source
        boolean tryFind = false;
        if (posLastFireplaceFound == null) {
            tryFind = true;
        } else if (posLastFireplaceFound != null) {
            IBlockState state = world.getBlockState(posLastFireplaceFound);
            if (state.getMaterial() != Material.FIRE) {
                posLastFireplaceFound = null;
                tryFind = true;
            }
        }

        if (tryFind) {
            //TODO: line of sight check

            int range = 20;
            for (int x = -range; x <= range; x++) {
                for (int y = -range/2; y <= range/2; y++) {
                    for (int z = -range; z <= range; z++) {
                        BlockPos pos = this.getPosition().add(x, y, z);
                        IBlockState state = world.getBlockState(pos);
                        if (state.getMaterial() == Material.FIRE) {
                            System.out.println("found fire place spot to chill");
                            setFirelacePos(pos);
                            return;
                        }
                    }
                }
            }
        }


    }

    public boolean tryDumpInventoryIntoHomeChest() {
        TileEntity tile = world.getTileEntity(this.getHomePosition());
        if (tile instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest)tile;

            for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = this.inventory.getStackInSlot(i);

                if (itemstack != null) {
                    this.inventory.setInventorySlotContents(i, this.addItem(chest, itemstack));
                }
            }
        }
        //maybe return false if inventory not emptied entirely
        return true;
    }

    @Nullable
    public ItemStack addItem(TileEntityChest chest, ItemStack stack)
    {
        ItemStack itemstack = stack.copy();

        for (int i = 0; i < chest.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = chest.getStackInSlot(i);

            if (itemstack1 == null)
            {
                chest.setInventorySlotContents(i, itemstack);
                chest.markDirty();
                return null;
            }

            if (ItemStack.areItemsEqual(itemstack1, itemstack))
            {
                int j = Math.min(chest.getInventoryStackLimit(), itemstack1.getMaxStackSize());
                int k = Math.min(itemstack.stackSize, j - itemstack1.stackSize);

                if (k > 0)
                {
                    itemstack1.stackSize += k;
                    itemstack.stackSize -= k;

                    if (itemstack.stackSize <= 0)
                    {
                        chest.markDirty();
                        return null;
                    }
                }
            }
        }

        if (itemstack.stackSize != stack.stackSize)
        {
            chest.markDirty();
        }

        return itemstack;
    }

    public void setFirelacePos(BlockPos pos) {
        this.posLastFireplaceFound = pos;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        //TODO: replace with heal via hunger/food consumption
        if (!world.isRemote) {
            //if (world.getTotalWorldTime() % (20*5) == 0) {
                //this.heal(5);
            //}
        }

        if (world.isRemote) {
            if (clientHealthLastTracked != this.getHealth()) {
                if (this.getHealth() > clientHealthLastTracked) {
                    world.spawnParticle(EnumParticleTypes.HEART, false, this.posX, this.posY + 2.2, this.posZ, 0, 0, 0);
                }
                clientHealthLastTracked = this.getHealth();
            }
        }
    }

    @Override
    public void heal(float healAmount) {
        super.heal(healAmount);
    }

    @Override
    public boolean getIsWillingToMate(boolean updateFirst) {
        //TODO: use our own rules
        //return super.getIsWillingToMate(updateFirst);
        this.setIsWillingToMate(true);
        return true;
    }

    public boolean willBone(EntityKoaBase bonie) {
        EntityKoaBase boner = this;
        if (!bonie.getIsWillingToMate(true)) return false;
        if (boner.isChild() || bonie.isChild()) return false;
        if (boner.getOrientation() == Orientations.STRAIT) {
            if (boner.getGender() == bonie.getGender()) return false;
        } else if (boner.getOrientation() == Orientations.GAY) {
            if (boner.getGender() != bonie.getGender()) return false;
        }
        return true;
    }

    public int getVillageID() {
        return villageID;
    }

    public void setVillageID(int villageID) {
        this.villageID = villageID;
    }

    public TownKoaVillage getVillage() {
        WorldDataInstance data = this.world.getCapability(Tropicraft.WORLD_DATA_INSTANCE, null);
        if (data != null) {
            ISimulationTickable sim = data.getLocationByID(villageID);
            if (sim instanceof TownKoaVillage) {
                return (TownKoaVillage) sim;
            }
        }
        return null;
    }

    @Override
    public void setDead() {
        super.setDead();
        if (!world.isRemote) {
            System.out.println("hook dead " + this);
            TownKoaVillage village = getVillage();
            if (village != null) {
                village.hookEntityDied(this);
            }
        }
    }

    public void hookUnloaded() {
        if (!world.isRemote) {
            System.out.println("hook unloaded " + this);
            TownKoaVillage village = getVillage();
            if (village != null) {
                village.hookEntityDestroyed(this);
            }
        }
    }
}
