package net.tropicraft.core.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;


public class TropicalMusicDiscItem extends MusicDiscItem {
    
    private final RecordMusic type;

    public TropicalMusicDiscItem(RecordMusic type, Properties builder) {
        super(13, type.getSound(), builder);
        this.type = type;
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(getDescLine(1).applyTextStyle(TextFormatting.GRAY));
    }
    
    private ITextComponent getDescLine(int i) {
        return new TranslationTextComponent(this.getTranslationKey() + ".desc." + i);
    }
    
    @Override
    public ITextComponent getRecordDescription() {
        return getDescLine(0);
    }
    
    public RecordMusic getType() {
        return type;
    }
}
