package net.tropicraft;

import net.minecraft.util.ARGB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public record ColorRamp(
        float[] xs,
        float[] reds,
        float[] greens,
        float[] blues,
        float[] redGrads,
        float[] greenGrads,
        float[] blueGrads
) {
    public static Builder builder() {
        return new Builder();
    }

    public ColorRamp remap(float oldFrom, float oldTo, float newFrom, float newTo) {
        float[] xs = Arrays.copyOf(this.xs, this.xs.length);
        float[] redGrads = Arrays.copyOf(this.redGrads, this.redGrads.length);
        float[] greenGrads = Arrays.copyOf(this.greenGrads, this.greenGrads.length);
        float[] blueGrads = Arrays.copyOf(this.blueGrads, this.blueGrads.length);
        float scale = (newTo - newFrom) / (oldTo - oldFrom);
        for (int i = 0; i < xs.length; i++) {
            xs[i] = newFrom + (xs[i] - oldFrom) * scale;
        }
        for (int i = 0; i < redGrads.length; i++) {
            redGrads[i] /= scale;
            greenGrads[i] /= scale;
            blueGrads[i] /= scale;
        }
        return new ColorRamp(xs, reds, greens, blues, redGrads, greenGrads, blueGrads);
    }

    public int get(float x) {
        int index = indexAt(x);
        if (index <= 0) {
            return pack(reds[0], greens[0], blues[0]);
        } else if (index >= xs.length) {
            int lastIndex = xs.length - 1;
            return pack(reds[lastIndex], greens[lastIndex], blues[lastIndex]);
        }
        float a = x - xs[index];
        return pack(
                reds[index] + a * redGrads[index - 1],
                greens[index] + a * greenGrads[index - 1],
                blues[index] + a * blueGrads[index - 1]
        );
    }

    private static int pack(float red, float green, float blue) {
        return ARGB.color(255, (int) red, (int) green, (int) blue);
    }

    private int indexAt(float x) {
        int i = Arrays.binarySearch(xs, x);
        return i >= 0 ? i : -i - 1;
    }

    public static class Builder {
        private final List<Point> points = new ArrayList<>();

        private Builder() {
        }

        public Builder color(float x, int red, int green, int blue) {
            points.add(new Point(x, red, green, blue));
            return this;
        }

        public Builder color(float x, int color) {
            return color(x, ARGB.red(color), ARGB.green(color), ARGB.blue(color));
        }

        public ColorRamp build() {
            if (points.size() < 2) {
                throw new IllegalStateException("Cannot have less than 2 points");
            }

            float[] xs = new float[points.size()];
            float[] reds = new float[points.size()];
            float[] greens = new float[points.size()];
            float[] blues = new float[points.size()];
            float[] redGrads = new float[points.size() - 1];
            float[] greenGrads = new float[points.size() - 1];
            float[] blueGrads = new float[points.size() - 1];

            points.sort(Comparator.comparingDouble(Point::x));

            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                xs[i] = point.x();
                reds[i] = point.red();
                greens[i] = point.green();
                blues[i] = point.blue();
            }

            for (int i = 0; i < points.size() - 1; i++) {
                Point start = points.get(i);
                Point end = points.get(i + 1);
                float deltaX = end.x() - start.x();
                redGrads[i] = (end.red() - start.red()) / deltaX;
                greenGrads[i] = (end.green() - start.green()) / deltaX;
                blueGrads[i] = (end.blue() - start.blue()) / deltaX;
            }

            return new ColorRamp(xs, reds, greens, blues, redGrads, greenGrads, blueGrads);
        }
    }

    private record Point(float x, float red, float green, float blue) {
    }
}
