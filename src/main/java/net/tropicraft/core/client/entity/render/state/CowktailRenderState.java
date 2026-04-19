package net.tropicraft.core.client.entity.render.state;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.tropicraft.core.common.entity.passive.CowktailEntity;

public class CowktailRenderState extends LivingEntityRenderState {
    public CowktailEntity.Type type = CowktailEntity.Type.IRIS;
    public final BlockModelRenderState flowerModel = new BlockModelRenderState();
}
