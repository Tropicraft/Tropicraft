package net.tropicraft.core.common.dimension.feature;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicraftFeatures {

	public static final FruitTreeFeature GRAPEFRUIT_TREE = new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.GRAPEFRUIT_LEAVES.getDefaultState());
	public static final FruitTreeFeature ORANGE_TREE = new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.ORANGE_LEAVES.getDefaultState());
	public static final FruitTreeFeature LEMON_TREE = new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.LEMON_LEAVES.getDefaultState());
	public static final FruitTreeFeature LIME_TREE = new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.LIME_LEAVES.getDefaultState());

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onFeaturesRegistry(final RegistryEvent.Register<Feature<?>> event) {
        	event.getRegistry().register(GRAPEFRUIT_TREE.setRegistryName("grapefruit_tree"));
        	event.getRegistry().register(ORANGE_TREE.setRegistryName("orange_tree"));
        	event.getRegistry().register(LEMON_TREE.setRegistryName("lemon_tree"));
        	event.getRegistry().register(LIME_TREE.setRegistryName("lime_tree"));
        }
    }
}
