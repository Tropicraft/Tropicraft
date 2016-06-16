package net.tropicraft.core.common.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

/**
 * Basic interface for all Tropicraft blocks to implement to make doing states easier
 * @author Cojo
 *
 */
public interface ITropicraftBlock {
	/** Get the name of this block based on its state */
	public String getStateName(IBlockState state);
	
	/** Get the properties of this block from its variant */
	public IProperty[] getProperties();
}