package net.tropicraft.core.common.block.tileentity;

import com.google.common.collect.Sets;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicraftTileEntityTypes {
    
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Constants.MODID);

    public static final RegistryObject<BlockEntityType<BambooChestTileEntity>> BAMBOO_CHEST = BLOCK_ENTITIES.register(
            "bamboo_chest", () -> new BlockEntityType<>(BambooChestTileEntity::new, Sets.newHashSet(TropicraftBlocks.BAMBOO_CHEST.get()), null));
    public static final RegistryObject<BlockEntityType<SifterTileEntity>> SIFTER = BLOCK_ENTITIES.register(
            "sifter", () -> new BlockEntityType<>(SifterTileEntity::new, Sets.newHashSet(TropicraftBlocks.SIFTER.get()), null));
    public static final RegistryObject<BlockEntityType<DrinkMixerTileEntity>> DRINK_MIXER = BLOCK_ENTITIES.register(
            "drink_mixer", () -> new BlockEntityType<>(DrinkMixerTileEntity::new, Sets.newHashSet(TropicraftBlocks.DRINK_MIXER.get()), null));
    public static final RegistryObject<BlockEntityType<AirCompressorTileEntity>> AIR_COMPRESSOR = BLOCK_ENTITIES.register(
            "air_compressor", () -> new BlockEntityType<>(AirCompressorTileEntity::new, Sets.newHashSet(TropicraftBlocks.AIR_COMPRESSOR.get()), null));
    public static final RegistryObject<BlockEntityType<VolcanoTileEntity>> VOLCANO = BLOCK_ENTITIES.register(
            "tile_entity_volcano", () -> new BlockEntityType<>(VolcanoTileEntity::new, Sets.newHashSet(TropicraftBlocks.VOLCANO.get()), null));
}
