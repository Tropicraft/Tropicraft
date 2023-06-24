package net.tropicraft.core.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class TropicalMusicDiscItem extends RecordItem {
    
    private final RecordMusic type;

    public TropicalMusicDiscItem(RecordMusic type, Properties builder) {
        super(13, type::getSound, builder, type.lengthInTicks());
        this.type = type;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(getDescLine(1).copy().withStyle(ChatFormatting.GRAY));
    }
    
    private MutableComponent getDescLine(int i) {
        return Component.translatable(this.getDescriptionId() + ".desc." + i);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public MutableComponent getDisplayName() {
        return this.getDescLine(0);
    }

    public RecordMusic getType() {
        return type;
    }
}
