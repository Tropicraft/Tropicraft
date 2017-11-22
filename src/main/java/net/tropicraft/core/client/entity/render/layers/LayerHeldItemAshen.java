package net.tropicraft.core.client.entity.render.layers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.tropicraft.core.client.entity.model.ModelAshen;
import net.tropicraft.core.common.entity.hostile.EntityAshen;
import net.tropicraft.core.common.entity.hostile.EntityAshenHunter;
import net.tropicraft.core.registry.ItemRegistry;

public class LayerHeldItemAshen extends LayerHeldItem {

	private ModelAshen modelAshen;

	public LayerHeldItemAshen(RenderLivingBase<?> livingEntityRendererIn, ModelAshen model) {
		super(livingEntityRendererIn);
		this.modelAshen = model;
	}

	@Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
        ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
        ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();

        if (!itemstack.isEmpty() || !itemstack1.isEmpty())
        {
            GlStateManager.pushMatrix();

            if (this.livingEntityRenderer.getMainModel().isChild)
            {
                float f = 0.5F;
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderHeldItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            GlStateManager.popMatrix();
        }
    }
	
	private void renderHeldItem(EntityLivingBase entity, ItemStack itemstack, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide)
	{
		if (!itemstack.isEmpty())
		{
			this.renderEquippedItems(entity);
		}
	}

	protected void renderEquippedItems(EntityLivingBase entityliving) {
		if (entityliving instanceof EntityAshenHunter) {
			if (((EntityAshen) entityliving).getActionState() == 2) {
				GL11.glPushMatrix();
				modelAshen.leftArm.postRender(0.0625F);
				GL11.glTranslatef(-0.35F, -0.45F, -0.025F);
				GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
				float scale = 0.5F;
				GL11.glScalef(scale, scale, scale);
				Minecraft.getMinecraft().getItemRenderer().renderItem(entityliving, new ItemStack(ItemRegistry.bambooSpear), TransformType.THIRD_PERSON_RIGHT_HAND);

				GL11.glPopMatrix();
				GL11.glPushMatrix();
				modelAshen.rightArm.postRender(0.0625F);

				GL11.glTranslatef(0.4F, -0.30F, -0.145F);
				GL11.glRotatef(130F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(5F, 0.0F, 0.0F, 1.0F);

				GL11.glScalef(scale, scale, scale);
				//TODO CHANGE TO BLOW GUN
				Minecraft.getMinecraft().getItemRenderer().renderItem(entityliving, new ItemStack(ItemRegistry.dagger), TransformType.THIRD_PERSON_LEFT_HAND);
				GL11.glPopMatrix();
			}
		}
	}

}
