package net.tropicraft.block.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.tropicraft.entity.koa.EntityKoaBase;
import net.tropicraft.entity.koa.EntityKoaFisher;
import net.tropicraft.entity.koa.EntityKoaHunter;
import net.tropicraft.registry.TCBlockRegistry;
import CoroUtil.OldUtil;
import CoroUtil.componentAI.ICoroAI;

public class TileEntityKoaChest extends TileEntityChest
    implements IInventory
{
	
	//private String mobID = "Koa Man";
    
    private boolean unbreakable = false;
	private int delay;
	
	public int housePop = 2;
	public int housePop_hunters = 1;
	public int housePop_fishers = 1;
	
	public int[] entIDs;
	public EntityKoaBase[] entRefs;
	private int hunters;
	private int fishers;
	private boolean needListUpdate = false;

    public TileEntityKoaChest()
    {
        super();
        entIDs = new int[housePop];
        entRefs = new EntityKoaBase[housePop];
        for (int i = 0; i < entIDs.length; i++) {
        	entIDs[i] = -1;
        	entRefs[i] = null;
        }
        delay = 80; //DONT CHANGE FROM 80!!!
        
    }
    
    public void spawnKoa(World world) {
    	

    	updateList();
        int koaCount = getHomeKoaCount();
        
        int spawnCount = housePop - koaCount;
        
        //System.out.println("KOA CHEST TRY SPAWN " + spawnCount);
        
        int spawnTry = 0;
        
        //for (int var11 = 0; var11 < spawnCount; ++var11)
        while ((hunters < housePop_hunters || fishers < housePop_fishers) && spawnTry < 40)
        {
        	spawnTry++;
        	EntityKoaBase var2;

        	if (hunters < housePop_hunters) {
        		hunters++;
        		var2 = new EntityKoaHunter(this.worldObj);
        		//var2.initJobAndStates(EnumJob.HUNTER, true);
        		//System.out.println("spawning koa hunter");
        	} else {
        		fishers++;
        		var2 = new EntityKoaFisher(this.worldObj);
        		//var2.initJobAndStates(EnumJob.FISHERMAN, true);
        		//System.out.println("spawning koa fisherman");
        	}
        	
	        if (var2 != null)
	        {
	            double var4 = (double)this.xCoord + 0.5D;// + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * 1.0D;
	            double var6 = (double)(this.yCoord + 1);
	            double var8 = (double)this.zCoord + 0.5D;// + (this.worldObj.rand.nextDouble() - this.worldObj.rand.nextDouble()) * 1.0D;
	            EntityLiving var10 = var2 instanceof EntityLiving ? (EntityLiving)var2 : null;
	            var2.setLocationAndAngles(var4, var6, var8, this.worldObj.rand.nextFloat() * 360.0F, 0.0F);
	
	            //if (getCanSpawnHere(var2))
	            //{
	                //this.writeNBTTagsTo(var2);
	            	var2.getAIAgent().homeX = this.xCoord;
	            	var2.getAIAgent().homeY = this.yCoord;
	            	var2.getAIAgent().homeZ = this.zCoord;
	            	
	            	addToList(var2);
	            	
	                this.worldObj.spawnEntityInWorld(var2);
	                var2.getAIAgent().spawnedOrNBTReloadedInit();
	                //this.worldObj.playAuxSFX(2004, this.xCoord, this.yCoord, this.zCoord, 0);
	
	                if (var10 != null)
	                {
	                    var10.spawnExplosionParticle();
	                }
	
	                //this.updateDelay();
	            /*} else {
	            	System.out.println("failed to place spawn ent for koa chest");
	            }*/
	        } else { return; }
        }
    }
    
    public void addToList(EntityKoaBase ent) {
    	int j;
    	for (j = 0; j < entIDs.length; j++) {
    		if (entIDs[j] == -1) {
    			entIDs[j] = ent.getAIAgent().entID;
    			//System.out.println("Adding: " + ent.entID);
    			entRefs[j] = ent;
    			break;
    		} else {
    			//System.out.println("WTF?: " + entIDs[j]);
    			
    		}
    	}
    }
    
    public void updateList() {
    	int j;
    	for (j = 0; j < entIDs.length; j++) {
    		Entity ent = OldUtil.getEntByPersistantID(worldObj, entIDs[j]);
    		if (ent == null) {
    			entIDs[j] = -1;
        		entRefs[j] = null;
    		} else if (ent.isDead) {
    			entIDs[j] = -1;
        		entRefs[j] = null;
    		}
    	}
    }
    
    public int getHomeKoaCount() {
    	float dist = 160;
        
        List<ICoroAI> ents = this.worldObj.getEntitiesWithinAABB(ICoroAI.class, AxisAlignedBB.getAABBPool().getAABB((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand(dist, dist/2, dist));
        
        hunters = 0;
        fishers = 0;
        
        int existing = 0;
        
        for (int i = 0; i < ents.size(); i++) {
        	
        	int j = 0;
        	for (j = 0; j < entIDs.length; j++) {
	        	//entIDs[j] = nbttagcompound.getInteger("entID_" + i);
	        	Entity ent = (Entity)ents.get(i);//TropicraftMod.proxy.getEntByID(entIDs[j]);
	        	if (ent instanceof EntityKoaBase && ent != null && entIDs[j] == ((EntityKoaBase)ent).getAIAgent().entID && ((EntityKoaBase)ent).getAIAgent().entID != -1) {
	        		existing++;
	        		//System.out.println("derp: " + existing);
	        		if (ent instanceof EntityKoaHunter) {
	        			hunters++;
	        		}
	        		if (ent instanceof EntityKoaFisher) {
	            		fishers++;
	            	} else {
	            		int dsdf = 0;
	            	}
	        		//entRefs[i] = (EntityKoaManly)ent;
	        		//do nothing, occupied
	            	break;
	        	} else {
	        		
	        	}
	        }
        	
        	if (j < entIDs.length) {
        		//anything to do? weve got count already
        	}
        	
        	
        }
        
        return existing;
        
    }
    
    public boolean getCanSpawnHere(Entity ent)
    {
    	boolean b1 = !this.worldObj.checkBlockCollision(ent.boundingBox);
    	boolean b2 = ent.worldObj.getCollidingBoundingBoxes(ent, ent.boundingBox).isEmpty();
    	boolean b3 = !ent.worldObj.isAnyLiquid(ent.boundingBox);
        return b1 && /*b2 && */b3;
    }
    
    public boolean anyPlayerInRange()
    {
        return this.worldObj.getClosestPlayer((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D, 80.0D) != null;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
    	
    	super.updateEntity();
    	
        if (!this.anyPlayerInRange()) return;
        	
    
	    if (this.delay == 0)
	    {
	    	this.delay = 20 * 60 * 5;
	    	
	    	//this.delay = 100;
	    	
	    	if (!worldObj.isRemote && !this.needListUpdate) spawnKoa(worldObj);
	    	
	    }
	
	    if (this.delay > 0)
	    {
	    	
	    	if (this.needListUpdate && this.delay < 20) {
	    		for (int i = 0; i < entIDs.length; i++) {
		        	//entIDs[i] = nbttagcompound.getInteger("entID_" + i);
		        	//World worldRef = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(TropicraftMod.tropicsDimensionID); //bad coro
	    			Entity ent = OldUtil.getEntByPersistantID(worldObj, entIDs[i]);
		        	if (ent instanceof EntityKoaBase) {
		        		entRefs[i] = (EntityKoaBase)ent;
		        	} else {
		        		entIDs[i] = -1;
		        		entRefs[i] = null;
		        	}
		        	//System.out.println("read in " + entIDs[i]);
		        }
	    		needListUpdate = false;
	    	}
	    	
	        --this.delay;
	        return;
	    }
	
	    
    }

    public String getInvName()
    {
        return "Koa chest";
    }

    public void checkForAdjacentChests()
    {
        if (adjacentChestChecked)
        {
            return;
        }
        adjacentChestChecked = true;
        adjacentChestZNeg = null;
        adjacentChestXPos = null;
        adjacentChestXNeg = null;
        adjacentChestZPos = null;
        if (worldObj.getBlock(xCoord - 1, yCoord, zCoord) == TCBlockRegistry.koaChest)
        {
            adjacentChestXNeg = (TileEntityKoaChest)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
        }
        if (worldObj.getBlock(xCoord + 1, yCoord, zCoord) == TCBlockRegistry.koaChest)
        {
            adjacentChestXPos = (TileEntityKoaChest)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
        }
        if (worldObj.getBlock(xCoord, yCoord, zCoord - 1) == TCBlockRegistry.koaChest)
        {
            adjacentChestZNeg = (TileEntityKoaChest)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
        }
        if (worldObj.getBlock(xCoord, yCoord, zCoord + 1) == TCBlockRegistry.koaChest)
        {
            adjacentChestZPos = (TileEntityKoaChest)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
        }
        if (adjacentChestZNeg != null)
        {
            adjacentChestZNeg.updateContainingBlockInfo();
        }
        if (adjacentChestZPos != null)
        {
            adjacentChestZPos.updateContainingBlockInfo();
        }
        if (adjacentChestXPos != null)
        {
            adjacentChestXPos.updateContainingBlockInfo();
        }
        if (adjacentChestXNeg != null)
        {
            adjacentChestXNeg.updateContainingBlockInfo();
        }
    }
    
    public boolean isUnbreakable() {
        return unbreakable;
    }
    
    public void setIsUnbreakable(boolean flag) {
        unbreakable = flag;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        unbreakable = nbttagcompound.getBoolean("unbreakable");
        
        //if (true) return;
        
        try {
	        for (int i = 0; i < entIDs.length; i++) {
	        	entIDs[i] = nbttagcompound.getInteger("entID_" + i);
	        	/*World worldRef = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(127); //bad coro
	        	Entity ent = TropicraftMod.proxy.getEntByPersistantID(worldRef, entIDs[i]);
	        	if (ent instanceof EntityKoaMemberNew) {
	        		entRefs[i] = (EntityKoaMemberNew)ent;
	        	} else {
	        		entIDs[i] = -1;
	        		entRefs[i] = null;
	        	}
	        	
	        	System.out.println("read in " + entIDs[i]);*/
	        }
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        needListUpdate  = true;
        //entIDs[]
        
        //String str = nbttagcompound.getString("entIDs");
        
        //String hmm = "121212;121212;121212;121212";
        /*String vals[] = str.split(";"); 
        
        
        for (int i = 0; i < entIDs.length; i++) {
        	entIDs[i] = Integer.valueOf(vals[i]);
        }*/
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("unbreakable", unbreakable);
        
        try {
	        for (int i = 0; i < entIDs.length; i++) {
	        	nbttagcompound.setInteger("entID_" + i, entIDs[i]);// = nbttagcompound.getInteger();
	        	//System.out.println("write out " + entIDs[i]);
	        	/*Entity ent = TropicraftMod.proxy.getEntByID(entIDs[i]);
	        	if (ent instanceof EntityKoaMemberNew) {
	        		entRefs[i] = (EntityKoaMemberNew)ent;
	        	} else {
	        		entIDs[i] = -1;
	        		entRefs[i] = null;
	        	}*/
	        }
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }
    
    /*@Override
    public void openInventory()
    {
        ++this.numUsingPlayers;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, TropicraftBlocks.koaChest.blockID, 1, this.numUsingPlayers);
    }

    @Override
    public void closeInventory()
    {
        --this.numUsingPlayers;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, TropicraftBlocks.koaChest.blockID, 1, this.numUsingPlayers);
    }*/
    
    @Override
    public Block getBlockType() {
    	return TCBlockRegistry.koaChest;
    }
}
