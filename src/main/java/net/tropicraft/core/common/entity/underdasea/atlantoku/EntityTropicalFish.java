package net.tropicraft.core.common.entity.underdasea.atlantoku;

import java.util.List;

import net.minecraft.entity.Entity;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.ItemFishBucket;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityTropicalFish extends EntitySchoolableFish implements IAtlasFish {

	public boolean targetHook;
	public Entity hook;
	public boolean hasBeenPlaced;
	public boolean isCatchable;
	
	public static final String[] NAMES = { "Clownfish", "Queen Angelfish", "Yellow Tang", "Butterflyfish",
			"Geophagus Surinamensis", "Betta Fish", "Regal Tang", "Royal Gamma" };

	private static final DataParameter<Integer> TEXTURE_COLOR = EntityDataManager
			.<Integer>createKey(EntityTropicalFish.class, DataSerializers.VARINT);

	public EntityTropicalFish(World world) {
		super(world);
		targetHook = false;
		leader = null;
		setSize(.3F, .4F);
		int color = world.rand.nextInt(NAMES.length);
		setColor(color);
		this.setExpRate(3);
		setIsLeader(true);
		this.setSchoolSizeRange(12, 24);
		isCatchable = true;
		this.setSwimSpeeds(1f, 1.5f, 4f);
		this.setMaxHealth(2);
		this.setFleesPlayers(true, 5D);
	}

	@Override
	public EntitySchoolableFish setSchoolLeader(EntityTropicraftWaterBase leader) {
		if(leader instanceof EntityTropicalFish) {
			setColor(((EntityTropicalFish)leader).getColor());
		}
		return super.setSchoolLeader(leader);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		int color = this.world.rand.nextInt(NAMES.length);
		this.getDataManager().register(TEXTURE_COLOR, Integer.valueOf(color));
        this.setDropStack(new ItemStack(ItemRegistry.rawTropicalFish, 1, color), 1);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {   
	    ItemStack stack = player.getHeldItem(hand);
		if (!stack.isEmpty() && stack.getItem() == ItemRegistry.fishingNet) {
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
					player.inventory.setInventorySlotContents(bucketSlot,
							fishHolder = new ItemStack(ItemRegistry.fishBucket));
					player.inventoryContainer.detectAndSendChanges();
				}
				if (ItemFishBucket.addFish(fishHolder, this)) {
					player.swingArm(hand);
					world.playSound(player, getPosition(), SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.PLAYERS,
							0.25f, 1f + (rand.nextFloat() * 0.4f));
					getEntityWorld().removeEntity(this);
					return true;
				}
			}
		}

		return false;
	}

	private boolean isFishHolder(ItemStack stack) {
		return !stack.isEmpty()
				&& (stack.getItem() == ItemRegistry.tropicsWaterBucket || stack.getItem() == ItemRegistry.fishBucket);
	}

	public int getColor() {
		return this.dataManager.get(TEXTURE_COLOR);
	}

	public void setColor(int color) {
		this.dataManager.set(TEXTURE_COLOR, Integer.valueOf(color));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("Placed", hasBeenPlaced);
		nbttagcompound.setInteger("Color", Integer.valueOf(getColor()));
		super.writeEntityToNBT(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		// color = nbttagcompound.getShort("Color");
		// Following conditional is to prevent fish despawning if the world
		// was created prior to introduction of the placed flag
		if (nbttagcompound.hasKey("Placed")) {
			hasBeenPlaced = nbttagcompound.getBoolean("Placed");
		} else {
			hasBeenPlaced = true;
		}

		setColor(Integer.valueOf(nbttagcompound.getInteger("Color")));
		super.readEntityFromNBT(nbttagcompound);
	}

	@Override
	public EntityLivingBase getAttackTarget() {
		return null;
	}

	public void checkForHook() {
		List<EntityFishHook> list = world.getEntitiesWithinAABB(EntityFishHook.class,
				this.getEntityBoundingBox().grow(10, 10, 10));
		if (list.isEmpty()) {
			targetHook = false;
			hook = null;
			return;
		}
		hook = (EntityFishHook) (list.get(0));
		targetHook = true;
	}

	@Override
	public double getYOffset() {
		return 0.0D;
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