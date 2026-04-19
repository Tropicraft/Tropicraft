package net.tropicraft.core.common.trade;

import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.FilteredFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.List;
import java.util.Optional;

import static net.minecraft.advancements.criterion.DataComponentMatchers.Builder.components;
import static net.minecraft.core.component.predicates.EnchantmentsPredicate.enchantments;
import static net.minecraft.world.level.storage.loot.functions.DiscardItem.discardItem;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;

public interface TropicraftTrades {
    ResourceKey<VillagerTrade> KOA_FISHER_1_TROPICAL_FISH = createKey("koa_fisher/1/tropical_fish");
    ResourceKey<VillagerTrade> KOA_FISHER_1_FISHING_NET = createKey("koa_fisher/1/fishing_net");
    ResourceKey<VillagerTrade> KOA_FISHER_1_FISHING_ROD = createKey("koa_fisher/1/fishing_rod");
    ResourceKey<VillagerTrade> KOA_FISHER_1_FRESH_MARLIN = createKey("koa_fisher/1/fresh_marlin");
    ResourceKey<VillagerTrade> KOA_FISHER_1_SARDINE_BUCKET = createKey("koa_fisher/1/sardine_bucket");
    ResourceKey<VillagerTrade> KOA_FISHER_1_PIRANHA_BUCKET = createKey("koa_fisher/1/piranha_bucket");
    ResourceKey<VillagerTrade> KOA_FISHER_1_TROPICAL_FERTILIZER = createKey("koa_fisher/1/tropical_fertilizer");
    ResourceKey<VillagerTrade> KOA_FISHER_2_COOKED_FISH = createKey("koa_fisher/2/cooked_fish");
    ResourceKey<VillagerTrade> KOA_FISHER_2_COOKED_RAY = createKey("koa_fisher/2/cooked_ray");
    ResourceKey<VillagerTrade> KOA_FISHER_3_GRAPEFRUIT = createKey("koa_fisher/3/grapefruit");
    ResourceKey<VillagerTrade> KOA_FISHER_3_LEMON = createKey("koa_fisher/3/lemon");
    ResourceKey<VillagerTrade> KOA_FISHER_3_LIME = createKey("koa_fisher/3/lime");
    ResourceKey<VillagerTrade> KOA_HUNTER_1_FROG_LEG = createKey("koa_hunter/1/frog_leg");
    ResourceKey<VillagerTrade> KOA_HUNTER_1_IGUANA_LEATHER = createKey("koa_hunter/1/iguana_leather");
    ResourceKey<VillagerTrade> KOA_HUNTER_1_SCALE = createKey("koa_hunter/1/scale");
    ResourceKey<VillagerTrade> KOA_HUNTER_2_BAMBOO_SPEAR = createKey("koa_hunter/2/bamboo_spear");
    ResourceKey<VillagerTrade> KOA_HUNTER_2_BAMBOO_STICK = createKey("koa_hunter/2/bamboo_stick");
    ResourceKey<VillagerTrade> KOA_HUNTER_3_SCALE_HELMET = createKey("koa_hunter/3/scale_helmet");
    ResourceKey<VillagerTrade> KOA_HUNTER_3_SCALE_CHESTPLATE = createKey("koa_hunter/3/scale_chestplate");
    ResourceKey<VillagerTrade> KOA_HUNTER_4_SCALE_LEGGINGS = createKey("koa_hunter/4/scale_leggings");
    ResourceKey<VillagerTrade> KOA_HUNTER_4_SCALE_BOOTS = createKey("koa_hunter/4/scale_boots");

    static void bootstrap(BootstrapContext<VillagerTrade> context) {
        context.register(KOA_FISHER_1_TROPICAL_FISH, itemToPearl(Items.TROPICAL_FISH, 20, 8, 2));
        context.register(KOA_FISHER_1_FISHING_NET, itemToPearl(TropicraftItems.FISHING_NET.get(), 1, 8, 2));
        context.register(KOA_FISHER_1_FISHING_ROD, itemToPearl(Items.FISHING_ROD, 1, 8, 2));
        context.register(KOA_FISHER_1_FRESH_MARLIN, itemToPearl(TropicraftItems.FRESH_MARLIN.get(), 3, 8, 2));
        context.register(KOA_FISHER_1_SARDINE_BUCKET, itemToPearl(TropicraftItems.SARDINE_BUCKET.get(), 1, 4, 2));
        context.register(KOA_FISHER_1_PIRANHA_BUCKET, itemToPearl(TropicraftItems.PIRANHA_BUCKET.get(), 1, 3, 2));
        context.register(KOA_FISHER_1_TROPICAL_FERTILIZER, itemToPearl(TropicraftItems.TROPICAL_FERTILIZER.get(), 5, 8, 2));

        context.register(KOA_FISHER_2_COOKED_FISH, pearlToItem(TropicraftItems.COOKED_FISH.get(), 8, 1, 8, 10));
        context.register(KOA_FISHER_2_COOKED_RAY, pearlToItem(TropicraftItems.COOKED_RAY.get(), 6, 1, 8, 10));

        context.register(KOA_FISHER_3_GRAPEFRUIT, itemToPearl(TropicraftItems.GRAPEFRUIT.get(), 12, 12, 15));
        context.register(KOA_FISHER_3_LEMON, itemToPearl(TropicraftItems.LEMON.get(), 12, 12, 15));
        context.register(KOA_FISHER_3_LIME, itemToPearl(TropicraftItems.LIME.get(), 12, 12, 15));

        context.register(KOA_HUNTER_1_FROG_LEG, itemToPearl(TropicraftItems.FROG_LEG.get(), 5, 8, 2));
        context.register(KOA_HUNTER_1_IGUANA_LEATHER, itemToPearl(TropicraftItems.IGUANA_LEATHER.get(), 2, 8, 2));
        context.register(KOA_HUNTER_1_SCALE, itemToPearl(TropicraftItems.SCALE.get(), 5, 8, 2));

        context.register(KOA_HUNTER_2_BAMBOO_SPEAR, pearlToEnchantedItem(context, TropicraftItems.BAMBOO_SPEAR.get(), 1, 8, 10));
        context.register(KOA_HUNTER_2_BAMBOO_STICK, itemToPearl(TropicraftItems.BAMBOO_STICK.get(), 32, 12, 8));

        context.register(KOA_HUNTER_3_SCALE_HELMET, pearlToEnchantedItem(context, TropicraftItems.SCALE_HELMET.get(), 4, 4, 15));
        context.register(KOA_HUNTER_3_SCALE_CHESTPLATE, pearlToEnchantedItem(context, TropicraftItems.SCALE_CHESTPLATE.get(), 6, 4, 15));

        context.register(KOA_HUNTER_4_SCALE_LEGGINGS, pearlToEnchantedItem(context, TropicraftItems.SCALE_LEGGINGS.get(), 5, 4, 20));
        context.register(KOA_HUNTER_4_SCALE_BOOTS, pearlToEnchantedItem(context, TropicraftItems.SCALE_BOOTS.get(), 4, 4, 20));
    }

    private static VillagerTrade itemToPearl(ItemLike item, int count, int maxUses, int xp) {
        return new VillagerTrade(
                new TradeCost(item, count),
                new ItemStackTemplate(TropicraftItems.WHITE_PEARL.get()),
                maxUses,
                xp,
                0.05f,
                Optional.empty(),
                List.of()
        );
    }

    private static VillagerTrade pearlToItem(ItemLike item, int count, int sellCount, int maxUses, int xp) {
        return new VillagerTrade(
                new TradeCost(TropicraftItems.WHITE_PEARL, sellCount),
                new ItemStackTemplate(item.asItem(), count),
                maxUses,
                xp,
                0.05f,
                Optional.empty(),
                List.of()
        );
    }

    private static VillagerTrade pearlToEnchantedItem(BootstrapContext<?> context, ItemLike item, int sellCount, int maxUses, int xp) {
        HolderSet<Enchantment> enchantmentsForTradedEquipment = context.lookup(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_TRADED_EQUIPMENT);
        return new VillagerTrade(
                new TradeCost(TropicraftItems.WHITE_PEARL, sellCount),
                new ItemStackTemplate(item.asItem(), 1),
                maxUses,
                xp,
                0.05f,
                Optional.empty(),
                enchantedItem(context.lookup(Registries.ITEM), enchantmentsForTradedEquipment, item.asItem())
        );
    }

    private static List<LootItemFunction> enchantedItem(HolderGetter<Item> items, HolderSet<Enchantment> options, Item expectedItem) {
        ItemPredicate hasEnchantment = new ItemPredicate.Builder()
                .of(items, expectedItem)
                .withComponents(components().partial(
                        DataComponentPredicates.ENCHANTMENTS,
                        enchantments(List.of(new EnchantmentPredicate(Optional.empty(), MinMaxBounds.Ints.ANY)))
                ).build())
                .build();
        return List.of(
                new EnchantWithLevelsFunction.Builder(between(5, 15)).withOptions(options).includeAdditionalCostComponent().build(),
                FilteredFunction.filtered(hasEnchantment)
                        .onFail(Optional.of(discardItem().build()))
                        .build()
        );
    }

    static ResourceKey<VillagerTrade> createKey(String name) {
        return Tropicraft.resourceKey(Registries.VILLAGER_TRADE, name);
    }
}
