package net.tropicraft.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntitySifter;
import net.tropicraft.registry.TCItemRegistry;

public class BlockSifter extends BlockTropicraft implements ITileEntityProvider {

    public BlockSifter() {
        super(Material.wood);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntitySifter();
    }
    
    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return true;
    }
    
    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int d, float f1, float f2, float f3) {
        if (world.isRemote) {
            return true;
        }
        
        ItemStack stack = entityplayer.inventory.getCurrentItem();

        TileEntitySifter tileentitysifta = (TileEntitySifter) world.getTileEntity(i, j, k);

        if (tileentitysifta != null && stack != null && !tileentitysifta.isSifting()) {
            Item helditem = stack.getItem();
            if (helditem == Item.getItemFromBlock(Blocks.sand) || (helditem == TCItemRegistry.ore && stack.getItemDamage() == 5)) {
            entityplayer.getCurrentEquippedItem().stackSize--;
            tileentitysifta.setSifting(true);
            }
          //  System.out.println("setting te sifta" + tileentitysifta.sifting);
        }
        return true;
    } // /o/ \o\ /o\ \o\ /o\ \o/ /o/ /o/ \o\ \o\ /o/ /o/ \o/ /o\ \o/ \o/ /o\ /o\ \o/ \o/ /o/ \o\o\o\o\o\o\o\o\o\ :D

}
