package net.tropicraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBambooChute extends BlockTropicraft {
    @SideOnly(Side.CLIENT)
	private IIcon sideIcon;
    
    @SideOnly(Side.CLIENT) 
    private IIcon bottomIcon;

    @SideOnly(Side.CLIENT) 
    private IIcon topIcon;

    @SideOnly(Side.CLIENT) 
    private IIcon indentIcon;
    
    @SideOnly(Side.CLIENT)
    private IIcon leafIcon;
    
    @SideOnly(Side.CLIENT)
    private IIcon leafFlippedIcon;
	
	public BlockBambooChute() {
		super(Material.plants);
		setHardness(1.0F);
		setResistance(4.0F);
        float f = 0.375F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        this.setTickRandomly(true);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
        this.setBlockTextureName(TCNames.bambooChute);
	}
	
    @Override
	public boolean isBlockNormalCube() {
    	return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
    public int getRenderType() {
        return TCRenderIDs.bambooChute;
    }

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		sideIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_side");
		topIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_top");
		bottomIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_bottom");
		indentIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_indent");
		leafIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_leaf");
		leafFlippedIcon = new IconFlipped(leafIcon, true, false);
	}
	
	public static IIcon getBambooIcon(String what) {
		if (what.equals("side")) {
			return TCBlockRegistry.bambooChute.sideIcon;
		} else if (what.equals("top")) {
			return TCBlockRegistry.bambooChute.topIcon;
		} else if (what.equals("bottom")) {
			return TCBlockRegistry.bambooChute.bottomIcon;
		} else if (what.equals("indent")) {
			return TCBlockRegistry.bambooChute.indentIcon;
		} else if (what.equals("leaf")) {
			return TCBlockRegistry.bambooChute.leafIcon;
		} else if (what.equals("leafFlipped")) {
			return TCBlockRegistry.bambooChute.leafFlippedIcon;
		} else {
			return null;
		}
	}

}
