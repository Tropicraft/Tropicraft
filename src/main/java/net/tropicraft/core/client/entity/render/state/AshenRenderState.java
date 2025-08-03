package net.tropicraft.core.client.entity.render.state;

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AshenRenderState extends ArmedEntityRenderState {
    public AshenEntity.AshenState actionState = AshenEntity.AshenState.PEACEFUL;
    public boolean swinging;
    public boolean hasMask;
    public byte maskType;
}
