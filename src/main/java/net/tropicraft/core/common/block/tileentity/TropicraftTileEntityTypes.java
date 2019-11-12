package net.tropicraft.core.common.block.tileentity;

import com.google.common.collect.Sets;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicraftTileEntityTypes {
    
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Constants.MODID);

    public static final RegistryObject<TileEntityType<BambooChestTileEntity>> BAMBOO_CHEST = TILE_ENTITIES.register(
            "bamboo_chest", () -> new TileEntityType<>(BambooChestTileEntity::new, Sets.newHashSet(TropicraftBlocks.BAMBOO_CHEST.get()), null));
    public static final RegistryObject<TileEntityType<SifterTileEntity>> SIFTER = TILE_ENTITIES.register(
            "sifter", () -> new TileEntityType<>(SifterTileEntity::new, Sets.newHashSet(TropicraftBlocks.SIFTER.get()), null));
    public static final RegistryObject<TileEntityType<DrinkMixerTileEntity>> DRINK_MIXER = TILE_ENTITIES.register(
            "drink_mixer", () -> new TileEntityType<>(DrinkMixerTileEntity::new, Sets.newHashSet(TropicraftBlocks.DRINK_MIXER.get()), null));
    public static final RegistryObject<TileEntityType<VolcanoTileEntity>> VOLCANO = TILE_ENTITIES.register(
            "tile_entity_volcano", () -> new TileEntityType<>(VolcanoTileEntity::new, Sets.newHashSet(TropicraftBlocks.VOLCANO.get()), null));
    public static final RegistryObject<TileEntityType<DonationTileEntity>> DONATION = TILE_ENTITIES.register(
            "donation", () -> new TileEntityType<>(DonationTileEntity::new, Sets.newHashSet(TropicraftBlocks.DONATION.get()), null));
}
