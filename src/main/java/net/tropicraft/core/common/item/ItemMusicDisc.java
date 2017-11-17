package net.tropicraft.core.common.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.RecordInformation;

public class ItemMusicDisc extends ItemRecord {
    
    /** Name of the artist of the track */
	private String artistName;
	
	/** Name of this song */
	private String songName;

	public ItemMusicDisc(String recordName, String artistName, SoundEvent soundIn) {
		super("tropicraft:" + recordName, soundIn);
		this.songName = recordName;
		this.artistName = artistName;
	}
	
    /**
     * Return the title for this record.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public String getRecordNameLocal() {
        return String.format("%s - %s", artistName, I18n.translateToLocal("item.tropicraft." + songName + ".name"));
    }
	
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        String[] extraInfo = RecordInformation.getInformation(songName);
        
        if (extraInfo != null) {
        	for (String line : extraInfo) {
        		tooltip.add(line);
        	}
        }
    }
}
