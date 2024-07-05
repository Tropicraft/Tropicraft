package net.tropicraft;

import com.google.common.base.Suppliers;
import com.google.common.reflect.Reflection;
import com.mojang.brigadier.CommandDispatcher;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.Util;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
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
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.TropicsConfigs;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.command.TropicraftCommands;
import net.tropicraft.core.common.command.debug.MapBiomesCommand;
import net.tropicraft.core.common.dimension.TropicraftPackRegistries;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;
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
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.item.TropicraftArmorMaterials;
import net.tropicraft.core.common.item.TropicraftDataComponents;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.item.scuba.ScubaData;
import net.tropicraft.core.common.item.scuba.ScubaGogglesItem;
import net.tropicraft.core.common.sound.Sounds;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Mod(Constants.MODID)
public class Tropicraft {
    public static final ProviderType<RegistrateTagsProvider.Impl<Biome>> BIOME_TAGS = ProviderType.register("tags/biome", type -> (p, e) -> {
        // We don't dump from registries through Registrate, so there's some duplicate work building these registries to inject as context
        PackOutput output = e.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = TropicraftPackRegistries.createLookup(e.getLookupProvider())
                .thenApply(RegistrySetBuilder.PatchedRegistries::full);
        return new RegistrateTagsProvider.Impl<>(p, type, "biome", output, Registries.BIOME, provider, e.getExistingFileHelper());
    });

    private static final Supplier<Registrate> REGISTRATE = Suppliers.memoize(() -> Registrate.create(Constants.MODID)
            .defaultCreativeTab(Constants.MODID, builder -> builder.icon(() -> new ItemStack(TropicraftBlocks.PALM_SAPLING.get()))).build()
            .addDataGenerator(ProviderType.LANG, TropicraftLangKeys::generate)
    );

    public static Registrate registrate() {
        return REGISTRATE.get();
    }

    public Tropicraft(ModContainer container, IEventBus modBus) {
        // General mod setup
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);

        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        container.registerConfig(ModConfig.Type.COMMON, TropicsConfigs.COMMON_SPEC);

        // Registry objects
        Sounds.REGISTER.register(modBus);
        ScubaGogglesItem.ATTRIBUTES.register(modBus);
        MixerRecipes.addMixerRecipes();
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
    }

    private static final Pattern QUALIFIER = Pattern.compile("-\\w+\\+\\d+");

    public static String getCompatVersion() {
        return getCompatVersion(ModList.get().getModContainerById(Constants.MODID).orElseThrow(IllegalStateException::new).getModInfo().getVersion().toString());
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
        PackOutput output = event.getGenerator().getPackOutput();
        event.getGenerator().addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), TropicraftPackRegistries.BUILDER, Set.of(Constants.MODID)));
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        TropicraftCommands.register(dispatcher);

        // Dev only debug!
        if (!FMLEnvironment.production) {
            MapBiomesCommand.register(dispatcher);
        }
    }

    @EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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
