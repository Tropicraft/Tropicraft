package net.tropicraft.core.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.SandColors;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.ITropicraftBlock;

public class TropicraftRenderUtils {

	public static ResourceLocation getTexture(String path) {
		ResourceLocation derp = new ResourceLocation(Info.MODID, path);
		return derp;
	}

	public static ResourceLocation getTextureArmor(String path) {
		return getTexture(String.format("textures/models/armor/%s.png", path));
	}

	public static ResourceLocation getTextureBlock(String path) {
		return getTexture(String.format("textures/blocks/%s.png", path));
	}

	public static ResourceLocation getTextureEntity(String path) {
		return getTexture(String.format("textures/entity/%s.png", path));
	}

	public static ResourceLocation getTextureGui(String path) {
		return getTexture(String.format("textures/gui/%s.png", path));
	}

	public static ResourceLocation getTextureTE(String path) {
		return getTexture(String.format("textures/blocks/te/%s.png", path));
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
		Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
		return resource;
	}
	
    public static final IBlockColor SAND_COLORING = new IBlockColor() {
        @Override
    	@SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
            return SandColors.getColor(state.getBlock().getMetaFromState(state));
        }
    };
    
    public static final IItemColor BLOCK_ITEM_COLORING = new IItemColor() {
        @Override
        public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            return SandColors.getColor(tintIndex);
        }
    };
}
