package net.tropicraft.core.common.dimension;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.PortalWaterBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Set;

public class TropicraftPoiTypes extends PoiType {

    public static final DeferredRegister<PoiType> POI_TYPE = DeferredRegister.create(ForgeRegistries.POI_TYPES, Constants.MODID);
    public static final RegistryObject<PoiType> TROPICRAFT_PORTAL = registerPoi("tropic_portal", TropicraftBlocks.PORTAL_WATER, 0, 1);

    public TropicraftPoiTypes(String p_27362_, Set<BlockState> p_27363_, int p_27364_, int p_27366_) {
        super(p_27362_, p_27363_, p_27364_, p_27366_);
    }

    private static RegistryObject<PoiType> registerPoi(String pKey, RegistryObject<? extends Block> block, int pMaxFreeTickets, int pValidRange) {
        return POI_TYPE.register(pKey, () -> new TropicraftPoiTypes(pKey, getBlockStates(block), pMaxFreeTickets, pValidRange));
    }

    private static Set<BlockState> getBlockStates(RegistryObject<? extends Block> pBlock) {
        return ImmutableSet.copyOf(pBlock.get().getStateDefinition().getPossibleStates());
    }
}
