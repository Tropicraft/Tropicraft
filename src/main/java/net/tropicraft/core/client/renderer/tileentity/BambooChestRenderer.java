package net.tropicraft.core.client.renderer.tileentity;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.block.tileentity.BambooChestBlockEntity;
import org.jspecify.annotations.Nullable;

public class BambooChestRenderer extends ChestRenderer<BambooChestBlockEntity> {
    public static final SpriteId SPRITE = sprite("bamboo");
    public static final SpriteId LEFT_SPRITE = sprite("bamboo_left");
    public static final SpriteId RIGHT_SPRITE = sprite("bamboo_right");

    private static SpriteId sprite(String chestName) {
        return new SpriteId(Sheets.CHEST_SHEET, Tropicraft.id("entity/chest/" + chestName));
    }

    public BambooChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected @Nullable SpriteId getCustomSprite(BambooChestBlockEntity blockEntity, ChestRenderState renderState) {
        return switch (renderState.type) {
            case LEFT -> LEFT_SPRITE;
            case RIGHT -> RIGHT_SPRITE;
            default -> SPRITE;
        };
    }
}
