package net.tropicraft.core.client.entity.render.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.tropicraft.core.common.entity.underdasea.StarfishType;

public class StarfishRenderState extends EntityRenderState {
    public StarfishType type = StarfishType.RED;
    public float growthProgress = 1.0f;
    public boolean hasRedOverlay;
}
