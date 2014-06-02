package net.tropicraft.item.tool;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.google.common.collect.Sets;

public class ItemTropicraftShovel extends ItemTropicraftTool {

	private static final HashSet<Block> blocksEffectiveAgainst = Sets.newHashSet(new Block[] {Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium});
	
	public ItemTropicraftShovel(ToolMaterial toolMaterial, String textureName) {
		super(1.0F, toolMaterial, blocksEffectiveAgainst);
		this.setTextureName(textureName);
	}

	/**
	 * canHarvestBlock, perhaps?
	 */
	@Override
    public boolean func_150897_b(Block block) {
        return block == Blocks.snow_layer ? true : block == Blocks.snow;
    }
}
