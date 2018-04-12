package net.tropicraft.core.encyclopedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TropicalBook {

    public static enum ReadState {
        HIDDEN,
        VISIBLE,
        READ,
        ;
    }
    
    /**
     * List of page names that may be visible by the player
     * The pages should only be considered visible if the byte > 0
     * The pages are also marked as read if the byte > 1
     */
    private HashMap<String, ReadState> visiblePages = new HashMap<String, ReadState>();
    
    /** Maps of internal page names to translated strings (from the contents file)
     Pages are sorted according to the order of the "<pagename>.title" entries
     in the contents file */
    private HashMap<String, String> pageTitles = new HashMap<String, String>();
    private HashMap<String, String> pageDescriptions = new HashMap<String, String>();
    private List<String> sortedPages = new ArrayList<String>();
    
    private final String fileName;
    
    public String outsideTexture;
    public String insideTexture;
    
    public TropicalBook(String savedDataFile, String contentsFile, String outsideTex, String insideTex) {

        fileName = savedDataFile;
        outsideTexture = outsideTex;
        insideTexture = insideTex;

        BufferedReader contents = new BufferedReader(new InputStreamReader(TropicalBook.class.getResourceAsStream(contentsFile)));
        String line;
        try {
            while ((line = contents.readLine()) != null) {
                if (!line.contains("=") || line.trim().startsWith("#")) {
                    continue;
                }
                String[] split = line.split("=", 2);
                String name = split[0].trim();
                String entry = split[1].trim();
                if (name.toLowerCase().endsWith(".title")) {
                    pageTitles.put(name.substring(0, name.length() - ".title".length()), entry);
                    sortedPages.add(name.substring(0, name.length() - ".title".length()));
                } else if (name.toLowerCase().endsWith(".desc")) {
                    pageDescriptions.put(name.substring(0, name.length() - ".desc".length()), entry);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TropicalBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    protected File getSaveFile() {
        File root = DimensionManager.getCurrentSaveRootDirectory();
        if (root == null) {
            ServerData server = Minecraft.getMinecraft().getCurrentServerData();
            if (server == null) {
                throw new IllegalStateException("Cannot load encyclopedia outside of a game!");
            }
            // FIXME Encyclopedia save data breaks if the client changes the server name
            // Needs to be moved to serverside storage
            return Paths.get("encyclopedia-servers", server.serverName, fileName).toFile();
        }
        return new File(root, fileName);
    }
    
    // This method is fired on both sides so will work in SSP/SMP
    @SubscribeEvent
    public void loadData(WorldEvent.Load event) {
        if (event.getWorld().provider.getDimension() != 0) {
            return;
        }
        try {
            File dataFile = getSaveFile();
            visiblePages.clear();
            if (dataFile.canRead()) {
                try (InputStream dataInput = new FileInputStream(dataFile)) {
                    NBTTagCompound data = CompressedStreamTools.readCompressed(dataInput);

                    Iterator<String> it = data.getKeySet().iterator();

                    while (it.hasNext()) {
                        String tagName = it.next();
                        ReadState s = ReadState.values()[data.getByte(tagName) % ReadState.values().length];
                        visiblePages.put(tagName, s);
                    }
                }
            }
        } catch (IOException ex) {
            LogManager.getLogger().error("Error reading encyclopedia data.", ex);
        }
    }
    
    // SSP fallback save
    @SubscribeEvent
    public void saveData(WorldEvent.Save event) {
        if (event.getWorld().provider.getDimension() != 0) {
            return;
        }
        saveData();
    }
    
    // SMP fallback save
    @SubscribeEvent
    public void saveData(ClientDisconnectionFromServerEvent event) {
        saveData();
    }
    
    private void saveData() {
        try {
            File dataFile = getSaveFile();
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
            if (dataFile.canWrite()) {
                try (OutputStream dataOutput = new FileOutputStream(dataFile)) {
                    NBTTagCompound data = new NBTTagCompound();
                    for (String s : visiblePages.keySet()) {
                        data.setByte(s, (byte) visiblePages.get(s).ordinal());
                    }
                    CompressedStreamTools.writeCompressed(data, dataOutput);
                }
            }
        } catch (IOException ex) {
            LogManager.getLogger().error("Error writing encyclopedia data.", ex);
        }
    }
    
    public boolean hasRecipeList() {
        return false;
    }
    
    public boolean isPageVisible(String entry) {
        return visiblePages.containsKey(entry) && visiblePages.get(entry) != ReadState.HIDDEN;
    }
    
    public boolean isPageVisible(int i) {
        return isPageVisible(getPageName(i));
    }
    
    public boolean hasPageBeenRead(String entry) {
        return visiblePages.containsKey(entry) && visiblePages.get(entry) == ReadState.READ;
    }
    
    public boolean hasPageBeenRead(int i) {
        return hasPageBeenRead(getPageName(i));
    }
    
    public void markPageAsNewlyVisible(String entry) {
        visiblePages.put(entry, ReadState.VISIBLE);
        saveData();
    }
    
    public void markPageAsNewlyVisible(int i) {
        markPageAsNewlyVisible(getPageName(i));
    }
    
    public void markPageAsRead(String entry) {
        visiblePages.put(entry, ReadState.READ);
        saveData();
    }
    
    public void markPageAsRead(int i) {
        markPageAsRead(getPageName(i));
    }
    
    public boolean pageExists(String name) {
        if (pageTitles.containsKey(name)) {
            return true;
        }
        return false;
    }
    
    /*
     * Decides what pages to mark as visible based on the contents of the
     * given player inventory
     */
    public abstract void updatePagesFromInventory(InventoryPlayer inv);
    
    public int getPageCount() {
        return sortedPages.size();
    }
    
    public int entriesPerIndexPage() {
        return 12;
    }
    
    public boolean hasIndexIcons() {
        return false;
    }
    
    public ItemStack getPageItemStack(int page) {
        return null;
    }

    protected String getPageName(int i) {
        if (i >= 0 && i < sortedPages.size()) {
            return sortedPages.get(i);
        }
        return null;
    }
    
    public String getPageTitleNotVisible(int i) {
        return "Page not found";
    }
    
    private String getPageTitleByName(String name) {
        if (pageExists(name)) {
            return /* this part adds underline, silly tundy"\247n"+*/pageTitles.get(name);
        }
        return null;
    }
    
    public String getPageTitleByIndex(int i) {
        return getPageTitleByName(getPageName(i));
    }
    
    private String getPageDescriptionByName(String name) {
        if (pageExists(name)) {
            return pageDescriptions.get(name);
        }
        return null;
    }
    
    public String getPageDescriptionsByIndex(int i) {
        return getPageDescriptionByName(getPageName(i));
    }
    
    protected List<String> getSortedPages() {
        return sortedPages;
    }

    public int getRecipeCount(int page) {
        return 0;
    }
    
}
