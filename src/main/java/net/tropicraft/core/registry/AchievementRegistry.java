package net.tropicraft.core.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementRegistry extends TropicraftRegistry {

	public static final AchievementPage achievementPage = new AchievementPage("Tropicraft");

	public static Achievement craftPinaColada;
	public static Achievement drinkPinaColada;

	public static void init() {
		AchievementPage.registerAchievementPage(achievementPage);

		craftPinaColada = addAchievement("achievement.tc_craft_pina_colada", "tc_craft_pina_colada", 0, 0, new ItemStack(ItemRegistry.cocktail, 1, 4), null);
		drinkPinaColada = addAchievement("achievement.tc_drink_pina_colada", "tc_drink_pina_colada", 2, 0, new ItemStack(ItemRegistry.cocktail, 1, 4), null);
	}

	// Borrowed with love from BoP :)
	private static Achievement addAchievement(String unlocalizedName, String identifier, int column, int row, ItemStack iconStack, Achievement parent) {
		Achievement achievement = new Achievement(unlocalizedName, identifier, column, row, iconStack, parent);
		achievement.registerStat();
		achievementPage.getAchievements().add(achievement);
		return achievement;
	}

}
