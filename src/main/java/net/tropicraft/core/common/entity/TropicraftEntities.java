package net.tropicraft.core.common.entity;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.client.renderer.entity.DolphinRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.WaterAnimal;
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
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.entity.render.*;
import net.tropicraft.core.common.TropicraftTags;
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
import net.tropicraft.core.common.item.RecordMusic;
import net.tropicraft.core.common.item.TropicalFertilizerItem;
import net.tropicraft.core.common.item.TropicraftItems;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftEntities {
    public static final Registrate REGISTRATE = Tropicraft.registrate();

    private static final float EGG_WIDTH = 0.4F;
    private static final float EGG_HEIGHT = 0.5F;

    // TODO review -- tracking range is in chunks...these values seem way too high

    public static final RegistryEntry<EntityType<EntityKoaHunter>> KOA = REGISTRATE.entity("koa", EntityKoaHunter::new, MobCategory.MISC)
            .properties(b -> b.sized(0.6F, 1.95F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .setShouldReceiveVelocityUpdates(true))
            .attributes(EntityKoaBase::createAttributes)
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .renderer(() -> KoaRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<TropiCreeperEntity>> TROPICREEPER = REGISTRATE.entity("tropicreeper", TropiCreeperEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.6F, 1.7F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .attributes(TropiCreeperEntity::createAttributes)
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .loot((lootTables, entity) -> lootTables.add(entity, LootTable.lootTable()
                    .withPool(LootPool.lootPool().add(LootItem.lootTableItem(TropicraftItems.MUSIC_DISCS.get(RecordMusic.EASTERN_ISLES).get()))
                            .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER,
                                    EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS))))))
            .renderer(() -> TropiCreeperRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<IguanaEntity>> IGUANA = REGISTRATE.entity("iguana", IguanaEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(1.0F, 0.4F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .fireImmune()
                    .setShouldReceiveVelocityUpdates(true))
            .attributes(IguanaEntity::createAttributes)
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, TropicraftItems.IGUANA_LEATHER, TropicraftItems.SCALE, ConstantValue.exactly(3)))
            .renderer(() -> IguanaRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<UmbrellaEntity>> UMBRELLA = REGISTRATE.entity("umbrella", UmbrellaEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(1.0F, 4.0F)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false))
            .renderer(() -> UmbrellaRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<ChairEntity>> CHAIR = REGISTRATE.entity("chair", ChairEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(1.5F, 0.5F)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false))
            .renderer(() -> ChairRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<BeachFloatEntity>> BEACH_FLOAT = REGISTRATE.entity("beach_float", BeachFloatEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(2F, 0.175F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false))
            .renderer(() -> BeachFloatRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<TropiSkellyEntity>> TROPISKELLY = REGISTRATE.entity("tropiskelly", TropiSkellyEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.7F, 1.95F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules)
            .attributes(TropiSkellyEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> TropiSkellyRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<EIHEntity>> EIH = REGISTRATE.entity("eih", EIHEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(1.2F, 3.25F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .attributes(EIHEntity::createAttributes)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, TropicraftBlocks.CHUNK.get(), ConstantValue.exactly(3)))
            .lang("Easter Island Head")
            .renderer(() -> EIHRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<WallItemEntity>> WALL_ITEM = REGISTRATE.entity("wall_item", (EntityType.EntityFactory<WallItemEntity>) WallItemEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.5F, 0.5F)
                    .setTrackingRange(8)
                    .setUpdateInterval(Integer.MAX_VALUE)
                    .setShouldReceiveVelocityUpdates(false))
            .renderer(() -> WallItemRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<BambooItemFrame>> BAMBOO_ITEM_FRAME = REGISTRATE.entity("bamboo_item_frame", (EntityType.EntityFactory<BambooItemFrame>) BambooItemFrame::new, MobCategory.MISC)
            .properties(b -> b.sized(0.5F, 0.5F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false))
            .renderer(() -> BambooItemFrameRenderer::new)
            .register();
    // TODO: Register again when volcano eruption is finished
    public static final RegistryEntry<EntityType<LavaBallEntity>> LAVA_BALL = null;//register("lava_ball", TropicraftEntities::lavaBall);
    public static final RegistryEntry<EntityType<SeaTurtleEntity>> SEA_TURTLE = REGISTRATE.entity("turtle", SeaTurtleEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(0.8F, 0.35F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SeaTurtleEntity::canSpawnOnLand)
            .attributes(SeaTurtleEntity::createAttributes)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.TURTLE_SHELL))
            .renderer(() -> SeaTurtleRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<MarlinEntity>> MARLIN = REGISTRATE.entity("marlin", MarlinEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(1.4F, 0.95F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob)
            .attributes(MarlinEntity::createAttributes)
            .tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, (RegistryEntry<Item>) TropicraftItems.FRESH_MARLIN, UniformGenerator.between(1, 3)))
            .renderer(() -> MarlinRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<FailgullEntity>> FAILGULL = REGISTRATE.entity("failgull", FailgullEntity::new, MobCategory.AMBIENT)
            .properties(b -> b.sized(0.4F, 0.6F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules)
            .attributes(FailgullEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> FailgullRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<TropicraftDolphinEntity>> DOLPHIN = REGISTRATE.entity("dolphin", TropicraftDolphinEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(1.4F, 0.5F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob)
            .attributes(TropicraftDolphinEntity::createAttributes)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, (RegistryEntry<TropicalFertilizerItem>) TropicraftItems.TROPICAL_FERTILIZER, UniformGenerator.between(1, 3)))
            .renderer(() -> DolphinRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<SeahorseEntity>> SEAHORSE = REGISTRATE.entity("seahorse", SeahorseEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.5F, 0.6F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob)
            .attributes(SeahorseEntity::createAttributes)
            .tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> SeahorseRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<PoisonBlotEntity>> POISON_BLOT = REGISTRATE.entity("poison_blot", (EntityType.EntityFactory<PoisonBlotEntity>) PoisonBlotEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.25F, 0.25F)
                    .setTrackingRange(4)
                    .setUpdateInterval(20)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> PoisonBlotRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<TreeFrogEntity>> TREE_FROG = REGISTRATE.entity("tree_frog", TreeFrogEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.6F, 0.4F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .attributes(TreeFrogEntity::createAttributes)
            .loot((lootTables, entity) -> lootTables.add(entity, LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                    EntityPredicate.Builder.entity().flags(new EntityFlagsPredicate(null, null, null, null, false))
                                            .nbt(new NbtPredicate(Util.make(new CompoundTag(), c -> c.putInt("Type", TreeFrogEntity.Type.GREEN.ordinal()))))))
                            .add(LootItem.lootTableItem(TropicraftItems.POISON_FROG_SKIN.get())))
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2))
                            .add(LootItem.lootTableItem(TropicraftItems.FROG_LEG.get())))))
            .renderer(() -> TreeFrogRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<SeaUrchinEntity>> SEA_URCHIN = REGISTRATE.entity("sea_urchin", SeaUrchinEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.5F, 0.5F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob)
            .attributes(SeaUrchinEntity::createAttributes)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.SEA_URCHIN_ROE))
            .renderer(() -> SeaUrchinRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<SeaUrchinEggEntity>> SEA_URCHIN_EGG_ENTITY = REGISTRATE.entity("sea_urchin_egg", SeaUrchinEggEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.4F, 0.5F)
                    .setTrackingRange(6)
                    .setUpdateInterval(15)
                    .setShouldReceiveVelocityUpdates(false))
            .attributes(SeaUrchinEggEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> ClientSetup::seaUrchinEggRenderer)
            .register();

    public static final RegistryEntry<EntityType<StarfishEntity>> STARFISH = REGISTRATE.entity("starfish", StarfishEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.5F, 0.5F)
                    .setTrackingRange(4)
                    .setUpdateInterval(15)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob)
            .attributes(StarfishEntity::createAttributes)
            .tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.STARFISH))
            .renderer(() -> StarfishRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<StarfishEggEntity>> STARFISH_EGG = REGISTRATE.entity("starfish_egg", StarfishEggEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.4F, 0.5F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false))
            .attributes(StarfishEggEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> ClientSetup::starfishEggRenderer)
            .register();
    public static final RegistryEntry<EntityType<VMonkeyEntity>> V_MONKEY = REGISTRATE.entity("v_monkey", VMonkeyEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.8F, 0.8F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .attributes(VMonkeyEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .lang("Vervet Monkey")
            .renderer(() -> VMonkeyRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<SardineEntity>> RIVER_SARDINE = REGISTRATE.entity("sardine", SardineEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.3F, 0.4F)
                    .setTrackingRange(4)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
            .attributes(SardineEntity::createAttributes)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.RAW_FISH))
            .renderer(() -> SardineRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<PiranhaEntity>> PIRANHA = REGISTRATE.entity("piranha", PiranhaEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(0.3F, 0.4F)
                    .setTrackingRange(4)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
            .attributes(PiranhaEntity::createAttributes)
            .tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.RAW_FISH))
            .renderer(() -> PiranhaRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<TropicraftTropicalFishEntity>> TROPICAL_FISH = REGISTRATE.entity("tropical_fish", TropicraftTropicalFishEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.3F, 0.4F)
                    .setTrackingRange(4)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules)
            .attributes(TropicraftTropicalFishEntity::createAttributes)
            .tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.RAW_FISH))
            .renderer(() -> TropicraftTropicalFishRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<EagleRayEntity>> EAGLE_RAY = REGISTRATE.entity("eagle_ray", EagleRayEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(2F, 0.4F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob)
            .attributes(EagleRayEntity::createAttributes)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.RAW_RAY))
            .lang("Spotted Eagle Ray")
            .renderer(() -> EagleRayRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<TropiSpiderEntity>> TROPI_SPIDER = REGISTRATE.entity("tropi_spider", TropiSpiderEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(1.4F, 0.9F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules)
            .attributes(TropiSpiderEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> TropiSpiderRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<TropiSpiderEggEntity>> TROPI_SPIDER_EGG = REGISTRATE.entity("tropi_spider_egg", TropiSpiderEggEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(EGG_WIDTH, EGG_HEIGHT)
                    .setTrackingRange(6)
                    .setUpdateInterval(10)
                    .setShouldReceiveVelocityUpdates(false))
            .attributes(TropiSpiderEggEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .lang("Tropics Spider Egg")
            .renderer(() -> ClientSetup::tropiSpiderEggRenderer)
            .register();
    public static final RegistryEntry<EntityType<AshenMaskEntity>> ASHEN_MASK = REGISTRATE.entity("ashen_mask", AshenMaskEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.8F, 0.2F)
                    .setTrackingRange(6)
                    .setUpdateInterval(100)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> AshenMaskRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<AshenEntity>> ASHEN = REGISTRATE.entity("ashen", AshenEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.5F, 1.3F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules)
            .attributes(AshenEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> AshenRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<ExplodingCoconutEntity>> EXPLODING_COCONUT = REGISTRATE.entity("exploding_coconut", (EntityType.EntityFactory<ExplodingCoconutEntity>) ExplodingCoconutEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.25F, 0.25F)
                    .setTrackingRange(4)
                    .setUpdateInterval(10)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> ThrownItemRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<SharkEntity>> HAMMERHEAD = REGISTRATE.entity("hammerhead", SharkEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(2.4F, 1.4F)
                    .setTrackingRange(5)
                    .setUpdateInterval(2)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob)
            .attributes(SharkEntity::createAttributes)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, (RegistryEntry<TropicalFertilizerItem>) TropicraftItems.TROPICAL_FERTILIZER, UniformGenerator.between(1, 3)))
            .renderer(() -> SharkRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<SeaTurtleEggEntity>> SEA_TURTLE_EGG = REGISTRATE.entity("turtle_egg", SeaTurtleEggEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(EGG_WIDTH, EGG_HEIGHT)
                    .setTrackingRange(6)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(false))
            .attributes(SeaTurtleEggEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .lang("Sea Turtle Egg")
            .renderer(() -> ClientSetup::seaTurtleEggRenderer)
            .register();
    public static final RegistryEntry<EntityType<TropiBeeEntity>> TROPI_BEE = REGISTRATE.entity("tropibee", TropiBeeEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.4F, 0.6F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules)
            .attributes(TropiBeeEntity::createAttributes)
            .tag(EntityTypeTags.BEEHIVE_INHABITORS)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> TropiBeeRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<CowktailEntity>> COWKTAIL = REGISTRATE.entity("cowktail", CowktailEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.9F, 1.4F)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .attributes(CowktailEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> CowktailRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<ManOWarEntity>> MAN_O_WAR = REGISTRATE.entity("man_o_war", ManOWarEntity::new, MobCategory.WATER_AMBIENT)
            .properties(b -> b.sized(0.6F, 0.8F)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnSurfaceOceanWaterMob)
            .attributes(ManOWarEntity::createAttributes)
            .loot((lootTables, entity) -> dropItemsWithEnchantBonus(lootTables, entity, Items.SLIME_BALL, UniformGenerator.between(3, 4)))
            .lang("Man o' War")
            .renderer(() -> ManOWarRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<TapirEntity>> TAPIR = REGISTRATE.entity("tapir", TapirEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.8F, 1.0F)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .attributes(TapirEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> TapirRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<JaguarEntity>> JAGUAR = REGISTRATE.entity("jaguar", JaguarEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.9F, 1.0F)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .attributes(JaguarEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> JaguarRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<BasiliskLizardEntity>> BROWN_BASILISK_LIZARD = REGISTRATE.entity("brown_basilisk_lizard", BasiliskLizardEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.7F, 0.4F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .attributes(BasiliskLizardEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> BasiliskLizardRenderer::brown)
            .register();
    public static final RegistryEntry<EntityType<BasiliskLizardEntity>> GREEN_BASILISK_LIZARD = REGISTRATE.entity("green_basilisk_lizard", BasiliskLizardEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.7F, 0.4F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .attributes(BasiliskLizardEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> BasiliskLizardRenderer::green)
            .register();
    public static final RegistryEntry<EntityType<HummingbirdEntity>> HUMMINGBIRD = REGISTRATE.entity("hummingbird", HummingbirdEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.5F, 0.5F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HummingbirdEntity::canHummingbirdSpawnOn)
            .attributes(HummingbirdEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> HummingbirdRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<FiddlerCrabEntity>> FIDDLER_CRAB = REGISTRATE.entity("fiddler_crab", FiddlerCrabEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.5F, 0.2F)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FiddlerCrabEntity::canCrabSpawn)
            .attributes(FiddlerCrabEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> FiddlerCrabRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<SpiderMonkeyEntity>> SPIDER_MONKEY = REGISTRATE.entity("spider_monkey", SpiderMonkeyEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.5F, 0.6F)
                    .setTrackingRange(10)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .attributes(SpiderMonkeyEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> SpiderMonkeyRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<WhiteLippedPeccaryEntity>> WHITE_LIPPED_PECCARY = REGISTRATE.entity("white_lipped_peccary", WhiteLippedPeccaryEntity::new, MobCategory.MONSTER)
            .properties(b -> b.sized(0.7F, 0.8F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canAnimalSpawn)
            .attributes(WhiteLippedPeccaryEntity::createAttributes)
            .loot(TropicraftEntities::noDrops)
            .renderer(() -> WhiteLippedPeccaryRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<CuberaEntity>> CUBERA = REGISTRATE.entity("cubera", CuberaEntity::new, MobCategory.WATER_CREATURE)
            .properties(b -> b.sized(1.2F, 0.8F)
                    .setTrackingRange(8)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .spawnPlacement(SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicraftEntities::canSpawnOceanWaterMob)
            .attributes(CuberaEntity::createAttributes)
            .loot((lootTables, entity) -> dropItem(lootTables, entity, TropicraftItems.RAW_FISH))
            .renderer(() -> CuberaRenderer::new)
            .register();
    public static final RegistryEntry<EntityType<FishingBobberEntity>> FISHING_BOBBER = REGISTRATE.entity("fishing_bobber", (EntityType.EntityFactory<FishingBobberEntity>) FishingBobberEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(1.2F, 0.8F)
                    /*.setTrackingRange(8)*/
                    .setTrackingRange(128)
                    .setUpdateInterval(3)
                    .setShouldReceiveVelocityUpdates(true))
            .attributes(FishingBobberEntity::createAttributes)
            .renderer(() -> FishingBobberEntityRenderer::new)
            .register();

    public static final RegistryEntry<EntityType<SpearEntity>> SPEAR = REGISTRATE.entity("spear", (EntityType.EntityFactory<SpearEntity>) SpearEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.8F, 0.8F)
                    .setTrackingRange(4).updateInterval(20)
                    .setShouldReceiveVelocityUpdates(true))
            .renderer(() -> SpearRenderer::new)
            .register();

    //    private static void lavaBall(EntityType.Builder<LavaBallEntity> b) {
//        return EntityType.Builder.<LavaBallEntity>of(LavaBallEntity::new, MobCategory.MISC)
//                .sized(1.0F, 1.0F)
//                .setTrackingRange(8)
//                .setUpdateInterval(3)
//                .setShouldReceiveVelocityUpdates(true);
//    }

    public static boolean canAnimalSpawn(EntityType<? extends Mob> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState groundState = worldIn.getBlockState(pos.below());
        return groundState.getBlock() == Blocks.GRASS_BLOCK
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
    private static <T extends LivingEntity> void dropItemsWithEnchantBonus(RegistrateEntityLootTables lootTables, EntityType<T> entity, RegistryEntry<Item> loot, RegistryEntry<Item> multiLoot, NumberProvider range) {
        lootTables.add(entity, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(loot.get()))
                )
                .withPool(LootPool.lootPool()
                        .setRolls(range)
                        .add(LootItem.lootTableItem(multiLoot.get())
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0F, 1F / 3F))))
                ));
    }

    // Just drops a single item, not affected by enchantment
    private static <T extends LivingEntity> void dropItem(RegistrateEntityLootTables lootTables, EntityType<T> entity, RegistryEntry<? extends ItemLike> loot) {
        lootTables.add(entity, LootTable.lootTable().withPool(
                LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(loot.get()))));
    }

    private static <T extends LivingEntity> void dropItemsWithEnchantBonus(RegistrateEntityLootTables lootTables, EntityType<T> entity, RegistryEntry<? extends ItemLike> loot, NumberProvider range) {
        dropItemsWithEnchantBonus(lootTables, entity, loot.get(), range);
    }

    // Drops several items that are affected by Enchantment
    // Looting will at most double yield with Looting III
    private static <T extends LivingEntity> void dropItemsWithEnchantBonus(RegistrateEntityLootTables lootTables, EntityType<T> entity, ItemLike loot, NumberProvider range) {
        lootTables.add(entity,
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(range).add(LootItem.lootTableItem(loot)
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0F, 1F / 3F))))));
    }
}
