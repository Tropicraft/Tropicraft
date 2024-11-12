package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public final class ModelAnimator {
    static final float PI = (float) Math.PI;
    static final float DEG_TO_RAD = (float) (Math.PI / 180.0f);
    static final float TAU = 2 * PI;

    @Nullable
    static Cycle cycle;

    public static void look(ModelPart part, float yaw, float pitch) {
        part.xRot = pitch * DEG_TO_RAD;
        part.yRot = yaw * DEG_TO_RAD;
    }

    public static Cycle cycle(float time, float scale) {
        Cycle cycle = ModelAnimator.cycle;
        ModelAnimator.cycle = null;

        if (cycle == null) cycle = new Cycle();

        return cycle.set(time, scale);
    }

    public static void rotateAround(ModelPart part, Vector3f axis, float angle) {
        rotateBy(part, new Quaternionf().setAngleAxis(angle, axis.x, axis.y, axis.z));
    }

    public static void rotateBy(ModelPart part, Quaternionf quaternion) {
        Matrix3f newRotation = new Matrix3f()
                .rotationZYX(part.zRot, part.yRot, part.xRot)
                .rotate(quaternion);
        setRotationFromMatrix(part, newRotation);
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
