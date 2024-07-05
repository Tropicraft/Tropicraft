package net.tropicraft.core.client.scuba;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.tropicraft.Constants;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import net.tropicraft.core.common.item.scuba.ScubaData;

import javax.annotation.Nullable;

@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ScubaAmbienceTicker {

    public static final SoundEvent SHALLOW_SCUBA = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Constants.MODID, "scuba.shallow"));
    public static final SoundEvent DEEP_SCUBA = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Constants.MODID, "scuba.deep"));

    @Nullable
    private static SoundEvent currentSound;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null) {
            Camera renderInfo = mc.getEntityRenderDispatcher().camera;
            Entity renderViewEntity = mc.getCameraEntity();
            if (renderViewEntity instanceof Player player) {
                if (renderInfo != null && renderInfo.getFluidInCamera() == FogType.WATER && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ScubaArmorItem) {
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
            Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(sound.getLocation(), SoundSource.AMBIENT, 0.4f, 1.0f, SoundInstance.createUnseededRandom(), true, 0, SoundInstance.Attenuation.NONE, 0.0f, 0.0f, 0.0f, true));
        }
    }

    private static void stop() {
        if (currentSound != null) {
            Minecraft.getInstance().getSoundManager().stop(currentSound.getLocation(), SoundSource.AMBIENT);
            currentSound = null;
        }
    }
}
