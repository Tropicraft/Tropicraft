package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.util.TropicraftWorldUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicsPortal extends BlockFluidClassic {
    
    /** Amount of time player must spend in teleport block to teleport */
    private static final int TIME_UNTIL_TELEPORT = 250;

    public BlockTropicsPortal(Fluid fluid, Material material) {
        super(fluid, material);
        this.setCreativeTab(null);
        setTickRandomly(true);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
    }
    
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
        if (!world.isRemote && entity instanceof EntityPlayerMP && world.getBlockMetadata(i, j, k) == 8) {
            EntityPlayerMP player = (EntityPlayerMP)entity;
            entity.setAir(300);
            player.timeUntilPortal++;

            // DEBUG:      System.out.println(((EntityPlayerMP)entity).timeUntilPortal);

            if (player.timeUntilPortal > TIME_UNTIL_TELEPORT)
            {
                if (player.isPotionActive(Potion.confusion.id)) {
                    player.timeUntilPortal = 0;
                    player.removePotionEffect(Potion.confusion.id);
                    TropicraftWorldUtils.teleportPlayer(player);
                } else {
                    player.timeUntilPortal = 0;
                    player.addChatMessage(new ChatComponentText("You should drink a pi\u00f1a colada before teleporting!"));
                }
            }
        }
    }

    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        return false;
    }
    
    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        
    }
    
    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        return false;
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
        return Float.MAX_VALUE;
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
        return TCBlockRegistry.tropicsWater.getIcon(side, meta);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
        if (iblockaccess.isAirBlock(i, j, k)) {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int i, int j, int k, int side) {
        Material material = world.getBlock(i, j, k).getMaterial();
        if (material == blockMaterial) {
            return false;
        }
        if (side == 1) {
            return true;
        }
        return false;

    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World par1World) {
        return 10;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return null;
    }

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3) {
        return null;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        if (world.isRemote) {
            sparkle(world, i, j, k);
        }
    }

    /**
     * Renders particle effects in the water to give it a shimmer
     * @param world
     * @param i
     * @param j
     * @param k
     */
    @SideOnly(Side.CLIENT)
    private void sparkle(World world, int i, int j, int k) {
        Random random = world.rand;

        int maxCount = 2;

        if (world.getBlockMetadata(i, j, k) == 0 && world.isRemote) {
            for (int count = 0; count < maxCount; count++) {
                world.spawnParticle("bubble", i + random.nextDouble(), j + random.nextDouble(), k + random.nextDouble(), 0D, 0D, 0D);
            }
        }

        if (world.isAirBlock(i, j + 1, k) && world.isRemote) {
            for (int count = 0; count < maxCount; count++) {
                world.spawnParticle("splash", i + random.nextDouble(), j + 0.9, k + random.nextDouble(), 0D, 0D, 0D);
            }
        }
    }
}
