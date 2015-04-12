package net.tropicraft.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.tropicraft.util.EffectHelper;
import CoroUtil.packet.PacketHelper;
import CoroUtil.util.CoroUtilEntity;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TCPacketEvents {
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPacketFromServer(FMLNetworkEvent.ClientCustomPacketEvent event) {
		
		try {
			NBTTagCompound nbt = PacketHelper.readNBTTagCompound(event.packet.payload());
			
			String packetCommand = nbt.getString("packetCommand");
			
			System.out.println("Tropicraft packet command from server: " + packetCommand);
			
			if (packetCommand.equals("effectAdd")) {
				EffectHelper.addEntry(Minecraft.getMinecraft().thePlayer, nbt.getInteger("effectTime"));
			} else if (packetCommand.equals("effectRemove")) {
				EffectHelper.removeEntry(Minecraft.getMinecraft().thePlayer);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@SubscribeEvent
	public void onPacketFromClient(FMLNetworkEvent.ServerCustomPacketEvent event) {
		EntityPlayerMP entP = ((NetHandlerPlayServer)event.handler).playerEntity;
		
		try {
			NBTTagCompound nbt = PacketHelper.readNBTTagCompound(event.packet.payload());
			
			String packetCommand = nbt.getString("packetCommand");
			
			//Weather.dbg("Weather2 packet command from client: " + packetCommand);
			
			if (packetCommand.equals("")) {
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	
    
    @SideOnly(Side.CLIENT)
    public String getSelfUsername() {
    	return CoroUtilEntity.getName(Minecraft.getMinecraft().thePlayer);
    }
	
}
