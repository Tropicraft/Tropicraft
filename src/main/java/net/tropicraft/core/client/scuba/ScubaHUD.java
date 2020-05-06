package net.tropicraft.core.client.scuba;

import org.apache.commons.lang3.time.DurationFormatUtils;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tropicraft.Constants;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import net.tropicraft.core.common.item.scuba.ScubaData;

@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MODID, bus = Bus.FORGE)
public class ScubaHUD {
    
    @SubscribeEvent
    public static void renderHUD(RenderGameOverlayEvent event) {
        Entity renderViewEntity = Minecraft.getInstance().renderViewEntity;
        if (event.getType() == ElementType.TEXT && renderViewEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) renderViewEntity;
            // TODO support other slots than chest?
            ItemStack chestStack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            Item chestItem = chestStack.getItem();
            if (chestItem instanceof ScubaArmorItem) {
                LazyOptional<ScubaData> data = player.getCapability(ScubaData.CAPABILITY);
                int airRemaining = ((ScubaArmorItem)chestItem).getRemainingAir(chestStack);
                TextFormatting airColor;
                if (airRemaining < 20 * 60) { // 1 minute
                    // Flash white/red
                    int speed = airRemaining < 20 * 10 ? 5 : 10;
                    airColor = (player.world.getGameTime() / speed) % 4 == 0 ? TextFormatting.WHITE : TextFormatting.RED;
                } else if (airRemaining < 20 * 60 * 5) { // 5 minutes
                    airColor = TextFormatting.GOLD;
                } else {
                    airColor = TextFormatting.GREEN;
                }
                data.ifPresent(d -> drawHUDStrings(
                    TropicraftLangKeys.SCUBA_AIR_TIME.format(airColor + DurationFormatUtils.formatDuration(airRemaining * (1000 / 20), "HH:mm:ss")),
                    TropicraftLangKeys.SCUBA_DIVE_TIME.format(DurationFormatUtils.formatDuration(d.getDiveTime() * (1000 / 20), "HH:mm:ss")),
                    TropicraftLangKeys.SCUBA_DEPTH.format(String.format("%.1f", ScubaData.getDepth(player))),
                    TropicraftLangKeys.SCUBA_MAX_DEPTH.format(String.format("%.1f", d.getMaxDepth()))));
            }
        }
    }
    
    private static void drawHUDStrings(ITextComponent... components) {
        FontRenderer fr = Minecraft.getInstance().fontRenderer;
        MainWindow mw = Minecraft.getInstance().mainWindow;

        int startY = mw.getScaledHeight() - 5 - (fr.FONT_HEIGHT * components.length);
        int startX = mw.getScaledWidth() - 5;
        
        for (ITextComponent text : components) {
            String s = text.getFormattedText();
            fr.drawStringWithShadow(s, startX - fr.getStringWidth(s), startY, -1);
            startY += fr.FONT_HEIGHT;
        }
    }
}
