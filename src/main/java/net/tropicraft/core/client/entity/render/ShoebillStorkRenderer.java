package net.tropicraft.core.client.entity.render;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.ShoebillStorkModel;
import net.tropicraft.core.client.entity.render.layer.ShoebillShoesLayer;
import net.tropicraft.core.client.entity.render.state.ShoebillStorkRenderState;
import net.tropicraft.core.common.entity.IkWalker;
import net.tropicraft.core.common.entity.passive.ShoebillStorkEntity;

public class ShoebillStorkRenderer extends MobRenderer<ShoebillStorkEntity, ShoebillStorkRenderState, ShoebillStorkModel> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/shoebill_stork.png");

    public ShoebillStorkRenderer(EntityRendererProvider.Context context) {
        super(context, new ShoebillStorkModel(context.bakeLayer(TropicraftRenderLayers.SHOEBILL_STORK_LAYER)), 0.3f);
        addLayer(new ShoebillShoesLayer(this, new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getEquipmentRenderer()));
    }

    @Override
    public ShoebillStorkRenderState createRenderState() {
        return new ShoebillStorkRenderState();
    }

    @Override
    public void extractRenderState(ShoebillStorkEntity entity, ShoebillStorkRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.flightAnimation = entity.getFlightAnimation(partialTicks);
        state.kickAnimation = entity.getKickAnimation(partialTicks);

        IkWalker.EntitySpace entitySpace = IkWalker.EntitySpace.from(entity, partialTicks);
        state.leftFootPos.set(entity.leftFoot().solveModelPosition(entitySpace, partialTicks));
        state.rightFootPos.set(entity.rightFoot().solveModelPosition(entitySpace, partialTicks));
        state.feetEquipment = getEquipmentIfRenderable(entity, EquipmentSlot.FEET);
    }

    private static ItemStack getEquipmentIfRenderable(LivingEntity entity, EquipmentSlot slot) {
        ItemStack itemStack = entity.getItemBySlot(slot);
        return HumanoidArmorLayer.shouldRender(itemStack, slot) ? itemStack.copy() : ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getTextureLocation(ShoebillStorkRenderState state) {
        return TEXTURE;
    }
}
