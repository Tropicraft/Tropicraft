package net.tropicraft.densityfunction.explorer;

import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;

public class Camera {
    private static final float MOUSE_SENSITIVITY = 0.15f;

    private final Vector3f position = new Vector3f();
    private final Quaternionf rotation = new Quaternionf();

    private float rotationX;
    private float rotationY;

    public void moveTo(float x, float y, float z) {
        position.set(x, y, z);
    }

    public void handleInput(long window, float deltaTime) {
        int[] windowWidth = new int[1];
        int[] windowHeight = new int[1];
        GLFW.glfwGetWindowSize(window, windowWidth, windowHeight);

        double grabCursorX = windowWidth[0] / 2.0;
        double grabCursorY = windowHeight[0] / 2.0;

        double[] mouseX = new double[1];
        double[] mouseY = new double[1];
        GLFW.glfwGetCursorPos(window, mouseX, mouseY);
        rotate(
                (float) (grabCursorY - mouseY[0]) * MOUSE_SENSITIVITY,
                (float) (grabCursorX - mouseX[0]) * MOUSE_SENSITIVITY
        );
        GLFW.glfwSetCursorPos(window, grabCursorX, grabCursorY);

        float speed = 200.0f;
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) {
            speed *= 2.0f;
        }

        position.add(getInputDirection(window).mul(deltaTime * speed).rotateY(rotationY * Mth.DEG_TO_RAD));
    }

    private static Vector3f getInputDirection(long window) {
        float forward = getInputAxisDirection(window, GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_S);
        float right = getInputAxisDirection(window, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_D);
        float up = getInputAxisDirection(window, GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_SPACE);
        return new Vector3f(right, up, forward);
    }

    private static float getInputAxisDirection(long window, int negativeKey, int positiveKey) {
        float direction = 0.0f;
        if (GLFW.glfwGetKey(window, negativeKey) == GLFW.GLFW_PRESS) {
            direction -= 1.0f;
        }
        if (GLFW.glfwGetKey(window, positiveKey) == GLFW.GLFW_PRESS) {
            direction += 1.0f;
        }
        return direction;
    }

    private void rotate(float x, float y) {
        rotationX += x;
        rotationX = Mth.clamp(rotationX, -90.0f, 90.0f);
        rotationY += y;
        rotation.rotationZYX(0.0f, rotationY * Mth.DEG_TO_RAD, rotationX * Mth.DEG_TO_RAD);
    }

    public Vector3fc position() {
        return position;
    }

    public Quaternionfc rotation() {
        return rotation;
    }
}
