package CoroUtil.util;

import java.util.Iterator;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class CoroUtilNBT {

	public static CompoundNBT copyOntoNBT(CompoundNBT nbtSource, CompoundNBT nbtDest) {
		CompoundNBT newNBT = (CompoundNBT) nbtDest.copy();

		String tagName = "";
		//do magic
		try {
			Iterator it = nbtSource.keySet().iterator();
			while (it.hasNext()) {
				tagName = (String) it.next();
				INBT data = nbtSource.get(tagName);
				newNBT.put(tagName, data);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return newNBT;
	}
	
	/*private static NBTTagCompound copyOntoNBTTagCompound nbtSource, NBTTagCompound nbtDest) {
		NBTTagCompound
	}*/
	
	//this should probably be recursive
	/*private static NBTTagCompound copyOntoRecursive(NBTTagCompound nbtSource, NBTTagCompound nbttagcompound)
    {
		try {
			
			
			Collection dataCl = nbtSource.getTags();
			Iterator it = dataCl.iterator();
			
			while (it.hasNext()) {
				NBTBase data = (NBTBase)it.next();
				if (data instanceof NBTTagCompound) {
					NBTTagCompound resultCopy = copyOntoRecursive((NBTTagCompound)data, nbttagcompound);
				}
				String entryName = data.getName();
			}
			
			
			
			
			
			
			
			//nbttagcompound = new NBTTagCompound(this.getName());
			Map tagMap = (Map)c_CoroAIUtil.getPrivateValueSRGMCP(NBTTagCompound.class, nbtSource, "tagMap", "tagMap");
	        Iterator iterator = tagMap.entrySet().iterator();
	
	        while (iterator.hasNext())
	        {
	        	
	            String s = (String)iterator.next();
	            nbttagcompound.put(s, ((NBTBase)tagMap.get(s)).copy());
	        }
		} catch (Exception ex) {
			ex.printStackTrace();
		}

        return nbttagcompound;
    }*/
	
	public static void writeCoords(String name, BlockCoord coords, CompoundNBT nbt) {
    	nbt.putInt(name + "X", coords.posX);
    	nbt.putInt(name + "Y", coords.posY);
    	nbt.putInt(name + "Z", coords.posZ);
    }
    
    public static BlockCoord readCoords(String name, CompoundNBT nbt) {
    	if (nbt.contains(name + "X")) {
    		return new BlockCoord(nbt.getInt(name + "X"), nbt.getInt(name + "Y"), nbt.getInt(name + "Z"));
    	} else {
    		return null;
    	}
    }
}
