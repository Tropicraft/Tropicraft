package build;

import build.world.Build;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class ItemEditTool extends Item {
    
    private boolean tmp;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	
	public int mode = 0;
	
	public int subMode = 0;
	
	public int x1 = 0;
	public int y1 = 0;
	public int z1 = 0;
	public int x2 = 0;
	public int y2 = 0;
	public int z2 = 0;
	
	Build buildData;

    public ItemEditTool(int parMode)
    {
        super();
        this.maxStackSize = 1;
        mode = parMode;
        xCoord = 0;
		yCoord = 0;
		zCoord = 0;
		tmp = true;
		
		buildData = new Build(0, 0, 0, "build");
    }
    
    public IIcon getIconFromDamage(int par1) {
    	return Items.fishing_rod.getIconFromDamage(0);
    }
    
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	
    	if (par2World.isRemote) return par1ItemStack;
    	
    	int i = (int)par3EntityPlayer.posX;
    	int j = (int)par3EntityPlayer.posY-1;
    	int k = (int)par3EntityPlayer.posZ;
    	
    	if (mode == 0) {
	    	
    	} else if (mode == 1) {
    		if (subMode == 0) {
    			x1 = i; y1 = j; z1 = k;
    			System.out.println("minCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			//ZCGame.instance.setModeMessage("minCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			subMode++;
    		} else {
    			x2 = i; y2 = j; z2 = k;
    			System.out.println("maxCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			//ZCGame.instance.setModeMessage("maxCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			buildData.resetData();
    			buildData.recalculateLevelSize(x1, y1, z1, x2, y2, z2);
    			buildData.writeNBT();
    			subMode = 0;
    		}
    	} else if (mode == 2) {
    		//ZCGame.instance.setPlayerSpawn(par3EntityPlayer, i, j+1, k);
    	}
        return par1ItemStack;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int i, int j, int k, int par7, float f1, float f2, float f3)
    {
    	
    	if (true) return false;
    	
    	if (par3World.isRemote) return false;
    	
    	if (mode == 0) {
    		
    	} else if (mode == 1) {
    		if (subMode == 0) {
    			x1 = i; y1 = j; z1 = k;
    			System.out.println("minCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			//ZCGame.instance.setModeMessage("minCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			subMode++;
    		} else {
    			x2 = i; y2 = j; z2 = k;
    			System.out.println("maxCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			//ZCGame.instance.setModeMessage("maxCoords Set -> X: "+String.valueOf(i)+" Y: "+String.valueOf(j)+" Z: "+String.valueOf(k));
    			buildData.recalculateLevelSize(x1, y1, z1, x2, y2, z2);
    			buildData.writeNBT();
    			subMode = 0;
    		}
    	} else {
    	
    		onItemRightClick(par1ItemStack, par3World, par2EntityPlayer);
    	}
    	return true;
    }
    
    public boolean wasKeyDown = false;
    
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	
    	if (par5) {
    		if (!par2World.isRemote) {
    			if (temp()) {
    				if (!wasKeyDown) {
    					//ServerTickHandler.buildMan.addBuild(new BuildJob(0, (int)par3Entity.posX, (int)par3Entity.posY, (int)par3Entity.posZ, buildData));
    				}
    				wasKeyDown = true;
    			} else {
    				wasKeyDown = false;
    			}
    		}
    	}
    	
    	/*mode = ZCGame.instance.mapMan.editToolMode;
    	if (ZCGame.instance.mapMan.editMode && par5) {
    		ZCGame.instance.showEditToolMode();
    	}*/
    }
    
    @SideOnly(Side.CLIENT)
    public boolean temp() {
    	if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
    		return true;
    	}
    	return false;
    }
}
