package net.tropicraft.core.common.entity.underdasea.atlantoku;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.ai.EntityAIFindLeader;
import net.tropicraft.core.common.entity.ai.EntityAIFollowLeader;
import net.tropicraft.core.common.item.ItemFishBucket;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityTropicalFish extends EntityTropicraftWaterBase implements IAtlasFish{

	public boolean inSchool;
	public EntityTropicalFish leader; 
	public boolean targetHook;
	public Entity hook;
	public boolean hasBeenPlaced;
	public boolean isCatchable;
	public static final String[] names = {"Clownfish", "Queen Angelfish", "Yellow Tang", "Butterflyfish", "Geophagus Surinamensis", "Betta Fish"
		, "Regal Tang", "Royal Gamma"};

	private static final DataParameter<Integer> TEXTURE_COLOR = EntityDataManager.<Integer>createKey(EntityTropicalFish.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> SHOULD_SPAWN_SCHOOL = EntityDataManager.<Boolean>createKey(EntityTropicalFish.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_LEADER = EntityDataManager.<Boolean>createKey(EntityTropicalFish.class, DataSerializers.BOOLEAN);

	public EntityTropicalFish(World world) {
		super(world);
		targetHook = false;
		inSchool = false;
		leader = null;      
		setSize(.3F, .4F);
		setColor(world.rand.nextInt(names.length));
		setShouldSpawnSchool(true);
		setIsLeader(true);
		isCatchable = true;
		this.experienceValue = 3;
        this.setSwimSpeeds(1f, 0.2f, 4f);
        this.setSchoolable(true);

	}

	public EntityTropicalFish(World world, EntityLiving entityliving, int i) {
		super(world);
		setShouldSpawnSchool(false);
		targetHook = false;
		inSchool = false;
		leader = null;
		setColor(i);
		setSize(.3F, .4F);
		setIsLeader(true);
		setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
	}

	/**
	 * Will spawn a new fish of the same type that follows the original fish
	 */
	public EntityTropicalFish(EntityTropicalFish original) {
		this(original.world);
		setShouldSpawnSchool(false);
		targetHook = false;
		inSchool = true;
		leader = original;
		setColor(original.getColor());
		setSize(.3F, .4F);
		setIsLeader(false);
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
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(1, new EntityAIFindLeader(this));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.25D));
	}

	@Override
	public void entityInit() {
		super.entityInit();

		int color = this.world.rand.nextInt(names.length);
		this.getDataManager().register(TEXTURE_COLOR, Integer.valueOf(color));
		this.getDataManager().register(SHOULD_SPAWN_SCHOOL, Boolean.valueOf(false));
		this.getDataManager().register(IS_LEADER, Boolean.valueOf(false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
	}
	
	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {

		if (stack != null && stack.getItem() == ItemRegistry.fishingNet) {

			final int firstHotbarSlot = 0;
			int bucketSlot = -1;
			for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
				ItemStack s = player.inventory.getStackInSlot(firstHotbarSlot + i);
				if (isFishHolder(s)) {
					bucketSlot = firstHotbarSlot + i;
					break;
				}
			}

			if (bucketSlot == -1 && isFishHolder(player.getHeldItemOffhand())) {
				bucketSlot = 36;
			}

			if (bucketSlot >= 0) {
				ItemStack fishHolder = player.inventory.getStackInSlot(bucketSlot);
				if (fishHolder.getItem() == ItemRegistry.tropicsWaterBucket) {
					player.inventory.setInventorySlotContents(bucketSlot, fishHolder = new ItemStack(ItemRegistry.fishBucket));
					player.inventoryContainer.detectAndSendChanges();
				}
				if (ItemFishBucket.addFish(fishHolder, this)) {
					player.swingArm(hand);
					world.playSound(player, getPosition(), SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.PLAYERS, 0.25f, 1f + (rand.nextFloat() * 0.4f));
					getEntityWorld().removeEntity(this);
					return true;
				}
			}
		}

		return false;
	}
	
	private boolean isFishHolder(ItemStack stack) {
		return stack != null && (stack.getItem() == ItemRegistry.tropicsWaterBucket || stack.getItem() == ItemRegistry.fishBucket);
	}
	
	public boolean getIsLeader() {
		return this.dataManager.get(IS_LEADER);
	}
	
	public void setIsLeader(boolean flag) {
		this.dataManager.set(IS_LEADER, Boolean.valueOf(flag));
	}

	public int getColor() {
		return this.dataManager.get(TEXTURE_COLOR);
	}

	public void setColor(int color) {
		this.dataManager.set(TEXTURE_COLOR, Integer.valueOf(color));
	}

	public void setShouldSpawnSchool(boolean spawnStatus) {
		this.dataManager.set(SHOULD_SPAWN_SCHOOL, Boolean.valueOf(spawnStatus));
	}

	public boolean getShouldSpawnSchool() {
		return this.dataManager.get(SHOULD_SPAWN_SCHOOL);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("Placed", hasBeenPlaced);
		nbttagcompound.setInteger("Color", Integer.valueOf(getColor()));
		nbttagcompound.setBoolean("isLeader", getIsLeader());
		super.writeEntityToNBT(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		//  color = nbttagcompound.getShort("Color");
		// Following conditional is to prevent fish despawning if the world
		// was created prior to introduction of the placed flag
		if (nbttagcompound.hasKey("Placed")) {
			hasBeenPlaced = nbttagcompound.getBoolean("Placed");
		} else {
			hasBeenPlaced = true;
		}

		setShouldSpawnSchool(false);
		setColor(Integer.valueOf(nbttagcompound.getInteger("Color")));
		setIsLeader(Boolean.valueOf(nbttagcompound.getBoolean("isLeader")));
		super.readEntityFromNBT(nbttagcompound);
	}

	@Override
	public EntityLivingBase getAttackTarget() {
		return null;
	}

	public void checkForHook(){
		List<EntityFishHook> list = world.getEntitiesWithinAABB(EntityFishHook.class, this.getEntityBoundingBox().expand(10, 10, 10));
		if (list.isEmpty()){
			targetHook = false;
			hook = null;
			return;
		}
		hook = (EntityFishHook)(list.get(0));
		targetHook = true;
	}

	@Override
	public double getYOffset() {
		return 0.0D;
	}
	

		@Override
		public void onLivingUpdate(){
			if (getShouldSpawnSchool() && !world.isRemote) {
				// Note: min/max values include this fish
				int maxInSchool = 20;
				int minInSchool = 5;
				int numToSpawn = (new Random()).nextInt(1 + maxInSchool - minInSchool) + minInSchool - 1;
				for (int i = 0; i < numToSpawn; i++) {
					
					EntityTropicalFish fish = new EntityTropicalFish(this);
					world.spawnEntity(fish);
				}
				setShouldSpawnSchool(false);
			}
			super.onLivingUpdate();
		}

	@Override
	public boolean canDespawn() {
		return hasBeenPlaced;
	}

	public void disableDespawning() {
		hasBeenPlaced = true;
	}

	@Override
	public int getAtlasSlot() {
		return this.getDataManager().get(TEXTURE_COLOR);
	}
}