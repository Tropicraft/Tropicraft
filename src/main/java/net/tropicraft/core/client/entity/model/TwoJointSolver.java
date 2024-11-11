package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Matrix4x3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TwoJointSolver {
	private final ModelPart[] body;
	private final ModelPart hip;
	private final ModelPart knee;
	private final ModelPart foot;

	private final Vector3f hipOrigin;
	private final Vector3f footOrigin;

	private final Vector3f worldFootOrigin;
	private final Quaternionf worldFootRotation;

	private final float baseHipAngle;
	private final float baseKneeAngle;

	private final Vector3f hipBendAxis;
	private final Vector3f kneeBendAxis;

	private final float hipToKneeLength;
	private final float kneeToFootLength;

	public TwoJointSolver(ModelPart[] body, ModelPart hip, ModelPart knee, ModelPart foot) {
		this.body = body;
		this.hip = hip;
		this.knee = knee;
		this.foot = foot;

		Matrix4x3f localToWorld = getLocalToWorld();
		Quaternionf localToWorldRotation = localToWorld.getNormalizedRotation(new Quaternionf());

		Matrix4x3f hipToLocal = new Matrix4x3f();
		translateAndRotate(hip, hipToLocal);
		Matrix4x3f kneeToLocal = new Matrix4x3f(hipToLocal);
		translateAndRotate(knee, kneeToLocal);
		Matrix4x3f footToLocal = new Matrix4x3f(kneeToLocal);
		translateAndRotate(foot, footToLocal);

		hipOrigin = hipToLocal.transformPosition(new Vector3f());
		Vector3f kneeOrigin = kneeToLocal.transformPosition(new Vector3f());
		footOrigin = footToLocal.transformPosition(new Vector3f());

		worldFootOrigin = localToWorld.transformPosition(footOrigin, new Vector3f());
		worldFootRotation = footToLocal.getNormalizedRotation(new Quaternionf()).premul(localToWorldRotation);

		Vector3f hipToKnee = kneeOrigin.sub(hipOrigin, new Vector3f());
		Vector3f kneeToFoot = footOrigin.sub(kneeOrigin, new Vector3f());
		Vector3f hipToFoot = footOrigin.sub(hipOrigin, new Vector3f());

		Vector3f bendAxis = hipToFoot.cross(hipToKnee, new Vector3f());
		hipBendAxis = hipToLocal.invert().transformDirection(bendAxis, new Vector3f()).normalize();
		kneeBendAxis = kneeToLocal.invert().transformDirection(bendAxis, new Vector3f()).normalize();

		hipToKneeLength = hipOrigin.distance(kneeOrigin);
		kneeToFootLength = kneeOrigin.distance(footOrigin);

		baseHipAngle = hipToFoot.angle(hipToKnee);
		baseKneeAngle = hipToKnee.negate(new Vector3f()).angle(kneeToFoot);
	}

	public void apply(Vector3f target, float factor) {
		applyInWorldSpace(new Vector3f(target.x, 1.5f - target.y, target.z), factor);
	}

	public void applyRelativeToBase(float factor, float offsetX, float offsetY, float offsetZ) {
		applyInWorldSpace(worldFootOrigin.add(offsetX, offsetY, offsetZ, new Vector3f()), factor);
	}

	private void applyInWorldSpace(Vector3f footTargetWorld, float factor) {
		if (factor < Mth.EPSILON) {
			return;
		}
		Vector3f footTargetLocal = getWorldToLocal().transformPosition(footTargetWorld);
		footTargetLocal.lerp(footOrigin, 1.0f - factor);
		applyInLocalSpace(footTargetLocal);
	}

	private void applyInLocalSpace(Vector3f footTarget) {
		if (footTarget.distanceSquared(footOrigin) < Mth.EPSILON) {
			// Already close enough to the target position, and the numerics will break down
			return;
		}

		Vector3f hipToFoot = footOrigin.sub(hipOrigin, new Vector3f());
		Vector3f hipToTarget = footTarget.sub(hipOrigin, new Vector3f());

		// Clamped, as we don't want to try overextend
		float hipToTargetLength = Mth.clamp(hipToTarget.length(), Mth.EPSILON, hipToKneeLength + kneeToFootLength - Mth.EPSILON);

		// Find desired angles in the hip/knee/target triangle
		float newHipAngle = angleForSideLengths(hipToKneeLength, hipToTargetLength, kneeToFootLength);
		float newKneeAngle = angleForSideLengths(hipToKneeLength, kneeToFootLength, hipToTargetLength);
		ModelAnimator.rotateAround(hip, hipBendAxis, newHipAngle - baseHipAngle);
		ModelAnimator.rotateAround(knee, kneeBendAxis, newKneeAngle - baseKneeAngle);

		// Rotate the entire leg into place
		Quaternionf localToHip = new Quaternionf().rotationZYX(hip.zRot, hip.yRot, hip.xRot).conjugate();
		Vector3f finalAxis = hipToFoot.cross(hipToTarget, new Vector3f()).rotate(localToHip).normalize();
		float finalAngle = hipToFoot.angle(hipToTarget);
		ModelAnimator.rotateAround(hip, finalAxis, finalAngle);

		// Rotate the foot back to restore its absolute rotation in world space
		Quaternionf footRotation = getWorldKneeRotation(new Quaternionf()).conjugate().mul(worldFootRotation);
		ModelAnimator.setRotation(foot, footRotation);
	}

	private Matrix4x3f getLocalToWorld() {
		Matrix4x3f localToWorld = new Matrix4x3f();
		for (ModelPart part : body) {
			translateAndRotate(part, localToWorld);
		}
		return localToWorld;
	}

	private Matrix4x3f getWorldToLocal() {
		Matrix4x3f worldToLocal = new Matrix4x3f();
		for (int i = body.length - 1; i >= 0; i--) {
			translateAndRotateInverse(body[i], worldToLocal);
		}
		return worldToLocal;
	}

	// Cosine rule: gives the angle opposite to side c
	private static float angleForSideLengths(float a, float b, float c) {
		return (float) Math.acos(Mth.clamp(
				(a * a + b * b - c * c) / (2 * a * b),
				-1.0f, 1.0f
		));
	}

	private Quaternionf getWorldKneeRotation(Quaternionf result) {
		for (ModelPart part : body) {
			result.rotateZYX(part.zRot, part.yRot, part.xRot);
		}
		result.rotateZYX(hip.zRot, hip.yRot, hip.xRot);
		result.rotateZYX(knee.zRot, knee.yRot, knee.xRot);
		return result;
	}

	private static void translateAndRotate(ModelPart part, Matrix4x3f matrix) {
		matrix.translate(part.x / 16.0f, part.y / 16.0f, part.z / 16.0f);
		if (part.xRot != 0.0f || part.yRot != 0.0f || part.zRot != 0.0f) {
			matrix.rotateZYX(part.zRot, part.yRot, part.xRot);
		}
		if (part.xScale != 1.0f || part.yScale != 1.0f || part.zScale != 1.0f) {
			matrix.scale(part.xScale, part.yScale, part.zScale);
		}
	}

	private static void translateAndRotateInverse(ModelPart part, Matrix4x3f worldToLocal) {
		if (part.xScale != 1.0f || part.yScale != 1.0f || part.zScale != 1.0f) {
			worldToLocal.scale(1.0f / part.xScale, 1.0f / part.yScale, 1.0f / part.zScale);
		}
		if (part.xRot != 0.0f || part.yRot != 0.0f || part.zRot != 0.0f) {
			worldToLocal.rotate(new Quaternionf().rotationZYX(part.zRot, part.yRot, part.xRot).conjugate());
		}
		worldToLocal.translate(-part.x / 16.0f, -part.y / 16.0f, -part.z / 16.0f);
	}
}
