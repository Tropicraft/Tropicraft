package net.tropicraft.core.client.entity.render.layer;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TropiSkellyHeldItemLayer<T extends LivingEntity, M extends EntityModel<T> & IHasArm> extends HeldItemLayer<T, M> {
    public TropiSkellyHeldItemLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }
}
