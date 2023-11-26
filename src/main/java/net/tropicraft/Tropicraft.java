package net.tropicraft;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.Reflection;
import com.mojang.brigadier.CommandDispatcher;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.TropicsConfigs;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.command.TropicraftCommands;
import net.tropicraft.core.common.command.debug.MapBiomesCommand;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.TropicraftWorldgenRegistries;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeBuilder;
import net.tropicraft.core.common.dimension.carver.TropicraftCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftStructureTypes;
import net.tropicraft.core.common.dimension.feature.block_state_provider.TropicraftBlockStateProviders;
import net.tropicraft.core.common.dimension.feature.jigsaw.*;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.*;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTreeDecorators;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.item.scuba.ScubaData;
import net.tropicraft.core.common.item.scuba.ScubaGogglesItem;
import net.tropicraft.core.common.network.TropicraftPackets;
import net.tropicraft.core.common.sound.Sounds;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@Mod(Constants.MODID)
public class Tropicraft {
    public static final ProviderType<RegistrateTagsProvider.Impl<Biome>> BIOME_TAGS = ProviderType.register("tags/biome", type -> (p, e) -> {
        // We don't dump from registries through Registrate, so there's some duplicate work building these registries to inject as context
        PackOutput output = e.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = e.getLookupProvider().thenApply(TropicraftWorldgenRegistries::createLookup);
        return new RegistrateTagsProvider.Impl<>(p, type, "biome", output, Registries.BIOME, provider, e.getExistingFileHelper());
    });

    private static final NonNullLazy<Registrate> REGISTRATE = NonNullLazy.of(() -> Registrate.create(Constants.MODID)
            .defaultCreativeTab(Constants.MODID, builder -> builder.icon(() -> new ItemStack(TropicraftBlocks.PALM_SAPLING.get()))).build()
            .addDataGenerator(ProviderType.LANG, prov -> {
                prov.add("attribute.name." + ForgeMod.SWIM_SPEED.getId().getPath(), "Swim Speed");
                TropicraftLangKeys.generate(prov);
            })
    );

    public static Registrate registrate() {
        return REGISTRATE.get();
    }

    public Tropicraft() {
        // Compatible with all versions that match the semver (excluding the qualifier e.g. "-beta+42")
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(Tropicraft::getCompatVersion, (s, v) -> Tropicraft.isCompatibleVersion(s)));
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // General mod setup
        modBus.addListener(this::setup);
        modBus.addListener(this::gatherData);

        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TropicsConfigs.COMMON_SPEC);

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

        // Hack in our item frame models the way vanilla does
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modBus.addListener((RegisterClientReloadListenersEvent event) -> {
            StateDefinition<Block, BlockState> frameState = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).add(BooleanProperty.create("map")).create(Block::defaultBlockState, BlockState::new);
            ModelBakery.STATIC_DEFINITIONS = ImmutableMap.<ResourceLocation, StateDefinition<Block, BlockState>>builder()
                    .putAll(ModelBakery.STATIC_DEFINITIONS)
                    .put(TropicraftItems.BAMBOO_ITEM_FRAME.getId(), frameState)
                    .build();
        }));
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

    private void setup(final FMLCommonSetupEvent event) {
        TropicraftPackets.init();

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
        event.getGenerator().addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), TropicraftWorldgenRegistries.BUILDER, Set.of(Constants.MODID)));
    }

    private void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(ScubaData.class);
    }

    private void onServerStarting(final ServerStartingEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getServer().getCommands().getDispatcher();
        TropicraftCommands.register(dispatcher);

        // Dev only debug!
        if (!FMLEnvironment.production) {
            MapBiomesCommand.register(dispatcher);
        }
    }
}
