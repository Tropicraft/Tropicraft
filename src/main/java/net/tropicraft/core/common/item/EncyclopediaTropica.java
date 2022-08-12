package net.tropicraft.core.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.base.PatchouliSounds;

import javax.annotation.Nullable;
import java.util.List;

public class EncyclopediaTropica extends Item {

    public EncyclopediaTropica(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide()) {
            PatchouliAPI.get().openBookGUI((ServerPlayer)player, new ResourceLocation(Constants.MODID,"encyclopedia_tropica"));
            player.playSound(PatchouliSounds.BOOK_OPEN, 1.0F, (float)(0.7 + Math.random() * 0.4));
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, world, tooltipComponents, isAdvanced);

        tooltipComponents.add(new TranslatableComponent("tropicraft.encyclopedia_tropica.tooltip").withStyle(ChatFormatting.GRAY));
    }
}