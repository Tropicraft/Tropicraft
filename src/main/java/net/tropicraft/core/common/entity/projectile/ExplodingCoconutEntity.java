package net.tropicraft.core.common.entity.projectile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

public class ExplodingCoconutEntity extends ThrowableItemProjectile {
    
    public ExplodingCoconutEntity(EntityType<? extends ExplodingCoconutEntity> type, Level world) {
        super(type, world);
    }
    
    public ExplodingCoconutEntity(Level world, LivingEntity thrower) {
        super(TropicraftEntities.EXPLODING_COCONUT.get(), thrower, world);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHit(HitResult result) {
        if (!level.isClientSide) {
            level.explode(this, getX(), getY(), getZ(), 2.4F, Explosion.BlockInteraction.DESTROY);
            remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return TropicraftItems.EXPLODING_COCONUT.get();
    }
}
