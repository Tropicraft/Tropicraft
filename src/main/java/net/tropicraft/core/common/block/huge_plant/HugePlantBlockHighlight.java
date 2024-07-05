package net.tropicraft.core.common.block.huge_plant;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.tropicraft.Constants;

@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public final class HugePlantBlockHighlight {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    @SubscribeEvent
    public static void onHighlightBlock(RenderHighlightEvent.Block event) {
        ClientLevel world = CLIENT.level;
        if (world == null) return;

        BlockPos pos = event.getTarget().getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof HugePlantBlock) {
            renderHugePlantHighlight(event, world, pos, state);
        }
    }

    private static void renderHugePlantHighlight(RenderHighlightEvent.Block event, ClientLevel world, BlockPos pos, BlockState state) {
        HugePlantBlock.Shape shape = HugePlantBlock.Shape.matchIncomplete(state.getBlock(), world, pos);
        if (shape == null) return;

        VertexConsumer builder = event.getMultiBufferSource().getBuffer(RenderType.lines());

        Vec3 view = event.getCamera().getPosition();
        AABB aabb = shape.asAabb().move(-view.x, -view.y, -view.z);
        LevelRenderer.renderLineBox(event.getPoseStack(), builder, aabb, 0.0F, 0.0F, 0.0F, 0.4F);

        event.setCanceled(true);
    }
}
