package net.tropicraft.core.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class FoodDishItem extends Item {
    private final Supplier<? extends ItemLike> dish;

    public FoodDishItem(Properties properties, Supplier<? extends ItemLike> dish) {
        super(properties);
        this.dish = dish;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack resultStack = super.finishUsingItem(stack, level, entity);
        if (entity instanceof Player player && player.getAbilities().instabuild) {
            return resultStack;
        }
        return new ItemStack(dish.get());
    }
}
