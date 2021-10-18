package net.tropicraft.core.common.block.huge_plant;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public final class HugePlantBlockHighlight {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    @SubscribeEvent
    public static void onHighlightBlock(DrawHighlightEvent.HighlightBlock event) {
        ClientWorld world = CLIENT.world;
        if (world == null) return;

        BlockPos pos = event.getTarget().getPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof HugePlantBlock) {
            renderHugePlantHighlight(event, world, pos, state);
        }
    }

    private static void renderHugePlantHighlight(DrawHighlightEvent.HighlightBlock event, ClientWorld world, BlockPos pos, BlockState state) {
        HugePlantBlock.Shape shape = HugePlantBlock.Shape.match(state.getBlock(), world, pos);
        if (shape == null) return;

        IVertexBuilder builder = event.getBuffers().getBuffer(RenderType.getLines());

        AxisAlignedBB aabb = shape.asAabb(event.getInfo().getProjectedView());
        WorldRenderer.drawBoundingBox(event.getMatrix(), builder, aabb, 0.0F, 0.0F, 0.0F, 0.4F);

        event.setCanceled(true);
    }
}
