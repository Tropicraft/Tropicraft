package build;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    public static String getContentsFromResourceLocation(ResourceLocation resourceLocation) {
        try {
            IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
            IResource iresource = resourceManager.getResource(resourceLocation);
            String contents = IOUtils.toString(iresource.getInputStream(), StandardCharsets.UTF_8);
            return contents;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    public static NBTTagCompound getNBTFromResourceLocation(ResourceLocation resourceLocation) {
        try {
            URL url = UtilBuild.class.getResource("/assets/" + resourceLocation.getResourceDomain() + "/" + resourceLocation.getResourcePath());
            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(url.openStream());
            return nbttagcompound;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
