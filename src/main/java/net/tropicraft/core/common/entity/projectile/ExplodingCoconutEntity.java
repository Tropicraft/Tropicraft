package net.tropicraft.core.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.tropicraft.core.common.item.TropicraftItems;

public class ExplodingCoconutEntity extends ProjectileItemEntity {
    public ExplodingCoconutEntity(final EntityType<? extends ProjectileItemEntity> type, World world) {
        super(type, world);
    }

    @Override
	protected void onImpact(RayTraceResult result) {
        // TODO - why isn't this being called?
        if (!world.isRemote) {
            world.createExplosion(this, posX, posY, posZ, 2.4F, Explosion.Mode.DESTROY);
            remove();
        }
	}

    @Override
    protected Item getDefaultItem() {
        return TropicraftItems.EXPLODING_COCONUT.get();
    }
}
