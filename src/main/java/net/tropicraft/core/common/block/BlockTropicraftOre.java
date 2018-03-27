package net.tropicraft.core.common.block;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.core.common.enums.TropicraftOres;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class BlockTropicraftOre extends BlockTropicraftEnumVariants<TropicraftOres> {

	public BlockTropicraftOre() {
		super(Material.ROCK, TropicraftOres.class);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.STONE);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        switch (getVariant(state)) {
        case AZURITE:
            return ItemRegistry.azurite;
        case EUDIALYTE:
            return ItemRegistry.eudialyte;
        case ZIRCON:
            return ItemRegistry.zircon;
        case MANGANESE:
        case SHAKA:
            return Item.getItemFromBlock(BlockRegistry.ore);
        }
        return null;
    }

    @Override
    public int damageDropped(IBlockState state) {
        TropicraftOres variant = getVariant(state);
        if (variant == TropicraftOres.MANGANESE || variant == TropicraftOres.SHAKA) {
            return variant.ordinal();
        }
        return 0;
    }

	// TODO this is a lazy impl
    // your mom is a lazy impl
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        TropicraftOres variant = getVariant(state);
        if (variant == TropicraftOres.EUDIALYTE) {
            return 1 + random.nextInt(4 + fortune);
        } else if (variant == TropicraftOres.MANGANESE || variant == TropicraftOres.SHAKA) {
            return 1;
        } else {
            return 1 + random.nextInt(1 + fortune);
        }
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        return MathHelper.getInt(rand, 2, 7);
    }
    
    /**
     * Called when a user uses the creative pick block button on this block
     *
     * @param target The full target the player is looking at
     * @return A ItemStack to add to the player's inventory, empty itemstack if nothing should be added.
     */
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    	return new ItemStack(BlockRegistry.ore, 1, getVariant(state).ordinal());
    }
}
