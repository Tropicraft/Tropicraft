package net.tropicraft.core.common.block;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.core.common.enums.TropicraftOreBlocks;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockTropicraftOre extends BlockTropicraftEnumVariants<TropicraftOreBlocks> {

	public BlockTropicraftOre() {
		super(Material.ROCK, TropicraftOreBlocks.class);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.STONE);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        switch (getVariant(state)) {
        case AZURITE:
            return ItemRegistry.azurite;
        case EUDIALYTE:
            return ItemRegistry.eudialyte;
        case ZIRCON:
            return ItemRegistry.zircon;
        }
        return null;
    }

	// TODO this is a lazy impl
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        if (getVariant(state) == TropicraftOreBlocks.EUDIALYTE) {
            return 1 + random.nextInt(4 + fortune);
        } else {
            return 1 + random.nextInt(1 + fortune);
        }
    }
	
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        return MathHelper.getInt(rand, 2, 7);
    }
}
