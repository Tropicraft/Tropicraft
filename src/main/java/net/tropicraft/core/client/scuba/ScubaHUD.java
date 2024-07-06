package net.tropicraft.core.client.scuba;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import net.tropicraft.core.common.item.scuba.ScubaData;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.Locale;

@EventBusSubscriber(value = Dist.CLIENT, modid = Tropicraft.ID, bus = EventBusSubscriber.Bus.MOD)
public class ScubaHUD {

    @SubscribeEvent
    public static void registerOverlayRenderer(RegisterGuiLayersEvent event) {
        event.registerBelow(VanillaGuiLayers.DEBUG_OVERLAY, ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, "scuba_hud"), ScubaHUD::draw);
    }

    private static void draw(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Entity renderViewEntity = Minecraft.getInstance().cameraEntity;
        if (renderViewEntity instanceof Player player) {
            // TODO support other slots than chest?
            ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
            if (chestStack.getItem() instanceof ScubaArmorItem scuba) {
                ScubaData data = player.getData(ScubaData.ATTACHMENT);
                int airRemaining = scuba.getRemainingAir(chestStack);
                ChatFormatting airColor = getAirTimeColor(airRemaining);
                double depth = ScubaData.getDepth(player);
                Component depthStr;
                if (depth > 0) {
                    depthStr = Component.literal(String.format("%.1fm", depth));
                } else {
                    depthStr = TropicraftLangKeys.NA.component();
                }
                drawHUDStrings(graphics,
                        TropicraftLangKeys.SCUBA_AIR_TIME.format(airColor + formatTime(airRemaining)),
                        TropicraftLangKeys.SCUBA_DIVE_TIME.format(formatTime(data.getDiveTime())),
                        TropicraftLangKeys.SCUBA_DEPTH.format(depthStr),
                        TropicraftLangKeys.SCUBA_MAX_DEPTH.format(String.format(Locale.ROOT, "%.1fm", data.getMaxDepth())));
            }
        }
    }

    public static String formatTime(long time) {
        return DurationFormatUtils.formatDuration(time * (1000 / 20), "HH:mm:ss");
    }

    public static ChatFormatting getAirTimeColor(int airRemaining) {
        if (airRemaining < 20 * 60) { // 1 minute
            // Flash white/red
            int speed = airRemaining < 20 * 10 ? 250 : 500;
            return (Util.getMillis() / speed) % 4 == 0 ? ChatFormatting.WHITE : ChatFormatting.RED;
        } else if (airRemaining < 20 * 60 * 5) { // 5 minutes
            return ChatFormatting.GOLD;
        } else {
            return ChatFormatting.GREEN;
        }
    }

    private static void drawHUDStrings(GuiGraphics graphics, Component... lines) {
        Font font = Minecraft.getInstance().font;

        int x = graphics.guiWidth() - 5;
        int y = graphics.guiHeight() - 5 - (font.lineHeight * lines.length);
        for (Component line : lines) {
            graphics.drawString(font, line, x - font.width(line), y, CommonColors.WHITE);
            y += font.lineHeight;
        }
    }
}
