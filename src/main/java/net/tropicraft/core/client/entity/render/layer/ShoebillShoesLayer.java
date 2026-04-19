package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.equipment.Equippable;
import net.tropicraft.core.client.entity.model.ShoebillStorkModel;
import net.tropicraft.core.client.entity.render.ShoebillStorkRenderer;
import net.tropicraft.core.client.entity.render.state.ShoebillStorkRenderState;

public class ShoebillShoesLayer extends RenderLayer<ShoebillStorkRenderState, ShoebillStorkModel> {
    private final ShoesModel humanoidShoesModel;
    private final EquipmentLayerRenderer equipmentRenderer;

    public ShoebillShoesLayer(ShoebillStorkRenderer renderer, EntityRendererProvider.Context context) {
        super(renderer);
        humanoidShoesModel = new ShoesModel(context.bakeLayer(ModelLayers.PLAYER_ARMOR.feet()));
        equipmentRenderer = context.getEquipmentRenderer();
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ShoebillStorkRenderState state, float yRot, float xRot) {
        Equippable equippable = state.feetEquipment.get(DataComponents.EQUIPPABLE);
        if (equippable != null && HumanoidArmorLayer.shouldRender(state.feetEquipment, EquipmentSlot.FEET)) {
            RenderState shoesState = new RenderState(getParentModel(), state);
            equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(), humanoidShoesModel, shoesState, state.feetEquipment, poseStack, submitNodeCollector, state.lightCoords, state.outlineColor);
        }
    }

    private static class ShoesModel extends Model<RenderState> {
        private final ModelPart leftLeg;
        private final ModelPart rightLeg;

        public ShoesModel(ModelPart root) {
            super(root, RenderTypes::armorCutoutNoCull);
            leftLeg = root.getChild(PartNames.LEFT_LEG);
            rightLeg = root.getChild(PartNames.RIGHT_LEG);
        }

        @Override
        public void setupAnim(RenderState state) {
            super.setupAnim(state);
            state.model.setupAnim(state.state);
            state.model.copyShoesPoseTo(leftLeg, rightLeg);
        }
    }

    private record RenderState(
            ShoebillStorkModel model,
            ShoebillStorkRenderState state
    ) {
    }
}
