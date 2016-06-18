package net.tropicraft.core.registry;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.item.ItemTropicraftAxe;
import net.tropicraft.core.common.item.ItemTropicraftFood;
import net.tropicraft.core.common.item.ItemTropicraftPickaxe;
import net.tropicraft.core.common.item.ItemTropicsOre;

public class ItemRegistry extends TropicraftRegistry {

	// Ore gems
	public static Item azurite, eudialyte, zircon;

	// Yummy delicious fruits
	public static Item grapefruit, lemon, lime, orange;

	// Tool materials
	public static ToolMaterial materialZirconTools = EnumHelper.addToolMaterial("zircon", 1, 200, 4.5F, 1.0F, 14);
	public static ToolMaterial materialEudialyteTools = EnumHelper.addToolMaterial("eudialyte", 2, 750, 6.0F, 2.0F, 14);
	public static ToolMaterial materialZirconiumTools = EnumHelper.addToolMaterial("zirconium", 3, 1800, 8.5F, 3.0F, 10);

	// Tools and weapons
	public static Item hoeEudialyte;
	public static Item hoeZircon;
	public static Item pickaxeEudialyte;
	public static Item pickaxeZircon;
	public static Item shovelEudialyte;
	public static Item shovelZircon;
	public static Item axeEudialyte;
	public static Item axeZircon;
	public static Item swordEudialyte;
	public static Item swordZircon;

	// Plants
	public static Item bambooShoot;

	public static void preInit() {
		azurite = registerItem(new ItemTropicsOre(), "azurite");
		eudialyte = registerItem(new ItemTropicsOre(), "eudialyte");
		zircon = registerItem(new ItemTropicsOre(), "zircon");

		grapefruit = registerItem(new ItemTropicraftFood(2, 0.2F), "grapefruit");
		lemon = registerItem(new ItemTropicraftFood(2, 0.2F), "lemon");
		lime = registerItem(new ItemTropicraftFood(2, 0.2F), "lime");
		orange = registerItem(new ItemTropicraftFood(2, 0.2F), "orange");

		hoeEudialyte = registerItem(new ItemHoe(materialEudialyteTools), "hoe_eudialyte");
		hoeZircon = registerItem(new ItemHoe(materialZirconTools), "hoe_zircon");
		pickaxeEudialyte = registerItem(new ItemTropicraftPickaxe(materialEudialyteTools), "pickaxe_eudialyte");
		pickaxeZircon = registerItem(new ItemTropicraftPickaxe(materialZirconTools), "pickaxe_zircon");
		shovelEudialyte = registerItem(new ItemSpade(materialEudialyteTools), "shovel_eudialyte");
		shovelZircon = registerItem(new ItemSpade(materialZirconTools), "shovel_zircon");
		axeEudialyte = registerItem(new ItemTropicraftAxe(materialEudialyteTools, 6.0F, -3.1F), "axe_eudialyte");
		axeZircon = registerItem(new ItemTropicraftAxe(materialZirconTools, 6.0F, -3.2F), "axe_zircon");
		swordEudialyte = registerItem(new ItemSword(materialEudialyteTools), "sword_eudialyte");
		swordZircon = registerItem(new ItemSword(materialZirconTools), "sword_zircon");

		bambooShoot = registerItem( new ItemBlockSpecial(BlockRegistry.bambooShoot), "bamboo_shoots");
	}

	public static void init() {

	}

	private static Item registerItem(Item item, String name) {
		item.setUnlocalizedName(getNamePrefixed(name));
		item.setRegistryName(new ResourceLocation(Info.MODID, name));

		GameRegistry.register(item);
		item.setCreativeTab(CreativeTabRegistry.tropicraftTab);
		Tropicraft.proxy.registerItemVariantModel(item, name, 0);

		return item;
	}

}