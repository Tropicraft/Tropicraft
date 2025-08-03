package net.tropicraft.core.common.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;
import net.tropicraft.core.common.TropicraftTags;

public class TropicraftToolMaterials {
    public static final ToolMaterial BAMBOO = new ToolMaterial(
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            110,
            1.2f,
            0.0f,
            6,
            TropicraftTags.Items.BAMBOO_TOOL_MATERIALS
    );
    public static final ToolMaterial ZIRCON = new ToolMaterial(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            200,
            4.5f,
            1.0f,
            14,
            TropicraftTags.Items.ZIRCON_GEM
    );
    public static final ToolMaterial EUDIALYTE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            750,
            6.5f,
            2.0f,
            14,
            TropicraftTags.Items.EUDIALYTE_GEM
    );
    public static final ToolMaterial ZIRCONIUM = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            1800,
            8.5f,
            3.0f,
            10,
            TropicraftTags.Items.ZIRCONIUM_GEM
    );
}
