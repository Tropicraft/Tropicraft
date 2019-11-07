package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.tropicraft.Constants;
import net.tropicraft.core.common.entity.SeaTurtleEntity;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;

@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MODID)
public class PlayerRotationHandler {

    private static float rotationYawHead, prevRotationYawHead, rotationPitch, prevRotationPitch;
    
    private static float interpolateAndWrap(float cur, float prev, float partial) {
        return MathHelper.wrapDegrees(prev + ((cur - prev) * partial));
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        PlayerEntity p = event.getPlayer();
        Entity riding = p.getRidingEntity();
        if (riding instanceof BeachFloatEntity) {
            FurnitureEntity floaty = (FurnitureEntity) riding;
            GlStateManager.pushMatrix();
            GlStateManager.translated(event.getX(), event.getY(), event.getZ());

            GlStateManager.rotatef(-(floaty.prevRotationYaw + (event.getPartialRenderTick() * (floaty.rotationYaw - floaty.prevRotationYaw))), 0, 1, 0);
            GlStateManager.translated(0, 1.55, 1.55);
            GlStateManager.rotatef(-90, 1, 0, 0);

            // Cancel out player camera rotation
            float f = interpolateAndWrap(p.renderYawOffset, p.prevRenderYawOffset, event.getPartialRenderTick());
            GlStateManager.rotatef(f, 0, 1, 0);

            // Lock in head
            rotationYawHead = p.rotationYawHead;
            prevRotationYawHead = p.prevRotationYawHead;
            p.rotationYawHead = p.renderYawOffset;
            p.prevRotationYawHead = p.prevRenderYawOffset;
            rotationPitch = p.rotationPitch;
            prevRotationPitch = p.prevRotationPitch;
            p.rotationPitch = 10f;
            p.prevRotationPitch = 10f;

            GlStateManager.translated(-event.getX(), -event.getY(), -event.getZ());
        }
        if (riding instanceof SeaTurtleEntity) {
            SeaTurtleEntity turtle = (SeaTurtleEntity) riding;
            GlStateManager.pushMatrix();
            GlStateManager.translated(event.getX(), event.getY(), event.getZ());

            // Cancel out player camera rotation
            float pitch = interpolateAndWrap(turtle.rotationPitch, turtle.prevRotationPitch, event.getPartialRenderTick());
            float yaw = interpolateAndWrap(turtle.rotationYawHead, turtle.prevRotationYawHead, event.getPartialRenderTick());

            GlStateManager.translated(0, turtle.getMountedYOffset() - p.getYOffset(), 0);
            GlStateManager.rotatef(-yaw, 0, 1, 0);
            GlStateManager.translated(0, -0.1, 0); // TODO figure out why this budging is needed
            GlStateManager.rotatef(pitch, 1, 0, 0);
            GlStateManager.translated(0, 0.1, 0);
            GlStateManager.rotatef(yaw, 0, 1, 0);
            GlStateManager.translated(0, -turtle.getMountedYOffset() + p.getYOffset(), 0);

            Vec3d passengerOffset = (new Vec3d(-0.25f, 0.0D, 0.0D)).rotateYaw((float) (-Math.toRadians(yaw) - (Math.PI / 2)));
            GlStateManager.translated(passengerOffset.getX(), 0, passengerOffset.getZ());

            // Lock in head
            rotationPitch = p.rotationPitch;
            prevRotationPitch = p.prevRotationPitch;
            p.rotationPitch = 10f;
            p.prevRotationPitch = 10f;

            GlStateManager.translated(-event.getX(), -event.getY(), -event.getZ());
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        PlayerEntity p = event.getPlayer();
        if (p.getRidingEntity() instanceof BeachFloatEntity || p.getRidingEntity() instanceof SeaTurtleEntity) {
            GlStateManager.popMatrix();
            p.rotationPitch = rotationPitch;
            p.prevRotationPitch = prevRotationPitch;
        }
        if (p.getRidingEntity() instanceof BeachFloatEntity) {
            p.rotationYawHead = rotationYawHead;
            p.prevRotationYawHead = prevRotationYawHead;
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerSpecials(RenderLivingEvent.Specials.Pre<?, ?> event) {
        if (event.getEntity().getRidingEntity() instanceof BeachFloatEntity) {
            event.setCanceled(true);
        }
    }
}
