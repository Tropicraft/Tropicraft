package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;


public class BlockTropicraftOre extends BlockTropicraft {

    private Random rand = new Random();
    
	public BlockTropicraftOre() {
		super(Material.rock); // closest to ore
	}
	
	@Override
	public Item getItemDropped(int metadata, Random p_149650_2_, int fortune) {
        return TCItemRegistry.ore;
    }

	@Override
    public int quantityDropped(int meta, int fortune, Random random) {
	    if (this == TCBlockRegistry.eudialyteOre) return 4 + random.nextInt(3);
	    if (this == TCBlockRegistry.zirconOre) return 2 + random.nextInt(2);
	    if (this == TCBlockRegistry.azuriteOre) return 5 + random.nextInt(7);

        return 1;
    }

	@Override
    public int quantityDroppedWithBonus(int metadata, Random random) {
        if (metadata > 0 && Item.getItemFromBlock(this) != this.getItemDropped(0, random, metadata)) {
            int j = random.nextInt(metadata + 2) - 1;

            if (j < 0) {
                j = 0;
            }

            return this.quantityDropped(metadata, 0, random) * (j + 1);
        } else {
            return this.quantityDropped(metadata, 0, random);
        }
    }
    
    @Override
    public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
        if (this.getItemDropped(metadata, rand, fortune) != Item.getItemFromBlock(this)) {
            int j1 = 0;

            if (this == TCBlockRegistry.azuriteOre) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 5, 7);
            } else if (this == TCBlockRegistry.eudialyteOre) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 3, 5);
            } else if (this == TCBlockRegistry.zirconOre) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 1, 3);
            }

            return j1;
        }
        return 0;
    }
    
    @Override
    public int damageDropped(int metadata) {
        if (this == TCBlockRegistry.eudialyteOre) return 0;
        if (this == TCBlockRegistry.zirconOre) return 1;
        if (this == TCBlockRegistry.azuriteOre) return 2;
        
        return 0;
    }	
}
