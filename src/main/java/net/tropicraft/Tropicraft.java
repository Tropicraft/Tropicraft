package net.tropicraft;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.Reflection;
import com.mojang.brigadier.CommandDispatcher;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.NonNullLazy;
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
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.command.TropicraftCommands;
import net.tropicraft.core.common.command.debug.MapBiomesCommand;
import net.tropicraft.core.common.data.loot.TropicraftLootConditions;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeSource;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftCarvers;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGenerator;
import net.tropicraft.core.common.dimension.feature.*;
import net.tropicraft.core.common.dimension.feature.block_state_provider.TropicraftBlockStateProviders;
import net.tropicraft.core.common.dimension.feature.jigsaw.*;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.*;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTreeDecorators;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;
import net.tropicraft.core.common.dimension.noise.TropicraftNoiseGenSettings;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.item.scuba.ScubaData;
import net.tropicraft.core.common.item.scuba.ScubaGogglesItem;
import net.tropicraft.core.common.network.TropicraftPackets;

import java.util.regex.Pattern;

@Mod(Constants.MODID)
public class Tropicraft {
    public static final CreativeModeTab TROPICRAFT_ITEM_GROUP = (new CreativeModeTab("tropicraft") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(TropicraftBlocks.PALM_SAPLING.get());
        }
    });

    private static final NonNullLazy<Registrate> REGISTRATE = NonNullLazy.of(() -> Registrate.create(Constants.MODID)
            .creativeModeTab(() -> TROPICRAFT_ITEM_GROUP, "Tropicraft")
            .addDataGenerator(ProviderType.LANG, prov -> {
                prov.add("attribute.name." + ForgeMod.SWIM_SPEED.get().getRegistryName().getPath(), "Swim Speed");
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

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            // Client setup
            modBus.addListener(this::setupClient);
        });

        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);

        TropicraftDimension.addDefaultDimensionKey();

        // Registry objects
        ScubaGogglesItem.ATTRIBUTES.register(modBus);
        MixerRecipes.addMixerRecipes();
        TropicraftCarvers.CARVERS.register(modBus);
        TropicraftFoliagePlacers.REGISTER.register(modBus);
        TropicraftTrunkPlacers.REGISTER.register(modBus);
        TropicraftTreeDecorators.REGISTER.register(modBus);
        TropicraftFeatures.FEATURES.register(modBus);
        TropicraftFeatures.STRUCTURES.register(modBus);
        TropicraftBlockStateProviders.BLOCK_STATE_PROVIDERS.register(modBus);
        TropicraftStructurePieceTypes.REGISTER.register(modBus);
        TropicraftStructurePoolElementTypes.REGISTER.register(modBus);
        TropicraftProcessorTypes.REGISTER.register(modBus);

        TropicraftMiscFeatures.REGISTER.registerTo(modBus);
        TropicraftMiscPlacements.REGISTER.registerTo(modBus);
        TropicraftTreeFeatures.REGISTER.registerTo(modBus);
        TropicraftTreePlacements.REGISTER.registerTo(modBus);
        TropicraftVegetationFeatures.REGISTER.registerTo(modBus);
        TropicraftVegetationPlacements.REGISTER.registerTo(modBus);
        TropicraftProcessorLists.REGISTER.register(modBus);
        TropicraftConfiguredCarvers.REGISTER.register(modBus);
        TropicraftTemplatePools.REGISTER.register(modBus);
        TropicraftConfiguredStructures.REGISTER.register(modBus);
        TropicraftBiomes.REGISTER.register(modBus);
        TropicraftStructureSets.REGISTER.register(modBus);
        TropicraftNoiseGenSettings.REGISTER.register(modBus);

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
        ClientSetup.setupDimensionRenderInfo();
    }

    private void setup(final FMLCommonSetupEvent event) {
        TropicraftPackets.init();

        TropicraftChunkGenerator.register();
        TropicraftBiomeSource.register();

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
        TropicraftCommands.register(dispatcher);

        // Dev only debug!
        if (!FMLEnvironment.production) {
            MapBiomesCommand.register(dispatcher);
        }
    }
}
