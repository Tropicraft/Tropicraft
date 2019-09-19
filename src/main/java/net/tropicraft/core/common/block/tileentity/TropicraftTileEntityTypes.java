package net.tropicraft.core.common.block.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicraftTileEntityTypes {

    public static final TileEntityType<BambooChestTileEntity> BAMBOO_CHEST = new TileEntityType<>(BambooChestTileEntity::new, Sets.newHashSet(TropicraftBlocks.BAMBOO_CHEST), null);
    public static final TileEntityType<SifterTileEntity> SIFTER = new TileEntityType<>(SifterTileEntity::new, Sets.newHashSet(TropicraftBlocks.SIFTER), null);
    public static final TileEntityType<DrinkMixerTileEntity> DRINK_MIXER = new TileEntityType<>(DrinkMixerTileEntity::new, Sets.newHashSet(TropicraftBlocks.DRINK_MIXER), null);
    public static final TileEntityType<VolcanoTileEntity> VOLCANO = new TileEntityType<>(VolcanoTileEntity::new, Sets.newHashSet(TropicraftBlocks.VOLCANO), null);

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onTERegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(BAMBOO_CHEST.setRegistryName(new ResourceLocation(Constants.MODID, "tile_entity_bamboo_chest")));
            event.getRegistry().register(SIFTER.setRegistryName(new ResourceLocation(Constants.MODID, "tile_entity_sifter")));
            event.getRegistry().register(DRINK_MIXER.setRegistryName(new ResourceLocation(Constants.MODID, "tile_entity_drink_mixer")));
            event.getRegistry().register(VOLCANO.setRegistryName(new ResourceLocation(Constants.MODID, "tile_entity_volcano")));
        }
    }
}
