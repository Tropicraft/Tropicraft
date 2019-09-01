package net.tropicraft.core.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Info;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;
import net.tropicraft.core.common.entity.neutral.EIHEntity;
import net.tropicraft.core.common.entity.neutral.IguanaEntity;
import net.tropicraft.core.common.entity.passive.EntityKoaHunter;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

@Mod.EventBusSubscriber(modid = Info.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftEntities {
    public static EntityType<EntityKoaHunter> KOA_HUNTER;
    public static EntityType<TropiCreeperEntity> TROPI_CREEPER;
    public static EntityType<IguanaEntity> IGUANA;
    public static EntityType<UmbrellaEntity> UMBRELLA;
    public static EntityType<TropiSkellyEntity> TROPI_SKELLY;
    public static EntityType<EIHEntity> EIH;
    public static EntityType<WallItemEntity> WALL_ITEM;

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
        KOA_HUNTER = register(event, "koa", koaHunter());
        TROPI_CREEPER = register(event, "tropicreeper", tropicreeper());
        IGUANA = register(event, "iguana", iguana());
        UMBRELLA = register(event, "umbrella", umbrella());
        TROPI_SKELLY = register(event, "tropiskelly", tropiskelly());
        EIH = register(event, "eih", eih());
        WALL_ITEM = register(event, "wall_item", wallItem());
    }

    private static <T extends Entity> EntityType<T> register(final RegistryEvent.Register<EntityType<?>> event, final String name, final EntityType.Builder<T> entityType) {
        final EntityType<T> entityTypeCreated = entityType.build(name);
        event.getRegistry().register(entityTypeCreated.setRegistryName(name));
        return entityTypeCreated;
    }

    private static EntityType.Builder<WallItemEntity> wallItem() {
        return EntityType.Builder.<WallItemEntity>create(WallItemEntity::new, EntityClassification.MISC)
                .size(0.5F, 0.5F)
                .setTrackingRange(64)
                .setUpdateInterval(10)
                .setShouldReceiveVelocityUpdates(false)
                .setCustomClientFactory(($, world) -> new WallItemEntity(world));
    }

    private static EntityType.Builder<EIHEntity> eih() {
        return EntityType.Builder.create(EIHEntity::new, EntityClassification.CREATURE)
                .size(1.2F, 3.25F)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TropiSkellyEntity> tropiskelly() {
        return EntityType.Builder.create(TropiSkellyEntity::new, EntityClassification.CREATURE)
                .size(0.7F, 1.95F)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<UmbrellaEntity> umbrella() {
        return EntityType.Builder.<UmbrellaEntity>create(UmbrellaEntity::new, EntityClassification.MISC)
                .size(1.0F, 4.0F)
                .setTrackingRange(120)
                .setUpdateInterval(10)
                .setShouldReceiveVelocityUpdates(false)
                .setCustomClientFactory(($, world) -> new UmbrellaEntity(world));
    }

    private static EntityType.Builder<IguanaEntity> iguana() {
        return EntityType.Builder.create(IguanaEntity::new, EntityClassification.CREATURE)
                .size(1.0F, 0.4F)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .immuneToFire()
                .setShouldReceiveVelocityUpdates(true);
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
