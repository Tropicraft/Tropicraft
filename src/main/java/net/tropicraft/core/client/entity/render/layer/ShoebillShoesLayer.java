package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.equipment.Equippable;
import net.tropicraft.core.client.entity.model.ShoebillStorkModel;
import net.tropicraft.core.client.entity.render.state.ShoebillStorkRenderState;

public class ShoebillShoesLayer extends RenderLayer<ShoebillStorkRenderState, ShoebillStorkModel> {
    private final HumanoidArmorModel<HumanoidRenderState> humanoidShoesModel;
    private final EquipmentLayerRenderer equipmentRenderer;

    public ShoebillShoesLayer(RenderLayerParent<ShoebillStorkRenderState, ShoebillStorkModel> renderer, HumanoidArmorModel<HumanoidRenderState> humanoidShoesModel, EquipmentLayerRenderer equipmentRenderer) {
        super(renderer);
        this.humanoidShoesModel = humanoidShoesModel;
        this.equipmentRenderer = equipmentRenderer;

        humanoidShoesModel.setAllVisible(false);
        humanoidShoesModel.rightLeg.visible = true;
        humanoidShoesModel.leftLeg.visible = true;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ShoebillStorkRenderState state, float yRot, float xRot) {
        Equippable equippable = state.feetEquipment.get(DataComponents.EQUIPPABLE);
        if (equippable != null && HumanoidArmorLayer.shouldRender(state.feetEquipment, EquipmentSlot.FEET)) {
            getParentModel().copyShoesPoseTo(humanoidShoesModel);
            equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(), humanoidShoesModel, state.feetEquipment, poseStack, bufferSource, packedLight);
        }
    }
}
