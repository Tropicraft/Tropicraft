package net.tropicraft.item.tool;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.tropicraft.block.BlockTropicraft;
import net.tropicraft.registry.TCBlockRegistry;

import com.google.common.collect.Sets;

public class ItemTropicraftAxe extends ItemTropicraftTool {

	private static final HashSet<Block> effectiveBlocks = Sets.newHashSet(new Block[] {Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, 
			Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin,
			TCBlockRegistry.logs, TCBlockRegistry.mahoganyStairs, TCBlockRegistry.palmFence, TCBlockRegistry.palmFenceGate,
			TCBlockRegistry.palmStairs, TCBlockRegistry.planks});
	
	public ItemTropicraftAxe(ToolMaterial toolMaterial, String textureName) {
		super(3.0F, toolMaterial, effectiveBlocks);
		this.setTextureName(textureName);
	}

    public float func_150893_a(ItemStack itemstack, Block block) {
    	if (block instanceof BlockTropicraft) {
    		//TODO do stuff
    	}
    	
        return block.getMaterial() != Material.wood && block.getMaterial() != Material.plants && block.getMaterial() != Material.vine 
        		? super.func_150893_a(itemstack, block) : this.efficiencyOnProperMaterial;
    }
	
}
