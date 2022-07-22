package net.tropicraft.core.client.scuba;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.annotation.Nullable;

@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MODID, bus = Bus.FORGE)
public class ScubaHUD {
    
    @SubscribeEvent
    public static void renderHUD(RenderGameOverlayEvent event) {
        Entity renderViewEntity = Minecraft.getInstance().cameraEntity;
        if (event.getType() == ElementType.TEXT && renderViewEntity instanceof Player) {
            Player player = (Player) renderViewEntity;
            // TODO support other slots than chest?
            ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
            Item chestItem = chestStack.getItem();
            if (chestItem instanceof ScubaArmorItem) {
                LazyOptional<ScubaData> data = player.getCapability(ScubaData.CAPABILITY);
                int airRemaining = ((ScubaArmorItem)chestItem).getRemainingAir(chestStack);
                ChatFormatting airColor = getAirTimeColor(airRemaining, player.level);
                double depth = ScubaData.getDepth(player);
                String depthStr;
                if (depth > 0) {
                    depthStr = String.format("%.1fm", depth); 
                } else {
                    depthStr = TropicraftLangKeys.NA.getLocalizedText();
                }
                data.ifPresent(d -> drawHUDStrings(event.getMatrixStack(),
                    TropicraftLangKeys.SCUBA_AIR_TIME.format(airColor + formatTime(airRemaining)),
                    TropicraftLangKeys.SCUBA_DIVE_TIME.format(formatTime(d.getDiveTime())),
                    TropicraftLangKeys.SCUBA_DEPTH.format(depthStr),
                    TropicraftLangKeys.SCUBA_MAX_DEPTH.format(String.format("%.1fm", d.getMaxDepth()))));
            }
        }
    }
    
    public static String formatTime(long time) {
        return DurationFormatUtils.formatDuration(time * (1000 / 20), "HH:mm:ss");
    }
    
    public static ChatFormatting getAirTimeColor(int airRemaining, @Nullable Level world) {
        if (airRemaining < 20 * 60) { // 1 minute
            // Flash white/red
            int speed = airRemaining < 20 * 10 ? 5 : 10;
            return world != null && (world.getGameTime() / speed) % 4 == 0 ? ChatFormatting.WHITE : ChatFormatting.RED;
        } else if (airRemaining < 20 * 60 * 5) { // 5 minutes
            return ChatFormatting.GOLD;
        } else {
            return ChatFormatting.GREEN;
        }
    }
    
    private static void drawHUDStrings(PoseStack matrixStack, Component... components) {
        Font fr = Minecraft.getInstance().font;
        Window mw = Minecraft.getInstance().getWindow();

        int startY = mw.getGuiScaledHeight() - 5 - (fr.lineHeight * components.length);
        int startX = mw.getGuiScaledWidth() - 5;
        
        for (Component text : components) {
            String s = text.getString();
            fr.drawShadow(matrixStack, s, startX - fr.width(s), startY, -1);
            startY += fr.lineHeight;
        }
    }
}
