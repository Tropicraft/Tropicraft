package net.tropicraft.core.client.entity.render.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.tropicraft.core.client.entity.model.ModelAshen;
import net.tropicraft.core.client.entity.model.ModelVMonkey;
import net.tropicraft.core.common.entity.hostile.EntityAshen;
import net.tropicraft.core.common.entity.hostile.EntityAshenHunter;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.registry.ItemRegistry;
import org.lwjgl.opengl.GL11;

public class LayerHeldItemVMonkey extends LayerHeldItem {
    private ModelVMonkey modelVMonkey;

    public LayerHeldItemVMonkey(RenderLivingBase<?> livingEntityRendererIn, ModelVMonkey model) {
        super(livingEntityRendererIn);
        this.modelVMonkey = model;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack itemstack = entitylivingbaseIn.getHeldItemMainhand();

        if (!itemstack.isEmpty()) {
            GlStateManager.pushMatrix();
            this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            GlStateManager.popMatrix();
        }
    }

    private void renderHeldItem(EntityLivingBase entity, ItemStack itemstack, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide) {
        if (!itemstack.isEmpty() && entity instanceof EntityVMonkey) {
            this.renderEquippedItems((EntityVMonkey)entity);
        }
    }

    protected void renderEquippedItems(EntityVMonkey monkey) {
        if (monkey.isSitting() && !monkey.getHeldItemMainhand().isEmpty()) {
            GL11.glPushMatrix();
            //modelVMonkey.leftArm.postRender(0.0625F);
            GL11.glTranslatef(0.0F, 1.30F, -0.425F);
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            float scale = 0.5F;
            GL11.glScalef(scale, scale, scale);
            Minecraft.getMinecraft().getItemRenderer().renderItem(monkey, monkey.getHeldItemMainhand(), ItemCameraTransforms.TransformType.NONE);
            GL11.glPopMatrix();
        }
    }
}
