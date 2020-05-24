package net.tropicraft.core.client;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SkullItem;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;

public class TropicraftRenderUtils {

    /**
     * Cache of ResourceLocations for texture binding
     */
    private static Map<String, ResourceLocation> resLocMap = Maps.newHashMap();
    private static Map<String, Material> materialMap = Maps.newHashMap();

    public static IVertexBuilder getEntityCutoutBuilder(final IRenderTypeBuffer buffer, final ResourceLocation resourceLocation) {
        return buffer.getBuffer(RenderType.getEntityCutout(resourceLocation));
    }

    public static IBakedModel getBakedModel(final ItemRenderer itemRenderer, final ItemStack itemStack) {
        return itemRenderer.getItemModelMesher().getItemModel(itemStack);
    }

    public static void renderModel(final Material material, final Model model, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        model.render(stack, buffer.getBuffer(model.getRenderType(material.getTextureLocation())), combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
    }

    public static Material getBlockMaterial(final String path) {
        return materialMap.computeIfAbsent(path, m -> createBlockMaterial(path));
    }

    private static Material createBlockMaterial(final String path) {
        return new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, getTextureBlock(path));
    }

    public static Material getTEMaterial(final String path) {
        return materialMap.computeIfAbsent(path, m -> createTEMaterial(path));
    }

    private static Material createTEMaterial(final String path) {
        return new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, getTextureTE(path));
    }

    public static ResourceLocation getTexture(String path) {
        return resLocMap.computeIfAbsent(path, k -> getResLoc(path));
    }

    private static ResourceLocation getResLoc(String path) {
        return new ResourceLocation(Constants.MODID, path);
    }

    public static ResourceLocation getTextureArmor(String path) {
        return getTexture(String.format("textures/models/armor/%s.png", path));
    }

    public static ResourceLocation getTextureBlock(String path) {
        return getTexture(String.format("textures/block/%s.png", path));
    }

    public static ResourceLocation getTextureEntity(String path) {
        return getTexture(String.format("textures/entity/%s.png", path));
    }

    public static ResourceLocation getTextureGui(String path) {
        return getTexture(String.format("textures/gui/%s.png", path));
    }

    public static ResourceLocation getTextureTE(String path) {
        return getTexture(String.format("textures/block/te/%s.png", path));
    }

    public static ResourceLocation bindTextureArmor(String path) {
        return bindTexture(getTextureArmor(path));
    }

    public static ResourceLocation bindTextureEntity(String path) {
        return bindTexture(getTextureEntity(path));
    }

    public static ResourceLocation bindTextureGui(String path) {
        return bindTexture(getTextureGui(path));
    }

    public static ResourceLocation bindTextureTE(String path) {
        return bindTexture(getTextureTE(path));
    }

    public static ResourceLocation bindTextureBlock(String path) {
        return bindTexture(getTextureBlock(path));
    }

    public static ResourceLocation bindTexture(ResourceLocation resource) {
        Minecraft.getInstance().getTextureManager().bindTexture(resource);
        return resource;
    }

    public static void renderItem(ItemStack itemStack, final float scale, boolean leftHand, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn) {
        if (!itemStack.isEmpty()) {
            RenderSystem.pushMatrix();
            RenderSystem.disableLighting();
            RenderSystem.scalef(scale, scale, scale);

            // TODO what is this now?
            if (/*!Minecraft.getInstance().getItemRenderer().shouldRenderItemIn3D(stack) || */itemStack.getItem() instanceof SkullItem) {
                RenderSystem.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }

            RenderSystem.pushLightingAttributes();
            RenderHelper.enableStandardItemLighting();
            Minecraft.getInstance().getItemRenderer().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED, leftHand, stack, buffer, combinedLightIn, combinedOverlayIn, modelIn);
            RenderHelper.disableStandardItemLighting();
            RenderSystem.popAttributes();

            RenderSystem.enableLighting();
            RenderSystem.popMatrix();
        }
    }
}