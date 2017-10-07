package net.tropicraft.core.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.Info;
import net.tropicraft.SandColors;
import net.tropicraft.core.client.entity.model.ModelScubaGear;

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
			return SandColors.getColor(tintIndex - 1);
		}
	};

	public static void renderItem(ItemStack stack, float scale) {
		if (stack != null) {

			GlStateManager.pushMatrix();
			{
				GlStateManager.disableLighting();

				GlStateManager.scale(scale, scale, scale);

				if (!Minecraft.getMinecraft().getRenderItem().shouldRenderItemIn3D(stack) || stack.getItem() instanceof ItemSkull) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				GlStateManager.pushAttrib();
				RenderHelper.enableStandardItemLighting();
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popAttrib();

				GlStateManager.enableLighting();
			}
			GlStateManager.popMatrix();
		}
	}
}
