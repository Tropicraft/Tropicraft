package net.tropicraft.core.client.entity.render.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class EggRenderState extends LivingEntityRenderState {
    public boolean hatching;
    public float randRotater;
    public String texture = "";
    public boolean shouldRenderFlat;
}
