package net.tropicraft.core.common.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.block.tileentity.TileEntityFactory;

public class BlockDonation extends Block implements ITileEntityProvider {

	public BlockDonation() {
		super(Material.ROCK);
		this.setCreativeTab(null);
		this.setBlockUnbreakable();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return TileEntityFactory.getDonationTE();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
	    super.addInformation(stack, player, tooltip, advanced);
	    tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}

}
