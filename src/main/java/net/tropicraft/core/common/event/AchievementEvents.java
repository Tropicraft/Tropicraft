package net.tropicraft.core.common.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.registry.ItemRegistry;

public class AchievementEvents {
	
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) {
		if (event.player == null) return;
		
		if (event.crafting.getItem() == ItemRegistry.cocktail) {
			ItemStack pinaColada = MixerRecipes.getItemStack(Drink.pinaColada);
			if (pinaColada.isItemEqual(event.crafting)) {
				// TODO advancements event.player.addStat(AchievementRegistry.craftPinaColada);
			}
		}
	}

}
