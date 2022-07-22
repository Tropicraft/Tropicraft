package net.tropicraft.core.common.entity.passive;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.ai.*;
import net.tropicraft.core.common.item.TropicraftItems;

import javax.annotation.Nullable;
import java.util.*;

public class EntityKoaBase extends Villager {

    //TODO: consider serializing found water sources to prevent them refinding each time, which old AI did
    public long lastTimeFished = 0;

    public BlockPos posLastFireplaceFound = null;
    public List<BlockPos> listPosDrums = new ArrayList<>();
    public static int MAX_DRUMS = 12;

    public SimpleContainer inventory;

    private static final EntityDataAccessor<Integer> ROLE = SynchedEntityData.defineId(EntityKoaBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> GENDER = SynchedEntityData.defineId(EntityKoaBase.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityKoaBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DANCING = SynchedEntityData.defineId(EntityKoaBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LURE_ID = SynchedEntityData.defineId(EntityKoaBase.class, EntityDataSerializers.INT);

    private float clientHealthLastTracked = 0;

    public static int MAX_HOME_DISTANCE = 128;

    private int villageID = -1;

    private ResourceKey<Level> villageDimension;

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
    private static final int TRADE_COOLDOWN = 24000*3;
    private static final int DIVE_TIME_NEEDED = 60*60;

    public boolean debug = false;

    public int druggedTime = 0;

    private static final Set<RegistryObject<Item>> TEMPTATION_ITEMS = Sets.newHashSet(TropicraftItems.NIGEL_STACHE);

    private boolean isMating;
    private boolean isPlaying;

    private boolean finalizedSpawn;

    public static Predicate<Entity> ENEMY_PREDICATE =
            //TODO: 1.14 fix
            input -> (input instanceof Monster/* && !(input instanceof CreeperEntity)) || input instanceof EntityTropiSkeleton || input instanceof EntityIguana || input instanceof EntityAshen*/);



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

    public EntityKoaBase(EntityType<? extends EntityKoaBase> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();

        inventory = new SimpleContainer(9);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public long getLastTradeTime() {
        return lastTradeTime;
    }

    public void setLastTradeTime(long lastTradeTime) {
        this.lastTradeTime = lastTradeTime;
    }

    public Genders getGender() {
        return Genders.get(this.getEntityData().get(GENDER));
    }

    public void setGender(Genders gender) {
        this.getEntityData().set(GENDER, gender.ordinal());
    }

    public Roles getRole() {
        return Roles.get(this.getEntityData().get(ROLE));
    }

    public boolean isSitting() {
        return this.getEntityData().get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.getEntityData().set(SITTING, sitting);
    }

    public boolean isDancing() {
        return this.getEntityData().get(DANCING);
    }

    public void setDancing(boolean val) {
        this.getEntityData().set(DANCING, val);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ROLE, 0);
        this.getEntityData().define(GENDER, 0);
        this.getEntityData().define(SITTING, false);
        this.getEntityData().define(DANCING, false);
        this.getEntityData().define(LURE_ID, -1);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        if (!level.isClientSide) return;

        if (key == LURE_ID) {
            int id = this.getEntityData().get(LURE_ID);
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
            Entity ent = level.getEntity(id);
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

    static class KoaTradeForPearls implements VillagerTrades.ItemListing {
        private final Item item;
        private final int count;
        private final int maxUses;
        private final int givenXP;
        private final float priceMultiplier;

        public KoaTradeForPearls(ItemLike item, int count, int maxUses, int givenXP) {
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

    public Int2ObjectMap<VillagerTrades.ItemListing[]> getOfferMap() {

        //TODO: 1.14 fix missing tropical and river fish entries from tropicrafts fix
        //- consider adding vanillas ones too now

        Int2ObjectMap<VillagerTrades.ItemListing[]> offers = null;

        if (getRole() == Roles.FISHERMAN) {
            offers = toIntMap(ImmutableMap.of(1,
                    new VillagerTrades.ItemListing[]{
                            new KoaTradeForPearls(Items.TROPICAL_FISH, 20, 8, 2),
                            new KoaTradeForPearls(TropicraftItems.FISHING_NET.get(), 1, 8, 2),
                            new KoaTradeForPearls(Items.FISHING_ROD, 1, 8, 2),
                            new KoaTradeForPearls(TropicraftItems.FRESH_MARLIN.get(), 3, 8, 2),
                            new KoaTradeForPearls(TropicraftItems.SARDINE_BUCKET.get(), 1, 4, 2),
                            new KoaTradeForPearls(TropicraftItems.PIRANHA_BUCKET.get(), 1, 3, 2),
                            new KoaTradeForPearls(TropicraftItems.TROPICAL_FERTILIZER.get(), 5, 8, 2)
                    }));
        } else if (getRole() == Roles.HUNTER) {
            offers = toIntMap(ImmutableMap.of(1,
                    new VillagerTrades.ItemListing[]{
                            new KoaTradeForPearls(TropicraftItems.FROG_LEG.get(), 5, 8, 2),
                            new KoaTradeForPearls(TropicraftItems.IGUANA_LEATHER.get(), 2, 8, 2),
                            new KoaTradeForPearls(TropicraftItems.SCALE.get(), 5, 8, 2)
                    }));
        }

        return offers;

        /*toIntMap(ImmutableMap.of(1,
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

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> p_221238_0_) {
        return new Int2ObjectOpenHashMap<>(p_221238_0_);
    }

    /**
     * New main override for villager "trades" aka offers
     * @return
     */
    @Override
    protected void updateTrades() {
        VillagerData villagerdata = this.getVillagerData();
        Int2ObjectMap<VillagerTrades.ItemListing[]> int2objectmap = getOfferMap();
        if (int2objectmap != null && !int2objectmap.isEmpty()) {
            VillagerTrades.ItemListing[] avillagertrades$itrade = int2objectmap.get(villagerdata.getLevel());
            if (avillagertrades$itrade != null) {
                MerchantOffers merchantoffers = this.getOffers();
                this.addOffersFromItemListings(merchantoffers, avillagertrades$itrade, 2);
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

        this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
        this.targetSelector.getRunningGoals().forEach(WrappedGoal::stop);



        int curPri = 0;

        this.goalSelector.addGoal(curPri++, new FloatGoal(this));

        this.goalSelector.addGoal(curPri++, new EntityAIAvoidEntityOnLowHealth(this, LivingEntity.class, ENEMY_PREDICATE,
                12.0F, 1.4D, 1.4D, 15F));

        this.goalSelector.addGoal(curPri++, new EntityAIEatToHeal(this));

        this.goalSelector.addGoal(curPri++, new TradeWithPlayerGoal(this));

        this.goalSelector.addGoal(curPri++, new MeleeAttackGoal(this, 1F, true) {
            @Override
            public void start() {
                super.start();
                if (this.mob instanceof EntityKoaBase) {
                    ((EntityKoaBase) this.mob).setFightingItem();
                }
            }

            @Override
            protected double getAttackReachSqr(LivingEntity attackTarget) {
                return this.mob.getType().getDimensions().width * 2.5F * this.mob.getType().getDimensions().width * 2.5F + attackTarget.getType().getDimensions().width;
            }
        });

        this.goalSelector.addGoal(curPri++, new EntityAITemptHelmet(this, 1.0D, false, TEMPTATION_ITEMS));

        this.goalSelector.addGoal(curPri++, new MoveTowardsRestrictionGoal(this, 1D));
        this.goalSelector.addGoal(curPri++, new EntityAIKoaMate(this));
        this.goalSelector.addGoal(curPri++, new EntityAIChillAtFire(this));
        this.goalSelector.addGoal(curPri++, new EntityAIPartyTime(this));

        if (canFish()) {
            this.goalSelector.addGoal(curPri++, new EntityAIGoneFishin(this));
        }

        if (isBaby()) {
            this.goalSelector.addGoal(curPri++, new EntityAIPlayKoa(this, 1.2D));
        }

        this.goalSelector.addGoal(curPri, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(curPri++, new EntityAIWanderNotLazy(this, 1D, 40));
        this.goalSelector.addGoal(curPri++, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        //i dont think this one works, change to predicate
        if (canHunt()) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, ENEMY_PREDICATE));
        }


    }

    @Override
    public Villager getBreedOffspring(ServerLevel world, AgeableMob ageable) {
        EntityKoaHunter child = new EntityKoaHunter(TropicraftEntities.KOA_HUNTER.get(), this.level);
        child.finalizeSpawn(world, world.getCurrentDifficultyAt(child.blockPosition()), MobSpawnType.BREEDING, null, null);
        return child;
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();

        updateUniqueEntityAI();
    }

    public boolean canFish() {
        return this.getEntityData().get(ROLE) == Roles.FISHERMAN.ordinal();
    }

    public boolean canHunt() {
        return this.getEntityData().get(ROLE) == Roles.HUNTER.ordinal() && !isBaby();
    }

    public void setHunter() {
        this.getEntityData().set(ROLE, Integer.valueOf(Roles.HUNTER.ordinal()));
        this.setFightingItem();
    }

    public void setFisher() {
        this.getEntityData().set(ROLE, Integer.valueOf(Roles.FISHERMAN.ordinal()));
        this.setFishingItem();
    }

    @Override
    protected void customServerAiStep() {
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

    public static AttributeSupplier.Builder createAttributes() {
        return Villager.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.ARMOR, 2.0);
    }

    @Override
    public void setTarget(@Nullable LivingEntity entitylivingbaseIn) {
        super.setTarget(entitylivingbaseIn);
    }

    /** Copied from EntityMob */
    @Override
    public boolean doHurtTarget(Entity entityIn)
    {
        float damage = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        float knockback = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);

        if (entityIn instanceof LivingEntity)
        {
            damage += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)entityIn).getMobType());
            knockback += EnchantmentHelper.getKnockbackBonus(this);
        }

        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), damage);

        if (flag)
        {
            if (knockback > 0 && entityIn instanceof LivingEntity)
            {
                ((LivingEntity)entityIn).knockback(knockback * 0.5F, Mth.sin(this.getYRot() * 0.017453292F), -Mth.cos(this.getYRot() * 0.017453292F));
                this.setDeltaMovement(this.getDeltaMovement().x * 0.6D, this.getDeltaMovement().y, this.getDeltaMovement().z * 0.6D);
                /*this.motionX *= 0.6D;
                this.motionZ *= 0.6D;*/
            }

            int j = EnchantmentHelper.getFireAspect(this);

            if (j > 0)
            {
                entityIn.setSecondsOnFire(j * 4);
            }

            if (entityIn instanceof Player)
            {
                Player entityplayer = (Player)entityIn;
                ItemStack itemstack = this.getMainHandItem();
                ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof AxeItem && itemstack1.getItem() == Items.SHIELD)
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;

                    if (this.random.nextFloat() < f1)
                    {
                        entityplayer.getCooldowns().addCooldown(Items.SHIELD, 100);
                        this.level.broadcastEntityEvent(entityplayer, (byte)30);
                    }
                }
            }

            this.doEnchantDamageEffects(this, entityIn);
        }

        return flag;
    }
    
    /*private static final Field _buyingPlayer = Util.findField(VillagerEntity.class, "field_70962_h", "buyingPlayer");
    private static final Field _buyingList = Util.findField(VillagerEntity.class, "field_70963_i", "buyingList");*/
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        InteractionResult ret = InteractionResult.PASS;
        try {
            boolean doTrade = true;
            if (!this.level.isClientSide) {

                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (!stack.isEmpty() && stack.getItem() == TropicraftItems.POISON_FROG_SKIN.get()) {
                    doTrade = false;

                    //drug the koa and make him forget everything
                    dbg("koa drugged, zapping memory");
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    zapMemory();

                    druggedTime += 20*60*2;
                    addEffect(new MobEffectInstance(MobEffects.CONFUSION, druggedTime));
                    findAndSetDrums(true);

                }
                // [1.15] Cojo - commenting out until we know what we want koa scuba interaction to be
                /*else if (!stack.isEmpty() && stack.getItem() == ItemRegistry.diveComputer) {
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
                }*/

                if (doTrade) {
                    // Make the super method think this villager is already trading, to block the GUI from opening
                    //_buyingPlayer.set(this, player);
                    ret = super.mobInteract(player, hand);
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.restrictTo(this.blockPosition(), MAX_HOME_DISTANCE);

        rollDiceChild();

        rollDiceRole();
        rollDiceGender();

        updateUniqueEntityAI();

        finalizedSpawn = true;

        /*VillagerRegistry.VillagerProfession koaProfession = new VillagerRegistry.VillagerProfession("koa_profession", "");
        this.setProfession(koaProfession);*/

        //TODO: 1.14 make sure not needed, overwritten with getOfferMap now
        //initTrades();

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public void rollDiceChild() {
        int childChance = 20;
        if (childChance >= this.level.random.nextInt(100)) {
            this.setAge(-24000);
        }
    }

    public void rollDiceRole() {
        int randValRole = this.level.random.nextInt(Roles.values().length);
        if (randValRole == Roles.FISHERMAN.ordinal()) {
            this.setFisher();
        } else if (randValRole == Roles.HUNTER.ordinal()) {
            this.setHunter();
        }
    }

    public void rollDiceGender() {
        getEntityData().set(GENDER, random.nextInt(Genders.values().length));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("home_X", getRestrictCenter().getX());
        compound.putInt("home_Y", getRestrictCenter().getY());
        compound.putInt("home_Z", getRestrictCenter().getZ());

        if (posLastFireplaceFound != null) {
            compound.putInt("fireplace_X", posLastFireplaceFound.getX());
            compound.putInt("fireplace_Y", posLastFireplaceFound.getY());
            compound.putInt("fireplace_Z", posLastFireplaceFound.getZ());
        }

        compound.putLong("lastTimeFished", lastTimeFished);

        ListTag nbttaglist = new ListTag();

        for (int i = 0; i < this.inventory.getContainerSize(); ++i)
        {
            ItemStack itemstack = this.inventory.getItem(i);

            if (!itemstack.isEmpty())
            {
                CompoundTag nbttagcompound = new CompoundTag();
                nbttagcompound.putByte("Slot", (byte)i);
                itemstack.save(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        compound.put("koa_inventory", nbttaglist);
        compound.putInt("role_id", this.getEntityData().get(ROLE));
        compound.putInt("gender_id", this.getEntityData().get(GENDER));
        compound.putInt("village_id", villageID);

        if (villageDimension != null) {
            compound.putString("village_dimension", villageDimension.location().toString());
        }

        compound.putLong("lastTradeTime", lastTradeTime);

        for (int i = 0; i < listPosDrums.size(); i++) {
            compound.putInt("drum_" + i + "_X", listPosDrums.get(i).getX());
            compound.putInt("drum_" + i + "_Y", listPosDrums.get(i).getY());
            compound.putInt("drum_" + i + "_Z", listPosDrums.get(i).getZ());
        }

        compound.putInt("druggedTime", druggedTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("home_X")) {
            this.restrictTo(new BlockPos(compound.getInt("home_X"), compound.getInt("home_Y"), compound.getInt("home_Z")), MAX_HOME_DISTANCE);
        }

        if (compound.contains("fireplace_X")) {
            this.setFirelacePos(new BlockPos(compound.getInt("fireplace_X"), compound.getInt("fireplace_Y"), compound.getInt("fireplace_Z")));
        }

        lastTimeFished = compound.getLong("lastTimeFished");

        if (compound.contains("koa_inventory", 9)) {
            ListTag nbttaglist = compound.getList("koa_inventory", 10);
            //this.initHorseChest();

            for (int i = 0; i < nbttaglist.size(); ++i) {
                CompoundTag nbttagcompound = nbttaglist.getCompound(i);
                int j = nbttagcompound.getByte("Slot") & 255;

                this.inventory.setItem(j, ItemStack.of(nbttagcompound));
            }
        }

        this.villageID = compound.getInt("village_id");

        //backwards compat
        if (!compound.contains("village_dimension")) {
            this.villageDimension = level.dimension();
        } else {
            this.villageDimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(compound.getString("village_dim_id")));
        }

        if (compound.contains("role_id")) {
            this.getEntityData().set(ROLE, compound.getInt("role_id"));
        } else {
            rollDiceRole();
        }
        if (compound.contains("gender_id")) {
            this.getEntityData().set(GENDER, compound.getInt("gender_id"));
        } else {
            rollDiceGender();
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
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
    }

    public void setFightingItem() {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TropicraftItems.DAGGER.get()));
    }

    public void monitorHomeVillage() {
        if (villageDimension != null) {
            //if (villageID != -1) {

                //if not in home dimension, full reset
                if (this.level.dimension() != villageDimension) {
                    dbg("koa detected different dimension, zapping memory");
                    zapMemory();
                    addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5));
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
                TileEntity tile = world.getTileEntity(getRestrictCenter());
                if ((tile instanceof ChestTileEntity)) {
                    village = createNewVillage(getRestrictCenter());
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

        if (!force && (level.getGameTime()+this.getId()) % (20*30) != 0) return;

        //validate home position
        boolean tryFind = false;
        if (getRestrictCenter().equals(BlockPos.ZERO)) {
            tryFind = true;
        } else {
            BlockEntity tile = level.getBlockEntity(getRestrictCenter());
            if (!(tile instanceof ChestBlockEntity)) {
                //home position isnt a chest, keep current position but find better one
                tryFind = true;
            }
        }

        if (tryFind) {
            int range = 20;
            for (int x = -range; x <= range; x++) {
                for (int y = -range / 2; y <= range / 2; y++) {
                    for (int z = -range; z <= range; z++) {
                        BlockPos pos = this.blockPosition().offset(x, y, z);
                        BlockEntity tile = level.getBlockEntity(pos);
                        if (tile instanceof ChestBlockEntity) {
                            //System.out.println("found chest, updating home position to " + pos);
                            dbg("found chest, updating home position to " + pos);
                            restrictTo(pos, MAX_HOME_DISTANCE);
                            return;
                        }
                    }
                }
            }
        }
    }

    public boolean findAndSetTownID(boolean force) {
        if (!force && (level.getGameTime()+this.getId()) % (20*30) != 0) return false;

        boolean tryFind = false;

        if (villageID == -1 || villageDimension == null) {
            tryFind = true;
            //make sure return status is correct
            villageID = -1;
        }

        if (tryFind) {
            List<EntityKoaBase> listEnts = level.getEntitiesOfClass(EntityKoaBase.class, new AABB(this.blockPosition()).inflate(20, 20, 20));
            Collections.shuffle(listEnts);
            for (EntityKoaBase ent : listEnts) {
                if (ent.villageID != -1 && ent.villageDimension != null) {
                    this.setVillageAndDimID(ent.villageID, ent.villageDimension);
                    break;
                }
            }
        }

        return this.villageID != -1;
    }

    public void findAndSetFireSource(boolean force) {

        //this.setHomePosAndDistance(this.getRestrictCenter(), 128);

        if (!force && (level.getGameTime()+this.getId()) % (20*30) != 0) return;

        //validate fire source
        boolean tryFind = false;
        if (posLastFireplaceFound == null) {
            tryFind = true;
        } else if (posLastFireplaceFound != null) {
            BlockState state = level.getBlockState(posLastFireplaceFound);
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
                        BlockPos pos = this.blockPosition().offset(x, y, z);
                        BlockState state = level.getBlockState(pos);
                        if (state.getBlock() == Blocks.CAMPFIRE) {
                            dbg("found fire place spot to chill");
                            setFirelacePos(pos);
                            return;
                        }
                    }
                }
            }

            List<EntityKoaBase> listEnts = level.getEntitiesOfClass(EntityKoaBase.class, new AABB(this.blockPosition()).inflate(20, 20, 20));
            Collections.shuffle(listEnts);
            for (EntityKoaBase ent : listEnts) {
                if (ent.posLastFireplaceFound != null) {
                    BlockState state = level.getBlockState(ent.posLastFireplaceFound);
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
        if ((level.getGameTime()+this.getId()) % (20) != 0) return;

        List<EntityKoaBase> listEnts = level.getEntitiesOfClass(EntityKoaBase.class, new AABB(this.blockPosition()).inflate(10, 5, 10));
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
        final BlockState state = level.getBlockState(pos);
        return state.is(TropicraftTags.Blocks.BONGOS) || state.is(Blocks.NOTE_BLOCK);
    }

    public void findAndSetDrums(boolean force) {

        //this.setHomePosAndDistance(this.getRestrictCenter(), 128);

        if (!force && (level.getGameTime()+this.getId()) % (20*30) != 0) return;

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

        List<EntityKoaBase> listEnts = level.getEntitiesOfClass(EntityKoaBase.class, new AABB(this.blockPosition()).inflate(20, 20, 20));
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
                    BlockPos pos = this.blockPosition().offset(x, y, z);
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
        BlockEntity tile = level.getBlockEntity(getRestrictCenter());
        if (tile instanceof ChestBlockEntity) {
            ChestBlockEntity chest = (ChestBlockEntity)tile;

            for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);

                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(i, this.addItem(chest, itemstack));
                }
            }
        }
        //maybe return false if inventory not emptied entirely
        return true;
    }

    @Nullable
    public ItemStack addItem(ChestBlockEntity chest, ItemStack stack)
    {
        ItemStack itemstack = stack.copy();

        for (int i = 0; i < chest.getContainerSize(); ++i)
        {
            ItemStack itemstack1 = chest.getItem(i);

            if (itemstack1.isEmpty())
            {
                chest.setItem(i, itemstack);
                chest.setChanged();
                return ItemStack.EMPTY;
            }

            if (ItemStack.isSame(itemstack1, itemstack))
            {
                int j = Math.min(chest.getMaxStackSize(), itemstack1.getMaxStackSize());
                int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

                if (k > 0)
                {
                    itemstack1.grow(k);
                    itemstack.shrink(k);

                    if (itemstack.getCount() <= 0)
                    {
                        chest.setChanged();
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (itemstack.getCount() != stack.getCount())
        {
            chest.setChanged();
        }

        return itemstack;
    }

    public void setFirelacePos(BlockPos pos) {
        this.posLastFireplaceFound = pos;
    }

    @Override
    public int getAirSupply() {
        return super.getAirSupply();
    }

    @Override
    public void aiStep() {
        if (finalizedSpawn) {
            finalizedSpawn = false;
            findAndSetHomeToCloseChest(true);
            findAndSetFireSource(true);
            findAndSetDrums(true);
        }

        this.updateSwingTime();
        super.aiStep();

        if (wasInWater) {
            if (!isInWater()) {
                if (horizontalCollision) {
                    this.setDeltaMovement(getDeltaMovement().add(0, 0.4F, 0));
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
                this.getNavigation().stop();
            }
        }

        if (isInWater()) {
            //children have different hitbox size, use different values to keep them from getting stuck under docks and drowning
            //changing this doesnt derp up their pathing like it does for adults
            if (isBaby()) {
                if (this.getDeltaMovement().y < -0.1F) {
                    this.getDeltaMovement().add(0, 0.25F, 0);
                }
            } else {
                if (this.getDeltaMovement().y < -0.2F) {
                    this.getDeltaMovement().add(0, 0.15F, 0);
                }
            }
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.60D);

            this.setPathfindingMalus(BlockPathTypes.WATER, 8);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.28D);

            this.setPathfindingMalus(BlockPathTypes.WATER, -1);
        }

        wasInWater = isInWater();

        if (!wasNightLastTick) {
            if (!this.level.isDay()) {
                //roll dice once
                rollDiceParty();
            }
        }

        wasNightLastTick = !this.level.isDay();

        if (!level.isClientSide) {
            //if (world.getGameTime() % (20*5) == 0) {
                //this.heal(5);
            //}

            if (druggedTime > 0) {
                druggedTime--;
            }
        }

        if (level.isClientSide) {
            //heal indicator, has a bug that spawns a heart on reload into world but not a big deal
            if (clientHealthLastTracked != this.getHealth()) {
                if (this.getHealth() > clientHealthLastTracked) {
                    level.addParticle(ParticleTypes.HEART, false, getX(), getY() + 2.2, getZ(), 0, 0, 0);
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

    public int getVillageID() {
        return villageID;
    }

    /*public void setVillageID(int villageID) {
        this.villageID = villageID;
    }*/

    public void setVillageAndDimID(int villageID, ResourceKey<Level> villageDimID) {
        this.villageID = villageID;
        this.villageDimension = villageDimID;
    }

    public ResourceKey<Level> getVillageDimension() {
        return villageDimension;
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
    public void remove(Entity.RemovalReason pReason) {
        super.remove(pReason);
        if (!level.isClientSide) {
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
        if (!level.isClientSide) {
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
        if (!this.level.isClientSide) {
            if (lure != null) {
                this.getEntityData().set(LURE_ID, this.lure.getId());
            } else {
                this.getEntityData().set(LURE_ID, -1);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (this.getHealth() <= 0) {
            if (source.getEntity() instanceof LivingEntity) {
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
        long time = level.getDayTime();
        long day = time / 24000;
        //party every 3rd night
        //System.out.println(time + " - " + day + " - " + (day % 3 == 0));
        return day % 3 == 0;
    }

    public void rollDiceParty() {

        if (isPartyNight()) {
            int chance = 90;
            if (chance >= this.level.random.nextInt(100)) {
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
    public Component getDisplayName() {
        return new TranslatableComponent("entity.tropicraft.koa." +
                getGender().toString().toLowerCase(Locale.ROOT) + "." +
                getRole().toString().toLowerCase(Locale.ROOT) + ".name"
        );
    }

    @Override
    public void thunderHit(ServerLevel world, LightningBolt lightning) {
    }

    public void zapMemory() {
        listPosDrums.clear();
        restrictTo(BlockPos.ZERO, -1);
        setFirelacePos(null);

        villageDimension = null;
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
        if (soundIn == SoundEvents.VILLAGER_YES || soundIn == SoundEvents.VILLAGER_NO) {
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

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.KOA_SPAWN_EGG.get());
    }
}
