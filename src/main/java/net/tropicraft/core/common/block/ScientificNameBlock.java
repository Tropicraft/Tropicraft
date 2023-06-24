package net.tropicraft.core.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ScientificNameBlock extends Block {
	private final String scientificName;

	public ScientificNameBlock(Properties properties, String scientificName) {
		super(properties);
		this.scientificName = scientificName;
	}

	@Override
	public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.literal(scientificName).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
	}
}