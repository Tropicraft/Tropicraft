package net.tropicraft;

import com.google.common.base.Suppliers;
import com.google.common.reflect.Reflection;
import com.mojang.brigadier.CommandDispatcher;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.DataProviderInitializer;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.Util;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
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
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.TropicraftPackRegistries;
import net.tropicraft.core.common.TropicsConfigs;
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
import net.tropicraft.core.common.item.TropicraftArmorMaterials;
import net.tropicraft.core.common.item.TropicraftDataComponents;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.item.scuba.ScubaData;
import net.tropicraft.core.common.item.scuba.ScubaGogglesItem;
import net.tropicraft.core.common.sound.Sounds;

import java.util.function.Supplier;
import java.util.regex.Pattern;

@Mod(Tropicraft.ID)
public class Tropicraft {
    public static final String ID = "tropicraft";

    public static final ProviderType<RegistrateTagsProvider.Impl<Biome>> BIOME_TAGS = ProviderType.registerDynamicTag("tags/biome", "biome", Registries.BIOME);

    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB = resourceKey(Registries.CREATIVE_MODE_TAB, ID);

    private static final Supplier<Registrate> REGISTRATE = Suppliers.memoize(() -> {
        Registrate registrate = Registrate.create(ID)
                .defaultCreativeTab(CREATIVE_TAB.location().getPath(), builder -> builder.icon(() -> new ItemStack(TropicraftBlocks.PALM_SAPLING.get()))).build()
                .addDataGenerator(ProviderType.LANG, TropicraftLangKeys::generate);
        DataProviderInitializer initializer = registrate.getDataGenInitializer();
        TropicraftPackRegistries.addTo(initializer);
        initializer.addDependency(ProviderType.ADVANCEMENT, ProviderType.DYNAMIC);
        initializer.addDependency(ProviderType.RECIPE, ProviderType.DYNAMIC);
        initializer.addDependency(BIOME_TAGS, ProviderType.DYNAMIC);
        TropicraftBiomes.setup(registrate);
        return registrate;
    });

    public static Registrate registrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public static <T> ResourceKey<T> resourceKey(ResourceKey<? extends Registry<T>> registry, String path) {
        return ResourceKey.create(registry, location(path));
    }

    public Tropicraft(ModContainer container, IEventBus modBus) {
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);

        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        container.registerConfig(ModConfig.Type.COMMON, TropicsConfigs.COMMON_SPEC);

        // Registry objects
        Sounds.REGISTER.register(modBus);
        ScubaGogglesItem.ATTRIBUTES.register(modBus);
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
        TropicraftArmorMaterials.REGISTER.register(modBus);
        TropicraftDrinkActions.REGISTER.register(modBus);
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

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event.includeServer(), new StructureConverter(ID, output, event.getInputs()));
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        TropicraftCommands.register(dispatcher);

        // Dev only debug!
        if (!FMLEnvironment.production) {
            MapBiomesCommand.register(dispatcher);
        }
    }

    @EventBusSubscriber(modid = ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    private static class ClientHandler {
        @SubscribeEvent
        public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
            // Hack in our item frame models the way vanilla does
            StateDefinition<Block, BlockState> frameState = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).add(BooleanProperty.create("map")).create(Block::defaultBlockState, BlockState::new);
            BlockStateModelLoader.STATIC_DEFINITIONS = Util.copyAndPut(
                    BlockStateModelLoader.STATIC_DEFINITIONS,
                    TropicraftItems.BAMBOO_ITEM_FRAME.getId(), frameState
            );
        }
    }
}
