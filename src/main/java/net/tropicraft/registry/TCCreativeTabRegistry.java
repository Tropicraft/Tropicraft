package net.tropicraft.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TCCreativeTabRegistry {

    public static final CreativeTabs tabBlock = new CreativeTabBlockTC("buildingBlocks");
    public static final CreativeTabs tabFood = new CreativeTabFoodTC("food");
    public static final CreativeTabs tabTools = new CreativeTabToolsTC("tools");
    public static final CreativeTabs tabCombat = new CreativeTabCombatTC("combat");
    public static final CreativeTabs tabDecorations = new CreativeTabDecoTC("decorations");
    public static final CreativeTabs tabMaterials = new CreativeTabMaterialsTC("materials");
    public static final CreativeTabs tabMusic = new CreativeTabMusicTC("music");
    public static final CreativeTabs tabMisc = new CreativeTabMiscTC("misc");

    public static class CreativeTabDecoTC extends CreativeTabs {
        public CreativeTabDecoTC(String name) {
            super(name);
        }

        @Override
        public Item getTabIconItem() {
            return TCItemRegistry.pearl;
        }
    }

    public static class CreativeTabMiscTC extends CreativeTabs {
        public CreativeTabMiscTC(String name) {
            super(name);
        }

        @Override
        public Item getTabIconItem() {
            return TCItemRegistry.fishingNet;
        }
    }

    public static class CreativeTabToolsTC extends CreativeTabs {
        public CreativeTabToolsTC(String name) {
            super(name);
        }

        @SideOnly(Side.CLIENT)
        @Override
        /**
         * Item to be displayed on this tab
         */
        public Item getTabIconItem() {
            return TCItemRegistry.pickaxeEudialyte;
        }
    }

    public static class CreativeTabCombatTC extends CreativeTabs {
        public CreativeTabCombatTC(String name) {
            super(name);
        }

        @Override
        public Item getTabIconItem() {
            return TCItemRegistry.swordZircon;
        }
    }

    public static class CreativeTabMaterialsTC extends CreativeTabs {
        public CreativeTabMaterialsTC(String name) {
            super(name);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return TCItemRegistry.fertilizer;
        }
    }

    public static class CreativeTabBlockTC extends CreativeTabs {
        public CreativeTabBlockTC(String name) {
            super(name);
        }

        @Override
        public Item getTabIconItem() {
            ItemStack stack = new ItemStack(TCBlockRegistry.bambooBundle);
            return stack.getItem();
        }
    }

    public static class CreativeTabFoodTC extends CreativeTabs {
        public CreativeTabFoodTC(String par2Str) {
            super(par2Str);
        }

        @Override
        public Item getTabIconItem() {
            return TCItemRegistry.lime;
        }
    }

    public static class CreativeTabMusicTC extends CreativeTabs {
        public CreativeTabMusicTC(String par2Str) {
            super(par2Str);
        }

        @SideOnly(Side.CLIENT)
        @Override
        public Item getTabIconItem() {
            return TCItemRegistry.recordEasternIsles;
        }
    }
}
