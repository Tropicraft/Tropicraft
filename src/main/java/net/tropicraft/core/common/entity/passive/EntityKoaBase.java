package net.tropicraft.core.common.entity.passive;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.TropicsConfigs;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.ai.*;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

import javax.annotation.Nullable;
import java.util.*;

public class EntityKoaBase extends VillagerEntity {

    //TODO: consider serializing found water sources to prevent them refinding each time, which old AI did
    public long lastTimeFished = 0;

    public BlockPos posLastFireplaceFound = null;
    public List<BlockPos> listPosDrums = new ArrayList<>();
    public static int MAX_DRUMS = 12;

    public Inventory inventory;

    private static final DataParameter<Integer> ROLE = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> GENDER = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ORIENTATION = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DANCING = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> LURE_ID = EntityDataManager.createKey(EntityKoaBase.class, DataSerializers.VARINT);

    private Goal taskFishing = new EntityAIGoneFishin(this);

    private float clientHealthLastTracked = 0;

    public static int MAX_HOME_DISTANCE = 128;

    private static final int INVALID_DIM = Integer.MAX_VALUE;
    private int villageID = -1;

    private int villageDimID = INVALID_DIM;

    private FishingBobberEntity lure;

    private boolean wasInWater = false;
    private boolean wasNightLastTick = false;
    private boolean wantsToParty = false;

    public boolean jumpingOutOfWater = false;

    public int hitIndex = 0;
    public int hitIndex2 = 0;
    public int hitIndex3 = 0;
    public int hitDelay = 0;
    private long lastTradeTime = 0;
    private static int TRADE_COOLDOWN = 24000*3;
    private static int DIVE_TIME_NEEDED = 60*60;

    public boolean debug = true;

    public int druggedTime = 0;

    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(/* TODO TropicraftItems.NIGEL_STACHE.get() */);

    private boolean isMating;
    private boolean isPlaying;

    public static Predicate<Entity> ENEMY_PREDICATE =
            //TODO: 1.14 fix
            input -> (input instanceof MonsterEntity/* && !(input instanceof CreeperEntity)) || input instanceof EntityTropiSkeleton || input instanceof EntityIguana || input instanceof EntityAshen*/);



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

    public EntityKoaBase(EntityType<? extends EntityKoaBase> type, World worldIn) {
        super(type, worldIn);
        this.enablePersistence();


        inventory = new Inventory(9);
    }

    @Override
    protected void dealFireDamage(int amount) {

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
    protected void registerData() {
        super.registerData();
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

        if (!world.isRemote) return;

        if (key == LURE_ID) {
            int id = this.getDataManager().get(LURE_ID);
            if (id != -1) {
                scheduleEntityLookup(this, id);
            } else {
                setLure(null);
            }
        }
    }

    /**
     * Fixes race condition issue of dataparam packet coming in before lure client spawn packet
     * @param koa
     * @param id
     */
    @OnlyIn(Dist.CLIENT)
    public void scheduleEntityLookup(EntityKoaBase koa, int id) {
        Minecraft.getInstance().execute(() -> {
            Entity ent = world.getEntityByID(id);
            //TODO: 1.14 fix
            /*if (ent instanceof EntityFishHook) {
                setLure((EntityFishHook) ent);
                ((EntityFishHook) ent).angler = koa;
            } else {
                //System.out.println("fail lookup");
            }*/
        });
    }

    @Override
    protected void registerGoals()
    {
        //TODO: old 1.12 style tasks go here
    }

    static class KoaTradeForPearls implements VillagerTrades.ITrade {
        private final Item item;
        private final int count;
        private final int maxUses;
        private final int givenXP;
        private final float priceMultiplier;

        public KoaTradeForPearls(IItemProvider item, int count, int maxUses, int givenXP) {
            this.item = item.asItem();
            this.count = count;
            this.maxUses = maxUses;
            this.givenXP = givenXP;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            ItemStack itemstack = new ItemStack(this.item, this.count);
            return new MerchantOffer(itemstack, new ItemStack(TropicraftItems.WHITE_PEARL.get()), this.maxUses, this.givenXP, this.priceMultiplier);
        }
    }

    public Int2ObjectMap<VillagerTrades.ITrade[]> getOfferMap() {

        //TODO: 1.14 fix missing tropical and river fish entries from tropicrafts fix
        //- consider adding vanillas ones too now

        Int2ObjectMap<VillagerTrades.ITrade[]> offers = null;

        if (getRole() == Roles.FISHERMAN) {
            offers = func_221238_a(ImmutableMap.of(1,
                    new VillagerTrades.ITrade[]{
                            new KoaTradeForPearls(Items.TROPICAL_FISH, 20, 8, 2),
                            new KoaTradeForPearls(ItemRegistry.fishingNet, 1, 8, 2),
                            new KoaTradeForPearls(ItemRegistry.fishingRod, 1, 8, 2),
                            new KoaTradeForPearls(TropicraftItems.FRESH_MARLIN.get(), 3, 8, 2),
                            new KoaTradeForPearls(ItemRegistry.fertilizer, 5, 8, 2)
                    }));
        } else if (getRole() == Roles.HUNTER) {
            offers = func_221238_a(ImmutableMap.of(1,
                    new VillagerTrades.ITrade[]{
                            new KoaTradeForPearls(TropicraftItems.FROG_LEG.get(), 5, 8, 2),
                            new KoaTradeForPearls(ItemRegistry.iguanaLeather, 2, 8, 2),
                            new KoaTradeForPearls(TropicraftItems.SCALE.get(), 5, 8, 2)
                    }));
        }

        return offers;

        /*func_221238_a(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new KoaTradeForPearls(Items.TROPICAL_FISH, 20, 8, 2),
                        new KoaTradeForPearls(ItemRegistry.fishingNet, 1, 8, 2),
                        new KoaTradeForPearls(ItemRegistry.fishingRod, 1, 8, 2),
                        new KoaTradeForPearls(ItemRegistry.freshMarlin, 3, 8, 2),
                        new KoaTradeForPearls(ItemRegistry.fertilizer, 5, 8, 2),
                        new VillagerTrades.EmeraldForItemsTrade(Items.POTATO, 26, 8, 2),
                        new VillagerTrades.EmeraldForItemsTrade(Items.CARROT, 22, 8, 2),
                        new VillagerTrades.EmeraldForItemsTrade(Items.BEETROOT, 15, 8, 2),
                        new VillagerTrades.ItemsForEmeraldsTrade(Items.BREAD, 1, 6, 8, 1)
                }, 2,
                new VillagerTrades.ITrade[]{
                        new VillagerTrades.EmeraldForItemsTrade(Blocks.PUMPKIN, 6, 6, 10),
                        new VillagerTrades.ItemsForEmeraldsTrade(Items.PUMPKIN_PIE, 1, 4, 5),
                        new VillagerTrades.ItemsForEmeraldsTrade(Items.APPLE, 1, 4, 8, 5)
                }, 3, new VillagerTrades.ITrade[]{
                        new VillagerTrades.ItemsForEmeraldsTrade(Items.COOKIE, 3, 18, 10),
                        new VillagerTrades.EmeraldForItemsTrade(Blocks.MELON, 4, 6, 20)
                }, 4, new VillagerTrades.ITrade[]{
                        new VillagerTrades.ItemsForEmeraldsTrade(Blocks.CAKE, 1, 1, 6, 15),
                        new VillagerTrades.SuspiciousStewForEmeraldTrade(Effects.SPEED, 160, 15),
                        new VillagerTrades.SuspiciousStewForEmeraldTrade(Effects.JUMP_BOOST, 160, 15),
                        new VillagerTrades.SuspiciousStewForEmeraldTrade(Effects.WEAKNESS, 140, 15),
                        new VillagerTrades.SuspiciousStewForEmeraldTrade(Effects.BLINDNESS, 120, 15),
                        new VillagerTrades.SuspiciousStewForEmeraldTrade(Effects.POISON, 280, 15),
                        new VillagerTrades.SuspiciousStewForEmeraldTrade(Effects.SATURATION, 7, 15)
                }, 5, new VillagerTrades.ITrade[]{
                        new VillagerTrades.ItemsForEmeraldsTrade(Items.GOLDEN_CARROT, 3, 3, 30),
                        new VillagerTrades.ItemsForEmeraldsTrade(Items.GLISTERING_MELON_SLICE, 4, 3, 30)}));*/
    }

    private static Int2ObjectMap<VillagerTrades.ITrade[]> func_221238_a(ImmutableMap<Integer, VillagerTrades.ITrade[]> p_221238_0_) {
        return new Int2ObjectOpenHashMap<>(p_221238_0_);
    }

    /**
     * New main override for villager "trades" aka offers
     * @return
     */
    @Override
    protected void populateTradeData() {
        VillagerData villagerdata = this.getVillagerData();
        Int2ObjectMap<VillagerTrades.ITrade[]> int2objectmap = getOfferMap();
        if (int2objectmap != null && !int2objectmap.isEmpty()) {
            VillagerTrades.ITrade[] avillagertrades$itrade = int2objectmap.get(villagerdata.getLevel());
            if (avillagertrades$itrade != null) {
                MerchantOffers merchantoffers = this.getOffers();
                this.addTrades(merchantoffers, avillagertrades$itrade, 2);
            }
        }
    }

    /*public void initTrades() {

        this.offers = new MerchantOffers();





        MerchantRecipeList list = new MerchantRecipeList();

        //stack count given is the base amount that will be multiplied based on value of currency traded for

        //item worth notes:
        //cooked marlin restores double that of cooked fish
        //1 leather drops, 4+ scales drop per mob

        if (getRole() == Roles.FISHERMAN) {

            addTradeForCurrencies(list, new ItemStack(Items.TROPICAL_FISH, 5));
            addTradeForCurrencies(list, new ItemStack(ItemRegistry.fishingNet, 1));
            addTradeForCurrencies(list, new ItemStack(ItemRegistry.fishingRod, 1));
            addTradeForCurrencies(list, new ItemStack(ItemRegistry.freshMarlin, 3));
            addTradeForCurrencies(list, new ItemStack(ItemRegistry.fertilizer, 5));

            //TODO: 1.14 fix
            for (int i = 0; i < EntityTropicalFish.NAMES.length; i++) {
                addTradeForCurrencies(list, new ItemStack(ItemRegistry.rawTropicalFish, 1, i));
            }
            for (int i = 0; i < ItemRiverFish.FISH_COLORS.length; i++) {
                addTradeForCurrencies(list, new ItemStack(ItemRegistry.rawRiverFish, 1, i));
            }

        } else if (getRole() == Roles.HUNTER) {

            addTradeForCurrencies(list, new ItemStack(ItemRegistry.frogLeg, 5));
            addTradeForCurrencies(list, new ItemStack(ItemRegistry.iguanaLeather, 2));
            addTradeForCurrencies(list, new ItemStack(ItemRegistry.scale, 5));

        }

        try {
            _buyingList.set(this, list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

    /*public void addTradeForCurrencies(MerchantRecipeList list, ItemStack sell) {
        double pearlWhiteWorth = 1;
        double pearlBlackWorth = 1.5D;

        List<Double> listTradeCosts = new ArrayList<>();

        ItemStack stack1 = sell.copy();
        stack1.setCount((int)Math.round(sell.getCount() * pearlWhiteWorth));
        list.add(new MerchantRecipe(new ItemStack(ItemRegistry.whitePearl), stack1));

        ItemStack stack2 = sell.copy();
        stack2.setCount((int)Math.round(sell.getCount() * pearlBlackWorth));
        list.add(new MerchantRecipe(new ItemStack(ItemRegistry.blackPearl), stack2));

    }*/

    public void updateUniqueEntityAI() {
        //TODO: 1.14 maybe not needed, since villagers use diff system
        /*Set<GoalSelector.EntityAITaskEntry> executingTaskEntries = ReflectionHelper.getPrivateValue(GoalSelector.class, this.goalSelector, "field_75780_b", "executingTaskEntries");
        if (executingTaskEntries != null) {
            for (GoalSelector.EntityAITaskEntry entry : this.goalSelector.taskEntries) {
                entry.action.resetTask();
            }
            executingTaskEntries.clear();
        }

        Set<GoalSelector.EntityAITaskEntry> executingTaskEntries2 = ReflectionHelper.getPrivateValue(GoalSelector.class, this.targetSelector, "field_75780_b", "executingTaskEntries");
        if (executingTaskEntries2 != null) {
            for (GoalSelector.EntityAITaskEntry entry : this.targetSelector.taskEntries) {
                entry.action.resetTask();
            }
            executingTaskEntries2.clear();
        }*/

        /*this.goalSelector.taskEntries.clear();
        this.targetSelector.taskEntries.clear();*/

        this.goalSelector.getRunningGoals().forEach(PrioritizedGoal::resetTask);
        this.targetSelector.getRunningGoals().forEach(PrioritizedGoal::resetTask);



        int curPri = 0;

        this.goalSelector.addGoal(curPri++, new SwimGoal(this));

        this.goalSelector.addGoal(curPri++, new EntityAIAvoidEntityOnLowHealth(this, LivingEntity.class, ENEMY_PREDICATE,
                12.0F, 1.4D, 1.4D, 15F));

        this.goalSelector.addGoal(curPri++, new EntityAIEatToHeal(this));

        this.goalSelector.addGoal(curPri++, new TradeWithPlayerGoal(this));

        this.goalSelector.addGoal(curPri++, new MeleeAttackGoal(this, 1F, true) {
            @Override
            public void startExecuting() {
                super.startExecuting();
                if (this.attacker instanceof EntityKoaBase) {
                    ((EntityKoaBase) this.attacker).setFightingItem();
                }
            }

            @Override
            protected double getAttackReachSqr(LivingEntity attackTarget) {
                return (double)(this.attacker.getType().getSize().width * 2.5F * this.attacker.getType().getSize().width * 2.5F + attackTarget.getType().getSize().width);
            }
        });

        this.goalSelector.addGoal(curPri++, new EntityAITemptHelmet(this, 1.0D, false, TEMPTATION_ITEMS));

        this.goalSelector.addGoal(curPri++, new MoveTowardsRestrictionGoal(this, 1D));
        this.goalSelector.addGoal(curPri++, new EntityAIKoaMate(this));
        this.goalSelector.addGoal(curPri++, new EntityAIChillAtFire(this));
        this.goalSelector.addGoal(curPri++, new EntityAIPartyTime(this));

        if (canFish()) {
            this.goalSelector.addGoal(curPri++, taskFishing);
        }

        if (isChild()) {
            this.goalSelector.addGoal(curPri++, new EntityAIPlayKoa(this, 1.2D));
        }

        this.goalSelector.addGoal(curPri, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(curPri++, new EntityAIWanderNotLazy(this, 1D, 40));
        this.goalSelector.addGoal(curPri++, new LookAtGoal(this, MobEntity.class, 8.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        //i dont think this one works, change to predicate
        if (canHunt()) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, ENEMY_PREDICATE));
        }


    }

    @Override
    public VillagerEntity createChild(AgeableEntity ageable) {
        //EntityVillager ent = super.createChild(ageable);
        EntityKoaHunter entityvillager = new EntityKoaHunter(TropicraftEntities.KOA_HUNTER.get(), this.world);
        entityvillager.onInitialSpawn(this.world, this.world.getDifficultyForLocation(new BlockPos(entityvillager)), SpawnReason.BREEDING, null, null);

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

        //TODO: 1.14 villagers use new task system, we need simple goal system
        //villager brain aka goal system should be off with no call to super.updateAITasks();

        //temp until we use AT
        /*Util.removeGoal(this, EntityAIHarvestFarmland.class);
        Util.removeGoal(this, EntityAIPlay.class);*/



        //this.setDead();
        /*if (isChild()) {
            setGrowingAge(0);
            onGrowingAdult();
        }*/

        monitorHomeVillage();
        //adjust home position to chest right nearby for easy item spawning
        findAndSetHomeToCloseChest(false);
        findAndSetFireSource(false);
        findAndSetDrums(false);
        findAndSetTownID(false);

    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Override
    public void setAttackTarget(@Nullable LivingEntity entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
    }

    /** Copied from EntityMob */
    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        float f = (float)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
        int i = 0;

        if (entityIn instanceof LivingEntity)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof LivingEntity)
            {
                ((LivingEntity)entityIn).knockBack(this, (float)i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.setMotion(this.getMotion().x * 0.6D, this.getMotion().y, this.getMotion().z * 0.6D);
                /*this.motionX *= 0.6D;
                this.motionZ *= 0.6D;*/
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof PlayerEntity)
            {
                PlayerEntity entityplayer = (PlayerEntity)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof AxeItem && itemstack1.getItem() == Items.SHIELD)
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
    
    /*private static final Field _buyingPlayer = Util.findField(VillagerEntity.class, "field_70962_h", "buyingPlayer");
    private static final Field _buyingList = Util.findField(VillagerEntity.class, "field_70963_i", "buyingList");*/
    
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {

        if (hand != Hand.MAIN_HAND) return false;

    	boolean ret = false;
    	try {
            boolean doTrade = true;
            if (!this.world.isRemote) {

                ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
                if (!stack.isEmpty() && stack.getItem() == TropicraftItems.POISON_FROG_SKIN.get()) {
                    doTrade = false;

                    //drug the koa and make him forget everything
                    dbg("koa drugged, zapping memory");
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    zapMemory();

                    druggedTime += 20*60*2;
                    addPotionEffect(new EffectInstance(Effects.NAUSEA, druggedTime));
                    findAndSetDrums(true);

                } else if (!stack.isEmpty() && stack.getItem() == ItemRegistry.diveComputer) {
                    long diveTime = 0;

                    doTrade = false;

                    //scan hotbar
                    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                        ItemStack stackScan = player.inventory.getStackInSlot(i);
                        if (!stackScan.isEmpty() && stackScan.getItem() == ItemRegistry.diveComputer) {

                            //for testing
                            //((ItemDiveComputer)stackScan.getItem()).setDiveTime(stackScan, 60 * 59);

                            //TODO: 1.14 uncomment
                            //diveTime = ((ItemDiveComputer)stackScan.getItem()).getDiveTime(stackScan);
                            break;
                        }
                    }

                    if (diveTime >= DIVE_TIME_NEEDED) {
                        if (world.getGameTime() > lastTradeTime + TRADE_COOLDOWN) {
                            if (player.inventory.addItemStackToInventory(new ItemStack(ItemRegistry.trimix, 1))) {
                                player.sendMessage(new TranslationTextComponent("entity.tropicraft.koa.trade.give"));
                                lastTradeTime = world.getGameTime();
                            } else {
                                player.sendMessage(new TranslationTextComponent("entity.tropicraft.koa.trade.space"));
                            }

                        } else {
                            player.sendMessage(new TranslationTextComponent("entity.tropicraft.koa.trade.cooldown"));
                        }
                    } else {
                        int timeLeft = (int) (DIVE_TIME_NEEDED - diveTime) / 60;
                        if (timeLeft == 0) timeLeft = 1;
                        player.sendMessage(new TranslationTextComponent("entity.tropicraft.koa.trade.not_enough_time", timeLeft));
                    }
                }

                if (doTrade) {
                    // Make the super method think this villager is already trading, to block the GUI from opening
                    //_buyingPlayer.set(this, player);
                    ret = super.processInteract(player, hand);
                    //_buyingPlayer.set(this, null);
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
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setHomePosAndDistance(this.getPosition(), MAX_HOME_DISTANCE);

        rollDiceChild();

        rollDiceRole();
        rollDiceGender();
        rollDiceOrientation();

        updateUniqueEntityAI();

        findAndSetHomeToCloseChest(true);
        findAndSetFireSource(true);
        findAndSetDrums(true);

        ILivingEntityData data = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);

        /*VillagerRegistry.VillagerProfession koaProfession = new VillagerRegistry.VillagerProfession("koa_profession", "");
        this.setProfession(koaProfession);*/

        //TODO: 1.14 make sure not needed, overwritten with getOfferMap now
        //initTrades();

        return data;
    }

    public void rollDiceChild() {
        int childChance = 20;
        if (childChance >= this.world.rand.nextInt(100)) {
            this.setGrowingAge(-24000);
        }
    }

    public void rollDiceRole() {
        int randValRole = this.world.rand.nextInt(Roles.values().length);
        if (randValRole == Roles.FISHERMAN.ordinal()) {
            this.setFisher();
        } else if (randValRole == Roles.HUNTER.ordinal()) {
            this.setHunter();
        }
    }

    public void rollDiceGender() {
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
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("home_X", getHomePosition().getX());
        compound.putInt("home_Y", getHomePosition().getY());
        compound.putInt("home_Z", getHomePosition().getZ());

        if (posLastFireplaceFound != null) {
            compound.putInt("fireplace_X", posLastFireplaceFound.getX());
            compound.putInt("fireplace_Y", posLastFireplaceFound.getY());
            compound.putInt("fireplace_Z", posLastFireplaceFound.getZ());
        }

        compound.putLong("lastTimeFished", lastTimeFished);

        ListNBT nbttaglist = new ListNBT();

        for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                CompoundNBT nbttagcompound = new CompoundNBT();
                nbttagcompound.putByte("Slot", (byte)i);
                itemstack.write(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        compound.put("koa_inventory", nbttaglist);

        compound.putInt("role_id", this.getDataManager().get(ROLE));
        compound.putInt("gender_id", this.getDataManager().get(GENDER));
        compound.putInt("orientation_id", this.getDataManager().get(ORIENTATION));

        compound.putInt("village_id", villageID);
        compound.putInt("village_dim_id", villageDimID);

        compound.putLong("lastTradeTime", lastTradeTime);

        for (int i = 0; i < listPosDrums.size(); i++) {
            compound.putInt("drum_" + i + "_X", listPosDrums.get(i).getX());
            compound.putInt("drum_" + i + "_Y", listPosDrums.get(i).getY());
            compound.putInt("drum_" + i + "_Z", listPosDrums.get(i).getZ());
        }

        compound.putInt("druggedTime", druggedTime);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("home_X")) {
            this.setHomePosAndDistance(new BlockPos(compound.getInt("home_X"), compound.getInt("home_Y"), compound.getInt("home_Z")), MAX_HOME_DISTANCE);
        }

        if (compound.contains("fireplace_X")) {
            this.setFirelacePos(new BlockPos(compound.getInt("fireplace_X"), compound.getInt("fireplace_Y"), compound.getInt("fireplace_Z")));
        }

        lastTimeFished = compound.getLong("lastTimeFished");

        if (compound.contains("koa_inventory", 9)) {
            ListNBT nbttaglist = compound.getList("koa_inventory", 10);
            //this.initHorseChest();

            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundNBT nbttagcompound = nbttaglist.getCompound(i);
                int j = nbttagcompound.getByte("Slot") & 255;

                this.inventory.setInventorySlotContents(j, ItemStack.read(nbttagcompound));
            }
        }

        this.villageID = compound.getInt("village_id");

        //backwards compat
        if (!compound.contains("village_dim_id")) {
            this.villageDimID = TropicsConfigs.tropicsDimensionID;
        } else {
            this.villageDimID = compound.getInt("village_dim_id");
        }

        if (compound.contains("role_id")) {
            this.getDataManager().set(ROLE, compound.getInt("role_id"));
        } else {
            rollDiceRole();
        }
        if (compound.contains("gender_id")) {
            this.getDataManager().set(GENDER, compound.getInt("gender_id"));
        } else {
            rollDiceGender();
        }
        if (compound.contains("orientation_id")) {
            this.getDataManager().set(ORIENTATION, compound.getInt("orientation_id"));
        } else {
            rollDiceOrientation();
        }

        this.lastTradeTime = compound.getLong("lastTradeTime");

        for (int i = 0; i < MAX_DRUMS; i++) {
            if (compound.contains("drum_" + i + "_X")) {
                this.listPosDrums.add(new BlockPos(compound.getInt("drum_" + i + "_X"),
                        compound.getInt("drum_" + i + "_Y"),
                        compound.getInt("drum_" + i + "_Z")));
            }
        }

        druggedTime = compound.getInt("druggedTime");

        updateUniqueEntityAI();
    }

    public void setFishingItem() {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.FISHING_ROD));
    }

    public void setFightingItem() {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ItemRegistry.dagger));
    }

    public void monitorHomeVillage() {
        if (villageDimID != INVALID_DIM) {
            //if (villageID != -1) {

                //if not in home dimension, full reset
                if (this.world.getDimension().getType().getId() != villageDimID) {
                    dbg("koa detected different dimension, zapping memory");
                    zapMemory();
                    addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5));
                }
            //}
        }

        //TODO: 1.14 re-add
        //findOrCreateNewVillage();
    }

    //TODO: 1.14 re-add
    /*public void findOrCreateNewVillage() {
        if ((world.getGameTime()+this.getEntityId()) % (20*5) != 0) return;

        if (getVillage() == null) {
            TownKoaVillage village = getVillageWithinRange(64);
            if (village != null) {
                dbg("Koa found a village to join: " + village.locationID);
                this.setVillageAndDimID(village.locationID, village.dimID);
            } else {
                //check we have a chest locked in
                TileEntity tile = world.getTileEntity(func_213384_dI());
                if ((tile instanceof ChestTileEntity)) {
                    village = createNewVillage(func_213384_dI());
                    if (village != null) {
                        this.setVillageAndDimID(village.locationID, village.dimID);
                        dbg("Koa created a new village!");
                    } else {
                        dbg("village wasnt created, critical error!");
                    }
                } else {
                    dbg("no village near and no chest!");
                }
            }
        }
    }*/

    //TODO: 1.14 re-add
    /*public TownKoaVillage createNewVillage(BlockPos pos) {
        WorldDataInstance storage = world.getCapability(Tropicraft.WORLD_DATA_INSTANCE, null);

        if (storage != null) {
            TownKoaVillage village = new TownKoaVillage();

            int newID = storage.getAndIncrementKoaIDVillage();
            village.initData(newID, world.provider.getDimension(), pos);

            //custom village changes
            village.isCustomVillage = true;

            //no koa regen
            village.minEntitiesToKeepAlive = 0;

            village.initFirstTime();

            //force new spawn coords for the 2 koa
            village.generateSpawnCoords();

            //NO GEN FOR CUSTOM!
            //village.genStructure();

            storage.addTickingLocation(village);

            return village;
        }
        return null;
    }*/

    public void findAndSetHomeToCloseChest(boolean force) {

        if (!force && (world.getGameTime()+this.getEntityId()) % (20*30) != 0) return;

        //validate home position
        boolean tryFind = false;
        if (getHomePosition() == null) {
            tryFind = true;
        } else {
            TileEntity tile = world.getTileEntity(getHomePosition());
            if (!(tile instanceof ChestTileEntity)) {
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
                        if (tile instanceof ChestTileEntity) {
                            //System.out.println("found chest, updating home position to " + pos);
                            dbg("found chest, updating home position to " + pos);
                            setHomePosAndDistance(pos, MAX_HOME_DISTANCE);
                            return;
                        }
                    }
                }
            }
        }
    }

    public boolean findAndSetTownID(boolean force) {
        if (!force && (world.getGameTime()+this.getEntityId()) % (20*30) != 0) return false;

        boolean tryFind = false;

        if (villageID == -1 || villageDimID == INVALID_DIM) {
            tryFind = true;
            //make sure return status is correct
            villageID = -1;
        }

        if (tryFind) {
            List<EntityKoaBase> listEnts = world.getEntitiesWithinAABB(EntityKoaBase.class, new AxisAlignedBB(this.getPosition()).grow(20, 20, 20));
            Collections.shuffle(listEnts);
            for (EntityKoaBase ent : listEnts) {
                if (ent.villageID != -1 && ent.villageDimID != INVALID_DIM) {
                    this.setVillageAndDimID(ent.villageID, ent.villageDimID);
                    break;
                }
            }
        }

        return this.villageID != -1;
    }

    public void findAndSetFireSource(boolean force) {

        //this.setHomePosAndDistance(this.func_213384_dI(), 128);

        if (!force && (world.getGameTime()+this.getEntityId()) % (20*30) != 0) return;

        //validate fire source
        boolean tryFind = false;
        if (posLastFireplaceFound == null) {
            tryFind = true;
        } else if (posLastFireplaceFound != null) {
            BlockState state = world.getBlockState(posLastFireplaceFound);
            if (state.getBlock() != Blocks.CAMPFIRE) {
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
                        BlockState state = world.getBlockState(pos);
                        if (state.getBlock() == Blocks.CAMPFIRE) {
                            dbg("found fire place spot to chill");
                            setFirelacePos(pos);
                            return;
                        }
                    }
                }
            }

            List<EntityKoaBase> listEnts = world.getEntitiesWithinAABB(EntityKoaBase.class, new AxisAlignedBB(this.getPosition()).grow(20, 20, 20));
            Collections.shuffle(listEnts);
            for (EntityKoaBase ent : listEnts) {
                if (ent.posLastFireplaceFound != null) {
                    BlockState state = world.getBlockState(ent.posLastFireplaceFound);
                    if (state.getBlock() == Blocks.CAMPFIRE) {
                        posLastFireplaceFound = new BlockPos(ent.posLastFireplaceFound);
                        dbg("found fire place spot to chill from entity");
                        return;
                    }
                }
            }
        }
    }

    //for other system not used
    public void syncBPM() {
        if ((world.getGameTime()+this.getEntityId()) % (20) != 0) return;

        List<EntityKoaBase> listEnts = world.getEntitiesWithinAABB(EntityKoaBase.class, new AxisAlignedBB(this.getPosition()).grow(10, 5, 10));
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

    public boolean isInstrument(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == BlockRegistry.bongo || state.getBlock() == Blocks.NOTE_BLOCK) {
            return true;
        }

        return false;
    }

    public void findAndSetDrums(boolean force) {

        //this.setHomePosAndDistance(this.func_213384_dI(), 128);

        if (!force && (world.getGameTime()+this.getEntityId()) % (20*30) != 0) return;

        Iterator<BlockPos> it = listPosDrums.iterator();
        while (it.hasNext()) {
            BlockPos pos = it.next();
            if (!isInstrument(pos)) {
                it.remove();
            }
        }

        if (listPosDrums.size() >= MAX_DRUMS) {
            return;
        }

        List<EntityKoaBase> listEnts = world.getEntitiesWithinAABB(EntityKoaBase.class, new AxisAlignedBB(this.getPosition()).grow(20, 20, 20));
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
                    if (isInstrument(pos)) {
                    //if (state.getBlock() == BlockRegistry.bongo) {

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

    /*public boolean tryGetVillage() {
        List<EntityKoaBase> listEnts = world.getEntitiesWithinAABB(EntityKoaBase.class, new AxisAlignedBB(this.getPosition()).grow(20, 20, 20));
        Collections.shuffle(listEnts);
        for (EntityKoaBase ent : listEnts) {
            if (ent.villageID != -1) {
                villageID = ent.villageID;
                return true;
            }
        }
        return false;
    }*/

    public boolean tryDumpInventoryIntoHomeChest() {
        TileEntity tile = world.getTileEntity(getHomePosition());
        if (tile instanceof ChestTileEntity) {
            ChestTileEntity chest = (ChestTileEntity)tile;

            for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = this.inventory.getStackInSlot(i);

                if (!itemstack.isEmpty()) {
                    this.inventory.setInventorySlotContents(i, this.addItem(chest, itemstack));
                }
            }
        }
        //maybe return false if inventory not emptied entirely
        return true;
    }

    @Nullable
    public ItemStack addItem(ChestTileEntity chest, ItemStack stack)
    {
        ItemStack itemstack = stack.copy();

        for (int i = 0; i < chest.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = chest.getStackInSlot(i);

            if (itemstack1.isEmpty())
            {
                chest.setInventorySlotContents(i, itemstack);
                chest.markDirty();
                return ItemStack.EMPTY;
            }

            if (ItemStack.areItemsEqual(itemstack1, itemstack))
            {
                int j = Math.min(chest.getInventoryStackLimit(), itemstack1.getMaxStackSize());
                int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

                if (k > 0)
                {
                    itemstack1.grow(k);
                    itemstack.shrink(k);

                    if (itemstack.getCount() <= 0)
                    {
                        chest.markDirty();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (itemstack.getCount() != stack.getCount())
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
    public void livingTick() {

        this.updateArmSwingProgress();
        super.livingTick();

        if (wasInWater) {
            if (!isInWater()) {
                if (collidedHorizontally) {
                    this.setMotion(getMotion().add(0, 0.4F, 0));
                    jumpingOutOfWater = true;
                }
            }
        }

        /**
         * hacky fix for koa spinning in spot when leaping out of water,
         * real issue is that their path node is still under the dock when theyre ontop of dock
         */
        if (jumpingOutOfWater) {
            if (onGround) {
                jumpingOutOfWater = false;
                this.getNavigator().clearPath();
            }
        }

        if (isInWater()) {
            //children have different hitbox size, use different values to keep them from getting stuck under docks and drowning
            //changing this doesnt derp up their pathing like it does for adults
            if (isChild()) {
                if (this.getMotion().y < -0.1F) {
                    this.getMotion().add(0, 0.25F, 0);
                }
            } else {
                if (this.getMotion().y < -0.2F) {
                    this.getMotion().add(0, 0.15F, 0);
                }
            }
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.60D);

            this.setPathPriority(PathNodeType.WATER, 8);
        } else {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);

            this.setPathPriority(PathNodeType.WATER, -1);
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
            //if (world.getGameTime() % (20*5) == 0) {
                //this.heal(5);
            //}

            if (druggedTime > 0) {
                druggedTime--;
            }
        }

        if (world.isRemote) {
            //heal indicator, has a bug that spawns a heart on reload into world but not a big deal
            if (clientHealthLastTracked != this.getHealth()) {
                if (this.getHealth() > clientHealthLastTracked) {
                    world.addParticle(ParticleTypes.HEART, false, this.posX, this.posY + 2.2, this.posZ, 0, 0, 0);
                }
                clientHealthLastTracked = this.getHealth();
            }
        }
    }

    @Override
    public void heal(float healAmount) {
        super.heal(healAmount);
    }

    //TODO: vanilla villager mating is using brain/memory/blackboard system now, we dont need all that for now at least, just stick with normal boolean state

    public void setMating(boolean mating) {
        this.isMating = mating;
    }

    public boolean isMating() {
        return this.isMating;
    }

    /*@Override*/
    public boolean getIsWillingToMate(boolean updateFirst) {
        //vanilla did food check here but hunters dont have any
        //our population limits work well enough to leave this to always true
        this.setIsWillingToMate(true);
        return true;
    }

    public void setIsWillingToMate(boolean b) {
        //NO-OP
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

    /*public void setVillageID(int villageID) {
        this.villageID = villageID;
    }*/

    public void setVillageAndDimID(int villageID, int villageDimID) {
        this.villageID = villageID;
        this.villageDimID = villageDimID;
    }

    public int getVillageDimID() {
        return villageDimID;
    }

    /*public void setVillageDimID(int villageDimID) {
        this.villageDimID = villageDimID;
    }*/

    //TODO: 1.14 readd
    /*public TownKoaVillage getVillage() {
        if (this.villageDimID == INVALID_DIM || this.villageID == -1) return null;

        World world = DimensionManager.getWorld(villageDimID);

        if (world != null) {
            WorldDataInstance data = world.getCapability(Tropicraft.WORLD_DATA_INSTANCE, null);
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
        }
        return null;
    }

    public TownKoaVillage getVillageWithinRange(int range) {
        World world = this.world;


        double distSq = range * range;
        double closestDist = 99999;
        TownKoaVillage closestVillage = null;

        if (world != null) {
            WorldDataInstance data = world.getCapability(Tropicraft.WORLD_DATA_INSTANCE, null);
            if (data != null) {
                for (ISimulationTickable entry : data.lookupTickingManagedLocations.values()) {
                    if (entry instanceof TownKoaVillage) {
                        TownKoaVillage village = (TownKoaVillage) entry;
                        if (village.getPopulationSize() < village.getMaxPopulationSize()) {
                            double dist = village.getOrigin().distanceSq(this.getPos());
                            if (dist < distSq && dist < closestDist) {
                                closestDist = dist;
                                closestVillage = village;
                            }
                        }
                    }
                }

            }
        }

        return closestVillage;
    }*/

    @Override
    public void remove() {
        super.remove();
        if (!world.isRemote) {
            //System.out.println("hook dead " + this);
            //TODO: 1.14 readd
            /*TownKoaVillage village = getVillage();
            if (village != null) {
                village.hookEntityDied(this);
            }*/
        }
    }

    //TODO: 1.14 readd listener for unload
    public void hookUnloaded() {
        if (!world.isRemote) {
            //System.out.println("hook unloaded " + this);
            //TODO: 1.14 readd
            /*TownKoaVillage village = getVillage();
            if (village != null) {
                village.hookEntityDestroyed(this);
            }*/
        }
    }

    public FishingBobberEntity getLure() {
        return lure;
    }

    public void setLure(FishingBobberEntity lure) {
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
            if (source.getTrueSource() instanceof LivingEntity) {
                //System.out.println("koa died by: " + source.getDamageType() + " - loc: " + source.getDamageLocation() + " - " + source.getDeathMessage((EntityLivingBase)source.getEntity()));
            } else {
                //System.out.println("koa died by: " + source.getDamageType() + " - loc: " + source.getDamageLocation());
            }
        }
        return result;
    }

    //TODO: 1.14 readd
    /*public void postSpawnGenderFix() {
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
    }*/

    //do not constantly use throughout night, as the night doesnt happen all on the same day
    //use asap and store value
    public boolean isPartyNight() {
        long time = world.getDayTime();
        long day = time / 24000;
        //party every 3rd night
        //System.out.println(time + " - " + day + " - " + (day % 3 == 0));
        return day % 3 == 0;
    }

    public void rollDiceParty() {

        if (isPartyNight()) {
            int chance = 90;
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

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("entity.tropicraft.koa." +
                getGender().toString().toLowerCase(Locale.ROOT) + "." +
                getRole().toString().toLowerCase(Locale.ROOT) + ".name"
        );
    }

    @Override
    public void onStruckByLightning(LightningBoltEntity lightningBolt) {
        //cancel super witch code
    }

    public void zapMemory() {
        listPosDrums.clear();
        setHomePosAndDistance(BlockPos.ZERO, -1);
        setFirelacePos(null);

        villageDimID = INVALID_DIM;
        villageID = -1;
    }

    public void dbg(String msg) {
        if (debug) {
            System.out.println(msg);
        }
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {

        //cancel villager trade sounds
        if (soundIn == SoundEvents.ENTITY_VILLAGER_YES || soundIn == SoundEvents.ENTITY_VILLAGER_NO) {
            return;
        }

        super.playSound(soundIn, volume, pitch);
    }

    public void setPlaying(boolean playing)
    {
        this.isPlaying = playing;
    }

    public boolean isPlaying()
    {
        return this.isPlaying;
    }

}


