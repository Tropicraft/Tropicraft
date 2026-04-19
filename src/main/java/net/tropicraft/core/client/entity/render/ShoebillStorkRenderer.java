package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;
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
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/shoebill_stork.png");

    public ShoebillStorkRenderer(EntityRendererProvider.Context context) {
        super(context, new ShoebillStorkModel(context.bakeLayer(TropicraftRenderLayers.SHOEBILL_STORK_LAYER)), 0.3f);
        addLayer(new ShoebillShoesLayer(this, context));
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
    public Identifier getTextureLocation(ShoebillStorkRenderState state) {
        return TEXTURE;
    }
}
