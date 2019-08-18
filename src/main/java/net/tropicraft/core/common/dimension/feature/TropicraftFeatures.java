package net.tropicraft.core.common.dimension.feature;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicraftFeatures {

	public static final FruitTreeFeature GRAPEFRUIT_TREE = new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.GRAPEFRUIT_LEAVES.getDefaultState());
	public static final FruitTreeFeature ORANGE_TREE = new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.ORANGE_LEAVES.getDefaultState());
	public static final FruitTreeFeature LEMON_TREE = new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.LEMON_LEAVES.getDefaultState());
	public static final FruitTreeFeature LIME_TREE = new FruitTreeFeature(NoFeatureConfig::deserialize, true, TropicraftBlocks.LIME_LEAVES.getDefaultState());
	public static final PalmTreeFeature NORMAL_PALM_TREE = new NormalPalmTreeFeature(NoFeatureConfig::deserialize, true);
	public static final PalmTreeFeature CURVED_PALM_TREE = new CurvedPalmTreeFeature(NoFeatureConfig::deserialize, true);
	public static final PalmTreeFeature LARGE_PALM_TREE = new LargePalmTreeFeature(NoFeatureConfig::deserialize, true);

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onFeaturesRegistry(final RegistryEvent.Register<Feature<?>> event) {
        	register(event, GRAPEFRUIT_TREE, "grapefruit_tree");
        	register(event, ORANGE_TREE, "orange_tree");
        	register(event, LEMON_TREE, "lemon_tree");
        	register(event, LIME_TREE, "lime_tree");
        	register(event, NORMAL_PALM_TREE, "normal_palm_tree");
			register(event, CURVED_PALM_TREE, "curved_palm_tree");
			register(event, LARGE_PALM_TREE, "large_palm_tree");
        }

        private static void register(final RegistryEvent.Register<Feature<?>> event, final Feature<?> feature, final String name) {
			event.getRegistry().register(feature.setRegistryName(new ResourceLocation(Constants.MODID, name)));
		}
    }
}
