package net.tropicraft.core.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.tropicraft.core.common.config.TropicraftConfig;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;

public class ExplodingCoconutEntity extends ProjectileItemEntity {
    
    public ExplodingCoconutEntity(EntityType<? extends ExplodingCoconutEntity> type, World world) {
        super(type, world);
    }
    
    public ExplodingCoconutEntity(World world, LivingEntity thrower) {
        super(TropicraftEntities.EXPLODING_COCONUT.get(), thrower, world);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!world.isRemote) {
            final Entity shooter = getShooter();
            if (shooter != null) {
                final boolean isOp = shooter instanceof PlayerEntity && ((PlayerEntity)shooter).canUseCommandBlock();

                // OPs and whitelisted users can use coconut bombs
                if (isOp || TropicraftConfig.coconutBombWhitelist.get().contains(shooter.getUniqueID().toString())) {
                    world.createExplosion(this, getPosX(), getPosY(), getPosZ(), 2.4F, Explosion.Mode.DESTROY);
                    remove();
                }
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return TropicraftItems.EXPLODING_COCONUT.get();
    }
}
