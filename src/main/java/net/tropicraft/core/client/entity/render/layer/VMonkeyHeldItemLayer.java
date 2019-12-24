package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class VMonkeyHeldItemLayer<T extends VMonkeyEntity, M extends EntityModel<T> & IHasArm> extends HeldItemLayer<T, M> {
    public VMonkeyHeldItemLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(T monkey, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
        if (monkey.isSitting() && !monkey.getHeldItemMainhand().isEmpty()) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 1.30F, -0.425F);
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            float scale = 0.5F;
            GL11.glScalef(scale, scale, scale);
            Minecraft.getInstance().getItemRenderer().renderItem(monkey.getHeldItemMainhand(), ItemCameraTransforms.TransformType.NONE);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected void translateToHand(HandSide handSide) {
        GlStateManager.translatef(0.09375F, 0.1875F, 0.0F);
    }
}
