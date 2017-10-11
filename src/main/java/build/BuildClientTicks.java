package build;

import build.config.BuildConfig;
import build.enums.EnumBuildState;
import build.enums.EnumCopyState;
import build.render.Overlays;
import build.world.Build;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class BuildClientTicks
{
	//plan to make dedi server compatible
	//client still controls the states
	//client still makes its own clip data, BUT also sends coords etc to server so it can do the same
	//server needs:
	/// handle clipboard for each player, support for array of things per player
	/// handles commands:
	//// copy command, sends all needed data, writes to players server clipboard, clients already ready
	//// build command, sends build coords and rotation
	
	//Player based fields
	public EnumBuildState buildState = EnumBuildState.NORMAL; //client
	public EnumCopyState copyState = EnumCopyState.NORMAL; //client
	
	public Build clipboardData; //client & server
	
    public MovingObjectPosition extendedMouseOver = null;
    
    public static BuildClientTicks i = new BuildClientTicks();
    
    public int direction = 0;
    public int lookDist = 20;
    public int lastPlayerSlot = 0; //to fix scrollwheel state lock bug
    
    public BuildClientTicks() {
    	clipboardData = new Build(0, 0, 0, "clipboard", true);
    	clipboardData.newFormat = true;
    }
    
    public void worldRenderTick(float partialTicks)
    {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	
    	if (mc.renderViewEntity != null) {
    		extendedMouseOver = mc.renderViewEntity.rayTrace(lookDist, partialTicks);
    		if (extendedMouseOver == null) {
	    		Vec3 vec3 = mc.renderViewEntity.getPosition(partialTicks);
	            Vec3 vec31 = mc.renderViewEntity.getLook(partialTicks);
	            Vec3 vec32 = vec3.addVector(vec31.xCoord * lookDist, vec31.yCoord * lookDist, vec31.zCoord * lookDist);
	            
	            extendedMouseOver = new MovingObjectPosition((int)vec32.xCoord, (int)vec32.yCoord, (int)vec32.zCoord, 0, vec32);
    		}
    	}
    	
    	
    	//TEMP!
    	try {
    		/*if (clipboardData != null) {
    			Overlays.renderBuildOutline(clipboardData, direction);
    		}*/
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	
    	if (buildState == EnumBuildState.PLACE) {
    		//EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
    		//int l = MathHelper.floor_double((double)((-player.rotationYaw-45) * 4.0F / 360.0F)/* + 0.5D*/) & 3;
    		Overlays.renderBuildOutline(clipboardData, direction);
    		
    		//glitchy, doesnt rotate right sometimes
    		Overlays.renderDirectionArrow(clipboardData, direction);
    		
    		/*Vec3 pos = Vec3.createVectorHelper(clipboardData.map_coord_minX, clipboardData.map_coord_minY, clipboardData.map_coord_minZ);
    		Vec3 angle = Vec3.createVectorHelper(0, 0, 0);

    		Overlays.renderBuildPreview(clipboardData, pos, angle);*/
    	} else if (copyState == EnumCopyState.SETMAX) {
    		int x = (int)MathHelper.floor_double(mc.thePlayer.posX);
    		int y = (int)MathHelper.floor_double(mc.thePlayer.posY - mc.thePlayer.yOffset);
    		int z = (int)MathHelper.floor_double(mc.thePlayer.posZ);
    		if (extendedMouseOver != null) {
    			x = extendedMouseOver.blockX;
    			y = extendedMouseOver.blockY;
    			z = extendedMouseOver.blockZ;
    		}
    		Overlays.renderBuildOutline(sx, sy, sz, x, y, z);
    	}
    }

    public void onRenderTick()
    {
    	String mainStr = "";
    	if (buildState == EnumBuildState.PLACE) {
    		mainStr = "Paste Mode: (" + BuildConfig.key_Build + ") to paste, (" + BuildConfig.key_Rotate + ") to rotate, (" + BuildConfig.key_Copy + ") to cancel. Dir: " + direction;
    	} else if (copyState == EnumCopyState.SETMIN) {
    		mainStr = "Copy Mode: Mark start coord with (" + BuildConfig.key_Copy + "), (" + BuildConfig.key_Build + ") to cancel";
    	} else if (copyState == EnumCopyState.SETMAX) {
    		mainStr = "Copy Mode: Mark end coord with (" + BuildConfig.key_Copy + "), (" + BuildConfig.key_Build + ") to cancel";
    	}
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	ScaledResolution var8 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int width = var8.getScaledWidth();
        int height = var8.getScaledHeight();
        
        int xOffset = width/2/* - 95 + 3*/;
        int yOffset = height - 50;
        
    	int strWidth = mc.fontRenderer.getStringWidth(mainStr);
    	mc.fontRenderer.drawStringWithShadow(mainStr, xOffset - strWidth/2, yOffset, 16777215);
    	
    }

    public void onTickInGUI(GuiScreen guiscreen)
    {
        
    }
    
    public boolean wasKeyDown = false;
    
    public void onTickInGame() {
		
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	
    	if (mc.thePlayer == null || mc.theWorld == null) return;
    	
    	if (mc.theWorld != null) {
    		clipboardData.dim = mc.theWorld.provider.dimensionId;
    	}
    	
    	if (buildState == EnumBuildState.PLACE) {
    		if (extendedMouseOver != null) {
    			//clipboardData.setCornerPosition(extendedMouseOver.blockX - (clipboardData.map_sizeX / 2), extendedMouseOver.blockY+1, extendedMouseOver.blockZ - (clipboardData.map_sizeZ / 2));
    			if (mc.thePlayer.isSneaking()) {
    				clipboardData.setCornerPosition(extendedMouseOver.blockX, (int)MathHelper.floor_double(mc.thePlayer.posY - mc.thePlayer.yOffset), extendedMouseOver.blockZ);
    			} else {
    				clipboardData.setCornerPosition(extendedMouseOver.blockX, extendedMouseOver.blockY+1, extendedMouseOver.blockZ);
    			}
    			
    		} else {
    			clipboardData.setCornerPosition((int)MathHelper.floor_double(mc.thePlayer.posX), (int)MathHelper.floor_double(mc.thePlayer.posY - mc.thePlayer.yOffset), (int)MathHelper.floor_double(mc.thePlayer.posZ));
    		}
    	}
    	
    	if (BuildConfig.enableEditMode && mc.currentScreen == null) {
    		updateInput();
    	} else {
    		copyState = EnumCopyState.NORMAL;
    		buildState = EnumBuildState.NORMAL;
    	}
    	
	}
    
    public void updateInput() {
    	
    	int k = Mouse.getEventDWheel();
    	if (k > 0) {
    		k = 1;
    	} else if (k < 0) { k = -1; }
    	
    	if (lastPlayerSlot != Minecraft.getMinecraft().thePlayer.inventory.currentItem) {
    		lastPlayerSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
    		if (k != 0) lookDist += k * 1;
    	}
    	
    	if (Keyboard.isKeyDown(Keyboard.getKeyIndex(BuildConfig.key_Copy))) {
    		if (buildState != EnumBuildState.NORMAL) {
        		buildState = EnumBuildState.NORMAL; //reset
    		} else {
    			if (!wasKeyDown) {
    				eventCopy();
    			}	
    		}
			wasKeyDown = true;
    	} else if (Keyboard.isKeyDown(Keyboard.getKeyIndex(BuildConfig.key_Build))) {
    		if (copyState != EnumCopyState.NORMAL) {
        		copyState = EnumCopyState.NORMAL; //reset
    		} else {
    			if (!wasKeyDown) {
    				eventBuild();
    			}
    		}
			wasKeyDown = true;
    	} else if (Keyboard.isKeyDown(Keyboard.getKeyIndex(BuildConfig.key_Rotate))) {
    		if (!wasKeyDown && buildState == EnumBuildState.PLACE) {
    			direction++;
    			if (direction > 3) direction = 0;
    		}
			wasKeyDown = true;
		} else {
			wasKeyDown = false;
		}
    }
    
    int sx; int sy; int sz; int ex; int ey; int ez;
    
    public void eventCopy() {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	if (copyState == EnumCopyState.NORMAL) {
    		lookDist = 20;
    		copyState = EnumCopyState.SETMIN;
    	} else if (copyState == EnumCopyState.SETMIN) {
    		copyState = EnumCopyState.SETMAX;
    		sx = (int)MathHelper.floor_double(mc.thePlayer.posX);
    		sy = (int)(mc.thePlayer.posY - mc.thePlayer.yOffset);
    		sz = (int)MathHelper.floor_double(mc.thePlayer.posZ);
    		if (extendedMouseOver != null) {
    			sx = extendedMouseOver.blockX;
    			sy = extendedMouseOver.blockY;
    			sz = extendedMouseOver.blockZ;
    		}
    	} else {
    		ex = (int)MathHelper.floor_double(mc.thePlayer.posX);
    		ey = (int)(mc.thePlayer.posY - mc.thePlayer.yOffset);
    		ez = (int)MathHelper.floor_double(mc.thePlayer.posZ);
    		if (extendedMouseOver != null) {
	    		ex = extendedMouseOver.blockX;
				ey = extendedMouseOver.blockY;
				ez = extendedMouseOver.blockZ;
    		}
			
    		//REWIRED TO SEND ACTION TO SERVER AS WELL
			clipboardData.recalculateLevelSize(sx, sy, sz, ex, ey, ez, true);
			clipboardData.scanLevelToData(FMLClientHandler.instance().getClient().theWorld);
			clipboardData.writeNBT();
			BuildMod.eventChannel.sendToServer(EventHandlerPacket.getBuildCommandPacket(clipboardData, 0, -1));
			//FMLClientHandler.instance().getClient().thePlayer.sendQueue.addToSendQueue(BuildPacketHandler.getBuildCommandPacket(clipboardData, 0, -1));
			
    		copyState = EnumCopyState.NORMAL;
    	}
    	
    	System.out.println(copyState);
    }
    
    public void eventBuild() {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	if (buildState == EnumBuildState.NORMAL) {
    		buildState = EnumBuildState.PLACE;
    	} else if (buildState == EnumBuildState.PLACE) {
    		buildState = EnumBuildState.NORMAL;
    		
    		//REWIRE TO SEND ACTION TO SERVER INSTEAD
    		
    		BuildMod.eventChannel.sendToServer(EventHandlerPacket.getBuildCommandPacket(clipboardData, 1, direction));
    		//FMLClientHandler.instance().getClient().thePlayer.sendQueue.addToSendQueue(BuildPacketHandler.getBuildCommandPacket(clipboardData, 1, direction));
    		
    		
    	}
    }
}
