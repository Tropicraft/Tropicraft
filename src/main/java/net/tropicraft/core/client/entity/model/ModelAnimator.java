package net.tropicraft.core.client.entity.model;

import com.mojang.math.Transformation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class ModelAnimator {
    static final float PI = (float) Math.PI;
    static final float DEG_TO_RAD = (float) (Math.PI / 180.0f);
    static final float TAU = 2 * PI;

    @Nullable
    static Cycle cycle;

    public static MeshTransformer scaling(float scaleX, float scaleY, float scaleZ) {
        float offsetY = -EntityModel.MODEL_Y_OFFSET * 16.0f * (1.0f - scaleY);
        return part -> part.transformed(pose -> pose.scaled(scaleX, scaleY, scaleZ).translated(0.0f, offsetY, 0.0f));
    }

    public static MeshTransformer hierarchicalBaby(String headName, float scale) {
        return hierarchicalBaby(headName, scale, 1.0f);
    }

    public static MeshTransformer hierarchicalBaby(String headName, float scale, float headScale) {
        MeshTransformer bodyScaling = MeshTransformer.scaling(scale);
        return mesh -> bodyScaling.apply(
                transformChildPart(mesh, headName, pose -> pose.scaled(headScale / scale))
        );
    }

    public static MeshDefinition transformChildPart(MeshDefinition mesh, String targetName, UnaryOperator<PartPose> targetTransformer) {
        MeshDefinition result = new MeshDefinition();
        for (Map.Entry<String, PartDefinition> entry : mesh.getRoot().getChildren()) {
            result.getRoot().addOrReplaceChild(entry.getKey(), transformChildPart(entry.getValue(), targetName, targetTransformer));
        }
        return result;
    }

    private static PartDefinition transformChildPart(PartDefinition part, String targetName, UnaryOperator<PartPose> targetTransformer) {
        PartDefinition result = part.transformed(pose -> pose);
        for (Map.Entry<String, PartDefinition> child : part.getChildren()) {
            if (child.getKey().equals(targetName)) {
                result.addOrReplaceChild(targetName, child.getValue().transformed(targetTransformer));
            } else {
                result.addOrReplaceChild(child.getKey(), transformChildPart(child.getValue(), targetName, targetTransformer));
            }
        }
        return result;
    }

    public static PartDefinition clearAllChildren(PartDefinition part, String childName) {
        PartDefinition child = part.clearChild(childName);
        for (Map.Entry<String, PartDefinition> grandchild : List.copyOf(child.getChildren())) {
            clearAllChildren(child, grandchild.getKey());
        }
        return child;
    }

    public static void look(ModelPart part, float yaw, float pitch) {
        part.xRot = pitch * DEG_TO_RAD;
        part.yRot = yaw * DEG_TO_RAD;
    }

    public static void look(ModelPart part, LivingEntityRenderState renderState) {
        look(part, renderState.yRot, renderState.xRot);
    }

    public static Cycle cycle(float time, float scale) {
        Cycle cycle = ModelAnimator.cycle;
        ModelAnimator.cycle = null;

        if (cycle == null) cycle = new Cycle();

        return cycle.set(time, scale);
    }

    public static void rotateAround(ModelPart part, Vector3f axis, float angle) {
        part.rotateBy(new Quaternionf().setAngleAxis(angle, axis.x, axis.y, axis.z));
    }

    public static void setRotation(ModelPart part, Quaternionf quaternion) {
        setRotationFromMatrix(part, new Matrix3f().rotation(quaternion));
    }

    private static void setRotationFromMatrix(ModelPart part, Matrix3f matrix) {
        // There's an equivalent helper on Quaternionf, but it seems incorrectly implemented
        Vector3f newAngles = matrix.getEulerAnglesZYX(new Vector3f());
        part.xRot = newAngles.x;
        part.yRot = newAngles.y;
        part.zRot = newAngles.z;
    }

    public static void rotateByInModelSpace(ModelPart[] path, ModelPart part, Quaternionf quaternion) {
        Quaternionf parentRotation = new Quaternionf();
        for (ModelPart parent : path) {
            parentRotation.rotateZYX(parent.zRot, parent.yRot, parent.xRot);
        }
        Quaternionf absoluteRotation = parentRotation.rotateZYX(part.zRot, part.yRot, part.xRot, new Quaternionf());
        Quaternionf newAbsoluteRotation = quaternion.mul(absoluteRotation, new Quaternionf());
        setRotation(part, newAbsoluteRotation.premul(parentRotation.conjugate()));
    }

    public static PartPose toPose(Matrix4f matrix) {
        Transformation transformation = new Transformation(matrix);
        Vector3f translation = transformation.getTranslation();
        Vector3f rotation = new Matrix3f()
                .rotation(transformation.getLeftRotation())
                .rotate(transformation.getRightRotation())
                .getEulerAnglesZYX(new Vector3f());
        Vector3f scale = transformation.getScale();
        return new PartPose(translation.x, translation.y, translation.z, rotation.x, rotation.y, rotation.z, scale.x, scale.y, scale.z);
    }

    public static final class Cycle implements AutoCloseable {
        private float time;
        private float scale;

        Cycle set(float time, float scale) {
            this.time = time;
            this.scale = scale;
            return this;
        }

        public float eval(float speed, float scale) {
            return eval(speed, scale, 0.0f, 0.0f);
        }

        public float eval(float speed, float scale, float delay, float offset) {
            float x = time * speed - delay;
            return (Mth.sin(TAU * x) * scale + offset) * this.scale;
        }

        public float twitchSymmetric(float interval, float speed, float scale) {
            if (time * speed % interval > 1.0f) {
                return 0.0f;
            }
            float forward = Mth.square(eval(speed, 1.0f));
            float backward = Mth.square(eval(speed * 0.5f, 1.0f, 0.5f, 0.0f));
            return scale * (forward - 0.5f * backward);
        }

        public float twitchAsymmetric(float interval, float speed, float scale) {
            if (time * speed % interval > 1.0f) {
                return 0.0f;
            }
            return scale * Mth.square(eval(speed, 1.0f));
        }

        public float periodic(float interval, float fade, float length, float scale) {
            float animationLength = length + fade * 2.0f;
            float cycleLength = animationLength + interval;
            float animationTime = (time % cycleLength) - interval;
            if (animationTime < 0.0f) {
                return 0.0f;
            }
            if (animationTime < fade) {
                return Mth.sin(animationTime / fade * Mth.HALF_PI) * scale;
            } else if (animationTime > fade + length) {
                return Mth.sin((animationLength - animationTime) / fade * Mth.HALF_PI) * scale;
            }
            return scale;
        }

        public float evalSkewed(float speed, float scale, float delay, float offset, float skew, float squareness) {
            float x = TAU * (time * speed - delay);
            float modifiedSin = squareness * Mth.sin(x);
            float value = modifiedSin / Mth.sqrt(Mth.square(skew + Mth.cos(x)) + Mth.square(modifiedSin));
            return (value * scale + offset) * this.scale;
        }

        public float time() {
            return time;
        }

        @Override
        public void close() {
            ModelAnimator.cycle = this;
        }
    }
}
