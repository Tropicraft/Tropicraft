package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.tropicraft.core.common.entity.BambooItemFrame;

public class BambooItemFrameRenderer extends ItemFrameRenderer<BambooItemFrame> {
    public static final StateDefinition<Block, BlockState> FAKE_BLOCK_STATE = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR)
            .add(BlockStateProperties.MAP)
            .create(Block::defaultBlockState, BlockState::new);

    private final BlockModelResolver blockModelResolver;

    public BambooItemFrameRenderer(EntityRendererProvider.Context context) {
        super(context);
        blockModelResolver = context.getBlockModelResolver();
    }

    @Override
    public void extractRenderState(BambooItemFrame entity, ItemFrameRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (!state.isInvisible) {
            BlockState fakeState = FAKE_BLOCK_STATE.any().setValue(BlockStateProperties.MAP, state.mapId != null);
            blockModelResolver.update(state.frameModel, fakeState, BLOCK_DISPLAY_CONTEXT);
        } else {
            state.frameModel.clear();
        }
    }
}
