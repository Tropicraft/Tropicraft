package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;

import static net.minecraft.client.renderer.Atlases.CHEST_ATLAS;

@OnlyIn(Dist.CLIENT)
public class BambooChestRenderer extends ChestTileEntityRenderer<BambooChestTileEntity> {
//
    public static final Material CHEST_MATERIAL = getChestMaterial("normal");
    public static final Material CHEST_LEFT_MATERIAL = getChestMaterial("normal_left");
    public static final Material CHEST_RIGHT_MATERIAL = getChestMaterial("normal_right");

    public static ResourceLocation REGULAR_TEXTURE = TropicraftRenderUtils.getTextureTE("bamboo_chest");
    public static ResourceLocation LARGE_TEXTURE = TropicraftRenderUtils.getTextureTE("large_bamboo_chest");

    public BambooChestRenderer(TileEntityRendererDispatcher renderDispatcher) {
        super(renderDispatcher);
    }

    @Override
    protected Material getMaterial(BambooChestTileEntity tileEntity, ChestType chestType) {
        // TODO redo bamboo chest texture to use left/right correctly
        return getChestMaterial(chestType, CHEST_MATERIAL, CHEST_LEFT_MATERIAL, CHEST_RIGHT_MATERIAL);
    }

    private static Material getChestMaterial(ChestType chestType, Material normalMaterial, Material leftMaterial, Material rightMaterial) {
        switch(chestType) {
            case LEFT:
                return leftMaterial;
            case RIGHT:
                return rightMaterial;
            case SINGLE:
            default:
                return normalMaterial;
        }
    }

    private static Material getChestMaterial(String p_228774_0_) {
        return new Material(CHEST_ATLAS, new ResourceLocation(Constants.MODID, "entity/chest/" + p_228774_0_));
    }
}