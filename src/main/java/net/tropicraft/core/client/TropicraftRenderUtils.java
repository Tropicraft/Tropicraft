package net.tropicraft.core.client;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;

import java.util.Map;

public class TropicraftRenderUtils {

    /**
     * Cache of ResourceLocations for texture binding
     */
    private static Map<String, ResourceLocation> resLocMap = Maps.newHashMap();

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
}