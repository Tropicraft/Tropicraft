package net.tropicraft.entity.ai.jobs;

import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.tropicraft.Tropicraft;
import net.tropicraft.entity.koa.EntityKoaFisher;
import net.tropicraft.entity.underdasea.EntityTropicraftWaterMob;
import net.tropicraft.registry.TCItemRegistry;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;
import CoroUtil.entity.EntityTropicalFishHook;
import CoroUtil.entity.EnumActState;
import CoroUtil.entity.EnumJobState;
import CoroUtil.util.CoroUtilEntity;
import CoroUtil.util.CoroUtilInventory;
import CoroUtil.util.CoroUtilItem;

public class JobFish extends JobBase {
	
	public float maxCastStr = 1; 
	
	public int fishingTimeout;
	
	public int dryCastX;
	public int dryCastY;
	public int dryCastZ;
	
	public int pfRangeHome = 128;
	public float castingStrength = 1F;
	
	public JobFish(JobManager jm) {
		super(jm);
	}
	
	@Override
	public boolean shouldExecute() {
		return ai.entityToAttack == null;
	}
	
	@Override
	public boolean shouldContinue() {
		return state == EnumJobState.IDLE;
	}
	
	@Override
	public void onIdleTick() {
		super.onIdleTick();
	}
	
	@Override
	public void setJobState(EnumJobState ekos) {
		super.setJobState(ekos);
		Tropicraft.dbg(ekos);
	}
	
	@Override
	public void onLowHealth() {
		super.onLowHealth();
		if (hitAndRunDelay == 0 && ent.getDistanceToEntity(ai.lastFleeEnt) > 3F) {
			hitAndRunDelay = entInt.getCooldownRanged()+1;
			ai.entityToAttack = ai.lastFleeEnt;
			if (ai.entityToAttack != null) {
				ent.faceEntity(ai.entityToAttack, 180F, 180F);
				if (ai.useInv) {
					ai.entInv.performRightClick();
    			} else {
    				entInt.attackRanged(ai.entityToAttack, ent.getDistanceToEntity(ai.lastFleeEnt));
    			}
			}
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		
		//Tropicraft.dbg("fisher state: " + state);
		
		if (ai.entInv.inventory == null) return;
		
		if (ai.entInv != null && getFishEntity() != null) {
			if (state == EnumJobState.IDLE || getFishEntity().isDead) {
				getFishEntity().setDead();
				getFishEntity().catchFish();
				setFishEntity(null);
			}
		}
		
		Random rand = new Random();
		
		ai.maxDistanceFromHome = 16F;
		if (state == EnumJobState.IDLE) {
			if (findWater()) {
				setJobState(EnumJobState.W1);
			} else {
				if (rand.nextInt(150) == 0 && ent.getNavigator().noPath()) {
					ai.updateWanderPath();
				}
			}
		//walking to source
		} else if (state == EnumJobState.W1) {
			if (ai.targY < 0) { 
				setJobState(EnumJobState.IDLE);
				return;
			}
			if (!ent.isInWater()) {
				//System.out.println(ent.entityId + " - " + state + " - pathing bug debug, has path: " + !ent.getNavigator().noPath());
				if (walkingTimeout <= 0 || ent.getNavigator().noPath()) {
					float tdist = (float)ent.getDistance((int)ai.targX, (int)ai.targY, (int)ai.targZ);
					/*if (ent.name.startsWith("Akamu")) {
						int ee = 1;
					}*/
					//System.out.println("find water walk to");
					//walkingTimeout = 100; //weird fix to prevent brutce force
					//System.out.println(ai.targX + " - " + ai.targY + " - " + ai.targZ);
					ai.walkTo(ent, (int)ai.targX, (int)ai.targY/*-1*/, (int)ai.targZ, pfRangeHome, 600);
				}
			} else {
				if (findLandClose()) {
					setJobState(EnumJobState.W4);
				}
			}
			
			double distStart = 0.5D;
			double distEnd = 1D;
			double distStep = 0.5D;
			double lookStartStop = 0;
			double lookStep = 30D;
			
			boolean test = isFacingWater(distStart, distEnd, distStep, lookStartStop, lookStep);
	
			//System.out.println("isFacingWater: " + test);
			
			if (ent.getDistance(ai.targX, ai.targY, ai.targZ) < 8F || ent.isInWater() || test) {
				//Aim at location
				//ent.rotationPitch -= 35;
				if (CoroUtilEntity.canCoordBeSeenFromFeet(ent, (int)ai.targX, (int)ai.targY, (int)ai.targZ) || test) {
					
					ai.setState(EnumActState.IDLE);
					setJobState(EnumJobState.W2);
					ent.getNavigator().clearPathEntity();
					castLine();
				} else {
					setJobState(EnumJobState.IDLE);
				}
			}
		//Waiting on fish
		} else if (state == EnumJobState.W2) {
			//moveSpeed = 0F;
			if (!ent.isInWater()) {
				ent.getNavigator().clearPathEntity();
				ai.faceCoord((int)ai.targX, (int)ai.targY, (int)ai.targZ, 90, 90);
			} else {
				//trying reset if in water
				if (findLandClose()) {
					if (getFishEntity() != null) catchFish();
					setJobState(EnumJobState.W4);
					return;
				}
			}
			
			if (getFishEntity() != null && getFishEntity().bobber instanceof EntityTropicraftWaterMob) {
				EntityTropicraftWaterMob fish = (EntityTropicraftWaterMob)getFishEntity().bobber;
				
				if (fish.outOfWaterTick > 10) {
					
					ai.entInv.setSlotActive(ai.entInv.slot_Melee);
					ai.entInv.performLeftClick(fish, 0);
					if (fish.getHealth() < 0) {
						catchFish();
					}
				}
			}
			
			if (getFishEntity() == null || fishingTimeout <= 0 || (getFishEntity() != null && (getFishEntity().inGround || (getFishEntity().ticksCatchable > 0 && getFishEntity().ticksCatchable < 10)))) {
				
				System.out.println("fish entity: " + getFishEntity());
				
				if (getFishEntity() != null) {
					catchFish();
				}
				if (getFishCount() > 4 || (rand.nextInt(1) == 0 && getFishCount() >= 2)) {
					//return to base!
					ai.walkTo(ent, ai.homeX, ai.homeY, ai.homeZ, pfRangeHome, 600);
					setJobState(EnumJobState.W3);
				} else {
					if (rand.nextInt(2) == 0) {
						//sets back to find water, maybe new location
						setJobState(EnumJobState.IDLE);
					} else {
						//sets back to walking to water, forces an instant recast of lure - CHANGED!
						//setJobState(EnumJobState.W1);
						castLine();
					}
				}
				
				ai.setState(EnumActState.IDLE);
				//ent.moveSpeed = 0.7F;
			} else {
				fishingTimeout--;
			}
			
		//Return to base
		} else if (state == EnumJobState.W3) {
			if (getFishEntity() != null) {
				catchFish();
			}
			if (walkingTimeout <= 0 || (ent.getNavigator().noPath() && ent.worldObj.getWorldTime() % 20 == 0)) {
				//System.out.println("fisher: trying to path home: " + ai.homeX + ", " + ai.homeY + ", " + ai.homeZ);
				ai.walkTo(ent, ai.homeX, ai.homeY, ai.homeZ, pfRangeHome, 600);
			}
			if (ent.getDistance(ai.homeX, ai.homeY, ai.homeZ) < 2F) {
				//drop off fish in nearby tile entity chest, assumably where homeXYZ is
				ai.faceCoord((int)(ai.homeX-0.5F), (int)ai.homeY, (int)(ai.homeZ-0.5F), 180, 180);
				transferJobItems(ai.homeX, ai.homeY, ai.homeZ);
				//set to idle, which will go back to fishing mode
				setJobState(EnumJobState.IDLE);
			}
		//Get back to dry cast spot and cast
		} else if (state == EnumJobState.W4) {
			
			if (ent.getDistance(dryCastX, ent.posY, dryCastZ) < 5F || ent.onGround) {
				dryCastX = (int)Math.floor(ent.posX+0.5);
				dryCastY = (int)ent.boundingBox.minY;
				dryCastZ = (int)Math.floor(ent.posZ+0.5);
				ai.setState(EnumActState.IDLE);
				ent.getNavigator().clearPathEntity();
				castLine();
				setJobState(EnumJobState.W2);
				return;
			}
			
			if (walkingTimeout <= 0 || (ent.getNavigator().noPath() && ent.worldObj.getWorldTime() % 20 == 0)) {
				if (ent.getDistance(dryCastX, dryCastY, dryCastZ) < 64F) {
					ai.walkTo(ent, dryCastX, dryCastY, dryCastZ, ai.maxPFRange, 600);
				} else {
					ai.walkTo(ent, ai.targX, ai.targY, ai.targZ, ai.maxPFRange, 600);
				}
				
			} else {
				
			}
		}
		
		//System.out.println("AIDBG: " + ent.entityId + " - " + state);
	}
	
	//bug occured where fish hook had different entity as angler, resulting in hook failing to set fishEntity null on correct angler
	public void catchFish() {
		if (getFishEntity() == null) return;
		getFishEntity().setDead();
		getFishEntity().catchFish();
		setFishEntity(null);
	}
	
	@Override
	public boolean hookHit(DamageSource ds, int damage) {
		if (getFishEntity() != null) {
			catchFish();
			ai.setState(EnumActState.IDLE);
		}
		return super.hookHit(ds, damage);
	}
	
	protected boolean findLandClose() {
		if (findLand()) {
			catchFish();
			dryCastX = ai.targX;
			dryCastY = ai.targY;
			dryCastZ = ai.targZ;
			return true;
		}
		/*if (ent.getDistance(dryCastX, dryCastY, dryCastZ) < 16F && dryCastX != (int)Math.floor(ent.posX) && dryCastZ != (int)Math.floor(ent.posZ)) {
			//targX = dryCastX;
			//targY = dryCastY;
			//targZ = dryCastZ;
			ai.walkTo(ent, dryCastX, dryCastY, dryCastZ, 32F, 100);
			setJobState(EnumJobState.W4);
			return true;
		} else if (findLand()) {
			return true;
		}*/
		return false;
	}
	
	protected void castLine() {
		if (!ent.isInWater()) {
			dryCastX = (int)Math.floor(ent.posX+0.5);
			dryCastY = (int)ent.boundingBox.minY;
			dryCastZ = (int)Math.floor(ent.posZ+0.5);
		}
		double dist = ent.getDistance((int)ai.targX, (int)ai.targY, (int)ai.targZ);
		
		ai.faceCoord((int)ai.targX, (int)ai.targY, (int)ai.targZ, 180, 180);
		//ent.faceCoord((int)homeX, (int)homeY, (int)homeZ, 180, 180);
		castingStrength = (float)dist/17F;
		if (castingStrength < 0.25) castingStrength = 0.25F;
		if (castingStrength > maxCastStr) {
			castingStrength = maxCastStr;
		}
		ent.rotationPitch -= 25F;//dist*1.5;
		
		//System.out.println(castingStrength);
		
		//Select fishing rod
		ai.entInv.setSlotActive(ai.entInv.slot_Tool);
		//c_CoroAIUtil.equipFishingRod(ent);
		
		//Shoot lure
		fishingTimeout = 400;
		if (getFishEntity() != null) {
			getFishEntity().catchFish();
		}
		if (getFishEntity() != null) {
			getFishEntity().setDead();
		}
		setFishEntity(null);
		ai.entInv.performRightClick();
	}
	
	public EntityTropicalFishHook getFishEntity() {
		return ai.entInv.fishEntity;
		/*if (ai.ent instanceof EntityKoaFisher) {
			return ai.fishEntity;
		} else {
			System.out.println("JobFish used by unsupported entity type, not getting fish entity");
		}
		return null;*/
	}
	
	public void setFishEntity(EntityTropicalFishHook parEnt) {
		ai.entInv.fishEntity = parEnt;
		/*if (ai.ent instanceof EntityKoaFisher) {
			ai.fishEntity = parEnt;
		} else {
			System.out.println("JobFish used by unsupported entity type, not setting fish entity");
		}*/
	}
	
	protected int getFishCount() {
		//Add other fish here as there are more to actually get
		int count = 0;
		count += CoroUtilInventory.getItemCount(ai.entInv.inventory, Item.itemRegistry.getNameForObject(Items.fish));
		count += CoroUtilInventory.getItemCount(ai.entInv.inventory, Item.itemRegistry.getNameForObject(TCItemRegistry.freshMarlin));
		return count;
	}
	
	protected boolean isFish(Item id) {
		//Add other fish here as there are more to actually get
		return id == Items.fish || id == TCItemRegistry.freshMarlin;
	}
	
	protected void transferJobItems(int x, int y, int z) {
		
		if (CoroUtilInventory.isChest(ent.worldObj.getBlock(x, y-1, z))) {
			y--;
		}
		boolean transferred = false;
		if (CoroUtilInventory.chestTryTransfer(ent.worldObj, ai.entInt, x, y, z)) {
			
		} else {
			tossItems();
		}
	}
	
	public void tossItems() {
		for(int j = 0; j < ai.entInv.inventory.invList.length; j++)
        {
            if(ai.entInv.inventory.invList[j] != null && isFish(ai.entInv.inventory.invList[j].getItem()))
            {
            	CoroUtilItem.tossItemFromEntity(ent, ai.entInv.inventory.invList[j], false, false);
            	//ai.entInv.fakePlayer.dropPlayerItemWithRandomChoice(ai.entInv.inventory.decrStackSize(j, ai.entInv.inventory.invList[j].stackSize), false);
            }
        }
	}

}
