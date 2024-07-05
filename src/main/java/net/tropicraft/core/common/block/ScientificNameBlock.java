package net.tropicraft.core.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ScientificNameBlock extends Block {
    public static final MapCodec<ScientificNameBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            propertiesCodec(),
            Codec.STRING.fieldOf("scientific_name").forGetter(b -> b.scientificName)
    ).apply(i, ScientificNameBlock::new));

    private final String scientificName;

    public ScientificNameBlock(Properties properties, String scientificName) {
        super(properties);
        this.scientificName = scientificName;
    }

    @Override
    protected MapCodec<ScientificNameBlock> codec() {
        return CODEC;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal(scientificName).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
    }
}
