package net.tropicraft.block;

import net.minecraft.block.material.Material;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockBambooChute extends BlockTropicraft {

	public BlockBambooChute() {
		super(Material.plants);
		setHardness(1.0F);
		setResistance(4.0F);
        float f = 0.375F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        this.setTickRandomly(true);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
	}
	
    @Override
    public int getRenderType() {
        return TCRenderIDs.bambooChute;
    }
}
