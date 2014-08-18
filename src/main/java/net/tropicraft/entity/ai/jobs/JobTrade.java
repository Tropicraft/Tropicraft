package net.tropicraft.entity.ai.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.terraingen.BiomeEvent.GetWaterColor;
import net.tropicraft.Tropicraft;
import net.tropicraft.block.tileentity.TileEntityPurchasePlate;
import net.tropicraft.economy.ItemEntry;
import net.tropicraft.economy.ItemValues;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCKoaCurrencyRegistry;
import CoroUtil.componentAI.jobSystem.JobBase;
import CoroUtil.componentAI.jobSystem.JobManager;
import CoroUtil.util.CoroUtilBlock;

public class JobTrade extends JobBase {

	public float tradeDistTrigger;
	
	public int tradeLastItemOffer;
	
	public ChunkCoordinates tradeBlockPos;
	//public ChunkCoordinates frameBlockPos;
	
	public TileEntityPurchasePlate tradePlate;
	
	public EntityPlayer activeTrader;
	
	public ArrayList<ItemStack> offeredItems = new ArrayList();
	
	public Block idTradeBlock;
	
	public JobTrade(JobManager jm) {
		super(jm);
	}
	
	public void convertOfferingsToCurrency(int newCredit) {
		
		offeredItems.clear();
		
		int leftToConvert = newCredit;
		
		while (leftToConvert > TCKoaCurrencyRegistry.currency.getMaxStackSize()) {
			offeredItems.add(new ItemStack(TCKoaCurrencyRegistry.currency.getItem(), TCKoaCurrencyRegistry.currency.getMaxStackSize()));
			//activeTrader.inventory.addItemStackToInventory();
			leftToConvert -= TCKoaCurrencyRegistry.currency.getMaxStackSize();
		}
		
		if (leftToConvert > 0) {
			offeredItems.add(new ItemStack(TCKoaCurrencyRegistry.currency.getItem(), leftToConvert));
		}
	}
	
	public int getOfferedItemsValue() {
		int value = 0;
		try {
			for (int i = 0; i < offeredItems.size(); i++) {
				//value += (float)ValuedItems.getItemCost(offeredItems.get(i).getItem()) / (float)ValuedItems.getBuyItemCount(offeredItems.get(i).getItem()) * offeredItems.get(i).stackSize;
				value += ItemValues.getItemEntry(offeredItems.get(i)).getTotalValue(offeredItems.get(i));//(float)ValuedItems.getItemCost(offeredItems.get(i).getItem()) / (float)ValuedItems.getBuyItemCount(offeredItems.get(i).getItem()) * offeredItems.get(i).stackSize;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return value;
	}
	
	public int returnCredit() {
		int value = 0;
		try {
			for (int i = 0; i < offeredItems.size(); i++) {
				activeTrader.inventory.addItemStackToInventory(offeredItems.get(i));
			}
			offeredItems.clear();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return value;
	}
	
	@Override
	public void tick() {
		
		//something is making trader wander off way too far, fuck whatever that code is, eat this bandaid
		if (tradeBlockPos != null && (ai.targX != tradeBlockPos.posX || ai.targY != tradeBlockPos.posY+1 || ai.targZ != tradeBlockPos.posZ) && ent.getDistance(tradeBlockPos.posX, tradeBlockPos.posY, tradeBlockPos.posZ) > 15) {
			//System.out.println("shitty fix go!");
			ent.getNavigator().clearPathEntity();
			ai.walkTo(ent, tradeBlockPos.posX, tradeBlockPos.posY+1, tradeBlockPos.posZ, ai.maxPFRange, 600);
		}
		//setJobState(EnumJobState.IDLE);
		
		//TEMP!!!!!!!!
		idTradeBlock = TCBlockRegistry.purchasePlate;
		
		
		ai.maxDistanceFromHome = 0.5F;
		tradeDistTrigger = 4F;
		
		if (tradeBlockPos == null) {
			/*if (ent.worldObj.getWorldTime() % 10 == 0) */tradeBlockPos = tickFind(idTradeBlock, 25);
			
			if (tradeBlockPos != null) {
				TileEntity tile = ent.worldObj.getTileEntity(tradeBlockPos.posX, tradeBlockPos.posY, tradeBlockPos.posZ);
				
				if (tile != null) {
					((TileEntityPurchasePlate)tile).tradeKoa = entInt;
					tradePlate = (TileEntityPurchasePlate)tile;
				}
				
				//set home as tradeplate now
				ai.homeX = tradeBlockPos.posX;
				ai.homeY = tradeBlockPos.posY;
				ai.homeZ = tradeBlockPos.posZ;
			}
		}/* else if (frameBlockPos == null) {
			
		}*/
		
		//TODO: koa item frame usage
		/*if (ent.worldObj.getTotalWorldTime() % 100 == 0) {
			List list = ent.worldObj.getEntitiesWithinAABBExcludingEntity(ent, ent.boundingBox.expand(6, 3, 6));
			
			for(int j = 0; j < list.size(); j++)
	        {
	            Entity entity1 = (Entity)list.get(j);
	            if(entity1 instanceof EntityTCItemFrame && !((EntityTCItemFrame)entity1).getShouldDropContents()) {
	            	if (((EntityTCItemFrame)entity1).getDisplayedItem() == null) {
	            		((EntityTCItemFrame)entity1).setDisplayedItem(ItemValues.itemsBuyable.get(ent.worldObj.rand.nextInt(ItemValues.itemsBuyable.size())).item);//new ItemStack(ValuedItems.getItemBuyable(ent.worldObj.rand.nextInt(ValuedItems.buyables.size()))));
	            	}
	            }
	        }
		}*/
		
		if (activeTrader == null) {
			activeTrader = ent.worldObj.getClosestPlayerToEntity(ent, tradeDistTrigger);
			if (activeTrader != null) {
				tradeStart();
			}
		} else {
			if (ent.getDistanceToEntity(activeTrader) > tradeDistTrigger * 1.5) {
				//walked away, reset trade (give back anything?)
				tradeReset();
			} else {
				tradeTick();
			}
		}
	}
	
	public void tradeStart() {
		//System.out.println("trade start");
		if (tradePlate != null) {
			tradePlate.tradeState = 1;
			tradePlate.credit = getOfferedItemsValue();
		}
	}
	
	public void tradeConfirmCallback() {
		
		int credit = getOfferedItemsValue();
		ItemEntry ie = ItemValues.itemsBuyable.get(tradePlate.itemIndex);
		int cost = ie.value;
		
		if (credit >= cost) {
			credit -= cost;
			
			convertOfferingsToCurrency(credit);
			
			ItemStack is = ie.item.copy();
			
			activeTrader.inventory.addItemStackToInventory(is);
			
		} else {
			//not enough credit!
		}
		
		tradeSuccess();
	}
	
	public void tradeSuccess() {
		//dont use reset here, reset is for when player leaves
		//System.out.println("trade success");
		if (tradePlate != null) {
			tradePlate.tradeState = 1;
			tradePlate.credit = getOfferedItemsValue();
		}
	}
	
	public void tradeReset() {
		if (offeredItems != null) returnCredit();
		activeTrader = null;
		if (tradePlate != null) {
			tradePlate.tradeState = 0;
			tradePlate.credit = 0;
			tradePlate.activeTrader = null;
		}
		Tropicraft.dbg("trade reset");
	}
	
	public void tradeTick() {
		TileEntity tEnt = null;//
		if (tradeBlockPos != null) tEnt = ent.worldObj.getTileEntity(tradeBlockPos.posX, tradeBlockPos.posY, tradeBlockPos.posZ);
		if (tradeBlockPos != null && tEnt == null) {
			tradeBlockPos = null;
		} else {
			if (tEnt instanceof TileEntityPurchasePlate) {
				((TileEntityPurchasePlate) tEnt).activeTrader = this.activeTrader;
			}
		}
	}
	
	public ChunkCoordinates tickFind(Block id, int range) {
		
		for (int i = 0; i < 30; i++) {
			int randX = (int) ent.posX+ent.worldObj.rand.nextInt(range) - (range/2);
			int randY = (int) ent.posY+ent.worldObj.rand.nextInt(range) - (range/2);
			int randZ = (int) ent.posZ+ent.worldObj.rand.nextInt(range) - (range/2);
			
			Block foundID = ent.worldObj.getBlock(randX, randY, randZ);
			
			if (foundID == id) {
				Tropicraft.dbg("found trade block");
				return new ChunkCoordinates(randX, randY, randZ);
			} else {
				//System.out.println("fail");
			}
		}
		
		return null;
	}
	
	@Override
	public boolean shouldExecute() {
		//execute if no threat
		return isAreaSecure();
	}
	
	@Override
	public boolean shouldContinue() {
		//continue if there is a threat
		return !isAreaSecure();
	}
	
	public boolean isAreaSecure() {
		if (tradeBlockPos != null) { 
			List list = ent.worldObj.getEntitiesWithinAABBExcludingEntity(ent, AxisAlignedBB.getBoundingBox(tradeBlockPos.posX, tradeBlockPos.posY, tradeBlockPos.posZ, tradeBlockPos.posX, tradeBlockPos.posY, tradeBlockPos.posZ).expand(6, 3, 6));
			
			for(int j = 0; j < list.size(); j++)
	        {
	            Entity entity1 = (Entity)list.get(j);
	            if(entInt.isEnemy(entity1)) {
	            	return false;
	            }
	        }
		}
		return true;
	}
	
	@Override
	public void onIdleTickAct() {
		
		//ent.setHealth(20);
		//System.out.println(ent.currentAction);////getFoodStats().getFoodLevel());
		//System.out.println(ent.job.getPrimaryJobClass().state);
		
		//System.out.println(ent.entityId);
		
		if (activeTrader != null) {
			ent.faceEntity(activeTrader, 15, 15);
		} else if (tradeBlockPos != null) {
			//ai.faceCoord(tradeBlockPos.posX, tradeBlockPos.posY, tradeBlockPos.posZ, 15, 15);
		}
		
		if (activeTrader == null && ent.getNavigator().noPath()) {
			
			//EntityPlayer clPl = ent.worldObj.getClosestPlayerToEntity(ent, 5F);
			
			Random rand = new Random();
			
			if (tradeBlockPos != null && ent.worldObj.rand.nextInt(100) == 0) {
				int tryX = tradeBlockPos.posX;// - 1 + rand.nextInt(2);
				int tryZ = tradeBlockPos.posZ;// - 1 + rand.nextInt(2);
				if (!CoroUtilBlock.isAir(ent.worldObj.getBlock(tryX, MathHelper.floor_double(ent.posY-1), tryZ))/* && ent.worldObj.getBlockId(tryX, MathHelper.floor_double(ent.posY+1), tryZ) == 0*/) {
					ai.walkTo(ent, tradeBlockPos.posX, MathHelper.floor_double(ent.posY), tradeBlockPos.posZ, ai.maxPFRange, 600);
				}
				
			}
			
			/*if (tradeBlockPos != null && ent.getDistance(tradeBlockPos.posX, tradeBlockPos.posY, tradeBlockPos.posZ) > 4) {
				ai.walkTo(ent, tradeBlockPos.posX - 1 + rand.nextInt(2), tradeBlockPos.posY, tradeBlockPos.posZ - 1 + rand.nextInt(2), ai.maxPFRange, 600);
			} else {
				if (ent.worldObj.rand.nextInt(100) == 0) {
					int randsize = 2;
		    		ai.walkTo(ent, ai.homeX+ent.worldObj.rand.nextInt(randsize) - (randsize/2), ai.homeY+0, ai.homeZ+ent.worldObj.rand.nextInt(randsize) - (randsize/2),ai.maxPFRange, 600);
				}
			}*/
			
			/*if (ent.getDistance(ai.homeX, ai.homeY, ai.homeZ) > ai.maxDistanceFromHome) {
	    		
	    		int randsize = 4;
	    		//ai.walkTo(ent, ai.homeX+ai.rand.nextInt(randsize) - (randsize/2), ai.homeY+0, ai.homeZ+ai.rand.nextInt(randsize) - (randsize/2),ai.maxPFRange, 600);
	    		ai.walkTo(ent, ai.homeX, ai.homeY, ai.homeZ,ai.maxPFRange, 600);
	    		
			} else {
				if (ent.worldObj.rand.nextInt(10) == 0) {
					int randsize = 2;
		    		ai.walkTo(ent, ai.homeX+ent.worldObj.rand.nextInt(randsize) - (randsize/2), ai.homeY+0, ai.homeZ+ent.worldObj.rand.nextInt(randsize) - (randsize/2),ai.maxPFRange, 600);
				}
				
			}*/
			
			
		}
	}
	
	@Override
	public boolean hookInteract(EntityPlayer par1EntityPlayer) {
		if (!ent.worldObj.isRemote) {
			
			if (offeredItems == null) offeredItems = new ArrayList();
			ItemStack is = par1EntityPlayer.getCurrentEquippedItem();
			if (is != null) {
				//System.out.println(is);
				if (ItemValues.getItemEntry(is) != null) {
					par1EntityPlayer.inventory.mainInventory[par1EntityPlayer.inventory.currentItem] = null;
					offeredItems.add(is);
					
					if (tradePlate != null) {
						tradePlate.credit = getOfferedItemsValue();
						MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(tradePlate.getDescriptionPacket());
					}
				}
				
				//System.out.println();
			}
			return true;
		}
		return super.hookInteract(par1EntityPlayer);
	}
	
}
