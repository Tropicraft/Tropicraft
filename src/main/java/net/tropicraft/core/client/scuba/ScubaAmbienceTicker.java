package net.tropicraft.core.client.scuba;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.FogType;
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
        if (mc.level != null && mc.player != null) {
            Camera renderInfo = mc.getEntityRenderDispatcher().camera;
            Entity renderViewEntity = mc.getCameraEntity();
            if (renderInfo != null && renderViewEntity instanceof Player) {
                Player player = (Player) renderViewEntity;
                if (renderInfo.getFluidInCamera() == FogType.WATER && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ScubaArmorItem) {
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
            Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(sound.getLocation(), SoundSource.AMBIENT, 0.4f, 1.0f, true, 0, SoundInstance.Attenuation.NONE, 0.0F, 0.0F, 0.0F, true));
        }
    }
    
    private static void stop() {
        if (currentSound != null) {
            Minecraft.getInstance().getSoundManager().stop(currentSound.getLocation(), SoundSource.AMBIENT);
            currentSound = null;
        }
    }
}
