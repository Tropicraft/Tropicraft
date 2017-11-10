package net.tropicraft.core.common.entity.underdasea.atlantoku;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.EntityLand;
import net.tropicraft.core.registry.ItemRegistry;

public class EntityShark extends EntityTropicraftWaterBase implements IPredatorDiet {

	private static final DataParameter<Boolean> IS_BOSS = EntityDataManager.<Boolean>createKey(EntityShark.class, DataSerializers.BOOLEAN);
	
	private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), 
			BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS));
	private ArrayList<EntityPlayerMP> bossTargets = new ArrayList<EntityPlayerMP>();
	private boolean hasSetBoss = false;

	public EntityShark(World world) {
		super(world);
		this.setSwimSpeeds(1.3f, 2f, 0.6f, 3f, 5f);
		this.setSize(1.4F, 0.5F);
		this.setExpRate(5);
		this.setApproachesPlayers(true);
		this.setFishable(true);
		this.setDropStack(ItemRegistry.fertilizer, 3);
		this.setMaxHealth(10);
		this.setAttackDamage(4f);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.getDataManager().register(IS_BOSS, false);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.isBoss()) {
			// Set state to boss
			if (!hasSetBoss) {
				this.setBossTraits();
			}
			
			
			if (!world.isRemote) {
				
				
				// Search for suitable target
				EntityPlayer nearest = world.getClosestPlayerToEntity(this, 64D);
				if (nearest != null) {
					if (this.canEntityBeSeen(nearest) && nearest.isInWater() && !nearest.isCreative() && !nearest.isDead) {
						this.aggressTarget = nearest;
						this.setTargetHeading(this.aggressTarget.posX, this.aggressTarget.posY+1, this.aggressTarget.posZ, true);
						// Show health bar to target player
						if (nearest instanceof EntityPlayerMP) {
							if (!this.bossInfo.getPlayers().contains(nearest)) {
								this.bossTargets.add((EntityPlayerMP) nearest);
								this.bossInfo.addPlayer((EntityPlayerMP) nearest);
							}
						}
					} else {
						clearBossTargets();
					}
				} else {
					clearBossTargets();
				}
				
				// Heal if no target
				if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 80 == 0 && this.aggressTarget == null) {
					this.heal(1f);
					this.spawnExplosionParticle();
				}
				// Update health bar
				this.bossInfo.setPercent(this.rangeMap(this.getHealth(), 0, this.getMaxHealth(), 0, 1));
			}
		}

	}

	private void clearBossTargets() {
		if (this.bossTargets.size() > 0) {
			for (EntityPlayerMP p : this.bossTargets) {
				this.bossInfo.removePlayer(p);
			}
			this.bossTargets.clear();
		}
	}

	private void setBossTraits() {
		this.setSize(2.5F, 1.5F);
		this.setAttackDamage(8f);
		this.setDropStack(ItemRegistry.yellowFlippers, 1);
		this.setCustomNameTag("Elder Hammerhead");
		this.setSwimSpeeds(1.1f, 2.2f, 1.5f, 3f, 5f);
		this.setMaxHealth(20);
		this.setFishable(false);
		this.setExpRate(20);
		this.setTexture("hammerhead4");
		if(!world.isRemote) {
			this.bossInfo.setName(new TextComponentString("Elder Hammerhead"));
		}
		this.hasSetBoss = true;	
	}

	public EntityShark setBoss() {
		this.getDataManager().set(IS_BOSS, true);
		return this;
	}

	public boolean isBoss() {
		return this.getDataManager().get(IS_BOSS);
	}

	@Override
	public String[] getTexturePool() {
		return new String[] { "hammerhead1", "hammerhead2", "hammerhead3" };
	}

	@Override
	public Class[] getPreyClasses() {
		return new Class[] { EntityTropicalFish.class, EntityPiranha.class, EntityMarlin.class, EntityPlayer.class,
				EntitySeahorse.class, EntityLand.class };
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound n) {
		n.setBoolean("isBoss", isBoss());
		super.writeEntityToNBT(n);
	}

	@Override
	public void setDead() {
		clearBossTargets();
		this.bossInfo.setVisible(false);
		super.setDead();
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		if(this.bossInfo.getPlayers().contains(player))
			this.bossInfo.removePlayer(player);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound n) {
		if (n.getBoolean("isBoss")) {
			setBoss();
		}
		super.readEntityFromNBT(n);
	}

	@Override
	public boolean canDespawn() {
		return isBoss() ? false : super.canDespawn();
	}
}