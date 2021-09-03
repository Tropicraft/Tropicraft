package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public final class ModelAnimator {
    static final float PI = (float) Math.PI;
    static final float DEG_TO_RAD = (float) (Math.PI / 180.0F);
    static final float TAU = 2 * PI;

    static Cycle cycle;

    public static void look(ModelRenderer part, float yaw, float pitch) {
        part.rotateAngleX = pitch * DEG_TO_RAD;
        part.rotateAngleY = yaw * DEG_TO_RAD;
    }

    public static Cycle cycle(float time, float scale) {
        Cycle cycle = ModelAnimator.cycle;
        ModelAnimator.cycle = null;

        if (cycle == null) cycle = new Cycle();

        return cycle.set(time, scale);
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
            return this.eval(speed, scale, 0.0F, 0.0F);
        }

        public float eval(float speed, float scale, float delay, float offset) {
            float x = this.time * speed - delay;
            return (MathHelper.sin(TAU * x) * scale + offset) * this.scale;
        }

        @Override
        public void close() {
            ModelAnimator.cycle = this;
        }
    }
}
