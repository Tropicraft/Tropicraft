package net.tropicraft.core.common.dimension;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacerType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.PortalWaterBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.block_placer.HugePlantBlockPlacer;

import java.util.Set;
import java.util.function.Predicate;

public class TropicraftPoiTypes extends PoiType{

    public static final DeferredRegister<PoiType> POI_TYPE = DeferredRegister.create(ForgeRegistries.POI_TYPES, Constants.MODID);
    public static final RegistryObject<PoiType> TROPICRAFT_PORTAL = registerPoi("tropic_portal", TropicraftBlocks.PORTAL_WATER, 0, 1);

    public TropicraftPoiTypes(String p_27362_, Set<BlockState> p_27363_, int p_27364_, int p_27366_) {
        super(p_27362_, p_27363_, p_27364_, p_27366_);
    }

    private static RegistryObject<PoiType> registerPoi(String pKey, RegistryObject<PortalWaterBlock> block, int pMaxFreeTickets, int pValidRange) {
        return POI_TYPE.register(pKey, () -> new TropicraftPoiTypes(pKey, getBlockStates(block), pMaxFreeTickets, pValidRange));
    }

    private static Set<BlockState> getBlockStates(RegistryObject<PortalWaterBlock> pBlock) {
        return ImmutableSet.copyOf(pBlock.get().getStateDefinition().getPossibleStates());
    }
}
