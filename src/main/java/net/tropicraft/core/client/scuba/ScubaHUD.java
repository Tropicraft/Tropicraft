package net.tropicraft.core.client.scuba;

import org.apache.commons.lang3.time.DurationFormatUtils;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
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
            if (player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ScubaArmorItem) {
                LazyOptional<ScubaData> data = player.getCapability(ScubaData.CAPABILITY);
                data.ifPresent(d -> {
                    String diveTime = TropicraftLangKeys.SCUBA_DIVE_TIME.format(DurationFormatUtils.formatDuration(d.getDiveTime() * (1000 / 20), "HH:mm:ss")).getFormattedText();
                    String curDepth = TropicraftLangKeys.SCUBA_DEPTH.format(String.format("%.1f", ScubaData.getDepth(player))).getFormattedText();
                    String maxDepth = TropicraftLangKeys.SCUBA_MAX_DEPTH.format(String.format("%.1f", d.getMaxDepth())).getFormattedText();
                    
                    FontRenderer fr = Minecraft.getInstance().fontRenderer;
                    MainWindow mw = Minecraft.getInstance().mainWindow;

                    int width = Math.max(fr.getStringWidth(diveTime), Math.max(fr.getStringWidth(curDepth), fr.getStringWidth(maxDepth)));
                    int startY = mw.getScaledHeight() - 5 - fr.FONT_HEIGHT;
                    int startX = mw.getScaledWidth() - 5;

                    fr.drawStringWithShadow(maxDepth, startX - fr.getStringWidth(maxDepth), startY, -1);
                    startY -= fr.FONT_HEIGHT;
                    fr.drawStringWithShadow(curDepth, startX - fr.getStringWidth(curDepth), startY, -1);
                    startY -= fr.FONT_HEIGHT;
                    fr.drawStringWithShadow(diveTime, startX - fr.getStringWidth(diveTime), startY, -1);
                });
            }
        }
    }
}
