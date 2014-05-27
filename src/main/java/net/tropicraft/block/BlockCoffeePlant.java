package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCoffeePlant extends BlockTropicraft {

	/** Number of bushes high this plant can grow */
	public static final int MAX_HEIGHT = 3;

	/** The growth rate when this plant is fertile */
	public static final int GROWTH_RATE_FERTILE = 10;

	/** The growth rate when this plant is infertile */
	public static final int GROWTH_RATE_INFERTILE = 20;

	/** The ripening rate when fertile */
	public static final int RIPENING_RATE_FERTILE = 12;

	/** The ripening rate when infertile */
	public static final int RIPENING_RATE_INFERTILE = 25;

	@SideOnly(Side.CLIENT)
	public IIcon[] leafIcons;

	@SideOnly(Side.CLIENT)
	public IIcon stemIcon;

	public BlockCoffeePlant() {
		super(Material.plants);
		setTickRandomly(true);
		disableStats();
		this.setCreativeTab(null);
	}
	
	   /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return TCItemRegistry.coffeeBean;
    }

	@Override
	public IIcon getIcon(int side, int meta) {
		meta = meta&7; // last three bits
		return leafIcons[meta];
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		// top
		// x,y,z is not a bug - RenderBlocks passes in the coords of the neighboring block
		if (side == 1 && world.getBlock(x,y,z) == this) {
			return false;
		}

		// bottom
		if (side == 0 && world.getBlock(x,y,z) == this) {
			return false;
		}

		return super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}
	
	private void tryToGrowUpwards(World world, int x, int y, int z, Random random) {
        if (world.isAirBlock(x,y+1,z)) {
            int height;
            for (height = 1; world.getBlock(x,y-height,z) == this; ++height);

            if (height < MAX_HEIGHT && random.nextInt(isFertile(world, x, y-height, z) ? GROWTH_RATE_FERTILE : GROWTH_RATE_INFERTILE) == 0) {
                world.setBlock(x, y+1, z, this, 0, 2);
            }
        }
    }
	
	private void tryToRipen(World world, int x, int y, int z, Random random) {
        int meta = world.getBlockMetadata(x, y, z)&7;

        // sanity check, malqua's meta shifter might do this to us
        if (meta == 7) {
            world.setBlockMetadataWithNotify(x, y, z, 6, 2);
            return;
        }

        // already fully ripe?
        if (meta == 6) {
            return;
        }

        // don't grow in darkness
        if (world.getBlockLightValue(x, y + 1, z) < 9) {
            return;
        }

        // random chance of ripening
        if (random.nextInt(isFertile(world, x, y - 1, z) ? RIPENING_RATE_FERTILE : RIPENING_RATE_INFERTILE) != 0) {
            return;
        }

        // ripen
        world.setBlockMetadataWithNotify(x, y, z, meta+1, 2);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.isRemote) {
            return;
        }

        tryToGrowUpwards(world, x, y, z, random);
        tryToRipen(world, x, y, z, random);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ) {
        if ((world.getBlockMetadata(x, y, z) & 7) != 6) {
            return false;
        }

        // no world.isRemote check needed - dropBlockAsItem_do contains one already
        world.setBlock(x, y, z, this, 0, 3);
        ItemStack stack = new ItemStack(TCItemRegistry.coffeeBean, 1, 2);
        dropBlockAsItem(world, x, y, z, stack);
        return true;
    }
    
    /**
     *  Replaces idDropped
     */
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return TCItemRegistry.coffeeBean;
    }

    @Override
    public int damageDropped(int meta) {
        return 2;
    }
    
    /**
     * Called when a user uses the creative pick block button on this block
     * 
     * Replaces idPicked
     *
     * @param target The full target the player is looking at
     * @return A ItemStack to add to the player's inventory, Null if nothing should be added.
     */
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(TCItemRegistry.coffeeBean, 1, 0);
    }

    @Override
    public int getDamageValue(World par1World, int par2, int par3, int par4) {
        return 0;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
        return meta == 6 ? 1 : 0;
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, Block neighborBlock) {
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean canBlockStay (World world, int x, int y, int z) {
        Block soil = world.getBlock(x, y - 1, z);
        return (world.getFullBlockLightValue(x, y, z) >= 8 ||
                world.canBlockSeeTheSky(x, y, z))
                && (soil != null && (soil == this || soil.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (IPlantable)TCItemRegistry.coffeeBean)));
    }

    @Override
    public int getRenderType() {
        return TCRenderIDs.coffeePlant;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        leafIcons = new IIcon[7];
        
		for (int i = 0 ; i < 7 ; i++) {
			leafIcons[i] = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + i);
		}

        stemIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + "Stem");
    }

}
