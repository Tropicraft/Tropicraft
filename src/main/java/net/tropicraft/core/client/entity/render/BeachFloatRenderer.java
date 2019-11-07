package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tropicraft.core.client.entity.model.BeachFloatModel;
import net.tropicraft.core.common.entity.SeaTurtleEntity;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;

public class BeachFloatRenderer extends FurnitureRenderer<BeachFloatEntity> {

    public BeachFloatRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, "beach_float", new BeachFloatModel());
        shadowSize = .5F;
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Override
    protected double getYOffset() {
        return super.getYOffset() + 1.2;
    }
    
    @Override
    protected void setupTransforms() {
        GlStateManager.rotatef(-180f, 0, 1, 0);
    }
    
    @Override
    protected boolean rockOnZAxis() {
        return true;
    }

    // Player rendering
    
    private static float rotationYawHead, prevRotationYawHead, rotationPitch, prevRotationPitch;

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
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
            float f = p.renderYawOffset - p.prevRenderYawOffset;
            while (f < -180)
                f += 360;
            while (f >= 180)
                f -= 360;
            f = p.prevRenderYawOffset + (event.getPartialRenderTick() * f);

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
            float pitch = turtle.rotationPitch - turtle.prevRotationPitch;
            while (pitch < -180)
                pitch += 360;
            while (pitch >= 180)
                pitch -= 360;
            pitch = turtle.prevRotationPitch + (event.getPartialRenderTick() * pitch);
            float yaw = turtle.rotationYawHead - turtle.prevRotationYawHead;
            while (yaw < -180)
                yaw += 360;
            while (yaw >= 180)
                yaw -= 360;
            yaw = turtle.prevRotationYawHead + (event.getPartialRenderTick() * yaw);

            GlStateManager.translated(0, turtle.getMountedYOffset() - p.getYOffset(), 0);
            GlStateManager.rotatef(-yaw, 0, 1, 0);
            GlStateManager.translated(0, -0.1, 0); // TODO figure out why this budging is needed
            GlStateManager.rotatef(pitch, 1, 0, 0);
            GlStateManager.translated(0, 0.1, 0);
            GlStateManager.rotatef(yaw, 0, 1, 0);
            GlStateManager.translated(0, -turtle.getMountedYOffset() + p.getYOffset(), 0);
            
            Vec3d passengerOffset = (new Vec3d(-0.25f, 0.0D, 0.0D))
                    .rotateYaw((float) (-Math.toRadians(yaw) - (Math.PI / 2)));
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
    public void onRenderPlayerPost(RenderPlayerEvent.Post event) {
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
    public void onRenderPlayerSpecials(RenderLivingEvent.Specials.Pre<?, ?> event) {
        if (event.getEntity().getRidingEntity() instanceof BeachFloatEntity) {
            event.setCanceled(true);
        }
    }
}
