package net.tropicraft;

import com.google.common.base.Suppliers;
import com.google.common.reflect.Reflection;
import com.mojang.brigadier.CommandDispatcher;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.DataProviderInitializer;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.client.resources.model.BlockStateDefinitions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.Util;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.timeline.Timeline;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.tropicraft.core.client.TropicraftEquipmentAssets;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.client.entity.render.BambooItemFrameRenderer;
import net.tropicraft.core.common.TropicraftPackRegistries;
import net.tropicraft.core.common.TropicsConfigs;
import net.tropicraft.core.common.attribute.TropicraftAttributes;
import net.tropicraft.core.common.attribute.TropicraftEnvironmentAttributes;
import net.tropicraft.core.common.attribute.TropicraftTimelines;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.command.TropicraftCommands;
import net.tropicraft.core.common.command.debug.MapBiomesCommand;
import net.tropicraft.core.common.data.StructureConverter;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftStructureTypes;
import net.tropicraft.core.common.dimension.feature.block_state_provider.TropicraftBlockStateProviders;
import net.tropicraft.core.common.dimension.feature.jigsaw.AdjustBuildingHeightProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.AirToCaveAirProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SinkInGroundProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SmoothingGravityProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SteepPathProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.StructureSupportsProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.StructureVoidProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorTypes;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.HomeTreeBranchPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.SingleNoAirJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.TropicraftStructurePieceTypes;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.TropicraftStructurePoolElementTypes;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTreeDecorators;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;
import net.tropicraft.core.common.drinks.action.TropicraftDrinkActions;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.item.component.TropicraftDataComponents;
import net.tropicraft.core.common.item.scuba.ScubaData;
import net.tropicraft.core.common.sound.Sounds;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Mod(Tropicraft.ID)
public class Tropicraft {
    public static final String ID = "tropicraft";

    public static final ProviderType<RegistrateTagsProvider.Impl<Biome>> BIOME_TAGS = ProviderType.registerDynamicTag("tags/biome", "biome", Registries.BIOME);
    public static final ProviderType<RegistrateTagsProvider.Impl<Timeline>> TIMELINE_TAGS = ProviderType.registerDynamicTag("tags/timeline", "timeline", Registries.TIMELINE);

    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB = resourceKey(Registries.CREATIVE_MODE_TAB, ID);

    private static final Supplier<Registrate> REGISTRATE = Suppliers.memoize(() -> {
        Registrate registrate = Registrate.create(ID)
                .defaultCreativeTab(CREATIVE_TAB.identifier().getPath(), builder -> builder.icon(() -> new ItemStack(TropicraftBlocks.PALM_SAPLING.get()))).build()
                .addDataGenerator(ProviderType.LANG, TropicraftLangKeys::generate)
                .addDataGenerator(ProviderType.GENERIC_CLIENT, prov -> prov.add(data ->
                        new TropicraftEquipmentAssets.Provider(data.output()))
                );

        DataProviderInitializer initializer = registrate.getDataGenInitializer();
        TropicraftPackRegistries.addTo(initializer::add);
        initializer.addDependency(ProviderType.ADVANCEMENT, ProviderType.DYNAMIC);
        initializer.addDependency(ProviderType.RECIPE_RUNNER, ProviderType.DYNAMIC);
        initializer.addDependency(BIOME_TAGS, ProviderType.DYNAMIC);
        initializer.addDependency(TIMELINE_TAGS, ProviderType.DYNAMIC);
        TropicraftBiomes.setup(registrate);
        TropicraftTimelines.bootstrapTags(registrate);
        return registrate;
    });

    public static Registrate registrate() {
        return REGISTRATE.get();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(ID, path);
    }

    public static <T> ResourceKey<T> resourceKey(ResourceKey<? extends Registry<T>> registry, String path) {
        return ResourceKey.create(registry, id(path));
    }

    public Tropicraft(ModContainer container, IEventBus modBus) {
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);

        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        container.registerConfig(ModConfig.Type.COMMON, TropicsConfigs.COMMON_SPEC);

        // Registry objects
        Sounds.REGISTER.register(modBus);
        TropicraftAttributes.REGISTER.register(modBus);
        TropicraftEnvironmentAttributes.REGISTER.register(modBus);
        TropicraftCarvers.CARVERS.register(modBus);
        TropicraftFoliagePlacers.REGISTER.register(modBus);
        TropicraftTrunkPlacers.REGISTER.register(modBus);
        TropicraftTreeDecorators.REGISTER.register(modBus);
        TropicraftFeatures.FEATURES.register(modBus);
        TropicraftBlockStateProviders.BLOCK_STATE_PROVIDERS.register(modBus);
        TropicraftStructurePoolElementTypes.REGISTER.register(modBus);
        TropicraftProcessorTypes.REGISTER.register(modBus);
        TropicraftStructureTypes.REGISTER.register(modBus);
        TropicraftStructurePieceTypes.REGISTER.register(modBus);
        ScubaData.ATTACHMENT_TYPES.register(modBus);
        TropicraftDataComponents.REGISTER.register(modBus);
        TropicraftDrinkActions.REGISTER.register(modBus);

        modBus.addListener(TropicraftItems::onItemRegister);
        Reflection.initialize(TropicraftBlocks.class);

        modBus.addListener((AddPackFindersEvent event) ->
                event.addPackFinders(id("resourcepacks/tropicraft_texture_update"), PackType.CLIENT_RESOURCES, TropicraftLangKeys.TEXTURE_UPDATE_PACK.component(), PackSource.BUILT_IN, false, Pack.Position.TOP)
        );
    }

    private static final Pattern QUALIFIER = Pattern.compile("-\\w+\\+\\d+");

    public static String getCompatVersion() {
        return getCompatVersion(ModList.get().getModContainerById(ID).orElseThrow(IllegalStateException::new).getModInfo().getVersion().toString());
    }

    private static String getCompatVersion(String fullVersion) {
        return QUALIFIER.matcher(fullVersion).replaceAll("");
    }

    private void setup(FMLCommonSetupEvent event) {
        Reflection.initialize(
                TropicraftBiomeBuilder.class,
                SingleNoAirJigsawPiece.class, NoRotateSingleJigsawPiece.class, HomeTreeBranchPiece.class,
                AdjustBuildingHeightProcessor.class, AirToCaveAirProcessor.class, SinkInGroundProcessor.class,
                SmoothingGravityProcessor.class, SteepPathProcessor.class, StructureSupportsProcessor.class,
                StructureVoidProcessor.class,
                TropicraftTrunkPlacers.class,
                TropicraftFoliagePlacers.class,
                TropicraftTreeDecorators.class
        );
    }

    private void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        // Big hack, as we can't pass inputs in client data generation
        Path inputPath = event.getGenerator().getPackOutput().getOutputFolder().getParent().getParent().resolve("main/resources");
        generator.addProvider(true, new StructureConverter(ID, output, List.of(inputPath)));
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        TropicraftCommands.register(dispatcher);

        // Dev only debug!
        if (!FMLEnvironment.isProduction()) {
            MapBiomesCommand.register(dispatcher);
        }
    }

    @EventBusSubscriber(modid = ID, value = Dist.CLIENT)
    private static class ClientHandler {
        @SubscribeEvent
        public static void registerReloadListeners(AddClientReloadListenersEvent event) {
            // Hack in our item frame models the way vanilla does
            BlockStateDefinitions.STATIC_DEFINITIONS = Util.copyAndPut(
                    BlockStateDefinitions.STATIC_DEFINITIONS,
                    TropicraftItems.BAMBOO_ITEM_FRAME.getId(), BambooItemFrameRenderer.FAKE_BLOCK_STATE
            );
        }
    }
}
