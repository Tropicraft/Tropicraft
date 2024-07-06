package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.entity.SeaTurtleEntity;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import org.joml.Quaternionf;

@EventBusSubscriber(value = Dist.CLIENT, modid = Tropicraft.ID)
public class PlayerRotationHandler {

    private static float rotationYawHead, prevRotationYawHead, rotationPitch, prevRotationPitch;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        PoseStack stack = event.getPoseStack();
        Player p = event.getEntity();
        Entity riding = p.getVehicle();
        float partialTick = event.getPartialTick();

        if (riding instanceof BeachFloatEntity floaty) {
            stack.pushPose();
            stack.mulPose(Axis.YP.rotationDegrees(-Mth.rotLerp(partialTick, floaty.yRotO, floaty.getYRot())));
            Vec3 attachment = floaty.getAttachments().getClamped(EntityAttachment.PASSENGER, 0, 0.0f);
            float playerHeight = p.getDimensions(Pose.STANDING).height();
            stack.translate(-attachment.x, -attachment.y + 13.0 / 16.0, playerHeight / 2.0 - attachment.z);
            stack.mulPose(Axis.XN.rotationDegrees(90));

            // Cancel out player camera rotation
            stack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(partialTick, p.yBodyRotO, p.yBodyRot)));

            // Lock in head
            rotationYawHead = p.yHeadRot;
            prevRotationYawHead = p.yHeadRotO;
            p.yHeadRot = p.yBodyRot;
            p.yHeadRotO = p.yBodyRotO;
            rotationPitch = p.getXRot();
            prevRotationPitch = p.xRotO;
            p.setXRot(10.0f);
            p.xRotO = 10.0f;

            // Cancel limb swing
            p.walkAnimation.setSpeed(0.0f);
            p.walkAnimation.update(0.0f, 1.0f);
        }
        if (riding instanceof SeaTurtleEntity turtle) {
            stack.pushPose();

            // Cancel out player camera rotation
            float pitch = Mth.rotLerp(partialTick, turtle.xRotO, turtle.getXRot());
            float yaw = Mth.rotLerp(partialTick, turtle.yHeadRotO, turtle.yHeadRot);

            Quaternionf rotation = Axis.YN.rotationDegrees(yaw)
                    .mul(Axis.XP.rotationDegrees(pitch))
                    .mul(Axis.YP.rotationDegrees(yaw));

            Vec3 sitOffset = p.getAttachments().getClamped(EntityAttachment.VEHICLE, 0, 0);
            stack.rotateAround(rotation, (float) sitOffset.x, (float) sitOffset.y - 0.1f, (float) sitOffset.z);

            Vec3 passengerOffset = (new Vec3(-0.25f, 0.0, 0.0)).yRot((float) (-Math.toRadians(yaw) - (Math.PI / 2)));
            stack.translate(passengerOffset.x(), 0, passengerOffset.z());

            // Lock in head
            rotationPitch = p.getXRot();
            prevRotationPitch = p.xRotO;
            p.setXRot(10.0f);
            p.xRotO = 10.0f;
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player p = event.getEntity();
        if (p.getVehicle() instanceof BeachFloatEntity || p.getVehicle() instanceof SeaTurtleEntity) {
            event.getPoseStack().popPose();
            p.setXRot(rotationPitch);
            p.xRotO = prevRotationPitch;
        }
        if (p.getVehicle() instanceof BeachFloatEntity) {
            p.yHeadRot = rotationYawHead;
            p.yHeadRotO = prevRotationYawHead;
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerSpecials(RenderNameTagEvent event) {
        if (event.getEntity().getVehicle() instanceof BeachFloatEntity) {
            event.setCanRender(TriState.FALSE);
        }
    }
}
