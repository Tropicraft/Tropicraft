package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TropiSkellyHeldItemLayer<T extends LivingEntity, M extends EntityModel<T> & IHasArm> extends HeldItemLayer<T, M> {
    public TropiSkellyHeldItemLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    protected void translateToHand(HandSide handSide) {
        GlStateManager.translatef(0.09375F, 0.1875F, 0.0F);
    }
}
