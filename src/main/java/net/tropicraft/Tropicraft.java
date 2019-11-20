package net.tropicraft;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.model.ModelBakery;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tropicraft.core.client.BasicColorHandler;
import net.tropicraft.core.client.data.TropicraftBlockstateProvider;
import net.tropicraft.core.client.data.TropicraftItemModelProvider;
import net.tropicraft.core.client.data.TropicraftLangProvider;
import net.tropicraft.core.client.entity.render.*;
import net.tropicraft.core.client.tileentity.BambooChestRenderer;
import net.tropicraft.core.client.tileentity.DrinkMixerRenderer;
import net.tropicraft.core.client.tileentity.SifterRenderer;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;
import net.tropicraft.core.common.block.tileentity.SifterTileEntity;
import net.tropicraft.core.common.block.tileentity.TropicraftTileEntityTypes;
import net.tropicraft.core.common.command.CommandTropicsTeleport;
import net.tropicraft.core.common.data.TropicraftBlockTagsProvider;
import net.tropicraft.core.common.data.TropicraftItemTagsProvider;
import net.tropicraft.core.common.data.TropicraftLootTableProvider;
import net.tropicraft.core.common.data.TropicraftRecipeProvider;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeProviderTypes;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftCarvers;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGeneratorTypes;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.entity.BambooItemFrame;
import net.tropicraft.core.common.entity.SeaTurtleEntity;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;
import net.tropicraft.core.common.entity.neutral.EIHEntity;
import net.tropicraft.core.common.entity.neutral.IguanaEntity;
import net.tropicraft.core.common.entity.neutral.TreeFrogEntity;
import net.tropicraft.core.common.entity.passive.EntityKoaHunter;
import net.tropicraft.core.common.entity.passive.FailgullEntity;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.ChairEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;
import net.tropicraft.core.common.entity.underdasea.SeahorseEntity;
import net.tropicraft.core.common.entity.underdasea.TropicraftDolphinEntity;
import net.tropicraft.core.common.item.IColoredItem;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.network.TropicraftPackets;

@Mod(Constants.MODID)
public class Tropicraft
{
    public static final ItemGroup TROPICRAFT_ITEM_GROUP = (new ItemGroup("tropicraft") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(TropicraftFlower.RED_ANTHURIUM.get());
        }
    });

    public Tropicraft() {
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

        // Registry objects
        TropicraftBlocks.BLOCKS.register(modBus);
        TropicraftItems.ITEMS.register(modBus);
        TropicraftTileEntityTypes.TILE_ENTITIES.register(modBus);
        TropicraftEntities.ENTITIES.register(modBus);
        TropicraftBiomes.BIOMES.register(modBus);
        TropicraftBiomeProviderTypes.BIOME_PROVIDER_TYPES.register(modBus);
        TropicraftWorldUtils.DIMENSIONS.register(modBus);
        TropicraftCarvers.CARVERS.register(modBus);
        TropicraftFeatures.FEATURES.register(modBus);
        TropicraftChunkGeneratorTypes.CHUNK_GENERATOR_TYPES.register(modBus);

        // Hack in our item frame models the way vanilla does
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            StateContainer<Block, BlockState> frameState = new StateContainer.Builder<Block, BlockState>(Blocks.AIR).add(BooleanProperty.create("map")).create(BlockState::new);
    
            ModelBakery.STATE_CONTAINER_OVERRIDES = ImmutableMap.<ResourceLocation, StateContainer<Block, BlockState>>builder()
                    .putAll(ModelBakery.STATE_CONTAINER_OVERRIDES)
                    .put(BambooItemFrameRenderer.LOCATION_BLOCK, frameState)
                    .build();
        });
    }
    
    @OnlyIn(Dist.CLIENT)
    private void setupClient(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityKoaHunter.class, RenderKoaMan::new);
        RenderingRegistry.registerEntityRenderingHandler(TropiCreeperEntity.class, TropiCreeperRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(IguanaEntity.class, IguanaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(UmbrellaEntity.class, UmbrellaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ChairEntity.class, ChairRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(BeachFloatEntity.class, BeachFloatRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropiSkellyEntity.class, TropiSkellyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EIHEntity.class, EIHRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WallItemEntity.class, RenderWallItemEntity::new);
        RenderingRegistry.registerEntityRenderingHandler(BambooItemFrame.class, BambooItemFrameRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SeaTurtleEntity.class, RenderSeaTurtle::new);
        RenderingRegistry.registerEntityRenderingHandler(MarlinEntity.class, MarlinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(FailgullEntity.class, FailgullRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TropicraftDolphinEntity.class, TropicraftDolphinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SeahorseEntity.class, SeahorseRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TreeFrogEntity.class, TreeFrogRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(PoisonBlotEntity.class, PoisonBlotRenderer::new);

        ClientRegistry.bindTileEntitySpecialRenderer(BambooChestTileEntity.class, new BambooChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(SifterTileEntity.class, new SifterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DrinkMixerTileEntity.class, new DrinkMixerRenderer());
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
        TropicraftBiomes.addFeatures();
    }
    
    private void onServerStarting(final FMLServerStartingEvent event) {
        CommandTropicsTeleport.register(event.getServer().getCommandManager().getDispatcher());
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        if (event.includeClient()) {
            TropicraftBlockstateProvider blockstates = new TropicraftBlockstateProvider(gen, event.getExistingFileHelper());
            gen.addProvider(blockstates);
            gen.addProvider(new TropicraftItemModelProvider(gen, blockstates.getExistingHelper()));
            gen.addProvider(new TropicraftLangProvider(gen));
        }
        if (event.includeServer()) {
            gen.addProvider(new TropicraftBlockTagsProvider(gen));
            gen.addProvider(new TropicraftItemTagsProvider(gen));
            gen.addProvider(new TropicraftRecipeProvider(gen));
            gen.addProvider(new TropicraftLootTableProvider(gen));
        }
    }
}
