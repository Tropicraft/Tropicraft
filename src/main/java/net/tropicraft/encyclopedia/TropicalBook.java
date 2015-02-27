package net.tropicraft.encyclopedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TropicalBook {
    
    // Content mode is used to determine what tabs are avaliable in this book
    public static enum ContentMode {
        INFO,
        RECIPE
    }

    // Data file that saves which pages should be visible
    private File dataFile;
    
    public static File file = null;
    
    /**
     * List of page names that may be visible by the player
     * The pages should only be considered visible if the byte > 0
     * The pages are also marked as read if the byte > 1
     */
    private HashMap<String, Byte> visiblePages = new HashMap<String, Byte>();
    
    /** Maps of internal page names to translated strings (from the contents file)
     Pages are sorted according to the order of the "<pagename>.title" entries
     in the contents file */
    private HashMap<String, String> pageTitles = new HashMap<String, String>();
    private HashMap<String, String> pageDescriptions = new HashMap<String, String>();
    private List<String> sortedPages = new ArrayList<String>();
    
    public String outsideTexture;
    public String insideTexture;
    
    public TropicalBook(String savedDataFile, String contentsFile, String outsideTex, String insideTex) {

        outsideTexture = outsideTex;
        insideTexture = insideTex;

        dataFile = new File(getClientSidePath(), savedDataFile);        

        try {
            if (dataFile.canRead()) {
                InputStream dataInput = new FileInputStream(dataFile);
                NBTTagCompound data = CompressedStreamTools.readCompressed(dataInput);
                
                Iterator<?> it = data.func_150296_c().iterator();
                
                while (it.hasNext()) {
                    String tagName = (String)it.next();
                    Byte b = data.getByte(tagName);
                    visiblePages.put(tagName, b);
                }

                dataInput.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        BufferedReader contents = new BufferedReader(new InputStreamReader(TropicalBook.class.getResourceAsStream(contentsFile)));
        String line;
        try {
            while ((line = contents.readLine()) != null) {
                if (!line.contains("=")) {
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

    }
    
    @SideOnly(Side.CLIENT)
    public static String getClientSidePath() {
        return FMLClientHandler.instance().getClient().mcDataDir.getPath();
    }
    
    /**
     * gets the working dir (OS specific) for the specific application (which is always minecraft)
     */
/*    public static File getAppDir(String par0Str)
    {
        String var1 = System.getProperty("user.home", ".");
        File var2;

        switch (EnumOSHelper.field_90049_a[getOs().ordinal()])
        {
            case 1:
            case 2:
                var2 = new File(var1, '.' + par0Str + '/');
                break;
            case 3:
                String var3 = System.getenv("APPDATA");

                if (var3 != null)
                {
                    var2 = new File(var3, "." + par0Str + '/');
                }
                else
                {
                    var2 = new File(var1, '.' + par0Str + '/');
                }

                break;
            case 4:
                var2 = new File(var1, "Library/Application Support/" + par0Str);
                break;
            default:
                var2 = new File(var1, par0Str + '/');
        }

        if (!var2.exists() && !var2.mkdirs())
        {
            throw new RuntimeException("The working directory could not be created: " + var2);
        }
        else
        {
            return var2;
        }
    }*/
/*
    public static net.minecraft.util.EnumOS getOs()
    {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? EnumOS.WINDOWS : (var0.contains("mac") ? EnumOS.MACOS : (var0.contains("solaris") ? EnumOS.SOLARIS : (var0.contains("sunos") ? EnumOS.SOLARIS : (var0.contains("linux") ? EnumOS.LINUX : (var0.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }*/
    
    protected void saveData() {
        try {
            dataFile.createNewFile();
            if (dataFile.canWrite()) {
                OutputStream dataOutput = new FileOutputStream(dataFile);
                NBTTagCompound data = new NBTTagCompound();
                for (String s : visiblePages.keySet()) {
                    data.setByte(s, visiblePages.get(s));
                }
                CompressedStreamTools.writeCompressed(data, dataOutput);
                dataOutput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(TropicalBook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean hasRecipeList() {
        return false;
    }
    
    public boolean isPageVisible(String entry) {
        return visiblePages.containsKey(entry) && visiblePages.get(entry) > 0;
    }
    
    public boolean isPageVisible(int i) {
        return isPageVisible(getPageName(i));
    }
    
    public boolean hasPageBeenRead(String entry) {
        return visiblePages.containsKey(entry) && visiblePages.get(entry) > 1;
    }
    
    public boolean hasPageBeenRead(int i) {
        return hasPageBeenRead(getPageName(i));
    }
    
    public void markPageAsNewlyVisible(String entry) {
        visiblePages.put(entry, (byte)1);
        saveData();
    }
    
    public void markPageAsNewlyVisible(int i) {
        markPageAsNewlyVisible(getPageName(i));
    }
    
    public void markPageAsRead(String entry) {
        visiblePages.put(entry, (byte)2);
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
    
    /*
     * Returns the number of content pages the given page contains
     */
    public int getContentPageCount(int page, ContentMode mode) {
        return 1;
    }
    
    public int entriesPerIndexPage() {
        return 12;
    }
    
    public int entriesPerContentPage(ContentMode mode) {
        return 1;
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
    
}
