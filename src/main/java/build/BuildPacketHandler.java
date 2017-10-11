package build;


public class BuildPacketHandler/* implements IPacketHandler*/
{
    public BuildPacketHandler()
    {
    }

    /*@Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));

        if ("Build_Command".equals(packet.channel))
        {
            try
            {
            	if (player instanceof EntityPlayer) {
            		EntityPlayer entP = (EntityPlayer)player;
            		
            		if (entP.capabilities.isCreativeMode) {
            		
	            		Clipboard cb = (Clipboard)PlayerData.get(entP.username, "clipboard");
	            		int command = dis.readInt();
	            		int x = dis.readInt();
	            		int y = dis.readInt();
	            		int z = dis.readInt();
	            		System.out.println("cb: " + cb);
	            		if (command == 0) {
	            			int sX = dis.readInt();
	            			int sY = dis.readInt();
	            			int sZ = dis.readInt();
	            			cb.clipboardData = new Build(x, y, z, entP.username + "_clipboard", true);
	            			cb.clipboardData.dim = entP.worldObj.provider.dimensionId;
	            			//cb.clipboardData.recalculateLevelSize(x, y, z, sX, sY, sZ, true);
	            			cb.clipboardData.setCornerPosition(x, y, z);
	            			cb.clipboardData.map_sizeX = sX;
	            			cb.clipboardData.map_sizeY = sY;
	            			cb.clipboardData.map_sizeZ = sZ;
	            			cb.clipboardData.scanLevelToData();
	            			System.out.println("copy: " + x + ", " + y + ", " + z + ", " + sX + ", " + sY + ", " + sZ);
	            		} else {
	            			int dir = dis.readInt();
	            			cb.clipboardData.setCornerPosition(x, y, z);
	            			cb.clipboardData.dim = entP.worldObj.provider.dimensionId;
	            			BuildJob bj = new BuildJob(BuildServerTicks.buildMan.activeBuilds.size(), cb.clipboardData);
	                		bj.direction = dir;
	            			bj.rotation = (bj.direction * 90) + 180;
	            			System.out.println("build: " + x + ", " + y + ", " + z + ", dir:" + bj.direction);
	            			bj.useRotationBuild = true;
	            			bj.useFirstPass = false;
	            			bj.build_rate = 100;
	                		BuildServerTicks.buildMan.addBuild(bj);
	            		}
            		}
            	}
            	
            	
                //float val = dis.readFloat();
                
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    //commandID: 0 = copy, 1 = build
    //copy needs 6 ints, build needs 3 ints + direction int
    public static Packet250CustomPayload getBuildCommandPacket(Build build, int commandID, int direction) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(Integer.SIZE * 7);
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
        	dos.writeInt(commandID);
        	dos.writeInt(build.map_coord_minX);
        	dos.writeInt(build.map_coord_minY);
        	dos.writeInt(build.map_coord_minZ);
        	if (commandID == 0) {
	        	dos.writeInt(build.map_sizeX);
	        	dos.writeInt(build.map_sizeY);
	        	dos.writeInt(build.map_sizeZ);
        	} else {
        		dos.writeInt(direction);
        	}
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "Build_Command";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        
        return pkt;
	}*/
}
