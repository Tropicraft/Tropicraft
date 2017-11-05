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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
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
import net.tropicraft.core.common.item.scuba.ItemDiveComputer;
import net.tropicraft.core.common.town.ISimulationTickable;
import net.tropicraft.core.common.worldgen.village.TownKoaVillage;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

import java.lang.reflect.Field;
import java.util.*;

import javax.annotation.Nullable;

public class EntityKoaBase extends EntityVillager {

    //TODO: consider serializing found water sources to prevent them refinding each time, which old AI did
    public long lastTimeFished = 0;

    public BlockPos posLastFireplaceFound = null;
    public List<BlockPos> listPosDrums = new ArrayList<>();
    public static int MAX_DRUMS = 12;

    public InventoryBasic inventory;

    private static final DataParameter<Integer> ROLE = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> GENDER = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ORIENTATION = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DANCING = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> LURE_ID = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);

    private EntityAIBase taskFishing = new EntityAIGoneFishin(this);

    private float clientHealthLastTracked = 0;

    public static int MAX_HOME_DISTANCE = 128;

    private int villageID = -1;

    private EntityFishHook lure;

    private boolean wasInWater = false;
    private boolean wasNightLastTick = false;
    private boolean wantsToParty = false;

    public int hitIndex = 0;
    public int hitIndex2 = 0;
    public int hitIndex3 = 0;
    public int hitDelay = 0;
    private long lastTradeTime = 0;
    private static int TRADE_COOLDOWN = 24000*3;
    private static int DIVE_TIME_NEEDED = 60*60;

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

    public long getLastTradeTime() {
        return lastTradeTime;
    }

    public void setLastTradeTime(long lastTradeTime) {
        this.lastTradeTime = lastTradeTime;
    }

    public Genders getGender() {
        return Genders.get(this.getDataManager().get(GENDER));
    }

    public void setGender(Genders gender) {
        this.getDataManager().set(GENDER, gender.ordinal());
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

    public boolean isDancing() {
        return this.getDataManager().get(DANCING);
    }

    public void setDancing(boolean val) {
        this.getDataManager().set(DANCING, Boolean.valueOf(val));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(ROLE, Integer.valueOf(0));
        this.getDataManager().register(GENDER, Integer.valueOf(0));
        this.getDataManager().register(ORIENTATION, Integer.valueOf(0));
        this.getDataManager().register(SITTING, Boolean.valueOf(false));
        this.getDataManager().register(DANCING, Boolean.valueOf(false));
        this.getDataManager().register(LURE_ID, Integer.valueOf(-1));
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);

        if (key == LURE_ID) {
            int id = this.getDataManager().get(LURE_ID);
            if (id != -1) {
                Entity ent = world.getEntityByID(id);
                if (ent instanceof EntityFishHook) {
                    setLure((EntityFishHook) ent);
                    ((EntityFishHook) ent).angler = this;
                }
            } else {
                setLure(null);
            }
        }
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

            @Override
            protected double getAttackReachSqr(EntityLivingBase attackTarget) {
                return (double)(this.attacker.width * 2.5F * this.attacker.width * 2.5F + attackTarget.width);
            }
        });
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1D));
        this.tasks.addTask(5, new EntityAIKoaMate(this));
        this.tasks.addTask(6, new EntityAIChillAtFire(this));
        this.tasks.addTask(7, new EntityAIPartyTime(this));

        if (canFish()) {
            this.tasks.addTask(8, taskFishing);
        }

        if (isChild()) {
            this.tasks.addTask(9, new EntityAIPlayKoa(this, 1.2D));
        }

        this.tasks.addTask(10, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWanderNotLazy(this, 1D, 40));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));

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
        findAndSetDrums(false);

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
            boolean doTrade = false;
            if (!this.world.isRemote) {

                long diveTime = 0;

                //scan hotbar
                for (int i = 0; i < 9; i++) {
                    ItemStack stackScan = player.inventory.getStackInSlot(i);
                    if (!Util.isEmpty(stackScan) && stackScan.getItem() == ItemRegistry.diveComputer) {

                        //for testing
                        //((ItemDiveComputer)stackScan.getItem()).setDiveTime(stackScan, 60 * 59);

                        diveTime = ((ItemDiveComputer)stackScan.getItem()).getDiveTime(stackScan);
                        break;
                    }
                }

                if (diveTime >= DIVE_TIME_NEEDED) {
                    if (world.getTotalWorldTime() > lastTradeTime + TRADE_COOLDOWN) {
                        if (player.inventory.addItemStackToInventory(new ItemStack(ItemRegistry.trimix, 1))) {
                            player.sendMessage(new TextComponentTranslation("entity.tropicraft.koa.trade.give"));
                            lastTradeTime = world.getTotalWorldTime();
                        } else {
                            player.sendMessage(new TextComponentTranslation("entity.tropicraft.koa.trade.space"));
                        }

                    } else {
                        player.sendMessage(new TextComponentTranslation("entity.tropicraft.koa.trade.cooldown"));
                    }
                } else {
                    int timeLeft = (int) (DIVE_TIME_NEEDED - diveTime) / 60;
                    if (timeLeft == 0) timeLeft = 1;
                    player.sendMessage(new TextComponentTranslation("entity.tropicraft.koa.trade.not_enough_time", timeLeft));
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
        findAndSetDrums(true);

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

        compound.setLong("lastTradeTime", lastTradeTime);

        for (int i = 0; i < listPosDrums.size(); i++) {
            compound.setInteger("drum_" + i + "_X", listPosDrums.get(i).getX());
            compound.setInteger("drum_" + i + "_Y", listPosDrums.get(i).getY());
            compound.setInteger("drum_" + i + "_Z", listPosDrums.get(i).getZ());
        }
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

        this.lastTradeTime = compound.getLong("lastTradeTime");

        for (int i = 0; i < MAX_DRUMS; i++) {
            if (compound.hasKey("drum_" + i + "_X")) {
                this.listPosDrums.add(new BlockPos(compound.getInteger("drum_" + i + "_X"),
                        compound.getInteger("drum_" + i + "_Y"),
                        compound.getInteger("drum_" + i + "_Z")));
            }
        }

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
                            //System.out.println("found chest, updating home position to " + pos);
                            setHomePosAndDistance(pos, MAX_HOME_DISTANCE);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void findAndSetFireSource(boolean force) {

        //this.setHomePosAndDistance(this.getHomePosition(), 128);

        if (!force && (world.getTotalWorldTime()+this.getEntityId()) % (20*30) != 0) return;

        //validate fire source
        boolean tryFind = false;
        if (posLastFireplaceFound == null) {
            tryFind = true;
        } else if (posLastFireplaceFound != null) {
            IBlockState state = world.getBlockState(posLastFireplaceFound);
            if (state.getMaterial() != Material.FIRE) {
                //System.out.println("removing invalid fire spot");
                posLastFireplaceFound = null;
                tryFind = true;
            }
        }

        if (tryFind) {
            int range = 20;
            for (int x = -range; x <= range; x++) {
                for (int y = -range/2; y <= range/2; y++) {
                    for (int z = -range; z <= range; z++) {
                        BlockPos pos = this.getPosition().add(x, y, z);
                        IBlockState state = world.getBlockState(pos);
                        if (state.getMaterial() == Material.FIRE) {
                            //System.out.println("found fire place spot to chill");
                            setFirelacePos(pos);
                            return;
                        }
                    }
                }
            }

            List<EntityKoaBase> listEnts = world.getEntitiesWithinAABB(EntityKoaBase.class, new AxisAlignedBB(this.getPosition()).expand(20, 20, 20));
            Collections.shuffle(listEnts);
            for (EntityKoaBase ent : listEnts) {
                if (ent.posLastFireplaceFound != null) {
                    IBlockState state = world.getBlockState(ent.posLastFireplaceFound);
                    if (state.getMaterial() == Material.FIRE) {
                        posLastFireplaceFound = new BlockPos(ent.posLastFireplaceFound);
                        //System.out.println("found fire place spot to chill from entity");
                        return;
                    }
                }
            }
        }
    }

    public void syncBPM() {
        if ((world.getTotalWorldTime()+this.getEntityId()) % (20) != 0) return;

        List<EntityKoaBase> listEnts = world.getEntitiesWithinAABB(EntityKoaBase.class, new AxisAlignedBB(this.getPosition()).expand(10, 5, 10));
        //Collections.shuffle(listEnts);
        for (EntityKoaBase ent : listEnts) {
            if (hitDelay != ent.hitDelay) {
                hitDelay = ent.hitDelay;
                hitIndex = ent.hitIndex;
                hitIndex2 = ent.hitIndex2;
                hitIndex3 = ent.hitIndex3;
                return;
            }
        }
    }

    public void findAndSetDrums(boolean force) {

        //this.setHomePosAndDistance(this.getHomePosition(), 128);

        if (!force && (world.getTotalWorldTime()+this.getEntityId()) % (20*30) != 0) return;

        Iterator<BlockPos> it = listPosDrums.iterator();
        while (it.hasNext()) {
            BlockPos pos = it.next();
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() != BlockRegistry.bongo) {
                it.remove();
            }
        }

        if (listPosDrums.size() >= MAX_DRUMS) {
            return;
        }

        List<EntityKoaBase> listEnts = world.getEntitiesWithinAABB(EntityKoaBase.class, new AxisAlignedBB(this.getPosition()).expand(20, 20, 20));
        Collections.shuffle(listEnts);
        for (EntityKoaBase ent : listEnts) {
            if (listPosDrums.size() >= MAX_DRUMS) {
                return;
            }
            Iterator<BlockPos> it2 = ent.listPosDrums.iterator();
            while (it2.hasNext()) {
                BlockPos pos = it2.next();
                //IBlockState state = world.getBlockState(pos);

                boolean match = false;

                Iterator<BlockPos> it3 = listPosDrums.iterator();
                while (it3.hasNext()) {
                    BlockPos pos2 = it3.next();
                    //IBlockState state2 = world.getBlockState(pos2);
                    if (pos.equals(pos2)) {
                        match = true;
                        break;
                    }
                }

                if (!match) {
                    //System.out.println("drum pos ent: " + pos);
                    listPosDrums.add(pos);
                }

                if (listPosDrums.size() >= MAX_DRUMS) {
                    return;
                }
            }
        }

        int range = 20;
        for (int x = -range; x <= range; x++) {
            for (int y = -range/2; y <= range/2; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = this.getPosition().add(x, y, z);
                    IBlockState state = world.getBlockState(pos);
                    if (state.getBlock() == BlockRegistry.bongo) {

                        boolean match = false;

                        Iterator<BlockPos> it3 = listPosDrums.iterator();
                        while (it3.hasNext()) {
                            BlockPos pos2 = it3.next();
                            //IBlockState state2 = world.getBlockState(pos2);
                            if (pos.equals(pos2)) {
                                match = true;
                                break;
                            }
                        }

                        if (!match) {
                            //System.out.println("drum pos: " + pos);
                            listPosDrums.add(pos);
                        }

                        if (listPosDrums.size() >= MAX_DRUMS) {
                            return;
                        }

                    }
                }
            }
        }
    }

    public boolean tryGetVillage() {
        List<EntityKoaBase> listEnts = world.getEntitiesWithinAABB(EntityKoaBase.class, new AxisAlignedBB(this.getPosition()).expand(20, 20, 20));
        Collections.shuffle(listEnts);
        for (EntityKoaBase ent : listEnts) {
            if (ent.villageID != -1) {
                villageID = ent.villageID;
                return true;
            }
        }
        return false;
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
    public int getAir() {
        return super.getAir();
    }

    @Override
    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        super.onLivingUpdate();

        if (wasInWater) {
            if (!isInWater()) {
                if (isCollidedHorizontally) {
                    this.motionY += 0.4F;
                }
            }
        }

        if (isInWater()) {
            if (this.motionY < -0.2F) {
                this.motionY += 0.15F;
            } else {
                //this.motionY += 0.02F;
            }
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.60D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        }

        wasInWater = isInWater();

        if (!wasNightLastTick) {
            if (!this.world.isDaytime()) {
                //roll dice once
                rollDiceParty();
            }
        }

        wasNightLastTick = !this.world.isDaytime();

        if (!world.isRemote) {
            //if (world.getTotalWorldTime() % (20*5) == 0) {
                //this.heal(5);
            //}
        }

        if (world.isRemote) {
            //heal indicator, has a bug that spawns a heart on reload into world but not a big deal
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
        //vanilla did food check here but hunters dont have any
        //our population limits work well enough to leave this to always true
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
            } else {
                //System.out.println("critical: couldnt find village by ID");
            }
        } else {
            //System.out.println("critical: no world cap");
        }
        return null;
    }

    @Override
    public void setDead() {
        super.setDead();
        if (!world.isRemote) {
            //System.out.println("hook dead " + this);
            TownKoaVillage village = getVillage();
            if (village != null) {
                village.hookEntityDied(this);
            }
        }
    }

    public void hookUnloaded() {
        if (!world.isRemote) {
            //System.out.println("hook unloaded " + this);
            TownKoaVillage village = getVillage();
            if (village != null) {
                village.hookEntityDestroyed(this);
            }
        }
    }

    public EntityFishHook getLure() {
        return lure;
    }

    public void setLure(EntityFishHook lure) {
        this.lure = lure;
        if (!this.world.isRemote) {
            if (lure != null) {
                this.getDataManager().set(LURE_ID, this.lure.getEntityId());
            } else {
                this.getDataManager().set(LURE_ID, -1);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        boolean result = super.attackEntityFrom(source, amount);
        if (this.getHealth() <= 0) {
            if (source.getEntity() instanceof EntityLivingBase) {
                //System.out.println("koa died by: " + source.getDamageType() + " - loc: " + source.getDamageLocation() + " - " + source.getDeathMessage((EntityLivingBase)source.getEntity()));
            } else {
                //System.out.println("koa died by: " + source.getDamageType() + " - loc: " + source.getDamageLocation());
            }
        }
        return result;
    }

    public void postSpawnGenderFix() {
        TownKoaVillage village = getVillage();
        if (village != null) {
            //gender balencing, not factoring in orientation
            int maleCount = 0;
            int femaleCount = 0;
            for (int ordinal : village.lookupEntityToGender.values()) {
                if (EntityKoaBase.Genders.MALE.ordinal() == ordinal) {
                    maleCount++;
                } else if (EntityKoaBase.Genders.FEMALE.ordinal() == ordinal) {
                    femaleCount++;
                }
            }

            if (maleCount < femaleCount) {
                //System.out.println("force set to male");
                setGender(EntityKoaBase.Genders.MALE);
            } else {
                //System.out.println("force set to female");
                setGender(EntityKoaBase.Genders.FEMALE);
            }

            //System.out.println("population size: " + village.getPopulationSize() + ", males: " + maleCount + ", females: " + femaleCount);
        }
    }

    //do not constantly use throughout night, as the night doesnt happen all on the same day
    //use asap and store value
    public boolean isPartyNight() {
        long time = world.getWorldTime();
        long day = time / 24000;
        //party every 3rd night
        //System.out.println(time + " - " + day + " - " + (day % 3 == 0));
        return day % 3 == 0;
    }

    public void rollDiceParty() {

        if (isPartyNight()) {
            int chance = 50;
            if (chance >= this.world.rand.nextInt(100)) {
                wantsToParty = true;
                //System.out.println("roll dice party: " + wantsToParty);
                return;
            }
        }
        wantsToParty = false;

        //System.out.println("roll dice party: " + wantsToParty);
    }

    public boolean getWantsToParty() {
        return wantsToParty;
    }
}
