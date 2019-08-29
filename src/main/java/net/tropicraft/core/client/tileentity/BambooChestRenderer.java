package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.client.renderer.tileentity.model.LargeChestModel;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;

@OnlyIn(Dist.CLIENT)
public class BambooChestRenderer extends TileEntityRenderer<BambooChestTileEntity> {

    public static ResourceLocation REGULAR_TEXTURE = TropicraftRenderUtils.getTextureTE("bamboo_chest");
    public static ResourceLocation LARGE_TEXTURE = TropicraftRenderUtils.getTextureTE("large_bamboo_chest");

    private ChestModel chestModel;
    private ChestModel chestModelLarge;

    public BambooChestRenderer() {
        chestModel = new ChestModel();
        chestModelLarge = new LargeChestModel();
    }

    @Override
    public void render(BambooChestTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        BlockState lvt_10_1_ = te.hasWorld() ? te.getBlockState() : (BlockState) Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = lvt_10_1_.has(ChestBlock.TYPE) ? (ChestType)lvt_10_1_.get(ChestBlock.TYPE) : ChestType.SINGLE;
        if (chestType != ChestType.LEFT) {
            boolean isDouble = chestType != ChestType.SINGLE;
            ChestModel model = this.getChestModel(te, destroyStage, isDouble);
            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.scalef(isDouble ? 8.0F : 4.0F, 4.0F, 1.0F);
                GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
                GlStateManager.matrixMode(5888);
            } else {
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.translatef((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GlStateManager.scalef(1.0F, -1.0F, -1.0F);
            float lvt_14_1_ = ((Direction)lvt_10_1_.get(ChestBlock.FACING)).getHorizontalAngle();
            if ((double)Math.abs(lvt_14_1_) > 1.0E-5D) {
                GlStateManager.translatef(0.5F, 0.5F, 0.5F);
                GlStateManager.rotatef(lvt_14_1_, 0.0F, 1.0F, 0.0F);
                GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
            }

            this.applyLidRotation(te, partialTicks, model);
            model.renderAll();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }

    private ChestModel getChestModel(BambooChestTileEntity te, int destroyStage, boolean isDouble) {
        ResourceLocation resourceLoc;
        if (destroyStage >= 0) {
            resourceLoc = DESTROY_STAGES[destroyStage];
        } else {
            resourceLoc = isDouble ? LARGE_TEXTURE : REGULAR_TEXTURE;
        }

        this.bindTexture(resourceLoc);
        return isDouble ? chestModelLarge : chestModel;
    }

    private void applyLidRotation(BambooChestTileEntity tileEntity, float angle, ChestModel model) {
        float trueAngle = ((IChestLid)tileEntity).getLidAngle(angle);
        trueAngle = 1.0F - trueAngle;
        trueAngle = 1.0F - trueAngle * trueAngle * trueAngle;
        model.getLid().rotateAngleX = -(trueAngle * 1.5707964F);
    }
}