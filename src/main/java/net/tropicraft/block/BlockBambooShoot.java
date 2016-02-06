package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBambooShoot extends BlockTropicraft implements IPlantable {
	
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
	
	public BlockBambooShoot() {
		super(Material.plants);
//		setHardness(1.0F);
//		setResistance(4.0F);
        float f = 0.375F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        this.setTickRandomly(true);
        this.setCreativeTab(null);
        this.setBlockTextureName(TCNames.bambooShoot);
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
       // return TCRenderIDs.bambooShoot;
		return 1;
    }

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
/*		sideIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_side");
		topIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_top");
		bottomIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_bottom");
		indentIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_indent");
		leafIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName() + "_leaf");
		leafFlippedIcon = new IconFlipped(leafIcon, true, false);*/
		this.blockIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + this.getTextureName());
	}
	
	public static IIcon getBambooIcon(String iconString) {
		return null;
/*		if (iconString.equals("side")) {
			return TCBlockRegistry.bambooShoot.sideIcon;
		} else if (iconString.equals("top")) {
			return TCBlockRegistry.bambooShoot.topIcon;
		} else if (iconString.equals("bottom")) {
			return TCBlockRegistry.bambooShoot.bottomIcon;
		} else if (iconString.equals("indent")) {
			return TCBlockRegistry.bambooShoot.indentIcon;
		} else if (iconString.equals("leaf")) {
			return TCBlockRegistry.bambooShoot.leafIcon;
		} else if (iconString.equals("leafFlipped")) {
			return TCBlockRegistry.bambooShoot.leafFlippedIcon;
		} else {
			return null;
		}*/
	}
	
	/**
	 * Called every random tick of this block, only on the server (YEAH OK THANKS NEWT)
	 * @param world World instance
	 * @param x xCoord in the world
	 * @param y yCoord in the world
	 * @param z zCoord in the world
	 * @param random java.util.Random instance
	 */
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (world.isAirBlock(x, y + 1, z)) {
			int plantHeight;	//number of blocks tall
			for (plantHeight = 1; world.getBlock(x, y - plantHeight, z) == TCBlockRegistry.bambooShoot; plantHeight++) {
			}
			if (plantHeight < 12) {
				int meta = world.getBlockMetadata(x, y, z);
				if (meta == 8) {
					world.setBlock(x, y + 1, z, TCBlockRegistry.bambooShoot);
					world.setBlockMetadataWithNotify(x, y, z, 0, 3);
				} else {
					world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
				}
			}			
		}		
	}
	
	
    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
    	Block idBelow = world.getBlock(i, j - 1, k);
		Block idAdjacentX1 = world.getBlock(i - 1, j - 1, k);
		Block idAdjacentX2 = world.getBlock(i + 1, j - 1, k);
		Block idAdjacentZ1 = world.getBlock(i, j - 1, k - 1);
		Block idAdjacentZ2 = world.getBlock(i, j - 1, k + 1);
		
		if (idBelow == TCBlockRegistry.bambooShoot) {
			return true;
		}
		if (idBelow != Blocks.grass && idBelow != Blocks.dirt && idBelow != Blocks.sand) {
			return false;
		}
		if (idAdjacentX1 == Blocks.dirt || idAdjacentX1 == Blocks.grass || idAdjacentX1 == Blocks.sand) {
			return true;
		}
		if (idAdjacentX2 == Blocks.dirt || idAdjacentX2 == Blocks.grass || idAdjacentX2 == Blocks.sand) {
			return true;
		}
		if (idAdjacentZ1 == Blocks.dirt || idAdjacentZ1 == Blocks.grass || idAdjacentZ1 == Blocks.sand) {
			return true;
		} else {
			return idAdjacentZ2 == Blocks.grass;
		}
    }
    
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
    
    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    @Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        return this.canPlaceBlockAt(par1World, par2, par3, par4);
    }
    
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborID)
    {
        this.checkBlockCoordValid(world, x, y, z);
    }
    
    /**
     * Checks if current block pos is valid, if not, breaks the block as dropable item. Used for reed and cactus.
     */
    protected final void checkBlockCoordValid(World world, int x, int y, int z)
    {
        if (!this.canBlockStay(world, x, y, z))
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }
    
    /**
     * Determines if this block can support the passed in plant, allowing it to be planted and grow.
     * Some examples:
     *   Reeds check if its a reed, or if its sand/dirt/grass and adjacent to water
     *   Cacti checks if its a cacti, or if its sand
     *   Nether types check for soul sand
     *   Crops check for tilled soil
     *   Caves check if it's a colid surface
     *   Plains check if its grass or dirt
     *   Water check if its still water
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z position
     * @param direction The direction relative to the given position the plant wants to be, typically its UP
     * @param plant The plant that wants to check
     * @return True to allow the plant to be planted/stay.
     */
    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
    {
        Block plantID = plant.getPlant(world, x, y + 1, z);
        EnumPlantType plantType = plant.getPlantType(world, x, y + 1, z);

        if (plantID == TCBlockRegistry.bambooShoot)
        {
            return true;
        }
        
    	Block blockID = TCBlockRegistry.bambooShoot;
    	
        switch (plantType)
        {
            case Desert: return blockID == Blocks.sand;
            case Nether: return blockID == Blocks.soul_sand;
            case Crop:   return blockID == Blocks.farmland;
            case Cave:   return isBlockSolid(world, x, y, z, direction.flag);
            case Plains: return blockID == Blocks.grass || blockID == Blocks.dirt;
            case Water:  return world.getBlock(x, y, z).getMaterial() == Material.water && world.getBlockMetadata(x, y, z) == 0;
            case Beach:
                boolean isBeach = (blockID == Blocks.grass || blockID == Blocks.dirt || blockID == Blocks.sand);
                boolean hasWater = (world.getBlock(x - 1, y, z    ).getMaterial() == Material.water ||
                                    world.getBlock(x + 1, y, z    ).getMaterial() == Material.water ||
                                    world.getBlock(x,     y, z - 1).getMaterial() == Material.water ||
                                    world.getBlock(x,     y, z + 1).getMaterial() == Material.water);
                return isBeach && hasWater;
        }

        return false;
    }

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
		return EnumPlantType.Plains;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z) {
		return TCBlockRegistry.bambooShoot;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}
	
	@Override
	public Item getItemDropped(int meta, Random rand, int unused) {
		return TCItemRegistry.bambooShoot;
	}
}
