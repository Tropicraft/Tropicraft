package net.tropicraft.core.client.entity.render;

import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.TriState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.entity.SeaTurtleEntity;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import org.joml.Quaternionf;

import java.util.function.BiConsumer;

@EventBusSubscriber(value = Dist.CLIENT, modid = Tropicraft.ID)
public class PlayerRotationHandler {
    private static final ContextKey<BeachFloatState> BEACH_FLOAT_KEY = new ContextKey<>(Tropicraft.id("beach_float"));
    private static final ContextKey<TurtleState> TURTLE_KEY = new ContextKey<>(Tropicraft.id("sea_turtle"));

    private record BeachFloatState(
            float yRot,
            float offsetX,
            float offsetY,
            float offsetZ
    ) {
    }

    private record TurtleState(
            float xRot,
            float yRot,
            float offsetX,
            float offsetY,
            float offsetZ
    ) {
    }

    @SubscribeEvent
    public static void onRegisterRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier((Class<EntityRenderer<Avatar, AvatarRenderState>>) (Class<?>) AvatarRenderer.class, (avatar, state) -> {
            if (avatar.getVehicle() instanceof BeachFloatEntity beachFloat) {
                state.yRot = 0.0f;
                state.xRot = 10.0f;
                state.walkAnimationPos = 0.0f;
                state.walkAnimationSpeed = 0.0f;

                Vec3 attachment = beachFloat.getAttachments().getClamped(EntityAttachment.PASSENGER, 0, 0.0f);
                float playerHeight = avatar.getDimensions(Pose.STANDING).height();
                state.setRenderData(BEACH_FLOAT_KEY, new BeachFloatState(
                        Mth.rotLerp(state.partialTick, beachFloat.yRotO, beachFloat.getYRot()),
                        (float) -attachment.x,
                        (float) (-attachment.y + 13.0 / 16.0),
                        (float) (playerHeight / 2.0 - attachment.z)
                ));
            } else if (avatar.getVehicle() instanceof SeaTurtleEntity turtle) {
                state.xRot = 10.0f;

                Vec3 sitOffset = avatar.getAttachments().getClamped(EntityAttachment.VEHICLE, 0, 0);
                state.setRenderData(TURTLE_KEY, new TurtleState(
                        Mth.rotLerp(state.partialTick, turtle.xRotO, turtle.getXRot()),
                        Mth.rotLerp(state.partialTick, turtle.yHeadRotO, turtle.yHeadRot),
                        (float) sitOffset.x,
                        (float) sitOffset.y,
                        (float) sitOffset.z
                ));
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPlayer(RenderPlayerEvent.Pre<AbstractClientPlayer> event) {
        PoseStack stack = event.getPoseStack();
        AvatarRenderState state = event.getRenderState();

        BeachFloatState floatState = state.getRenderData(BEACH_FLOAT_KEY);
        if (floatState != null) {
            stack.pushPose();
            stack.mulPose(Axis.YP.rotationDegrees(-floatState.yRot));
            stack.translate(floatState.offsetX, floatState.offsetY, floatState.offsetZ);
            stack.mulPose(Axis.XN.rotationDegrees(90));
            // Cancel out player camera rotation
            stack.mulPose(Axis.YP.rotationDegrees(state.bodyRot));
        }

        TurtleState turtleState = state.getRenderData(TURTLE_KEY);
        if (turtleState != null) {
            stack.pushPose();

            // Cancel out player camera rotation
            Quaternionf rotation = Axis.YN.rotationDegrees(turtleState.yRot)
                    .mul(Axis.XP.rotationDegrees(turtleState.xRot))
                    .mul(Axis.YP.rotationDegrees(turtleState.yRot));

            stack.rotateAround(rotation, turtleState.offsetX, turtleState.offsetY - 0.1f, turtleState.offsetZ);

            Vec3 passengerOffset = (new Vec3(-0.25f, 0.0, 0.0)).yRot((float) (-Math.toRadians(turtleState.yRot) - (Math.PI / 2)));
            stack.translate(passengerOffset.x(), 0, passengerOffset.z());
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post<AbstractClientPlayer> event) {
        AvatarRenderState state = event.getRenderState();
        if (state.getRenderData(BEACH_FLOAT_KEY) != null || state.getRenderData(TURTLE_KEY) != null) {
            event.getPoseStack().popPose();
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerSpecials(RenderNameTagEvent.CanRender event) {
        BeachFloatState floatState = event.getEntityRenderState().getRenderData(BEACH_FLOAT_KEY);
        if (floatState != null) {
            event.setCanRender(TriState.FALSE);
        }
    }
}
