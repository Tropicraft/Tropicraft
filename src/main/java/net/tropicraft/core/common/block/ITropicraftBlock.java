package net.tropicraft.core.common.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	@SideOnly(Side.CLIENT)
	public IBlockColor getBlockColor();
	
	@SideOnly(Side.CLIENT)
	public IItemColor getItemColor();
}