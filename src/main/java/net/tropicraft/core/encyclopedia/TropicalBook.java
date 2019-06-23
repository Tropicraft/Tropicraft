package net.tropicraft.core.encyclopedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class TropicalBook {

    public static enum ReadState {
        HIDDEN,
        VISIBLE,
        READ,
        ;
    }
    
    private LinkedHashMap<String, Page> pages = new LinkedHashMap<>();
    private List<String> pageIds = new ArrayList<>();
    
    private Page currentBookmark;
    private Map<Page, Page> pageToBookmark = new IdentityHashMap<>();
    
    private HashMap<String, ReadState> visibilities = new HashMap<>();

    private final String fileName;
    
    public String outsideTexture;
    public String insideTexture;
    
    public TropicalBook(String savedDataFile, String outsideTex, String insideTex) {

        fileName = savedDataFile;
        outsideTexture = outsideTex;
        insideTexture = insideTex;

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void addPage(Page page) {
        if (!pages.containsKey(page.getId())) {
            this.pages.put(page.getId(), page);
            this.pageIds.add(page.getId());
            if (page.isBookmark()) {
                currentBookmark = page;
            } else if (currentBookmark != null) {
                this.pageToBookmark.put(page, currentBookmark);
            }
        } else {
            throw new IllegalArgumentException("Duplicate page: " + page.getId());
        }
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
            visibilities.clear();
            if (dataFile.canRead()) {
                try (InputStream dataInput = new FileInputStream(dataFile)) {
                    NBTTagCompound data = CompressedStreamTools.readCompressed(dataInput);

                    Iterator<String> it = data.getKeySet().iterator();

                    while (it.hasNext()) {
                        String tagName = it.next();
                        ReadState s = ReadState.values()[data.getByte(tagName) % ReadState.values().length];
                        visibilities.put(tagName, s);
                    }
                }
            }
        } catch (IllegalStateException | IOException ex) {
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
                    for (String s : visibilities.keySet()) {
                        data.setByte(s, (byte) visibilities.get(s).ordinal());
                    }
                    CompressedStreamTools.writeCompressed(data, dataOutput);
                }
            }
        } catch (IllegalStateException | IOException ex) {
            LogManager.getLogger().error("Error writing encyclopedia data.", ex);
        }
    }
    
    public boolean hasRecipeList() {
        return false;
    }
    
    public boolean isPageVisible(String entry) {
        return visibilities.containsKey(entry) && visibilities.get(entry) != ReadState.HIDDEN;
    }
    
    public boolean isPageVisible(int i) {
        return isPageVisible(getPageName(i));
    }
    
    public boolean hasPageBeenRead(String entry) {
        return visibilities.containsKey(entry) && visibilities.get(entry) == ReadState.READ;
    }
    
    public boolean hasPageBeenRead(int i) {
        return hasPageBeenRead(getPageName(i));
    }
    
    public void markPageAsNewlyVisible(String entry) {
        Page page = getPage(entry);
        visibilities.put(entry, page == null || !page.isBookmark() ? ReadState.VISIBLE : ReadState.READ);
        saveData();
    }
    
    public void markPageAsNewlyVisible(int i) {
        markPageAsNewlyVisible(getPageName(i));
    }
    
    public void markPageAsRead(String entry) {
        visibilities.put(entry, ReadState.READ);
        saveData();
    }
    
    public void markPageAsRead(int i) {
        markPageAsRead(getPageName(i));
    }
    
    public void hidePage(int i) {
        hidePage(getPageName(i));
    }
    
    public void hidePage(String entry) {
        visibilities.remove(entry);
        saveData();
    }
    
    public boolean pageExists(String name) {
        if (pages.containsKey(name)) {
            return true;
        }
        return false;
    }
    
    protected String getPageName(int i) {
        if (i >= 0 && i < pageIds.size()) {
            return pageIds.get(i);
        }
        return null;
    }
    
    /*
     * Decides what pages to mark as visible based on the contents of the
     * given player inventory
     */
//    public abstract void updatePagesFromInventory(InventoryPlayer inv);
    
    public int getPageCount() {
        return pages.size();
    }
    
    public String getPageTitleNotVisible() {
        return "Page not found";
    }

    public int getRecipeCount(int page) {
        return 0;
    }

    public Page getPage(int i) {
        return getPage(pageIds.get(i));
    }
    
    public Page getPage(String key) {
        return pages.get(key);
    }
    
    public void discoverPages(@Nonnull World world, @Nonnull EntityPlayer player) {
        for (Entry<String, Page> e : pages.entrySet()) {
            if (!isPageVisible(e.getKey()) && e.getValue().discover(world, player)) {
                markPageAsNewlyVisible(e.getKey());
                Page bookmark = pageToBookmark.get(e.getValue());
                if (bookmark != null) {
                    markPageAsRead(bookmark.getId());
                }
            }
        }
    }

    public void dumpSections() {
        Multimap<Page, Page> inverseSections = HashMultimap.create();
        for (Entry<Page, Page> e : pageToBookmark.entrySet()) {
            inverseSections.put(e.getValue(), e.getKey());
        }
        inverseSections.asMap().entrySet().forEach(System.out::println);
    }
}
