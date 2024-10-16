package net.tropicraft.core.common.entity;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.client.renderer.entity.DolphinRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.entity.render.*;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.TropicsConfigs;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.egg.SeaTurtleEggEntity;
import net.tropicraft.core.common.entity.egg.SeaUrchinEggEntity;
import net.tropicraft.core.common.entity.egg.StarfishEggEntity;
import net.tropicraft.core.common.entity.egg.TropiSpiderEggEntity;
import net.tropicraft.core.common.entity.hostile.AshenEntity;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;
import net.tropicraft.core.common.entity.neutral.*;
import net.tropicraft.core.common.entity.passive.*;
import net.tropicraft.core.common.entity.passive.basilisk.BasiliskLizardEntity;
import net.tropicraft.core.common.entity.passive.monkey.SpiderMonkeyEntity;
import net.tropicraft.core.common.entity.placeable.*;
import net.tropicraft.core.common.entity.projectile.ExplodingCoconutEntity;
import net.tropicraft.core.common.entity.projectile.LavaBallEntity;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;
import net.tropicraft.core.common.entity.projectile.SpearEntity;
import net.tropicraft.core.common.entity.underdasea.*;
import net.tropicraft.core.common.item.TropicalFertilizerItem;
import net.tropicraft.core.common.item.TropicraftItems;

@EventBusSubscriber(modid = Tropicraft.ID)
public class TropicraftEntities {
    public static final Registrate REGISTRATE = Tropicraft.registrate();

    private static final float EGG_WIDTH = 0.4f;
    private static final float EGG_HEIGHT = 0.5f;

    // TODO review -- tracking range is in chunks...these values seem way too high

    public static final EntityEntry<EntityKoaHunter> KOA = REGISTRATE.entity("koa", EntityKoaHunter::new, MobCategory.MISC)
            .properties(b -> b.sized(0.6f, 1.95f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .setShouldReceiveVelocityUpdates(true))
            .attributes(EntityKoaBase::createAttributes)
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .renderer(() -> KoaRenderer::new)
            .register();
    public static final EntityEntry<TropiCreeperEntity> TROPICREEPER = REGISTRATE.entity("tropicreeper", TropiCreeperEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.6f, 1.7f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .attributes(TropiCreeperEntity::createAttributes)
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .loot((lootTables, entity) -> lootTables.add(entity, LootTable.lootTable()
                    .withPool(LootPool.lootPool().add(LootItem.lootTableItem(TropicraftItems.MUSIC_DISC_EASTERN_ISLES.get()))
                            .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER,
                                    EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS))))))
            .renderer(() -> TropiCreeperRenderer::new)
            .register();
    public static final EntityEntry<IguanaEntity> IGUANA = REGISTRATE.entity("iguana", IguanaEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(1.0f, 0.4f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .setShouldReceiveVelocityUpdates(true))
            .attributes(IguanaEntity::createAttributes)
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, TropicraftItems.IGUANA_LEATHER, TropicraftItems.SCALE, ConstantValue.exactly(3)))
            .renderer(() -> IguanaRenderer::new)
            .register();
    public static final EntityEntry<UmbrellaEntity> UMBRELLA = REGISTRATE.entity("umbrella", UmbrellaEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(1.0f, 4.0f)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false))
            .renderer(() -> UmbrellaRenderer::new)
            .register();
    public static final EntityEntry<ChairEntity> CHAIR = REGISTRATE.entity("chair", ChairEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(1.5f, 0.5f)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false)
                    .passengerAttachments(new Vec3(0.0, 0.25, -0.125)))
            .renderer(() -> ChairRenderer::new)
            .register();
    public static final EntityEntry<BeachFloatEntity> BEACH_FLOAT = REGISTRATE.entity("beach_float", BeachFloatEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(2.0f, 0.175f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false)
                    .passengerAttachments(new Vec3(0.0, -10.0 / 16.0, -0.6)))
            .renderer(() -> BeachFloatRenderer::new)
            .register();
    public static final EntityEntry<TropiSkellyEntity> TROPISKELLY = REGISTRATE.entity("tropiskelly", TropiSkellyEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.7f, 1.95f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(TropiSkellyEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> TropiSkellyRenderer::new)
            .register();
    public static final EntityEntry<EIHEntity> EIH = REGISTRATE.entity("eih", EIHEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(1.2f, 3.25f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(EIHEntity::createAttributes)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, TropicraftBlocks.CHUNK.get(), ConstantValue.exactly(3)))
            .lang("Easter Island Head")
            .renderer(() -> EIHRenderer::new)
            .register();
    public static final EntityEntry<WallItemEntity> WALL_ITEM = REGISTRATE.entity("wall_item", (EntityType.EntityFactory<WallItemEntity>) WallItemEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.5f, 0.5f)
                    .setTrackingRange(8)
                    .setUpdateInterval(Integer.MAX_VALUE)
                    .setShouldReceiveVelocityUpdates(false))
            .renderer(() -> WallItemRenderer::new)
            .register();
    public static final EntityEntry<BambooItemFrame> BAMBOO_ITEM_FRAME = REGISTRATE.entity("bamboo_item_frame", (EntityType.EntityFactory<BambooItemFrame>) BambooItemFrame::new, MobCategory.MISC)
            .properties(b -> b.sized(0.5f, 0.5f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false))
            .renderer(() -> BambooItemFrameRenderer::new)
            .register();
    // TODO: Register again when volcano eruption is finished
    public static final EntityEntry<LavaBallEntity> LAVA_BALL = null;//register("lava_ball", TropicraftEntities::lavaBall);
    public static final EntityEntry<SeaTurtleEntity> SEA_TURTLE = REGISTRATE.entity("turtle", SeaTurtleEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(0.8f, 0.35f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true)
                    .passengerAttachments(new Vec3(0.0, 0.45625, 0.0))
            )
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SeaTurtleEntity::canSpawnOnLand, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(SeaTurtleEntity::createAttributes)
            .tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
            .tag(EntityTypeTags.AQUATIC)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.TURTLE_SHELL))
            .renderer(() -> SeaTurtleRenderer::new)
            .register();
    public static final EntityEntry<MarlinEntity> MARLIN = REGISTRATE.entity("marlin", MarlinEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(1.4f, 0.95f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(MarlinEntity::createAttributes)
            .tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, (RegistryEntry<Item, Item>) TropicraftItems.FRESH_MARLIN, UniformGenerator.between(1, 3)))
            .renderer(() -> MarlinRenderer::new)
            .register();
    public static final EntityEntry<FailgullEntity> FAILGULL = REGISTRATE.entity("failgull", FailgullEntity::new, MobCategory.AMBIENT)
            .properties(b -> b.sized(0.4f, 0.6f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true)
                    .eyeHeight(0.3f))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(FailgullEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> FailgullRenderer::new)
            .register();
    public static final EntityEntry<TropicraftDolphinEntity> DOLPHIN = REGISTRATE.entity("dolphin", TropicraftDolphinEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(1.4f, 0.5f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(TropicraftDolphinEntity::createAttributes)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, (RegistryEntry<Item, TropicalFertilizerItem>) TropicraftItems.TROPICAL_FERTILIZER, UniformGenerator.between(1, 3)))
            .renderer(() -> DolphinRenderer::new)
            .register();
    public static final EntityEntry<SeahorseEntity> SEAHORSE = REGISTRATE.entity("seahorse", SeahorseEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.5f, 0.6f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(SeahorseEntity::createAttributes)
            .tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> SeahorseRenderer::new)
            .register();
    public static final EntityEntry<PoisonBlotEntity> POISON_BLOT = REGISTRATE.entity("poison_blot", (EntityType.EntityFactory<PoisonBlotEntity>) PoisonBlotEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.25f, 0.25f)
                    .setTrackingRange(4)
                    .setUpdateInterval(20)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> PoisonBlotRenderer::new)
            .register();
    public static final EntityEntry<TreeFrogEntity> TREE_FROG = REGISTRATE.entity("tree_frog", TreeFrogEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.6f, 0.4f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(TreeFrogEntity::createAttributes)
            .loot((lootTables, entity) -> lootTables.add(entity, LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                    EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false))
                                            .nbt(new NbtPredicate(Util.make(new CompoundTag(), c -> c.putInt("Type", TreeFrogEntity.Type.GREEN.ordinal()))))))
                            .add(LootItem.lootTableItem(TropicraftItems.POISON_FROG_SKIN.get())))
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2))
                            .add(LootItem.lootTableItem(TropicraftItems.FROG_LEG.get())))))
            .renderer(() -> TreeFrogRenderer::new)
            .register();
    public static final EntityEntry<SeaUrchinEntity> SEA_URCHIN = REGISTRATE.entity("sea_urchin", SeaUrchinEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.5f, 0.5f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(SeaUrchinEntity::createAttributes)
            .tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.SEA_URCHIN_ROE))
            .renderer(() -> SeaUrchinRenderer::new)
            .register();
    public static final EntityEntry<SeaUrchinEggEntity> SEA_URCHIN_EGG_ENTITY = REGISTRATE.entity("sea_urchin_egg", SeaUrchinEggEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.4f, 0.5f)
                    .setTrackingRange(6)
                    .setUpdateInterval(15)
                    .setShouldReceiveVelocityUpdates(false))
            .attributes(SeaUrchinEggEntity::createAttributes)
            .tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> ClientSetup::seaUrchinEggRenderer)
            .register();

    public static final EntityEntry<StarfishEntity> STARFISH = REGISTRATE.entity("starfish", StarfishEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.5f, 0.5f)
                    .setTrackingRange(4)
                    .setUpdateInterval(15)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(StarfishEntity::createAttributes)
            .tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.STARFISH))
            .renderer(() -> StarfishRenderer::new)
            .register();
    public static final EntityEntry<StarfishEggEntity> STARFISH_EGG = REGISTRATE.entity("starfish_egg", StarfishEggEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.4f, 0.5f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false))
            .attributes(StarfishEggEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
            .renderer(() -> ClientSetup::starfishEggRenderer)
            .register();
    public static final EntityEntry<VMonkeyEntity> V_MONKEY = REGISTRATE.entity("v_monkey", VMonkeyEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.8f, 0.8f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(VMonkeyEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .lang("Vervet Monkey")
            .renderer(() -> VMonkeyRenderer::new)
            .register();
    public static final EntityEntry<SardineEntity> RIVER_SARDINE = REGISTRATE.entity("sardine", SardineEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.3f, 0.4f)
                    .setTrackingRange(4)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftFishUtils::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(SardineEntity::createAttributes)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.RAW_FISH))
            .renderer(() -> SardineRenderer::new)
            .register();
    public static final EntityEntry<PiranhaEntity> PIRANHA = REGISTRATE.entity("piranha", PiranhaEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(0.3f, 0.4f)
                    .setTrackingRange(4)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftFishUtils::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(PiranhaEntity::createAttributes)
            .tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.RAW_FISH))
            .renderer(() -> PiranhaRenderer::new)
            .register();
    public static final EntityEntry<TropicraftTropicalFishEntity> TROPICAL_FISH = REGISTRATE.entity("tropical_fish", TropicraftTropicalFishEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.3f, 0.4f)
                    .setTrackingRange(4)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftFishUtils::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(TropicraftTropicalFishEntity::createAttributes)
            .tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.RAW_FISH))
            .renderer(() -> TropicraftTropicalFishRenderer::new)
            .register();
    public static final EntityEntry<EagleRayEntity> EAGLE_RAY = REGISTRATE.entity("eagle_ray", EagleRayEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(2.0f, 0.4f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(EagleRayEntity::createAttributes)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.RAW_RAY))
            .lang("Spotted Eagle Ray")
            .renderer(() -> EagleRayRenderer::new)
            .register();
    public static final EntityEntry<TropiSpiderEntity> TROPI_SPIDER = REGISTRATE.entity("tropi_spider", TropiSpiderEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(1.4f, 0.9f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(TropiSpiderEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> TropiSpiderRenderer::new)
            .register();
    public static final EntityEntry<TropiSpiderEggEntity> TROPI_SPIDER_EGG = REGISTRATE.entity("tropi_spider_egg", TropiSpiderEggEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(EGG_WIDTH, EGG_HEIGHT)
                    .setTrackingRange(6)
                    .setUpdateInterval(10)
                    .setShouldReceiveVelocityUpdates(false))
            .attributes(TropiSpiderEggEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .lang("Tropics Spider Egg")
            .renderer(() -> ClientSetup::tropiSpiderEggRenderer)
            .register();
    public static final EntityEntry<AshenMaskEntity> ASHEN_MASK = REGISTRATE.entity("ashen_mask", AshenMaskEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.8f, 0.2f)
                    .setTrackingRange(6)
                    .setUpdateInterval(100)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> AshenMaskRenderer::new)
            .register();
    public static final EntityEntry<AshenEntity> ASHEN = REGISTRATE.entity("ashen", AshenEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.5f, 1.3f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(AshenEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> AshenRenderer::new)
            .register();
    public static final EntityEntry<ExplodingCoconutEntity> EXPLODING_COCONUT = REGISTRATE.entity("exploding_coconut", (EntityType.EntityFactory<ExplodingCoconutEntity>) ExplodingCoconutEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.25f, 0.25f)
                    .setTrackingRange(4)
                    .setUpdateInterval(10)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> ThrownItemRenderer::new)
            .register();
    public static final EntityEntry<SharkEntity> HAMMERHEAD = REGISTRATE.entity("hammerhead", SharkEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(2.4f, 1.4f)
                    .setTrackingRange(5)
                    .setUpdateInterval(2)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(SharkEntity::createAttributes)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, (RegistryEntry<Item, TropicalFertilizerItem>) TropicraftItems.TROPICAL_FERTILIZER, UniformGenerator.between(1, 3)))
            .renderer(() -> SharkRenderer::new)
            .register();
    public static final EntityEntry<SeaTurtleEggEntity> SEA_TURTLE_EGG = REGISTRATE.entity("turtle_egg", SeaTurtleEggEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(EGG_WIDTH, EGG_HEIGHT)
                    .setTrackingRange(6)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false))
            .attributes(SeaTurtleEggEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .lang("Sea Turtle Egg")
            .renderer(() -> ClientSetup::seaTurtleEggRenderer)
            .register();
    public static final EntityEntry<TropiBeeEntity> TROPI_BEE = REGISTRATE.entity("tropibee", TropiBeeEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.4f, 0.6f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(TropiBeeEntity::createAttributes)
            .tag(EntityTypeTags.BEEHIVE_INHABITORS)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, TropicraftItems.COOL_SHADES.get(), UniformGenerator.between(0, 1)))
            .renderer(() -> TropiBeeRenderer::new)
            .register();
    public static final EntityEntry<CowktailEntity> COWKTAIL = REGISTRATE.entity("cowktail", CowktailEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.9f, 1.4f)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(CowktailEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> CowktailRenderer::new)
            .register();
    public static final EntityEntry<ManOWarEntity> MAN_O_WAR = REGISTRATE.entity("man_o_war", ManOWarEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.6f, 0.8f)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true)
                    .eyeHeight(0.4f))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnSurfaceOceanWaterMob, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(ManOWarEntity::createAttributes)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, Items.SLIME_BALL, UniformGenerator.between(3, 4)))
            .lang("Man o' War")
            .tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
            .renderer(() -> ManOWarRenderer::new)
            .register();
    public static final EntityEntry<TapirEntity> TAPIR = REGISTRATE.entity("tapir", TapirEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.8f, 1.0f)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(TapirEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> TapirRenderer::new)
            .register();
    public static final EntityEntry<JaguarEntity> JAGUAR = REGISTRATE.entity("jaguar", JaguarEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.9f, 1.0f)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(JaguarEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> JaguarRenderer::new)
            .register();
    public static final EntityEntry<BasiliskLizardEntity> BROWN_BASILISK_LIZARD = REGISTRATE.entity("brown_basilisk_lizard", BasiliskLizardEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.7f, 0.4f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(BasiliskLizardEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> BasiliskLizardRenderer::brown)
            .register();
    public static final EntityEntry<BasiliskLizardEntity> GREEN_BASILISK_LIZARD = REGISTRATE.entity("green_basilisk_lizard", BasiliskLizardEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.7f, 0.4f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(BasiliskLizardEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> BasiliskLizardRenderer::green)
            .register();
    public static final EntityEntry<HummingbirdEntity> HUMMINGBIRD = REGISTRATE.entity("hummingbird", HummingbirdEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.5f, 0.5f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HummingbirdEntity::canHummingbirdSpawnOn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(HummingbirdEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> HummingbirdRenderer::new)
            .register();
    public static final EntityEntry<FiddlerCrabEntity> FIDDLER_CRAB = REGISTRATE.entity("fiddler_crab", FiddlerCrabEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.5f, 0.2f)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FiddlerCrabEntity::canCrabSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(FiddlerCrabEntity::createAttributes)
            .tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> FiddlerCrabRenderer::new)
            .register();
    public static final EntityEntry<SpiderMonkeyEntity> SPIDER_MONKEY = REGISTRATE.entity("spider_monkey", SpiderMonkeyEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.5f, 0.6f)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(SpiderMonkeyEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> SpiderMonkeyRenderer::new)
            .register();
    public static final EntityEntry<WhiteLippedPeccaryEntity> WHITE_LIPPED_PECCARY = REGISTRATE.entity("white_lipped_peccary", WhiteLippedPeccaryEntity::new, MobCategory.CREATURE)
            .properties(b -> b.sized(0.7f, 0.8f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(WhiteLippedPeccaryEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> WhiteLippedPeccaryRenderer::new)
            .register();
    public static final EntityEntry<CuberaEntity> CUBERA = REGISTRATE.entity("cubera", CuberaEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(1.2f, 0.8f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(CuberaEntity::createAttributes)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.RAW_FISH))
            .renderer(() -> CuberaRenderer::new)
            .register();
    public static final EntityEntry<FishingBobberEntity> FISHING_BOBBER = REGISTRATE.entity("fishing_bobber", (EntityType.EntityFactory<FishingBobberEntity>) FishingBobberEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(1.2f, 0.8f)
                    /*.setTrackingRange(8)*/
                    .setTrackingRange(128)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .attributes(FishingBobberEntity::createAttributes)
            .renderer(() -> FishingBobberEntityRenderer::new)
            .register();

    public static final EntityEntry<SpearEntity> SPEAR = REGISTRATE.entity("spear", (EntityType.EntityFactory<SpearEntity>) SpearEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.8f, 0.8f)
                    .setTrackingRange(4).updateInterval(20)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> SpearRenderer::new)
            .register();

    //    private static void lavaBall(EntityType.Builder<LavaBallEntity> b) {
//        return EntityType.Builder.<LavaBallEntity>of(LavaBallEntity::new, MobCategory.MISC)
//                .sized(1.0f, 1.0f)
//                .setTrackingRange(8)
//                .setUpdateInterval(3)
//                .setShouldReceiveVelocityUpdates(true);
//    }

    public static final RegistryEntry<EntityType<?>, EntityType<GibnutEntity>> GIBNUT = REGISTRATE.entity("gibnut", GibnutEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.7f, 0.3f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(GibnutEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> GibnutRenderer::new)
            .register();

    public static final RegistryEntry<EntityType<?>, EntityType<ManateeEntity>> MANATEE = REGISTRATE.entity("manatee", ManateeEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(2.0f, 1.3f)
                    .setTrackingRange(5)
                    .setUpdateInterval(2)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(ManateeEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> ManateeRenderer::new)
            .register();

    public static final RegistryEntry<EntityType<?>, EntityType<SlenderHarvestMouseEntity>> SLENDER_HARVEST_MOUSE = REGISTRATE.entity("slender_harvest_mouse", SlenderHarvestMouseEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.5f, 0.2f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(SlenderHarvestMouseEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> SlenderHarvestMouseRenderer::new)
            .register();

    public static final RegistryEntry<EntityType<?>, EntityType<ToucanEntity>> TOUCAN = REGISTRATE.entity("toucan", ToucanEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.5f, 0.5f)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ToucanEntity::canToucanSpawnOn, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            .attributes(ToucanEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> ToucanRenderer::new)
            .tag(EntityTypeTags.FALL_DAMAGE_IMMUNE)
            .register();

    public static boolean canAnimalSpawn(EntityType<? extends Mob> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState groundState = worldIn.getBlockState(pos.below());
        return groundState.is(Blocks.GRASS_BLOCK)
                || groundState.is(BlockTags.SAND)
                || groundState.is(TropicraftTags.Blocks.MUD);
    }

    public static <T extends Mob> boolean canSpawnOceanWaterMob(EntityType<T> waterMob, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
        int seaLevel = TropicraftDimension.getSeaLevel(world);
        return pos.getY() > 90 && pos.getY() < seaLevel && world.getFluidState(pos).is(FluidTags.WATER);
    }

    public static <T extends Mob> boolean canSpawnSurfaceOceanWaterMob(EntityType<T> waterMob, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
        int seaLevel = TropicraftDimension.getSeaLevel(world);
        return pos.getY() > seaLevel - 3 && pos.getY() < seaLevel && world.getFluidState(pos).is(FluidTags.WATER);
    }

    private static void noDrops(RegistrateEntityLootTables loot, EntityType<?> type) {
        loot.add(type, LootTable.lootTable());
    }

    // Drops a single item, not affected by enchantment, and several other items
    // that are affected by Enchantment
    // Looting will at most double yield with Looting III
    private static <T extends LivingEntity> void dropItemsWithEnchantBonus(RegistrateEntityLootTables lootTables, EntityType<T> entity, RegistryEntry<Item, ? extends Item> loot, RegistryEntry<Item, Item> multiLoot, NumberProvider range) {
        lootTables.add(entity, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(loot.get()))
                )
                .withPool(LootPool.lootPool()
                        .setRolls(range)
                        .add(LootItem.lootTableItem(multiLoot.get())
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(lootTables.getRegistries(), UniformGenerator.between(0.0f, 1.0f / 3.0f))))
                ));
    }

    // Just drops a single item, not affected by enchantment
    private static <T extends LivingEntity> void dropItem(RegistrateEntityLootTables lootTables, EntityType<T> entity, RegistryEntry<? extends ItemLike, ? extends ItemLike> loot) {
        lootTables.add(entity, LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(loot.get()))));
    }

    private static <T extends LivingEntity> void dropItemsWithEnchantBonus(RegistrateEntityLootTables lootTables, EntityType<T> entity, RegistryEntry<? extends ItemLike, ? extends ItemLike> loot, NumberProvider range) {
        dropItemsWithEnchantBonus(lootTables, entity, loot.get(), range);
    }

    // Drops several items that are affected by Enchantment
    // Looting will at most double yield with Looting III
    private static <T extends LivingEntity> void dropItemsWithEnchantBonus(RegistrateEntityLootTables lootTables, EntityType<T> entity, ItemLike loot, NumberProvider range) {
        lootTables.add(entity,
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(range).add(LootItem.lootTableItem(loot)
                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(lootTables.getRegistries(), UniformGenerator.between(0.0f, 1.0f / 3.0f))))));
    }

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.PositionCheck event) {
        ServerLevel level = event.getLevel().getLevel();
        if (level.dimension() == TropicraftDimension.WORLD) {
            if (!TropicsConfigs.COMMON.spawnHostileMobsInTropics.get()) {
                if (event.getSpawnType() == MobSpawnType.NATURAL || event.getSpawnType() == MobSpawnType.CHUNK_GENERATION) {
                    Mob mob = event.getEntity();
                    if (mob.getType() != TropicraftEntities.ASHEN.get() && (mob.getType().getCategory() == MobCategory.MONSTER || mob instanceof Enemy)) {
                        event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
                    }
                }
            }
        }
    }
}
