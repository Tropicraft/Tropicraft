package net.tropicraft.core.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Info;
import net.tropicraft.core.common.entity.passive.EntityKoaHunter;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

@Mod.EventBusSubscriber(modid = Info.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftEntities {
    public static EntityType<EntityKoaHunter> KOA_HUNTER;
    public static EntityType<TropiCreeperEntity> TROPI_CREEPER;

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
        KOA_HUNTER = register(event, "koa", koaHunter());
        TROPI_CREEPER = register(event, "tropicreeper", tropicreeper());
    }

    private static <T extends Entity> EntityType<T> register(final RegistryEvent.Register<EntityType<?>> event, final String name, final EntityType.Builder<T> entityType) {
        final EntityType<T> entityTypeCreated = entityType.build(name);
        event.getRegistry().register(entityTypeCreated.setRegistryName(name));
        return entityTypeCreated;
    }

    private static EntityType.Builder<EntityKoaHunter> koaHunter() {
        return EntityType.Builder.create(EntityKoaHunter::new, EntityClassification.MISC)
                .size(0.6F, 1.95F)
                .setTrackingRange(64)
                .setUpdateInterval(3)
                .immuneToFire()
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TropiCreeperEntity> tropicreeper() {
        return EntityType.Builder.create(TropiCreeperEntity::new, EntityClassification.CREATURE)
                .size(0.6F, 1.7F)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }
}
