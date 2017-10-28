package net.tropicraft.core.common.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.tropicraft.core.common.enums.ITropicraftVariant;
import net.tropicraft.core.registry.CreativeTabRegistry;

public class ItemBlockTropicraft extends ItemBlock {

	/** Variants to associate with this ItemBlock */
	protected ITropicraftVariant[] variants;
	
	/** Block this ItemBlock represents */
	protected Block block;
	
	/**
	 * Leave variants null to have none.
	 */
	public ItemBlockTropicraft(Block block, ITropicraftVariant... variants) {
		super(block);
		this.setHasSubtypes(variants != null && variants.length > 1);
		this.setMaxDamage(0);
		this.block = block;
		this.variants = variants;
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
		if (this.variants == null || this.variants.length == 0) {
			return super.getUnlocalizedName(itemstack);
		}
		return "item." + variants[itemstack.getMetadata() % variants.length].getUnlocName();
    }
}
