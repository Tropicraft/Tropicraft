package net.tropicraft.block.tileentity;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.economy.ItemValues;
import net.tropicraft.entity.ai.jobs.JobTrade;
import CoroUtil.componentAI.ICoroAI;



public class TileEntityPurchasePlate extends TileEntity
{
	
	public int itemIndex = 0;
	
	//public int cycleDelay = 60;
	//public boolean cycleItems = false;
	//public int cycleIndex = 0;
	//public int cycleCurDelay = cycleDelay;
	
	
	//Unique to tile entity rendering stuff
	public float startOffset;
	public float angle = 0F;
	
	public int watch_itemIndex = 0;
	public int watch_delay_itemIndex = 0;
	
	public int tradeState = 0; //0 = off, 1 = select, 2 = confirm
	public int watch_tradeState;
	
	public int credit;
	
	public ICoroAI tradeKoa;
	
	//For tooltip
	public EntityPlayer activeTrader = null;
    public long toolTipWaitStartTime = 0;
	public boolean showToolTip = false;
	public boolean watch_showToolTip = false;

    public TileEntityPurchasePlate()
    {
    	Random rand = new Random();
    	startOffset = (float)(Math.random() * Math.PI * 2.0D);
    	angle = rand.nextInt(360);
    	//cycleIndex = rand.nextInt(Buyables.count());
    	
    	//if (cycleItems) itemIndex = cycleIndex;
    }
    
    public void onClicked(boolean rightClick) {
    	
    	if (rightClick) {
			itemIndex++;
			tradeState = 1;
			if (itemIndex >= ItemValues.itemsBuyable.size()) {
				itemIndex = 0;
			}
    	} else {
    		if (tradeState == 1) {
    			tradeState++;
    		} else if (tradeState == 2) {
    			if (tradeKoa != null && !tradeKoa.getAIAgent().ent.isDead) {
    				//send to koa
    				JobTrade job = (JobTrade)tradeKoa.getAIAgent().jobMan.getFirstJobByClass(JobTrade.class);
    				if (job != null) job.tradeConfirmCallback();
    				
    				tradeState = 1;
    			}
    			
    		}
    	}
    	
    	sync();//MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(this.getDescriptionPacket());
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
    	//itemIndex = 4;
    	if (!this.worldObj.isRemote) {
	    	
    		int waitTime = 5 * 20;
    		int endTime = waitTime + (20 * 20);
    		
    		if (activeTrader != null && !activeTrader.getEntityData().hasKey("hasShownTradeToolTip")) {
    			
    			if (toolTipWaitStartTime == 0) {
        			toolTipWaitStartTime = worldObj.getWorldTime();
        		} else {
        			if (worldObj.getWorldTime() > toolTipWaitStartTime + waitTime) {
        				if (worldObj.getWorldTime() < toolTipWaitStartTime + endTime) {
    	    				//render
        					showToolTip = true;
        				} else {
        					showToolTip = false;
        					toolTipWaitStartTime = 0;
        					activeTrader.getEntityData().setBoolean("hasShownTradeToolTip", true);
        				}
        			}
        		}
    		} else {
    			showToolTip = false;
    			toolTipWaitStartTime = 0;
    		}
    		
	    	watchVariables();
    	}

        super.updateEntity();
        
    }
    
    public void watchVariables() {
    	
    	boolean update = false;
    	
    	if (showToolTip != watch_showToolTip) {
    		watch_showToolTip = showToolTip;
    		update = true;
    	}
    	
    	if (itemIndex != watch_itemIndex || watch_delay_itemIndex == 0 || watch_tradeState != tradeState) {
    		watch_delay_itemIndex = 40;
    		watch_tradeState = tradeState;
    		update = true;
    	}
    	
    	if (update) sync();//MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(this.getDescriptionPacket());
    	
    	if (watch_delay_itemIndex > 0) watch_delay_itemIndex--;
    	watch_itemIndex = itemIndex;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	try {
    		super.readFromNBT(par1NBTTagCompound);
    		this.itemIndex = par1NBTTagCompound.getInteger("itemIndex");
    		this.tradeState = par1NBTTagCompound.getInteger("tradeState");
    		this.credit = par1NBTTagCompound.getInteger("credit");
    		this.showToolTip = par1NBTTagCompound.getBoolean("showToolTip");
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
        //this.cycleCurDelay = par1NBTTagCompound.getInteger("cycleCurDelay");
        //cycleItems = par1NBTTagCompound.getBoolean("cycleItems");
        
        //this.delay = par1NBTTagCompound.getShort("Delay");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	try {
	        super.writeToNBT(par1NBTTagCompound);
	        par1NBTTagCompound.setInteger("itemIndex", this.itemIndex);
	        par1NBTTagCompound.setInteger("tradeState", this.tradeState);
	        par1NBTTagCompound.setInteger("credit", this.credit);
	        par1NBTTagCompound.setBoolean("showToolTip", showToolTip);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
        //par1NBTTagCompound.setInteger("cycleCurDelay", this.cycleCurDelay);
        //par1NBTTagCompound.setBoolean("cycleItems", cycleItems);
        //par1NBTTagCompound.setShort("Delay", (short)this.delay);
    }
    
    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    public void sync() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 5, nbttagcompound);
    }
}
