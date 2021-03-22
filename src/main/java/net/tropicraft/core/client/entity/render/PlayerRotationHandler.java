package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.Event.Result;
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
        MatrixStack stack = event.getMatrixStack();
        IRenderTypeBuffer buffers = event.getBuffers();
        PlayerEntity p = event.getPlayer();
        Entity riding = p.getRidingEntity();
        if (riding instanceof BeachFloatEntity) {
            FurnitureEntity floaty = (FurnitureEntity) riding;

            stack.push();
            stack.rotate(Vector3f.YP.rotationDegrees(-(floaty.prevRotationYaw + (event.getPartialRenderTick() * (floaty.rotationYaw - floaty.prevRotationYaw)))));
            stack.translate(0, 1.55, 1.55);
            stack.rotate(Vector3f.XN.rotationDegrees(90));

            // Cancel out player camera rotation
            float f = interpolateAndWrap(p.renderYawOffset, p.prevRenderYawOffset, event.getPartialRenderTick());
            stack.rotate(Vector3f.YP.rotationDegrees(f));

            // Lock in head
            rotationYawHead = p.rotationYawHead;
            prevRotationYawHead = p.prevRotationYawHead;
            p.rotationYawHead = p.renderYawOffset;
            p.prevRotationYawHead = p.prevRenderYawOffset;
            rotationPitch = p.rotationPitch;
            prevRotationPitch = p.prevRotationPitch;
            p.rotationPitch = 10f;
            p.prevRotationPitch = 10f;
            
            // Cancel limb swing
            p.limbSwing = 0;
            p.limbSwingAmount = 0;
            p.prevLimbSwingAmount = 0;
        }
        if (riding instanceof SeaTurtleEntity) {
            SeaTurtleEntity turtle = (SeaTurtleEntity) riding;
            stack.push();

            // Cancel out player camera rotation
            float pitch = interpolateAndWrap(turtle.rotationPitch, turtle.prevRotationPitch, event.getPartialRenderTick());
            float yaw = interpolateAndWrap(turtle.rotationYawHead, turtle.prevRotationYawHead, event.getPartialRenderTick());

            stack.translate(0, turtle.getMountedYOffset() - p.getYOffset(), 0);
            stack.rotate(Vector3f.YN.rotationDegrees(yaw));
            stack.translate(0, -0.1, 0); // TODO figure out why this budging is needed
            stack.rotate(Vector3f.XP.rotationDegrees(pitch));
            stack.translate(0, 0.1, 0);
            stack.rotate(Vector3f.YP.rotationDegrees(yaw));
            stack.translate(0, -turtle.getMountedYOffset() + p.getYOffset(), 0);

            Vector3d passengerOffset = (new Vector3d(-0.25f, 0.0D, 0.0D)).rotateYaw((float) (-Math.toRadians(yaw) - (Math.PI / 2)));
            stack.translate(passengerOffset.getX(), 0, passengerOffset.getZ());

            // Lock in head
            rotationPitch = p.rotationPitch;
            prevRotationPitch = p.prevRotationPitch;
            p.rotationPitch = 10f;
            p.prevRotationPitch = 10f;
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        PlayerEntity p = event.getPlayer();
        if (p.getRidingEntity() instanceof BeachFloatEntity || p.getRidingEntity() instanceof SeaTurtleEntity) {
            event.getMatrixStack().pop();
            p.rotationPitch = rotationPitch;
            p.prevRotationPitch = prevRotationPitch;
        }
        if (p.getRidingEntity() instanceof BeachFloatEntity) {
            p.rotationYawHead = rotationYawHead;
            p.prevRotationYawHead = prevRotationYawHead;
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerSpecials(RenderNameplateEvent event) {
        if (event.getEntity().getRidingEntity() instanceof BeachFloatEntity) {
            event.setResult(Result.DENY);
        }
    }
}
