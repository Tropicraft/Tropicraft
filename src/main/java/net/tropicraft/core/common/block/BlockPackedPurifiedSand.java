package net.tropicraft.core.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.tropicraft.core.registry.BlockRegistry;

public class BlockPackedPurifiedSand extends BlockTropicraft {

	public BlockPackedPurifiedSand() {
		super(Material.SAND);
		this.setHardness(2.0F);
		this.setResistance(30F);
		this.setSoundType(SoundType.SAND);
	}

	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(BlockRegistry.sands.getDefaultState().getBlock());
	}

	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(3) + 1;
	}
}
