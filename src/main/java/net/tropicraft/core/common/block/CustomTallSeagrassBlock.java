package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

public class CustomTallSeagrassBlock extends TallSeagrassBlock {
    private final String scientificName;
    private final Supplier<? extends SeagrassBlock> drop;

    public CustomTallSeagrassBlock(Properties p, String scientificName, Supplier<? extends SeagrassBlock> drop) {
        super(p);
        this.scientificName = scientificName;
        this.drop = drop;
    }

    @Override
    public MapCodec<TallSeagrassBlock> codec() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void appendHoverText(final ItemStack itemStack, final Item.TooltipContext context, final List<Component> tooltip, final TooltipFlag flag) {
        tooltip.add(Component.literal(scientificName).withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
    }

    @Override
    public ItemStack getCloneItemStack(final LevelReader level, final BlockPos pos, final BlockState state) {
        return new ItemStack(drop.get());
    }
}
