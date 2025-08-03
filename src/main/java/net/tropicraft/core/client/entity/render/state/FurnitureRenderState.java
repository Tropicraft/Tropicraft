package net.tropicraft.core.client.entity.render.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.item.DyeColor;

public class FurnitureRenderState extends EntityRenderState {
    public DyeColor color = DyeColor.WHITE;
    public float rockingAngle;
    public float yRot;
}
