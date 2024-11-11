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

        public float twitch(float interval, float speed, float scale) {
            if (time * speed % interval > 1.0f) {
                return 0.0f;
            }
            float forward = Mth.square(eval(speed, 1.0f));
            float backward = Mth.square(eval(speed * 0.5f, 1.0f, 0.5f, 0.0f));
            return scale * (forward - 0.5f * backward);
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
