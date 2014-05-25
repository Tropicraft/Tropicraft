package net.tropicraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.IPlantable;
import net.tropicraft.info.TCNames;

public class BlockPineapple extends BlockTallFlowers implements IPlantable {

	public BlockPineapple(String[] names) {
		super(names);
		//this.setBlockName(TCNames.pineapple);
		//this.setBlockTextureName(TCNames.pineapple);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {		
		topIcons = new IIcon[names.length];

		for (int i = 0 ; i < names.length ; i++) {
			topIcons[i] = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
		}

		bottomIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + TCNames.stem);
	}
}
