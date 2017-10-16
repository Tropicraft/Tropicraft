package build;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class UtilBuild {

    public static boolean debug = true;

    public static void dbg(String str) {
        if (debug) {
            System.out.println(str);
        }
    }

    public static void writeCoords(String name, BlockPos coords, NBTTagCompound nbt) {
        nbt.setInteger(name + "X", coords.getX());
        nbt.setInteger(name + "Y", coords.getY());
        nbt.setInteger(name + "Z", coords.getZ());
    }

    public static BlockPos readCoords(String name, NBTTagCompound nbt) {
        if (nbt.hasKey(name + "X")) {
            return new BlockPos(nbt.getInteger(name + "X"), nbt.getInteger(name + "Y"), nbt.getInteger(name + "Z"));
        } else {
            return null;
        }
    }

    //TODO: replace with server side friendly in jar path usage when ready
    public static String getSaveFolderPath() {
        return "./";
    }

}
