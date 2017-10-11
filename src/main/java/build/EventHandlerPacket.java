package build;

import CoroUtil.packet.INBTPacketHandler;
import CoroUtil.packet.PacketHelper;
import CoroUtil.util.CoroUtilEntity;
import build.playerdata.PlayerData;
import build.playerdata.objects.Clipboard;
import build.world.Build;
import build.world.BuildJob;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;

public class EventHandlerPacket {

	@SideOnly(Side.CLIENT)
	public World getClientWorld() {
		return Minecraft.getMinecraft().theWorld;
	}
	
	@SideOnly(Side.CLIENT)
	public INBTPacketHandler getClientDataInterface() {
		if (Minecraft.getMinecraft().currentScreen instanceof INBTPacketHandler) {
			return (INBTPacketHandler) Minecraft.getMinecraft().currentScreen;
		}
		return null;
	}
	
	@SubscribeEvent
	public void onPacketFromServer(FMLNetworkEvent.ClientCustomPacketEvent event) {
		
		try {
			NBTTagCompound nbt = PacketHelper.readNBTTagCompound(event.packet.payload());
			
			String command = nbt.getString("command");
			
			System.out.println("BuildMod packet command from server: " + command);
			
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@SubscribeEvent
	public void onPacketFromClient(FMLNetworkEvent.ServerCustomPacketEvent event) {
		EntityPlayer entP = ((NetHandlerPlayServer)event.handler).playerEntity;
		
		try {
			NBTTagCompound nbt = PacketHelper.readNBTTagCompound(event.packet.payload());
			
			String command = nbt.getString("command");
			
			System.out.println("BuildMod packet command from client: " + command);
			
			if (command.equals("Build_Command")) {
				
				if (entP.capabilities.isCreativeMode) {
					
					Clipboard cb = (Clipboard) PlayerData.get(CoroUtilEntity.getName(entP), "clipboard");
					
					int commandID = nbt.getInteger("commandID");
					int x = nbt.getInteger("posX");
					int y = nbt.getInteger("posY");
					int z = nbt.getInteger("posZ");
					
					if (commandID == 0) {
            			int sX = nbt.getInteger("sizeX");
            			int sY = nbt.getInteger("sizeY");
            			int sZ = nbt.getInteger("sizeZ");
            			cb.clipboardData = new Build(x, y, z, CoroUtilEntity.getName(entP) + "_clipboard", true);
            			cb.clipboardData.dim = entP.worldObj.provider.dimensionId;
            			//cb.clipboardData.recalculateLevelSize(x, y, z, sX, sY, sZ, true);
            			cb.clipboardData.setCornerPosition(x, y, z);
            			cb.clipboardData.map_sizeX = sX;
            			cb.clipboardData.map_sizeY = sY;
            			cb.clipboardData.map_sizeZ = sZ;
            			cb.clipboardData.scanLevelToData();
            			System.out.println("copy: " + x + ", " + y + ", " + z + ", " + sX + ", " + sY + ", " + sZ);
            		} else {
            			int dir = nbt.getInteger("direction");
            			cb.clipboardData.setCornerPosition(x, y, z);
            			cb.clipboardData.dim = entP.worldObj.provider.dimensionId;
            			BuildJob bj = new BuildJob(BuildServerTicks.buildMan.activeBuilds.size(), cb.clipboardData);
                		bj.setDirection(dir);
            			System.out.println("build: " + x + ", " + y + ", " + z + ", dir:" + bj.direction);
            			bj.useRotationBuild = true;
            			bj.useFirstPass = false;
            			bj.build_rate = 100;
                		BuildServerTicks.buildMan.addBuild(bj);
            		}
				}
				
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static FMLProxyPacket getBuildCommandPacket(Build build, int commandID, int direction) {
		NBTTagCompound data = new NBTTagCompound();
		
		data.setString("command", "Build_Command");
		
		data.setInteger("commandID", commandID);
		data.setInteger("posX", build.map_coord_minX);
		data.setInteger("posY", build.map_coord_minY);
		data.setInteger("posZ", build.map_coord_minZ);
		if (commandID == 0) {
			data.setInteger("sizeX", build.map_sizeX);
			data.setInteger("sizeY", build.map_sizeY);
			data.setInteger("sizeZ", build.map_sizeZ);
		} else {
			data.setInteger("direction", direction);
		}
		
		return PacketHelper.getNBTPacket(data, BuildMod.eventChannelName);
	}
	
}
