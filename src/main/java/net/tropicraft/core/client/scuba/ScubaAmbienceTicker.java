package net.tropicraft.core.client.scuba;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tropicraft.Constants;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import net.tropicraft.core.common.item.scuba.ScubaData;

@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MODID, bus = Bus.FORGE)
public class ScubaAmbienceTicker {
    
    public static final SoundEvent SHALLOW_SCUBA = new SoundEvent(new ResourceLocation(Constants.MODID, "scuba.shallow"));
    public static final SoundEvent DEEP_SCUBA = new SoundEvent(new ResourceLocation(Constants.MODID, "scuba.deep"));

    private static SoundEvent currentSound;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        if (event.phase != Phase.START) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.world != null && mc.player != null) {
            ActiveRenderInfo renderInfo = mc.getRenderManager().info;
            Entity renderViewEntity = mc.getRenderViewEntity();
            if (renderInfo != null && renderViewEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) renderViewEntity;
                if (renderInfo.getFluidState().isTagged(FluidTags.WATER) && player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ScubaArmorItem) {
                    if (ScubaData.getDepth(player) < 60) {
                        play(SHALLOW_SCUBA);
                        return;
                    } else {
                        play(DEEP_SCUBA);
                        return;
                    }
                }
            }
        }
        stop();
    }
    
    private static void play(SoundEvent sound) {
        if (currentSound != sound) {
            stop();
            currentSound = sound;
            Minecraft.getInstance().getSoundHandler().play(new SimpleSound(sound.getName(), SoundCategory.AMBIENT, 0.4f, 1.0f, true, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F, true));
        }
    }
    
    private static void stop() {
        if (currentSound != null) {
            Minecraft.getInstance().getSoundHandler().stop(currentSound.getName(), SoundCategory.AMBIENT);
            currentSound = null;
        }
    }
}
