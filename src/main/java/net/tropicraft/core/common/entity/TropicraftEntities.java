package net.tropicraft.core.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Info;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;
import net.tropicraft.core.common.entity.neutral.EIHEntity;
import net.tropicraft.core.common.entity.neutral.IguanaEntity;
import net.tropicraft.core.common.entity.passive.EntityKoaHunter;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;
import net.tropicraft.core.common.entity.projectile.LavaBallEntity;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Info.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Info.MODID);

    public static final RegistryObject<EntityType<EntityKoaHunter>> KOA_HUNTER = register("koa", TropicraftEntities::koaHunter);
    public static final RegistryObject<EntityType<TropiCreeperEntity>> TROPI_CREEPER = register("tropicreeper", TropicraftEntities::tropicreeper);
    public static final RegistryObject<EntityType<IguanaEntity>> IGUANA = register("iguana", TropicraftEntities::iguana);
    public static final RegistryObject<EntityType<UmbrellaEntity>> UMBRELLA = register("umbrella", TropicraftEntities::umbrella);
    public static final RegistryObject<EntityType<TropiSkellyEntity>> TROPI_SKELLY = register("tropiskelly", TropicraftEntities::tropiskelly);
    public static final RegistryObject<EntityType<EIHEntity>> EIH = register("eih", TropicraftEntities::eih);
    public static final RegistryObject<EntityType<WallItemEntity>> WALL_ITEM = register("wall_item", TropicraftEntities::wallItem);
    public static final RegistryObject<EntityType<BambooItemFrame>> BAMBOO_ITEM_FRAME = register("bamboo_item_frame", TropicraftEntities::bambooItemFrame);
    // TODO: Register again when volcano eruption is finished
    public static final RegistryObject<EntityType<LavaBallEntity>> LAVA_BALL = null;//register("lava_ball", TropicraftEntities::lavaBall);
    public static final RegistryObject<EntityType<SeaTurtleEntity>> SEA_TURTLE = register("turtle", TropicraftEntities::turtle);

    private static <E extends Entity, T extends EntityType<E>> RegistryObject<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup) {
        return ENTITIES.register(name, () -> sup.get().build(name));
    }

    private static EntityType.Builder<SeaTurtleEntity> turtle() {
        return EntityType.Builder.create(SeaTurtleEntity::new, EntityClassification.CREATURE)
                .size(0.9F, 0.4F)
                .setTrackingRange(80)
                .setUpdateInterval(1)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<BambooItemFrame> bambooItemFrame() {
        return EntityType.Builder.<BambooItemFrame>create(BambooItemFrame::new, EntityClassification.MISC)
                .size(0.5F, 0.5F)
                .setTrackingRange(64)
                .setUpdateInterval(10)
                .setShouldReceiveVelocityUpdates(false)
                .setCustomClientFactory(($, world) -> new BambooItemFrame(BAMBOO_ITEM_FRAME.get(), world));
    }

    private static EntityType.Builder<LavaBallEntity> lavaBall() {
        return EntityType.Builder.<LavaBallEntity>create(LavaBallEntity::new, EntityClassification.MISC)
                .size(1.0F, 1.0F)
                .setTrackingRange(64)
                .setUpdateInterval(10)
                .setShouldReceiveVelocityUpdates(true)
                .setCustomClientFactory(($, world) -> new LavaBallEntity(world));
    }

    private static EntityType.Builder<WallItemEntity> wallItem() {
        return EntityType.Builder.<WallItemEntity>create(WallItemEntity::new, EntityClassification.MISC)
                .size(0.5F, 0.5F)
                .setTrackingRange(64)
                .setUpdateInterval(10)
                .setShouldReceiveVelocityUpdates(false)
                .setCustomClientFactory(($, world) -> new WallItemEntity(WALL_ITEM.get(), world));
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
