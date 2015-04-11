package net.tropicraft.entity.placeable;

import io.netty.buffer.ByteBuf;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.Tropicraft;
import net.tropicraft.entity.EntityDartHelper;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;


public class EntitySnareTrap extends Entity implements IEntityAdditionalSpawnData {
	private static final int FULL_ID = 16;
	private static final int USERNAME_ID = 17;
	private static final int ENTITY_HEIGHT_ID = 18;
	public static final int MIN_HEIGHT = 3;
	public static final int MAX_HEIGHT = 6;
	public static final int SCAN_INTERVAL = 5;
	
	public EntityLivingBase caughtEntity;
	/** Stored AI tasks field of caught entities. AI tasks are disabled (set to
	 *  null) on catch to prevent mobs from attacking, and restored on release.
	 */
	private EntityAITasks caughtEntityAITasks;
	/**
	 * Stored move speed field of caught entities. Move speed is set to 0 on
	 * catch, then restored on release.
	 */
	private float caughtEntityMoveSpeed;

	public int trapPosX;
	public int trapPosY;
	public int trapPosZ;
	public int trapHeight;
	public ForgeDirection trapDirection;
	private boolean prevFull;
	/** Cooldown ticker to prevent us from scanning for entities every tick */
	private int scanCoolDown;

	public EntitySnareTrap(World par1World) {
		super(par1World);
		scanCoolDown = SCAN_INTERVAL;
		ignoreFrustumCheck = true;
		preventEntitySpawning = true;
	}
	
	public EntitySnareTrap(World world, int x, int y, int z, int height, ForgeDirection dir) {
		this(world);
		trapPosX = x;
		trapPosY = y;
		trapPosZ = z;
		trapHeight = height;
		trapDirection = dir;
		setSize(0.6f, height-0.5f);
		setLocationAndAngles(x+0.5, y, z+0.5, 0f, 0f);
	}
	
	// TODO NOTE: Replaced variables with these two methods to make it dynamic and not
	// needed to be stored in NBT. If it stops working because of that...put it back!
	/** The block ID of the block this trap is attached to at the top. */
	public Block getHookBlock(World world) {
		return world.getBlock(trapPosX - trapDirection.offsetX, trapPosY + trapHeight - 1, 
				trapPosZ - trapDirection.offsetZ);
	}
	
	/** The block ID of the block below this trap. */
	public Block getBottomBlock(World world) {
		return world.getBlock(trapPosX, trapPosY - 1, trapPosZ);
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (worldObj.isRemote) {
			// update our height
			boolean newFull = getFull();
			
			if (prevFull != newFull) {
				prevFull = newFull;
				if (newFull) {
					setSize(0.6f, trapHeight-getEntityHeight()-0.5f);
				} else {
					setSize(0.6f, trapHeight-0.5f);
				}
				// to make the new entity height take effect
				setLocationAndAngles(posX, posY, posZ, 0f, 0f);
			}
			
			// if the client player is trapped, prevent them from moving
			EntityPlayer player = Tropicraft.proxy.getClientPlayer();
			if (getUsername().equals(player.username)) {
				player.setLocationAndAngles(trapPosX+0.5, trapPosY, trapPosZ+0.5, player.rotationYaw, player.rotationPitch);
			}
			return;
		}
		
		if (caughtEntity == null) {
			scanCoolDown--;
			
			if (scanCoolDown == 0) {
				scanCoolDown = SCAN_INTERVAL;

				List ents = worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(5.0, 5.0, 5.0));
				for (Object obj: ents) {
					Entity ent = (Entity)obj;
					if (ent instanceof EntityLiving && ((EntityLiving)ent).isEntityAlive() && ent.posX >= trapPosX && ent.posX <= trapPosX+1 && ent.posZ >= trapPosZ && ent.posZ <= trapPosZ+1 && Math.abs(trapPosY-ent.posY) < 0.01) {
						boolean alreadyTrapped = false;
						
						for (Object trapObj: ents) {
							if (!(trapObj instanceof EntitySnareTrap)) {
								continue;
							}
							EntitySnareTrap trap = (EntitySnareTrap) trapObj;
							if (trap.caughtEntity == ent) {
								alreadyTrapped = true;
								break;
							}
						}
						
						if (!alreadyTrapped) {
							catchEntity((EntityLiving)ent);
						}
					}
				}
			}
		} else {
			if (!caughtEntity.isEntityAlive()) {
				releaseEntity();
			} else {
				caughtEntity.setLocationAndAngles(trapPosX+0.5, trapPosY, trapPosZ+0.5, caughtEntity.rotationYaw, caughtEntity.rotationPitch);
				caughtEntity.motionX = 0.0;
				caughtEntity.motionY = 0.0;
				caughtEntity.motionZ = 0.0;
			}
		}
		
		// possible bug with this check: dirt turning into grass below would
		// drop the trap
		if (worldObj.getBlock(trapPosX-trapDirection.offsetX, trapPosY+trapHeight-1, trapPosZ-trapDirection.offsetZ) != getHookBlock(worldObj) ||
			worldObj.getBlock(trapPosX, trapPosY-1, trapPosZ) != getBottomBlock(worldObj)) {
			if (caughtEntity != null) {
				releaseEntity();
			}
			this.dropItem(TCItemRegistry.snareTrap, 1);
			setDead();
		}
	}

	public void catchEntity(EntityLivingBase ent) {
		caughtEntity = ent;
		setFull(true);
		setEntityHeight(ent.height+ent.yOffset);
		worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F));
		setSize(0.6f, trapHeight-ent.height-ent.yOffset-0.5f);
		setLocationAndAngles(trapPosX+0.5, trapPosY+ent.height+ent.yOffset, trapPosZ+0.5, 0f, 0f);
		
		if (ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)ent;
			setUsername(player.username);
			PacketDispatcher.sendPacketToAllPlayers(TropicraftPacketHandler.getSnareTrapPacket(false, true, Collections.singleton(player.username)));
			Tropicraft.proxy.getUpsideDownUsernames().add(player.username);
		} else if (ent instanceof EntityLiving) {
			caughtEntityMoveSpeed = EntityDartHelper.getEntityMoveSpeed(ent);
			caughtEntityAITasks = EntityDartHelper.getEntityAITasks((EntityLiving) ent);
			EntityDartHelper.setEntityAITasks(ent, null);
			EntityDartHelper.setEntityMoveSpeed(ent, 0);
			EntityDartHelper.setIsEntityJumping(ent, false);
			if (ent instanceof EntityCreature) {
				EntityDartHelper.setEntityToAttack((EntityCreature) ent, null);
			}
			if (ent instanceof EntityCreeper) {
				EntityDartHelper.setCreeperIgnitionTime((EntityCreeper) ent, 0);
			}
		}
	}
	
	public void releaseEntity() {
		scanCoolDown = SCAN_INTERVAL;
		setFull(false);
		setUsername("");
		setSize(0.6f, trapHeight-0.5f);
		setLocationAndAngles(trapPosX+0.5, trapPosY, trapPosZ+0.5, 0f, 0f);
		if (caughtEntity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)caughtEntity;
			PacketDispatcher.sendPacketToAllPlayers(TropicraftPacketHandler.getSnareTrapPacket(false, false, Collections.singleton(player.username)));
			Tropicraft.proxy.getUpsideDownUsernames().remove(player.username);
		} else {
			EntityDartHelper.setEntityMoveSpeed(caughtEntity, caughtEntityMoveSpeed);
			EntityDartHelper.setEntityAITasks(caughtEntity, caughtEntityAITasks);
			caughtEntityAITasks = null;
		}
		caughtEntity = null;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float dmg) {
		if (worldObj.isRemote) {
			return true;
		}
		
		worldObj.playSoundAtEntity(this, "note.harp", 1.0F, 2f-rand.nextFloat()/2f);
		
		if (caughtEntity != null && source.getEntity() == caughtEntity) {
			if (rand.nextFloat() > 0.1f) {
				return false;
			}
		}
		
		if (caughtEntity != null) {
			releaseEntity();
		}
		
		Entity sourceEnt = source.getEntity();
		if (!(sourceEnt instanceof EntityPlayer && ((EntityPlayer)sourceEnt).capabilities.isCreativeMode)) {
			this.dropItem(TCItemRegistry.snareTrap, 1);
		}
		setDead();
		
		return true;
	}

	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(FULL_ID, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(USERNAME_ID, "");
		this.dataWatcher.addObject(ENTITY_HEIGHT_ID, Integer.valueOf(0));
	}
	
	public void setEntityHeight(float height) {
		this.dataWatcher.updateObject(ENTITY_HEIGHT_ID, Integer.valueOf((int)(height*100)));
	}
	
	public float getEntityHeight() {
		return this.dataWatcher.getWatchableObjectInt(ENTITY_HEIGHT_ID)/100f;
	}
	
	public void setUsername(String username) {
		this.dataWatcher.updateObject(USERNAME_ID, username);
	}
	
	public String getUsername() {
		return this.dataWatcher.getWatchableObjectString(USERNAME_ID);
	}
	
	public void setFull(boolean full) {
		this.dataWatcher.updateObject(FULL_ID, Byte.valueOf((byte)(full?1:0)));
	}
	
	public boolean getFull() {
		return this.dataWatcher.getWatchableObjectByte(FULL_ID) == 1;
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound var1) {
		trapHeight = var1.getInteger("Height");
		trapDirection = ForgeDirection.getOrientation(var1.getByte("Direction"));
		trapPosX = var1.getInteger("XPos");
		trapPosY = var1.getInteger("YPos");
		trapPosZ = var1.getInteger("ZPos");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1) {
		var1.setInteger("Height", trapHeight);
		var1.setByte("Direction", (byte)trapDirection.ordinal());
		var1.setInteger("XPos", trapPosX);
		var1.setInteger("YPos", trapPosY);
		var1.setInteger("ZPos", trapPosZ);
	}

	@Override
	public void writeSpawnData(ByteBuf data) {
		data.writeInt(this.trapPosX);
		data.writeInt(this.trapPosY);
		data.writeInt(this.trapPosZ);
		data.writeInt(this.trapHeight);
		data.writeByte(this.trapDirection.ordinal());		
	}

	@Override
	public void readSpawnData(ByteBuf data) {
		this.trapPosX = data.readInt();
		this.trapPosY = data.readInt();
		this.trapPosZ = data.readInt();
		this.trapHeight = data.readInt();
		this.trapDirection = ForgeDirection.getOrientation((int)data.readByte());
		setSize(0.6f, this.trapHeight-0.5f);
		setLocationAndAngles(this.trapPosX+0.5, this.trapPosY, trapPosZ+0.5, 0f, 0f);
	}
}