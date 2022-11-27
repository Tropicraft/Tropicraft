package net.tropicraft.core.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomTallSeagrassBlock extends TallSeagrassBlock {
	private final String scientificName;
	private final NonNullSupplier<? extends SeagrassBlock> drop;

	public CustomTallSeagrassBlock(Properties p, String scientificName, NonNullSupplier<? extends SeagrassBlock> drop) {
		super(p);
		this.scientificName = scientificName;
		this.drop = drop;
 	}

	@Override
	public void appendHoverText(final ItemStack itemStack, final @Nullable BlockGetter level, final List<Component> tooltip, final TooltipFlag flag) {
		tooltip.add(new TextComponent(scientificName).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter p_154749_, BlockPos p_154750_, BlockState p_154751_) {
		return new ItemStack(drop.get());
	}
}
