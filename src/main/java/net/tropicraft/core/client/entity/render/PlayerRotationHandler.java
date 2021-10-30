package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
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
        return Mth.wrapDegrees(prev + ((cur - prev) * partial));
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        PoseStack stack = event.getMatrixStack();
        MultiBufferSource buffers = event.getBuffers();
        Player p = event.getPlayer();
        Entity riding = p.getVehicle();
        if (riding instanceof BeachFloatEntity) {
            FurnitureEntity floaty = (FurnitureEntity) riding;

            stack.pushPose();
            stack.mulPose(Vector3f.YP.rotationDegrees(-(floaty.yRotO + (event.getPartialRenderTick() * (floaty.yRot - floaty.yRotO)))));
            stack.translate(0, 1.55, 1.55);
            stack.mulPose(Vector3f.XN.rotationDegrees(90));

            // Cancel out player camera rotation
            float f = interpolateAndWrap(p.yBodyRot, p.yBodyRotO, event.getPartialRenderTick());
            stack.mulPose(Vector3f.YP.rotationDegrees(f));

            // Lock in head
            rotationYawHead = p.yHeadRot;
            prevRotationYawHead = p.yHeadRotO;
            p.yHeadRot = p.yBodyRot;
            p.yHeadRotO = p.yBodyRotO;
            rotationPitch = p.xRot;
            prevRotationPitch = p.xRotO;
            p.xRot = 10f;
            p.xRotO = 10f;
            
            // Cancel limb swing
            p.animationPosition = 0;
            p.animationSpeed = 0;
            p.animationSpeedOld = 0;
        }
        if (riding instanceof SeaTurtleEntity) {
            SeaTurtleEntity turtle = (SeaTurtleEntity) riding;
            stack.pushPose();

            // Cancel out player camera rotation
            float pitch = interpolateAndWrap(turtle.xRot, turtle.xRotO, event.getPartialRenderTick());
            float yaw = interpolateAndWrap(turtle.yHeadRot, turtle.yHeadRotO, event.getPartialRenderTick());

            stack.translate(0, turtle.getPassengersRidingOffset() - p.getMyRidingOffset(), 0);
            stack.mulPose(Vector3f.YN.rotationDegrees(yaw));
            stack.translate(0, -0.1, 0); // TODO figure out why this budging is needed
            stack.mulPose(Vector3f.XP.rotationDegrees(pitch));
            stack.translate(0, 0.1, 0);
            stack.mulPose(Vector3f.YP.rotationDegrees(yaw));
            stack.translate(0, -turtle.getPassengersRidingOffset() + p.getMyRidingOffset(), 0);

            Vec3 passengerOffset = (new Vec3(-0.25f, 0.0D, 0.0D)).yRot((float) (-Math.toRadians(yaw) - (Math.PI / 2)));
            stack.translate(passengerOffset.x(), 0, passengerOffset.z());

            // Lock in head
            rotationPitch = p.xRot;
            prevRotationPitch = p.xRotO;
            p.xRot = 10f;
            p.xRotO = 10f;
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player p = event.getPlayer();
        if (p.getVehicle() instanceof BeachFloatEntity || p.getVehicle() instanceof SeaTurtleEntity) {
            event.getMatrixStack().popPose();
            p.xRot = rotationPitch;
            p.xRotO = prevRotationPitch;
        }
        if (p.getVehicle() instanceof BeachFloatEntity) {
            p.yHeadRot = rotationYawHead;
            p.yHeadRotO = prevRotationYawHead;
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerSpecials(RenderNameplateEvent event) {
        if (event.getEntity().getVehicle() instanceof BeachFloatEntity) {
            event.setResult(Result.DENY);
        }
    }
}
