package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.tropicraft.core.common.block.tileentity.IMachineBlock;

public abstract class MachineModel<T extends BlockEntity & IMachineBlock> extends Model {
    public MachineModel() {
        super(RenderType::entitySolid);
    }

    public abstract Iterable<ModelPart> getParts();

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
        getParts().forEach((part) -> {
            part.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
        });
    }
}
