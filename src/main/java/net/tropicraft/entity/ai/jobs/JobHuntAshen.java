package net.tropicraft.entity.ai.jobs;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;
import CoroUtil.entity.EnumJobState;
import CoroUtil.pathfinding.PFQueue;

public class JobHuntAshen extends JobBase {
	
	public long huntRange = 24;
	public long keepDistantRange = 14;
	
	public boolean xRay = false;
	
	public boolean useMelee = false;
	public int useMeleeCountdown = 0;
	public int useMeleeCountdownMax = 80;
	
	public Vec3 targetLastPos = null;
	public int targetNoMoveTicks = 0;
	public int targetNoMoveTicksMax = 4;
	public int panicTicks = 0;
	
	public JobHuntAshen(JobManager jm) {
		super(jm);
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public boolean shouldContinue() {
		return ai.entityToAttack == null || ai.entityToAttack.getDistanceToEntity(ent) > huntRange;
	}

	@Override
	public void onLowHealth() {
		if (ai.lastFleeEnt == null) return;
		super.onLowHealth();
		//if (this.name.equals("Makani")) {
		
		//}
		//System.out.println("hitAndRunDelay: " + hitAndRunDelay);
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
				//ent.attackEntity(ent.entityToAttack, ent.getDistanceToEntity(ent.entityToAttack));
				//System.out.println("H&R " + ent.name + " health: " + ent.getHealth());
			}
		} else {
			//ai.entityToAttack = null;
		}
	}
	
	@Override
	public boolean shouldTickCloseCombat() {
		if (!useMelee) {
			return false;
		} else {
			return super.shouldTickCloseCombat();
		}
	}
	
	@Override
	public boolean hookHit(DamageSource ds, int damage) {
		/*if (isEnemy(ds.getEntity())) {
			ai.entityToAttack = ds.getEntity();
		}*/
		
		//make him lose mask
		if (ent.getDataWatcher().getWatchableObjectInt(16) != -1) {
			panicTicks = 40;
			LostMask mask = new LostMask(ent.worldObj, ent.getDataWatcher().getWatchableObjectInt(16),  ent.posX, ent.posY + 1, ent.posZ, ent.rotationYaw);
			ent.getDataWatcher().updateObject(16, -1);
			ent.worldObj.spawnEntityInWorld(mask);
		}
		
		
		return true;
	}
	
	@Override
	public void setJobItems() {
		
		//c_CoroAIUtil.setItems_JobHunt(ai.entInv);
		
		
	}
	
	@Override
	public boolean checkDangers() {
		boolean returnVal = false;
		if (ent.getDataWatcher().getWatchableObjectInt(16) == -1) {
			ai.entityToAttack = null;
			ent.getDataWatcher().updateObject(17, 1);
			returnVal = true;
		} else if (ai.entityToAttack != null) {
			ent.getDataWatcher().updateObject(17, 2);	
		} else {
			ent.getDataWatcher().updateObject(17, 0);
		}
		if (returnVal == false && !useMelee && ai.entityToAttack != null && ai.entityToAttack.getDistanceToEntity(ai.ent) < keepDistantRange - 2) {
			//System.out.println("too close!");
			returnVal = true; 
		}
		if (!returnVal) {
			return checkHealth();
		} else {
			return true;
		}
		
	}
	
	@Override
	public boolean avoid(boolean actOnTrue) {
		int range = 25;
		boolean seesMask = false;
		LostMask clEnt = null;
		float closest = 9999F;
		
		if (panicTicks > 0) panicTicks--;
		
		if (ent.worldObj.getWorldTime() % 5 == 0) {
			if (panicTicks <= 0) {
			
				List list = ent.worldObj.getEntitiesWithinAABB(LostMask.class, ent.boundingBox.expand(range, range/2, range));
		        for(int j = 0; j < list.size(); j++)
		        {
		        	LostMask entity1 = (LostMask)list.get(j);
		            if(!entity1.isDead) {
		        		seesMask = true;
		        		float dist = ent.getDistanceToEntity(entity1);
		    			if (dist < closest) {
		    				closest = dist;
		    				clEnt = entity1;
		    			}
		            }
		        }
			}
	        
	        if (seesMask) {
	        	if (closest < 2D) {
	        		ent.getDataWatcher().updateObject(16, clEnt.getDataWatcher().getWatchableObjectInt(17));
	        		clEnt.setDead();
	        	} else {
	        		if (ent.worldObj.getWorldTime() % 10 == 0) PFQueue.getPath(ent, clEnt, range+16F);
	        	}
	        	return true;
	        } else { 
	        	return super.avoid(actOnTrue);
	        }
		}
		//let it still be in avoid mode, but doing mostly nothing except ticking onLowHealth
		return true;
	}
	
	@Override
	public void tick() {
		super.tick();
		jobHunter();
	}
	
	protected void jobHunter() {
	
		dontStrayFromHome = false;
		ai.maxDistanceFromHome = 48F;
		
		if (ai.entityToAttack != null && targetLastPos != null) {
			if (ent.worldObj.getWorldTime() % 10 == 0) {
				//System.out.println(ai.entityToAttack.getDistance(targetLastPos.xCoord, targetLastPos.yCoord, targetLastPos.zCoord));
				if (ai.entityToAttack.getDistance(targetLastPos.xCoord, targetLastPos.yCoord, targetLastPos.zCoord) < 0.5D) {
					targetNoMoveTicks++;
				} else {
					targetNoMoveTicks = 0;
				}
			}
			
			if (targetNoMoveTicks >= targetNoMoveTicksMax) {
				useMeleeCountdown = useMeleeCountdownMax;
				//System.out.println("attack! " + targetNoMoveTicks + " - " + useMelee);
			}
		} else {
			useMeleeCountdown = 0;
		}
		
		if (useMeleeCountdown > 0) {
			useMeleeCountdown--;
			useMelee = true;
		} else {
			useMelee = false;
		}
		
		setJobState(EnumJobState.IDLE);
		
		if (ent.getHealth() > ent.getHealth() * 0.90F && (ai.entityToAttack == null || ai.rand.nextInt(20) == 0)) {
			boolean found = false;
			Entity clEnt = null;
			float closest = 9999F;
	    	List list = ent.worldObj.getEntitiesWithinAABBExcludingEntity(ent, ent.boundingBox.expand(huntRange, huntRange/2, huntRange));
	        for(int j = 0; j < list.size(); j++)
	        {
	            Entity entity1 = (Entity)list.get(j);
	            if(isEnemy(entity1))
	            {
	            	if (xRay || ((EntityLivingBase) entity1).canEntityBeSeen(ent)) {
	            		if (sanityCheck(entity1)/* && entity1 instanceof EntityPlayer*/) {
	            			float dist = ent.getDistanceToEntity(entity1);
	            			if (dist < closest) {
	            				closest = dist;
	            				clEnt = entity1;
	            			}
		            		
		            		//found = true;
		            		//break;
	            		}
	            		//this.hasAttacked = true;
	            		//getPathOrWalkableBlock(entity1, 16F);
	            	}
	            }
	        }
	        if (clEnt != null) {
	        	if (ai.entityToAttack != clEnt) {
	        		ai.setTarget(clEnt);
	        	} else {
	        		//if (ent.getNavigator().noPath()) {
	        			ai.setTarget(clEnt);
	        		//}
	        	}
	        	
	        }
	        /*if (!found) {
	        	setState(EnumKoaActivity.IDLE);
	        }*/
		} else {
			
			if (ai.entityToAttack != null) {
				if (!useMelee) {
					if (ai.entityToAttack.getDistanceToEntity(ent) < keepDistantRange) {
						ent.getNavigator().clearPathEntity();
					}
				}
				if (ent.getNavigator().noPath() && (ent.getDistanceToEntity(ai.entityToAttack) > keepDistantRange + 1 || useMelee)) {
					PFQueue.getPath(ent, ai.entityToAttack, ai.maxPFRange);
				} else if (!useMelee && !ai.fleeing) {
					if (ai.entityToAttack.getDistanceToEntity(ent) < keepDistantRange) {
						ent.getNavigator().clearPathEntity();
					}
				}
			}
			
		}
		if (ent.worldObj.getWorldTime() % 10 == 0) {
			if (ai.entityToAttack != null) {
				targetLastPos = Vec3.createVectorHelper(ai.entityToAttack.posX, ai.entityToAttack.posY, ai.entityToAttack.posZ);
			}
		}
		ent.prevHealth = ent.getHealth();
	}
	
	public boolean sanityCheckHelp(Entity caller, Entity target) {
		if (ent.getHealth() < 10) {
			return false;
		}
		
		if (dontStrayFromHome) {
			if (target.getDistance(ai.homeX, ai.homeY, ai.homeZ) > ai.maxDistanceFromHome * 1.5) {
				return false;
			}
		}
		if (ai.rand.nextInt(2) == 0) {
			return true;
		}
		return false;
	}
	
	public boolean sanityCheck(Entity target) {
		if (ent.getHealth() < 6) {
			return false;
		}
		
		if (dontStrayFromHome) {
			if (target.getDistance(ai.homeX, ai.homeY, ai.homeZ) > ai.maxDistanceFromHome) {
				return false;
			}
		}
		return true;
	}
	
	
	
}
