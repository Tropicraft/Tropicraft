package net.tropicraft.core.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.level.Level;
import net.tropicraft.Constants;

import java.util.Locale;
import java.util.Map;

public class TropicraftRenderUtils {

    /**
     * Cache of ResourceLocations for texture binding
     */
    private static final Map<String, ResourceLocation> resLocMap = Maps.newHashMap();
    private static final Map<String, Material> materialMap = Maps.newHashMap();

    public static VertexConsumer getEntityCutoutBuilder(MultiBufferSource buffer, ResourceLocation resourceLocation) {
        return buffer.getBuffer(RenderType.entityCutout(resourceLocation));
    }

    public static BakedModel getBakedModel(ItemRenderer itemRenderer, ItemStack itemStack) {
        return itemRenderer.getItemModelShaper().getItemModel(itemStack);
    }

    public static void renderModel(Material material, Model model, PoseStack stack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        model.renderToBuffer(stack, buffer.getBuffer(model.renderType(material.texture())), combinedLightIn, combinedOverlayIn, CommonColors.WHITE);
    }

    public static Material getBlockMaterial(String path) {
        return materialMap.computeIfAbsent(path, m -> createBlockMaterial(path));
    }

    private static Material createBlockMaterial(String path) {
        return new Material(TextureAtlas.LOCATION_BLOCKS, getTextureBlock(path));
    }

    public static Material getTEMaterial(String path) {
        return materialMap.computeIfAbsent(path, m -> createTEMaterial(path));
    }

    private static Material createTEMaterial(String path) {
        return new Material(TextureAtlas.LOCATION_BLOCKS, getTextureTE(path));
    }

    public static ResourceLocation getTexture(String path) {
        return resLocMap.computeIfAbsent(path, k -> getResLoc(path));
    }

    private static ResourceLocation getResLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MODID, path);
    }

    public static ResourceLocation getTextureArmor(String path) {
        return getTexture(String.format(Locale.ROOT, "textures/models/armor/%s.png", path));
    }

    public static ResourceLocation getTextureBlock(String path) {
        return getTexture(String.format(Locale.ROOT, "textures/block/%s.png", path));
    }

    public static ResourceLocation getTextureEntity(String path) {
        return getTexture(String.format(Locale.ROOT, "textures/entity/%s.png", path));
    }

    public static ResourceLocation getTextureGui(String path) {
        return getTexture(String.format(Locale.ROOT, "textures/gui/%s.png", path));
    }

    public static ResourceLocation getTextureTE(String path) {
        return getTexture(String.format(Locale.ROOT, "textures/block/te/%s.png", path));
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
        Minecraft.getInstance().getTextureManager().bindForSetup(resource);
        return resource;
    }

    public static void renderItem(ItemStack itemStack, float scale, boolean leftHand, PoseStack stack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn, BakedModel modelIn, int seed, Level level) {
        if (!itemStack.isEmpty()) {
            stack.pushPose();
            stack.scale(scale, scale, scale);

            // TODO what is this now?
            if (/*!Minecraft.getInstance().getItemRenderer().shouldRenderItemIn3D(stack) || */itemStack.getItem() instanceof PlayerHeadItem) {
                stack.mulPose(Axis.YP.rotationDegrees(180.0F));
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, stack, buffer, level, seed);
            stack.popPose();
        }
    }
}
