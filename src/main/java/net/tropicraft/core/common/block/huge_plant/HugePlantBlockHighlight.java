package net.tropicraft.core.common.block.huge_plant;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.WindowRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;
import net.tropicraft.Tropicraft;

@EventBusSubscriber(modid = Tropicraft.ID, value = Dist.CLIENT)
public final class HugePlantBlockHighlight {
    @SubscribeEvent
    public static void onHighlightBlock(ExtractBlockOutlineRenderStateEvent event) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }

        BlockPos pos = event.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof HugePlantBlock) {
            renderHugePlantHighlight(event, world, pos, state);
        }
    }

    private static void renderHugePlantHighlight(ExtractBlockOutlineRenderStateEvent event, ClientLevel world, BlockPos pos, BlockState state) {
        HugePlantBlock.Shape shape = HugePlantBlock.Shape.matchIncomplete(state.getBlock(), world, pos);
        if (shape == null) {
            return;
        }

        Vec3 view = event.getCamera().position();
        VoxelShape aabb = Shapes.create(shape.asAabb().move(-view.x, -view.y, -view.z));

        event.addCustomRenderer((renderState, bufferSource, poseStack, translucentPass, levelRenderState) -> {
            VertexConsumer builder = bufferSource.getBuffer(RenderTypes.lines());

            WindowRenderState windowRenderState = Minecraft.getInstance().gameRenderer.getGameRenderState().windowRenderState;
            ShapeRenderer.renderShape(poseStack, builder, aabb, 0.0f, 0.0f, 0.0f, ARGB.black(0x66), windowRenderState.appropriateLineWidth);
            bufferSource.endLastBatch();

            return true;
        });
    }
}
