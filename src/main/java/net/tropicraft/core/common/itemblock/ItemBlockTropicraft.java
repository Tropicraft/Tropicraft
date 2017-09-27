package net.tropicraft.core.common.itemblock;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.registry.CreativeTabRegistry;

public class ItemBlockTropicraft extends ItemBlock {

	/** Names to associate with this ItemBlock */
	protected String[] names;
	
	/** Block this ItemBlock represents */
	protected Block block;
	
	public ItemBlockTropicraft(Block block, ArrayList<String> names) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.block = block;
		this.names = names.toArray(new String[names.size()]);
		this.setCreativeTab(CreativeTabRegistry.tropicraftTab);
	}

	/**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
	@Override
    public int getMetadata(int id) {
        return id;
    }
	
    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
	@Override
    public String getUnlocalizedName(ItemStack itemstack) {		
		if (this.names == null) {
			return super.getUnlocalizedName(itemstack);
		}
        int i = MathHelper.clamp(itemstack.getItemDamage(), 0, names.length - 1);
        return super.getUnlocalizedName() + "_" + names[i];
    }
}
