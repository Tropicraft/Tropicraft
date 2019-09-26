package net.tropicraft.core.common.block.tileentity;

import com.google.common.collect.Sets;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicraftTileEntityTypes {
    
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Info.MODID);

    public static final RegistryObject<TileEntityType<BambooChestTileEntity>> BAMBOO_CHEST = TILE_ENTITIES.register(
            "bamboo_chest", () -> new TileEntityType<>(BambooChestTileEntity::new, Sets.newHashSet(TropicraftBlocks.BAMBOO_CHEST.get()), null));
    public static final RegistryObject<TileEntityType<SifterTileEntity>> SIFTER = TILE_ENTITIES.register(
            "sifter", () -> new TileEntityType<>(SifterTileEntity::new, Sets.newHashSet(TropicraftBlocks.SIFTER.get()), null));
    public static final RegistryObject<TileEntityType<DrinkMixerTileEntity>> DRINK_MIXER = TILE_ENTITIES.register(
            "drink_mixer", () -> new TileEntityType<>(DrinkMixerTileEntity::new, Sets.newHashSet(TropicraftBlocks.DRINK_MIXER.get()), null));
}
