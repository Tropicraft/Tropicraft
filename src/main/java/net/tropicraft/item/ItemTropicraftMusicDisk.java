package net.tropicraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicraftMusicDisk extends ItemRecord {

    /**
     * Second half of the record name. Eg if image name is "record_easternisles" then this would be "easternisles"
     */
    private String imagePostfixName;
    
    /** Name of the artist of the track */
    private String artistName;
    
    public ItemTropicraftMusicDisk(String recordName, String imagePostfixName, String artist) {
        super(recordName);
        this.imagePostfixName = imagePostfixName;
        this.artistName = artist;
        setCreativeTab(TCCreativeTabRegistry.tabMusic);
    }
    
    /**
     * @return The unlocalized item name
     */
    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
    }

    /**
     * @param itemStack ItemStack instance of this item
     * @return The unlocalized item name
     */
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
    }

    /**
     * Get the actual name of the block
     * @param unlocalizedName Unlocalized name of the block
     * @return Actual name of the block, without the "tile." prefix
     */
    protected String getActualName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    /**
     * Return the title for this record.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public String getRecordNameLocal() {
        return String.format("%s - %s", artistName, StatCollector.translateToLocal("item.tropicraft:record_" + recordName + ".name"));
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(TCInfo.ICON_LOCATION + "record_" + this.imagePostfixName);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer ent, List list, boolean wat) {
        list.add(getRecordNameLocal());
    }
    
    /**
     * Retrieves the resource location of the sound to play for this record.
     * 
     * @param name The name of the record to play
     * @return The resource location for the audio, null to use default.
     */
    @Override
    public ResourceLocation getRecordResource(String name) {
        return new ResourceLocation(TCInfo.RECORDS_LOCATION + name.substring(8) + ".ogg");
    }
}
