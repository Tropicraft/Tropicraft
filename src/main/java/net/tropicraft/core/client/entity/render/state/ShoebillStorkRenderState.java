package net.tropicraft.core.client.entity.render.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class ShoebillStorkRenderState extends LivingEntityRenderState {
    public float flightAnimation;
    public final Vector3f leftFootPos = new Vector3f();
    public final Vector3f rightFootPos = new Vector3f();
    public ItemStack feetEquipment = ItemStack.EMPTY;
}
