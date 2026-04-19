package net.tropicraft.core.common.trade;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.tropicraft.Tropicraft;

import java.util.Optional;

public interface TropicraftTradeSets {
    ResourceKey<TradeSet> KOA_FISHER_LEVEL_1 = createKey("koa_fisher/level_1");
    ResourceKey<TradeSet> KOA_FISHER_LEVEL_2 = createKey("koa_fisher/level_2");
    ResourceKey<TradeSet> KOA_FISHER_LEVEL_3 = createKey("koa_fisher/level_3");

    ResourceKey<TradeSet> KOA_HUNTER_LEVEL_1 = createKey("koa_hunter/level_1");
    ResourceKey<TradeSet> KOA_HUNTER_LEVEL_2 = createKey("koa_hunter/level_2");
    ResourceKey<TradeSet> KOA_HUNTER_LEVEL_3 = createKey("koa_hunter/level_3");
    ResourceKey<TradeSet> KOA_HUNTER_LEVEL_4 = createKey("koa_hunter/level_4");

    // TODO: 1.14 fix missing tropical and river fish entries from tropicrafts fix
    //  - consider adding vanillas ones too now
    static void bootstrap(BootstrapContext<TradeSet> context) {
        register(context, KOA_FISHER_LEVEL_1,
                TropicraftTrades.KOA_FISHER_1_TROPICAL_FISH,
                TropicraftTrades.KOA_FISHER_1_FISHING_NET,
                TropicraftTrades.KOA_FISHER_1_FISHING_ROD,
                TropicraftTrades.KOA_FISHER_1_FRESH_MARLIN,
                TropicraftTrades.KOA_FISHER_1_SARDINE_BUCKET,
                TropicraftTrades.KOA_FISHER_1_PIRANHA_BUCKET,
                TropicraftTrades.KOA_FISHER_1_TROPICAL_FERTILIZER
        );
        register(context, KOA_FISHER_LEVEL_2,
                TropicraftTrades.KOA_FISHER_2_COOKED_FISH,
                TropicraftTrades.KOA_FISHER_2_COOKED_RAY
        );
        register(context, KOA_FISHER_LEVEL_3,
                TropicraftTrades.KOA_FISHER_3_GRAPEFRUIT,
                TropicraftTrades.KOA_FISHER_3_LEMON,
                TropicraftTrades.KOA_FISHER_3_LIME
        );

        register(context, KOA_HUNTER_LEVEL_1,
                TropicraftTrades.KOA_HUNTER_1_FROG_LEG,
                TropicraftTrades.KOA_HUNTER_1_IGUANA_LEATHER,
                TropicraftTrades.KOA_HUNTER_1_SCALE
        );
        register(context, KOA_HUNTER_LEVEL_2,
                TropicraftTrades.KOA_HUNTER_2_BAMBOO_SPEAR,
                TropicraftTrades.KOA_HUNTER_2_BAMBOO_STICK
        );
        register(context, KOA_HUNTER_LEVEL_3,
                TropicraftTrades.KOA_HUNTER_3_SCALE_HELMET,
                TropicraftTrades.KOA_HUNTER_3_SCALE_CHESTPLATE
        );
        register(context, KOA_HUNTER_LEVEL_4,
                TropicraftTrades.KOA_HUNTER_4_SCALE_LEGGINGS,
                TropicraftTrades.KOA_HUNTER_4_SCALE_BOOTS
        );
    }

    @SafeVarargs
    private static void register(BootstrapContext<TradeSet> context, ResourceKey<TradeSet> key, ResourceKey<VillagerTrade>... trades) {
        HolderGetter<VillagerTrade> lookup = context.lookup(Registries.VILLAGER_TRADE);
        context.register(key, new TradeSet(
                HolderSet.direct(lookup::getOrThrow, trades),
                ConstantValue.exactly(2),
                false,
                Optional.of(key.identifier())
        ));
    }

    static ResourceKey<TradeSet> createKey(String name) {
        return Tropicraft.resourceKey(Registries.TRADE_SET, name);
    }
}
