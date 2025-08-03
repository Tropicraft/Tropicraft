package net.tropicraft.core.client.entity.render.state;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

public class KoaRenderState extends HumanoidRenderState {
    public boolean isDancing;
    public boolean isSitting;
    public EntityKoaBase.Genders gender = EntityKoaBase.Genders.MALE;
    public EntityKoaBase.Roles role = EntityKoaBase.Roles.HUNTER;
}
