package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.tropicraft.Constants;
import net.tropicraft.core.common.entity.SeaTurtleEntity;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;

@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MODID)
public class PlayerRotationHandler {

    private static float rotationYawHead, prevRotationYawHead, rotationPitch, prevRotationPitch;
    
    private static float interpolateAndWrap(float cur, float prev, float partial) {
        return Mth.wrapDegrees(prev + ((cur - prev) * partial));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        PoseStack stack = event.getPoseStack();
        Player p = event.getEntity();
        Entity riding = p.getVehicle();
        final float partialTick = event.getPartialTick();

        if (riding instanceof BeachFloatEntity floaty) {
            stack.pushPose();
            stack.mulPose(Axis.YP.rotationDegrees(-(floaty.yRotO + (partialTick * (floaty.getYRot() - floaty.yRotO)))));
            stack.translate(0, 1.55, 1.55);
            stack.mulPose(Axis.XN.rotationDegrees(90));

            // Cancel out player camera rotation
            float f = interpolateAndWrap(p.yBodyRot, p.yBodyRotO, partialTick);
            stack.mulPose(Axis.YP.rotationDegrees(f));

            // Lock in head
            rotationYawHead = p.yHeadRot;
            prevRotationYawHead = p.yHeadRotO;
            p.yHeadRot = p.yBodyRot;
            p.yHeadRotO = p.yBodyRotO;
            rotationPitch = p.getXRot();
            prevRotationPitch = p.xRotO;
            p.setXRot(10f);
            p.xRotO = 10f;
            
            // Cancel limb swing
            p.walkAnimation.setSpeed(0.0f);
            p.walkAnimation.update(0.0f, 1.0f);
        }
        if (riding instanceof SeaTurtleEntity turtle) {
            stack.pushPose();

            // Cancel out player camera rotation
            float pitch = interpolateAndWrap(turtle.getXRot(), turtle.xRotO, partialTick);
            float yaw = interpolateAndWrap(turtle.yHeadRot, turtle.yHeadRotO, partialTick);

            stack.translate(0, turtle.getPassengersRidingOffset() - p.getMyRidingOffset(), 0);
            stack.mulPose(Axis.YN.rotationDegrees(yaw));
            stack.translate(0, -0.1, 0); // TODO figure out why this budging is needed
            stack.mulPose(Axis.XP.rotationDegrees(pitch));
            stack.translate(0, 0.1, 0);
            stack.mulPose(Axis.YP.rotationDegrees(yaw));
            stack.translate(0, -turtle.getPassengersRidingOffset() + p.getMyRidingOffset(), 0);

            Vec3 passengerOffset = (new Vec3(-0.25f, 0.0D, 0.0D)).yRot((float) (-Math.toRadians(yaw) - (Math.PI / 2)));
            stack.translate(passengerOffset.x(), 0, passengerOffset.z());

            // Lock in head
            rotationPitch = p.getXRot();
            prevRotationPitch = p.xRotO;
            p.setXRot(10f);
            p.xRotO = 10f;
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
            event.setResult(Result.DENY);
        }
    }
}
