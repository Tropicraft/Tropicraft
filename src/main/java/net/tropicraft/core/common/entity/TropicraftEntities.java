package net.tropicraft.core.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.egg.*;
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
import net.tropicraft.core.common.entity.underdasea.*;

import java.util.Random;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftEntities {

    private static final float EGG_WIDTH = 0.4F;
    private static final float EGG_HEIGHT = 0.5F;

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MODID);

    public static final RegistryObject<EntityType<EntityKoaHunter>> KOA_HUNTER = register("koa", TropicraftEntities::koaHunter);
    public static final RegistryObject<EntityType<TropiCreeperEntity>> TROPI_CREEPER = register("tropicreeper", TropicraftEntities::tropicreeper);
    public static final RegistryObject<EntityType<IguanaEntity>> IGUANA = register("iguana", TropicraftEntities::iguana);
    public static final RegistryObject<EntityType<UmbrellaEntity>> UMBRELLA = register("umbrella", TropicraftEntities::umbrella);
    public static final RegistryObject<EntityType<ChairEntity>> CHAIR = register("chair", TropicraftEntities::chair);
    public static final RegistryObject<EntityType<BeachFloatEntity>> BEACH_FLOAT = register("beach_float", TropicraftEntities::beachFloat);
    public static final RegistryObject<EntityType<TropiSkellyEntity>> TROPI_SKELLY = register("tropiskelly", TropicraftEntities::tropiskelly);
    public static final RegistryObject<EntityType<EIHEntity>> EIH = register("eih", TropicraftEntities::eih);
    public static final RegistryObject<EntityType<WallItemEntity>> WALL_ITEM = register("wall_item", TropicraftEntities::wallItem);
    public static final RegistryObject<EntityType<BambooItemFrame>> BAMBOO_ITEM_FRAME = register("bamboo_item_frame", TropicraftEntities::bambooItemFrame);
    // TODO: Register again when volcano eruption is finished
    public static final RegistryObject<EntityType<LavaBallEntity>> LAVA_BALL = null;//register("lava_ball", TropicraftEntities::lavaBall);
    public static final RegistryObject<EntityType<SeaTurtleEntity>> SEA_TURTLE = register("turtle", TropicraftEntities::turtle);
    public static final RegistryObject<EntityType<MarlinEntity>> MARLIN = register("marlin", TropicraftEntities::marlin);
    public static final RegistryObject<EntityType<FailgullEntity>> FAILGULL = register("failgull", TropicraftEntities::failgull);
    public static final RegistryObject<EntityType<TropicraftDolphinEntity>> DOLPHIN = register("dolphin", TropicraftEntities::dolphin);
    public static final RegistryObject<EntityType<SeahorseEntity>> SEAHORSE = register("seahorse", TropicraftEntities::seahorse);
    public static final RegistryObject<EntityType<PoisonBlotEntity>> POISON_BLOT = register("poison_blot", TropicraftEntities::poisonBlot);
    public static final RegistryObject<EntityType<TreeFrogEntity>> TREE_FROG = register("tree_frog", TropicraftEntities::treeFrog);
    public static final RegistryObject<EntityType<SeaUrchinEntity>> SEA_URCHIN = register("sea_urchin", TropicraftEntities::seaUrchin);
    public static final RegistryObject<EntityType<SeaUrchinEggEntity>> SEA_URCHIN_EGG_ENTITY = register("sea_urchin_egg", TropicraftEntities::seaUrchinEgg);
    public static final RegistryObject<EntityType<StarfishEntity>> STARFISH = register("starfish", TropicraftEntities::starfish);
    public static final RegistryObject<EntityType<StarfishEggEntity>> STARFISH_EGG = register("starfish_egg", TropicraftEntities::starfishEgg);
    public static final RegistryObject<EntityType<VMonkeyEntity>> V_MONKEY = register("v_monkey", TropicraftEntities::vervetMonkey);
    public static final RegistryObject<EntityType<SardineEntity>> RIVER_SARDINE = register("sardine", TropicraftEntities::riverSardine);
    public static final RegistryObject<EntityType<PiranhaEntity>> PIRANHA = register("piranha", TropicraftEntities::piranha);
    public static final RegistryObject<EntityType<TropicraftTropicalFishEntity>> TROPICAL_FISH = register("tropical_fish", TropicraftEntities::tropicalFish);
    public static final RegistryObject<EntityType<EagleRayEntity>> EAGLE_RAY = register("eagle_ray", TropicraftEntities::eagleRay);
    public static final RegistryObject<EntityType<TropiSpiderEntity>> TROPI_SPIDER = register("tropi_spider", TropicraftEntities::tropiSpider);
    public static final RegistryObject<EntityType<TropiSpiderEggEntity>> TROPI_SPIDER_EGG = register("tropi_spider_egg", TropicraftEntities::tropiSpiderEgg);
    public static final RegistryObject<EntityType<AshenMaskEntity>> ASHEN_MASK = register("ashen_mask", TropicraftEntities::ashenMask);
    public static final RegistryObject<EntityType<AshenEntity>> ASHEN = register("ashen", TropicraftEntities::ashen);
    public static final RegistryObject<EntityType<ExplodingCoconutEntity>> EXPLODING_COCONUT = register("exploding_coconut", TropicraftEntities::explodingCoconut);
    public static final RegistryObject<EntityType<SharkEntity>> HAMMERHEAD = register("hammerhead", TropicraftEntities::hammerhead);
    public static final RegistryObject<EntityType<SeaTurtleEggEntity>> SEA_TURTLE_EGG = register("turtle_egg", TropicraftEntities::turtleEgg);
    public static final RegistryObject<EntityType<TropiBeeEntity>> TROPI_BEE = register("tropibee", TropicraftEntities::tropiBee);
    public static final RegistryObject<EntityType<CowktailEntity>> COWKTAIL = register("cowktail", TropicraftEntities::cowktail);
    public static final RegistryObject<EntityType<ManOWarEntity>> MAN_O_WAR = register("man_o_war", TropicraftEntities::manOWar);
    public static final RegistryObject<EntityType<TapirEntity>> TAPIR = register("tapir", TropicraftEntities::tapir);
    public static final RegistryObject<EntityType<JaguarEntity>> JAGUAR = register("jaguar", TropicraftEntities::jaguar);
    public static final RegistryObject<EntityType<BasiliskLizardEntity>> BROWN_BASILISK_LIZARD = register("brown_basilisk_lizard", TropicraftEntities::basiliskLizard);
    public static final RegistryObject<EntityType<BasiliskLizardEntity>> GREEN_BASILISK_LIZARD = register("green_basilisk_lizard", TropicraftEntities::basiliskLizard);
    public static final RegistryObject<EntityType<HummingbirdEntity>> HUMMINGBIRD = register("hummingbird", TropicraftEntities::hummingbird);
    public static final RegistryObject<EntityType<FiddlerCrabEntity>> FIDDLER_CRAB = register("fiddler_crab", TropicraftEntities::fiddlerCrab);
    public static final RegistryObject<EntityType<SpiderMonkeyEntity>> SPIDER_MONKEY = register("spider_monkey", TropicraftEntities::spiderMonkey);
    public static final RegistryObject<EntityType<WhiteLippedPeccaryEntity>> WHITE_LIPPED_PECCARY = register("white_lipped_peccary", TropicraftEntities::whiteLippedPeccary);
    public static final RegistryObject<EntityType<CuberaEntity>> CUBERA = register("cubera", TropicraftEntities::cubera);
    public static final RegistryObject<EntityType<FishingBobberEntity>> FISHING_BOBBER = register("fishing_bobber", TropicraftEntities::fishingBobber);

    private static <E extends Entity, T extends EntityType<E>> RegistryObject<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup) {
        return ENTITIES.register(name, () -> sup.get().build(name));
    }

    // TODO review -- tracking range is in chunks...these values seem way too high

    private static EntityType.Builder<CowktailEntity> cowktail() {
        return EntityType.Builder.of(CowktailEntity::new, MobCategory.MONSTER)
                .sized(0.9F, 1.4F)
                .setTrackingRange(10)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<ManOWarEntity> manOWar() {
        return EntityType.Builder.of(ManOWarEntity::new, MobCategory.WATER_AMBIENT)
                .sized(0.6F, 0.8F)
                .setTrackingRange(10)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TropiBeeEntity> tropiBee() {
        return EntityType.Builder.of(TropiBeeEntity::new, MobCategory.MONSTER)
                .sized(0.4F, 0.6F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<SeaTurtleEggEntity> turtleEgg() {
        return EntityType.Builder.of(SeaTurtleEggEntity::new, MobCategory.MONSTER)
                .sized(EGG_WIDTH, EGG_HEIGHT)
                .setTrackingRange(6)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(false);
    }
    
    private static EntityType.Builder<SharkEntity> hammerhead() {
        return EntityType.Builder.of(SharkEntity::new, MobCategory.WATER_CREATURE)
                .sized(2.4F, 1.4F)
                .setTrackingRange(5)
                .setUpdateInterval(2)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<ExplodingCoconutEntity> explodingCoconut() {
        return EntityType.Builder.<ExplodingCoconutEntity>of(ExplodingCoconutEntity::new, MobCategory.MISC)
                .sized(0.25F, 0.25F)
                .setTrackingRange(4)
                .setUpdateInterval(10)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<AshenMaskEntity> ashenMask() {
        return EntityType.Builder.<AshenMaskEntity>of(AshenMaskEntity::new, MobCategory.MISC)
                .sized(0.8F, 0.2F)
                .setTrackingRange(6)
                .setUpdateInterval(100)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<AshenEntity> ashen() {
        return EntityType.Builder.of(AshenEntity::new, MobCategory.MONSTER)
                .sized(0.5F, 1.3F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TropiSpiderEntity> tropiSpider() {
        return EntityType.Builder.of(TropiSpiderEntity::new, MobCategory.MONSTER)
                .sized(1.4F, 0.9F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TropiSpiderEggEntity> tropiSpiderEgg() {
        return EntityType.Builder.of(TropiSpiderEggEntity::new, MobCategory.MONSTER)
                .sized(EGG_WIDTH, EGG_HEIGHT)
                .setTrackingRange(6)
                .setUpdateInterval(10)
                .setShouldReceiveVelocityUpdates(false);
    }

    private static EntityType.Builder<EagleRayEntity> eagleRay() {
        return EntityType.Builder.of(EagleRayEntity::new, MobCategory.WATER_CREATURE)
                .sized(2F, 0.4F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TropicraftTropicalFishEntity> tropicalFish() {
        return EntityType.Builder.of(TropicraftTropicalFishEntity::new, MobCategory.WATER_AMBIENT)
                .sized(0.3F, 0.4F)
                .setTrackingRange(4)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<SardineEntity> riverSardine() {
        return EntityType.Builder.of(SardineEntity::new, MobCategory.WATER_AMBIENT)
                .sized(0.3F, 0.4F)
                .setTrackingRange(4)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<PiranhaEntity> piranha() {
        return EntityType.Builder.of(PiranhaEntity::new, MobCategory.WATER_CREATURE)
                .sized(0.3F, 0.4F)
                .setTrackingRange(4)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<VMonkeyEntity> vervetMonkey() {
        return EntityType.Builder.of(VMonkeyEntity::new, MobCategory.MONSTER)
                .sized(0.8F, 0.8F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<StarfishEggEntity> starfishEgg() {
        return EntityType.Builder.of(StarfishEggEntity::new, MobCategory.WATER_AMBIENT)
                .sized(0.4F, 0.5F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(false);
    }

    private static EntityType.Builder<StarfishEntity> starfish() {
        return EntityType.Builder.of(StarfishEntity::new, MobCategory.WATER_AMBIENT)
                .sized(0.5F, 0.5F)
                .setTrackingRange(4)
                .setUpdateInterval(15)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<SeaUrchinEggEntity> seaUrchinEgg() {
        return EntityType.Builder.of(SeaUrchinEggEntity::new, MobCategory.WATER_AMBIENT)
                .sized(0.4F, 0.5F)
                .setTrackingRange(6)
                .setUpdateInterval(15)
                .setShouldReceiveVelocityUpdates(false);
    }

    private static EntityType.Builder<SeaUrchinEntity> seaUrchin() {
        return EntityType.Builder.of(SeaUrchinEntity::new, MobCategory.WATER_AMBIENT)
                .sized(0.5F, 0.5F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TreeFrogEntity> treeFrog() {
        return EntityType.Builder.of(TreeFrogEntity::new, MobCategory.MONSTER)
                .sized(0.6F, 0.4F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<PoisonBlotEntity> poisonBlot() {
        return EntityType.Builder.<PoisonBlotEntity>of(PoisonBlotEntity::new, MobCategory.MISC)
                .sized(0.25F, 0.25F)
                .setTrackingRange(4)
                .setUpdateInterval(20)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<SeahorseEntity> seahorse() {
        return EntityType.Builder.of(SeahorseEntity::new, MobCategory.WATER_AMBIENT)
                .sized(0.5F, 0.6F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TropicraftDolphinEntity> dolphin() {
        return EntityType.Builder.of(TropicraftDolphinEntity::new, MobCategory.WATER_CREATURE)
                .sized(1.4F, 0.5F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<FailgullEntity> failgull() {
        return EntityType.Builder.of(FailgullEntity::new, MobCategory.AMBIENT)
                .sized(0.4F, 0.6F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<MarlinEntity> marlin() {
        return EntityType.Builder.of(MarlinEntity::new, MobCategory.WATER_CREATURE)
                .sized(1.4F, 0.95F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<SeaTurtleEntity> turtle() {
        return EntityType.Builder.of(SeaTurtleEntity::new, MobCategory.MONSTER)
                .sized(0.8F, 0.35F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<BambooItemFrame> bambooItemFrame() {
        return EntityType.Builder.<BambooItemFrame>of(BambooItemFrame::new, MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(false);
    }

    private static EntityType.Builder<LavaBallEntity> lavaBall() {
        return EntityType.Builder.<LavaBallEntity>of(LavaBallEntity::new, MobCategory.MISC)
                .sized(1.0F, 1.0F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<WallItemEntity> wallItem() {
        return EntityType.Builder.<WallItemEntity>of(WallItemEntity::new, MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .setTrackingRange(8)
                .setUpdateInterval(Integer.MAX_VALUE)
                .setShouldReceiveVelocityUpdates(false);
    }

    private static EntityType.Builder<EIHEntity> eih() {
        return EntityType.Builder.of(EIHEntity::new, MobCategory.MONSTER)
                .sized(1.2F, 3.25F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TropiSkellyEntity> tropiskelly() {
        return EntityType.Builder.of(TropiSkellyEntity::new, MobCategory.MONSTER)
                .sized(0.7F, 1.95F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<UmbrellaEntity> umbrella() {
        return EntityType.Builder.<UmbrellaEntity>of(UmbrellaEntity::new, MobCategory.MISC)
                .sized(1.0F, 4.0F)
                .setTrackingRange(10)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(false);
    }

    private static EntityType.Builder<ChairEntity> chair() {
        return EntityType.Builder.<ChairEntity>of(ChairEntity::new, MobCategory.MISC)
                .sized(1.5F, 0.5F)
                .setTrackingRange(10)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(false);
    }

    private static EntityType.Builder<BeachFloatEntity> beachFloat() {
        return EntityType.Builder.<BeachFloatEntity>of(BeachFloatEntity::new, MobCategory.MISC)
                .sized(2F, 0.175F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(false);
    }
    
    private static EntityType.Builder<IguanaEntity> iguana() {
        return EntityType.Builder.of(IguanaEntity::new, MobCategory.MONSTER)
                .sized(1.0F, 0.4F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .fireImmune()
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityKoaHunter> koaHunter() {
        return EntityType.Builder.of(EntityKoaHunter::new, MobCategory.MISC)
                .sized(0.6F, 1.95F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .fireImmune()
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TropiCreeperEntity> tropicreeper() {
        return EntityType.Builder.of(TropiCreeperEntity::new, MobCategory.MONSTER)
                .sized(0.6F, 1.7F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<TapirEntity> tapir() {
        return EntityType.Builder.of(TapirEntity::new, MobCategory.MONSTER)
                .sized(0.8F, 1.0F)
                .setTrackingRange(10)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<JaguarEntity> jaguar() {
        return EntityType.Builder.of(JaguarEntity::new, MobCategory.MONSTER)
                .sized(0.9F, 1.0F)
                .setTrackingRange(10)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<BasiliskLizardEntity> basiliskLizard() {
        return EntityType.Builder.of(BasiliskLizardEntity::new, MobCategory.MONSTER)
                .sized(0.7F, 0.4F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<HummingbirdEntity> hummingbird() {
        return EntityType.Builder.of(HummingbirdEntity::new, MobCategory.MONSTER)
                .sized(0.5F, 0.5F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<FiddlerCrabEntity> fiddlerCrab() {
        return EntityType.Builder.of(FiddlerCrabEntity::new, MobCategory.MONSTER)
                .sized(0.5F, 0.2F)
                .setTrackingRange(10)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<SpiderMonkeyEntity> spiderMonkey() {
        return EntityType.Builder.of(SpiderMonkeyEntity::new, MobCategory.MONSTER)
                .sized(0.5F, 0.6F)
                .setTrackingRange(10)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<WhiteLippedPeccaryEntity> whiteLippedPeccary() {
        return EntityType.Builder.of(WhiteLippedPeccaryEntity::new, MobCategory.MONSTER)
                .sized(0.7F, 0.8F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<CuberaEntity> cubera() {
        return EntityType.Builder.of(CuberaEntity::new, MobCategory.WATER_CREATURE)
                .sized(1.2F, 0.8F)
                .setTrackingRange(8)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<FishingBobberEntity> fishingBobber() {
        return EntityType.Builder.<FishingBobberEntity>of(FishingBobberEntity::new, MobCategory.MISC)
                .sized(1.2F, 0.8F)
                /*.setTrackingRange(8)*/
                .setTrackingRange(128)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true);
    }

    public static void registerSpawns() {
        registerWaterSpawn(TROPICAL_FISH.get(), AbstractFish::checkFishSpawnRules);
        registerWaterSpawn(RIVER_SARDINE.get(), AbstractFish::checkFishSpawnRules);
        registerWaterSpawn(PIRANHA.get(), AbstractFish::checkFishSpawnRules);
        registerWaterSpawn(DOLPHIN.get(), TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(EAGLE_RAY.get(), TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(MARLIN.get(), TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(SEAHORSE.get(), TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(SEA_URCHIN.get(), TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(STARFISH.get(), TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(HAMMERHEAD.get(), TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(MAN_O_WAR.get(), TropicraftEntities::canSpawnSurfaceOceanWaterMob);

        registerLandSpawn(KOA_HUNTER.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(TROPI_CREEPER.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(IGUANA.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(TROPI_SKELLY.get(), Monster::checkMonsterSpawnRules);
        registerLandSpawn(TROPI_SPIDER.get(), Monster::checkMonsterSpawnRules);
        registerLandSpawn(EIH.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(SEA_TURTLE.get(), SeaTurtleEntity::canSpawnOnLand);
        registerLandSpawn(TREE_FROG.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(V_MONKEY.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(COWKTAIL.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(TAPIR.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(JAGUAR.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(BROWN_BASILISK_LIZARD.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(GREEN_BASILISK_LIZARD.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(HUMMINGBIRD.get(), HummingbirdEntity::canHummingbirdSpawnOn);
        registerNoRestrictionSpawn(FIDDLER_CRAB.get(), FiddlerCrabEntity::canCrabSpawn);
        registerLandSpawn(SPIDER_MONKEY.get(), TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(WHITE_LIPPED_PECCARY.get(), TropicraftEntities::canAnimalSpawn);
        registerWaterSpawn(CUBERA.get(), TropicraftEntities::canSpawnOceanWaterMob);

        registerLandSpawn(ASHEN.get(), Mob::checkMobSpawnRules);
        registerLandSpawn(FAILGULL.get(), Mob::checkMobSpawnRules);
        registerLandSpawn(TROPI_BEE.get(), Mob::checkMobSpawnRules);
        // TODO tropibee, or from nests?
    }

    public static boolean canAnimalSpawn(EntityType<? extends Mob> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random random) {
        BlockState groundState = worldIn.getBlockState(pos.below());
        return groundState.getBlock() == Blocks.GRASS_BLOCK
                || groundState.getMaterial() == Material.SAND
                || groundState.is(TropicraftTags.Blocks.MUD);
    }

    private static <T extends Mob> void registerLandSpawn(final EntityType<T> type, SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnPlacements.register(type, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends Mob> void registerWaterSpawn(final EntityType<T> type, SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnPlacements.register(type, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends Mob> void registerNoRestrictionSpawn(final EntityType<T> type,  SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnPlacements.register(type, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    public static <T extends Mob> boolean canSpawnOceanWaterMob(EntityType<T> waterMob, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
        int seaLevel = TropicraftDimension.getSeaLevel(world);
        return pos.getY() > 90 && pos.getY() < seaLevel && world.getFluidState(pos).is(FluidTags.WATER);
    }

    public static <T extends Mob> boolean canSpawnSurfaceOceanWaterMob(EntityType<T> waterMob, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
        int seaLevel = TropicraftDimension.getSeaLevel(world);
        return pos.getY() > seaLevel - 3 && pos.getY() < seaLevel && world.getFluidState(pos).is(FluidTags.WATER);
    }

    @SubscribeEvent
    public static void onCreateEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(KOA_HUNTER.get(), EntityKoaBase.createAttributes().build());
        event.put(TROPI_CREEPER.get(), TropiCreeperEntity.createAttributes().build());
        event.put(IGUANA.get(), IguanaEntity.createAttributes().build());
        event.put(TROPI_SKELLY.get(), TropiSkellyEntity.createAttributes().build());
        event.put(EIH.get(), EIHEntity.createAttributes().build());
        event.put(SEA_TURTLE.get(), SeaTurtleEntity.createAttributes().build());
        event.put(MARLIN.get(), MarlinEntity.createAttributes().build());
        event.put(FAILGULL.get(), FailgullEntity.createAttributes().build());
        event.put(DOLPHIN.get(), TropicraftDolphinEntity.createAttributes().build());
        event.put(SEAHORSE.get(), SeahorseEntity.createAttributes().build());
        event.put(TREE_FROG.get(), TreeFrogEntity.createAttributes().build());
        event.put(SEA_URCHIN.get(), SeaUrchinEntity.createAttributes().build());
        event.put(SEA_URCHIN_EGG_ENTITY.get(), EggEntity.createAttributes().build());
        event.put(STARFISH.get(), StarfishEntity.createAttributes().build());
        event.put(STARFISH_EGG.get(), EggEntity.createAttributes().build());
        event.put(V_MONKEY.get(), VMonkeyEntity.createAttributes().build());
        event.put(RIVER_SARDINE.get(), SardineEntity.createAttributes().build());
        event.put(PIRANHA.get(), PiranhaEntity.createAttributes().build());
        event.put(TROPICAL_FISH.get(), TropicraftTropicalFishEntity.createAttributes().build());
        event.put(EAGLE_RAY.get(), EagleRayEntity.createAttributes().build());
        event.put(TROPI_SPIDER.get(), TropiSpiderEntity.createAttributes().build());
        event.put(TROPI_SPIDER_EGG.get(), EggEntity.createAttributes().build());
        event.put(ASHEN.get(), AshenEntity.createAttributes().build());
        event.put(HAMMERHEAD.get(), SharkEntity.createAttributes().build());
        event.put(SEA_TURTLE_EGG.get(), EggEntity.createAttributes().build());
        event.put(TROPI_BEE.get(), TropiBeeEntity.createAttributes().build());
        event.put(COWKTAIL.get(), CowktailEntity.createAttributes().build());
        event.put(MAN_O_WAR.get(), ManOWarEntity.createAttributes().build());
        event.put(TAPIR.get(), TapirEntity.createAttributes().build());
        event.put(JAGUAR.get(), JaguarEntity.createAttributes().build());
        event.put(BROWN_BASILISK_LIZARD.get(), BasiliskLizardEntity.createAttributes().build());
        event.put(GREEN_BASILISK_LIZARD.get(), BasiliskLizardEntity.createAttributes().build());
        event.put(HUMMINGBIRD.get(), HummingbirdEntity.createAttributes().build());
        event.put(FIDDLER_CRAB.get(), FiddlerCrabEntity.createAttributes().build());
        event.put(SPIDER_MONKEY.get(), SpiderMonkeyEntity.createAttributes().build());
        event.put(WHITE_LIPPED_PECCARY.get(), WhiteLippedPeccaryEntity.createAttributes().build());
        event.put(CUBERA.get(), CuberaEntity.createAttributes().build());
    }
}
