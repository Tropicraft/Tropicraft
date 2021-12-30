package net.tropicraft;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.reflect.Reflection;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.command.CommandSource;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.tropicraft.core.client.BasicColorHandler;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.data.TropicraftBlockstateProvider;
import net.tropicraft.core.client.data.TropicraftItemModelProvider;
import net.tropicraft.core.client.data.TropicraftLangProvider;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.block.tileentity.TropicraftTileEntityTypes;
import net.tropicraft.core.common.command.CommandTropicsTeleport;
import net.tropicraft.core.common.command.debug.MapBiomesCommand;
import net.tropicraft.core.common.config.TropicraftConfig;
import net.tropicraft.core.common.data.*;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeProvider;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftCarvers;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGenerator;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredStructures;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.block_placer.TropicraftBlockPlacerTypes;
import net.tropicraft.core.common.dimension.feature.block_state_provider.TropicraftBlockStateProviders;
import net.tropicraft.core.common.dimension.feature.jigsaw.*;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.HomeTreeBranchPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.SingleNoAirJigsawPiece;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTreeDecorators;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftConfiguredSurfaceBuilders;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftSurfaceBuilders;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.IColoredItem;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.item.scuba.ScubaData;
import net.tropicraft.core.common.item.scuba.ScubaGogglesItem;
import net.tropicraft.core.common.network.TropicraftPackets;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Mod(Constants.MODID)
public class Tropicraft {
    public static final ItemGroup TROPICRAFT_ITEM_GROUP = (new ItemGroup("tropicraft") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(TropicraftFlower.RED_ANTHURIUM.get());
        }
    });

    private static final ForgeConfigSpec SERVER_CONFIG;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        SERVER_CONFIG = configBuilder.build();
    }

    public Tropicraft() {
        // Compatible with all versions that match the semver (excluding the qualifier e.g. "-beta+42")
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(Tropicraft::getCompatVersion, (s, v) -> Tropicraft.isCompatibleVersion(s)));
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // General mod setup
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            // Client setup
            modBus.addListener(this::setupClient);
            modBus.addListener(this::registerItemColors);
        });

        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);

        TropicraftDimension.addDefaultDimensionKey();

        // Registry objects
        TropicraftBlocks.BLOCKS.register(modBus);
        TropicraftBlocks.BLOCKITEMS.register(modBus);
        TropicraftItems.ITEMS.register(modBus);
        ScubaGogglesItem.ATTRIBUTES.register(modBus);
        MixerRecipes.addMixerRecipes();
        TropicraftTileEntityTypes.TILE_ENTITIES.register(modBus);
        TropicraftEntities.ENTITIES.register(modBus);
        TropicraftCarvers.CARVERS.register(modBus);
        TropicraftFeatures.FEATURES.register(modBus);
        TropicraftFoliagePlacers.FOLIAGE_PLACERS.register(modBus);
        TropicraftTreeDecorators.TREE_DECORATORS.register(modBus);
        TropicraftFeatures.STRUCTURES.register(modBus);
        TropicraftSurfaceBuilders.SURFACE_BUILDERS.register(modBus);
        TropicraftBlockStateProviders.BLOCK_STATE_PROVIDERS.register(modBus);
        TropicraftBlockPlacerTypes.BLOCK_PLACER_TYPES.register(modBus);

        // Hack in our item frame models the way vanilla does
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            StateContainer<Block, BlockState> frameState = new StateContainer.Builder<Block, BlockState>(Blocks.AIR).add(BooleanProperty.create("map")).createStateContainer(Block::getDefaultState, BlockState::new);

            ModelBakery.STATE_CONTAINER_OVERRIDES = ImmutableMap.<ResourceLocation, StateContainer<Block, BlockState>>builder()
                    .putAll(ModelBakery.STATE_CONTAINER_OVERRIDES)
                    .put(TropicraftItems.BAMBOO_ITEM_FRAME.getId(), frameState)
                    .build();
        });
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.comment(" Welcome to the Tropicraft per-world configuration!");
        builder.push(" Overworld Generation Values");

        TropicraftConfig.palmTreeDensityInOverworld = builder.comment(" Higher number = more palm trees generate closer together in the overworld")
            .defineInRange("palm_tree_density_overworld", 1, 1, 5);

        TropicraftConfig.generatePalmTreesInOverworld = getBooleanConfig(builder,
                "Should Tropicraft palm trees generate in the overworld? NOTE: You need these to get to the tropics dimension",
                "generate_palm_trees_in_overworld",
                true);

        TropicraftConfig.generatePalmTreesInOverworldBeachesOnly = getBooleanConfig(builder,
                "In the overworld, should Tropicraft palms only generate in beach biomes?",
                "generate_palm_trees_in_overworld_beaches_only",
                false);

        TropicraftConfig.generateEIHInOverworld = getBooleanConfig(builder,
                "In the overworld, should Easter Island Head statues generate?",
                "generate_eih_in_overworld",
                false);

        TropicraftConfig.generateTropicraftFlowersInOverworld = getBooleanConfig(builder,
                "In the overworld, should Tropicraft flowers generate?",
                "generate_tropicraft_flowers_in_overworld",
                false);

        TropicraftConfig.generatePineapplesInOverworld = getBooleanConfig(builder,
                "In the overworld, should pineapples generate? NOTE: You need these to get to the tropics dimension",
                "generate_pineapples_in_overworld",
                true);

        builder.pop();
        builder.push(" Behavior settings");
        TropicraftConfig.allowVolcanoEruption = getBooleanConfig(builder,
                "Should Tropicraft volanoes erupt, spewing lava everywhere over the land?",
                "allow_volcano_eruption",
              true);

        builder.pop();
        builder.push(" User-specific settings");

        TropicraftConfig.coconutBombWhitelist = builder.comment(" List of UUIDs (not usernames) of users that can use coconut bombs. These are DANGEROUS and EXPLOSIVE so give this power out wisely.")
            .defineList("coconut_bomb_whitelist", Lists.newArrayList(""), entry -> {
                if (!(entry instanceof String)) {
                    return false;
                }
                try {
                    UUID.fromString((String) entry);
                    return true;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            });
    }

    private static ForgeConfigSpec.BooleanValue getBooleanConfig(final ForgeConfigSpec.Builder builder, final String comment, final String id, final boolean value) {
        return builder.comment(" " + comment).define(id, value);
    }

    private static final Pattern QUALIFIER = Pattern.compile("-\\w+\\+\\d+");

    public static String getCompatVersion() {
        return getCompatVersion(ModList.get().getModContainerById(Constants.MODID).orElseThrow(IllegalStateException::new).getModInfo().getVersion().toString());
    }

    private static String getCompatVersion(String fullVersion) {
        return QUALIFIER.matcher(fullVersion).replaceAll("");
    }

    public static boolean isCompatibleVersion(String version) {
        return getCompatVersion().equals(getCompatVersion(version));
    }

    @OnlyIn(Dist.CLIENT)
    private void setupClient(final FMLClientSetupEvent event) {
        ClientSetup.setupBlockRenderLayers();

        ClientSetup.setupEntityRenderers(event);

        ClientSetup.setupTileEntityRenderers();

        ClientSetup.setupDimensionRenderInfo();
    }

    @OnlyIn(Dist.CLIENT)
    private void registerItemColors(ColorHandlerEvent.Item evt) {
        BasicColorHandler basic = new BasicColorHandler();
        for (RegistryObject<Item> ro : TropicraftItems.ITEMS.getEntries()) {
            Item item = ro.get();
            if (item instanceof IColoredItem) {
                evt.getItemColors().register(basic, item);
            }
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        TropicraftPackets.init();
        ScubaData.registerCapability();
        TropicraftEntities.registerSpawns();

        TropicraftChunkGenerator.register();
        TropicraftBiomeProvider.register();

        Reflection.initialize(
                SingleNoAirJigsawPiece.class, NoRotateSingleJigsawPiece.class, HomeTreeBranchPiece.class,
                AdjustBuildingHeightProcessor.class, AirToCaveAirProcessor.class, SinkInGroundProcessor.class,
                SmoothingGravityProcessor.class, SteepPathProcessor.class, StructureSupportsProcessor.class,
                StructureVoidProcessor.class,
                TropicraftTrunkPlacers.class
        );
    }

    private void onServerStarting(final FMLServerStartingEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getServer().getCommandManager().getDispatcher();
        CommandTropicsTeleport.register(dispatcher);

        // Dev only debug!
        if (!FMLEnvironment.production) {
            MapBiomesCommand.register(dispatcher);
        }
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            TropicraftBlockstateProvider blockstates = new TropicraftBlockstateProvider(gen, existingFileHelper);
            gen.addProvider(blockstates);
            gen.addProvider(new TropicraftItemModelProvider(gen, blockstates.getExistingHelper()));
            gen.addProvider(new TropicraftLangProvider(gen));
        }
        if (event.includeServer()) {
            TropicraftBlockTagsProvider blockTags = new TropicraftBlockTagsProvider(gen, existingFileHelper);
            gen.addProvider(blockTags);
            gen.addProvider(new TropicraftItemTagsProvider(gen, blockTags, existingFileHelper));
            gen.addProvider(new TropicraftRecipeProvider(gen));
            gen.addProvider(new TropicraftLootTableProvider(gen));
            gen.addProvider(new TropicraftEntityTypeTagsProvider(gen, existingFileHelper));

            gatherWorldgenData(gen);
        }
    }

    private void gatherWorldgenData(DataGenerator gen) {
        gen.addProvider(new TropicraftWorldgenProvider(gen, generator -> {
            TropicraftConfiguredFeatures features = generator.addConfiguredFeatures(TropicraftConfiguredFeatures::new);
            TropicraftConfiguredSurfaceBuilders surfaceBuilders = generator.addConfiguredSurfaceBuilders(TropicraftConfiguredSurfaceBuilders::new);
            TropicraftConfiguredCarvers carvers = generator.addConfiguredCarvers(TropicraftConfiguredCarvers::new);
            TropicraftProcessorLists processors = generator.addProcessorLists(TropicraftProcessorLists::new);
            TropicraftTemplatePools templates = generator.addTemplatePools(consumer -> new TropicraftTemplatePools(consumer, features, processors));
            TropicraftConfiguredStructures structures = generator.addConfiguredStructures(consumer -> new TropicraftConfiguredStructures(consumer, templates));

            generator.addBiomes(consumer -> {
                return new TropicraftBiomes(consumer, features, structures, carvers, surfaceBuilders);
            });
        }));
    }
}
