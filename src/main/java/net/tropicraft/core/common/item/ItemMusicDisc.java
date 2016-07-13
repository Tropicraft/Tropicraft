package net.tropicraft.core.common.item;

import java.util.List;

import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
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
        return String.format("%s - %s", artistName, I18n.translateToLocal("item.tropicraft:" + songName + ".name"));
    }
	
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        String[] extraInfo = RecordInformation.getInformation(songName);
        
        if (extraInfo != null) {
        	for (String line : extraInfo) {
        		tooltip.add(line);
        	}
        }
    }
    
    /** I don't think this is used */
    @Override
    public ResourceLocation getRecordResource(String name) {
        return new ResourceLocation("tropicraft:" + songName);
    }
}
