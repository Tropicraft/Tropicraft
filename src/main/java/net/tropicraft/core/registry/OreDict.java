package net.tropicraft.core.registry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class OreDict {

  public static void registerVanilla() {
    OreDictionary.registerOre("itemCoal", Items.COAL);
    OreDictionary.registerOre("itemCharcoal", new ItemStack(Items.COAL, 1, 1));
    OreDictionary.registerOre("itemLeather", Items.LEATHER);
  }

  private OreDict() {}
}