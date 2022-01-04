package net.tropicraft;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.Reflection;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.tropicraft.core.client.BasicColorHandler;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.data.TropicraftBlockstateProvider;
import net.tropicraft.core.client.data.TropicraftItemModelProvider;
import net.tropicraft.core.client.data.TropicraftLangProvider;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.TropicraftBlockEntityTypes;
import net.tropicraft.core.common.command.CommandTropicsTeleport;
import net.tropicraft.core.common.command.debug.MapBiomesCommand;
import net.tropicraft.core.common.data.TropicraftBlockTagsProvider;
import net.tropicraft.core.common.data.TropicraftEntityTypeTagsProvider;
import net.tropicraft.core.common.data.TropicraftItemTagsProvider;
import net.tropicraft.core.common.data.TropicraftLootTableProvider;
import net.tropicraft.core.common.data.TropicraftRecipeProvider;
import net.tropicraft.core.common.data.TropicraftWorldgenProvider;
import net.tropicraft.core.common.data.loot.TropicraftLootConditions;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeProvider;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftCarvers;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGenerator;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredStructures;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.block_state_provider.TropicraftBlockStateProviders;
import net.tropicraft.core.common.dimension.feature.jigsaw.AdjustBuildingHeightProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.AirToCaveAirProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SinkInGroundProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SmoothingGravityProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SteepPathProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.StructureSupportsProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.StructureVoidProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
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

import java.util.regex.Pattern;

@Mod(Constants.MODID)
public class Tropicraft {
    public static final CreativeModeTab TROPICRAFT_ITEM_GROUP = (new CreativeModeTab("tropicraft") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(TropicraftBlocks.PALM_SAPLING.get());
        }
    });

    public Tropicraft() {
        // Compatible with all versions that match the semver (excluding the qualifier e.g. "-beta+42")
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(Tropicraft::getCompatVersion, (s, v) -> Tropicraft.isCompatibleVersion(s)));
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // General mod setup
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);

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
        TropicraftBlockEntityTypes.BLOCK_ENTITIES.register(modBus);
        TropicraftEntities.ENTITIES.register(modBus);
        TropicraftCarvers.CARVERS.register(modBus);
//        TropicraftFoliagePlacers.FOLIAGE_PLACERS.register(modBus);
//        TropicraftTreeDecorators.TREE_DECORATORS.register(modBus);
        TropicraftFeatures.FEATURES.register(modBus);
        TropicraftFeatures.STRUCTURES.register(modBus);
        TropicraftBlockStateProviders.BLOCK_STATE_PROVIDERS.register(modBus);

        // Hack in our item frame models the way vanilla does
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            StateDefinition<Block, BlockState> frameState = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).add(BooleanProperty.create("map")).create(Block::defaultBlockState, BlockState::new);

            ModelBakery.STATIC_DEFINITIONS = ImmutableMap.<ResourceLocation, StateDefinition<Block, BlockState>>builder()
                    .putAll(ModelBakery.STATIC_DEFINITIONS)
                    .put(TropicraftItems.BAMBOO_ITEM_FRAME.getId(), frameState)
                    .build();
        });
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
        TropicraftEntities.registerSpawns();

        TropicraftChunkGenerator.register();
        TropicraftBiomeProvider.register();

        Reflection.initialize(
                SingleNoAirJigsawPiece.class, NoRotateSingleJigsawPiece.class, HomeTreeBranchPiece.class,
                AdjustBuildingHeightProcessor.class, AirToCaveAirProcessor.class, SinkInGroundProcessor.class,
                SmoothingGravityProcessor.class, SteepPathProcessor.class, StructureSupportsProcessor.class,
                StructureVoidProcessor.class,
                TropicraftTrunkPlacers.class,
                TropicraftFoliagePlacers.class,
                TropicraftTreeDecorators.class,
                TropicraftLootConditions.class
        );
    }

    private void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(ScubaData.class);
    }

    private void onServerStarting(final ServerStartingEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getServer().getCommands().getDispatcher();
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
            gen.addProvider(new TropicraftWorldgenProvider(gen));
        }
    }
}
