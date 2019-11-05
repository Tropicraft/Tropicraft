package extendedrenderer.particle.entity;

import CoroUtil.util.CoroUtilParticle;
import extendedrenderer.placeholders.Quaternion;
import extendedrenderer.placeholders.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

public class ParticleTexExtraRender extends ParticleTexFX {

	private int severityOfRainRate = 2;

	private int extraParticlesBaseAmount = 5;

	public boolean noExtraParticles = false;

	//public float[] cachedLight;
	
	public ParticleTexExtraRender(World worldIn, double posXIn, double posYIn,
			double posZIn, double mX, double mY, double mZ,
			TextureAtlasSprite par8Item) {
		super(worldIn, posXIn, posYIn, posZIn, mX, mY, mZ, par8Item);

		/*cachedLight = new float[CoroUtilParticle.rainPositions.length];
		if (worldObj.getGameTime() % 5 == 0) {
			for (int i = 0; i < cachedLight.length; i++) {
				Vec3 vec = CoroUtilParticle.rainPositions[i];
				cachedLight[i] = getBrightnessNonLightmap(new BlockPos(posX+vec.xCoord, posY+vec.yCoord, posZ+vec.zCoord), 1F);
			}
		}*/
	}

	public int getSeverityOfRainRate() {
		return severityOfRainRate;
	}

	public void setSeverityOfRainRate(int severityOfRainRate) {
		this.severityOfRainRate = severityOfRainRate;
	}

	public int getExtraParticlesBaseAmount() {
		return extraParticlesBaseAmount;
	}

	public void setExtraParticlesBaseAmount(int extraParticlesBaseAmount) {
		this.extraParticlesBaseAmount = extraParticlesBaseAmount;
	}

	@Override
	public void tickExtraRotations() {
		//super.tickExtraRotations();

		if (isSlantParticleToWind()) {
			rotationYaw = (float)Math.toDegrees(Math.atan2(motionZ, motionX)) - 90;
			double motionXZ = Math.sqrt(motionX * motionX + motionZ * motionZ);
			//motionXZ = motionX/* + motionZ*/;
			rotationPitch = -(float)Math.toDegrees(Math.atan2(motionXZ, Math.abs(motionY)));
			//rotationPitch = rotationPitch;
			//rotationPitch = -45;
			//rotationPitch *= 10F;
		}

		/*if (!quatControl) {
			Entity ent = Minecraft.getInstance().getRenderViewEntity();
			updateQuaternion(ent);
		}*/
	}

	@Override
	public void renderParticle(BufferBuilder worldRendererIn, ActiveRenderInfo entityIn,
			float partialTicks, float rotationX, float rotationZ,
			float rotationYZ, float rotationXY, float rotationXZ) {

		//override rotations
		if (!facePlayer) {
			rotationX = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			rotationYZ = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
	        rotationXY = -rotationYZ * MathHelper.sin(this.rotationPitch * (float)Math.PI / 180.0F);
	        rotationXZ = rotationX * MathHelper.sin(this.rotationPitch * (float)Math.PI / 180.0F);
	        rotationZ = MathHelper.cos(this.rotationPitch * (float)Math.PI / 180.0F);
		} else {
			if (this.isSlantParticleToWind()) {
				rotationXZ = (float) -this.motionZ;
				rotationXY = (float) -this.motionX;
			}
			//rotationXZ = 6.28F;
			//rotationXY = 1;
			//rotationZ -= 1;
		}


		float f = this.getMinU();
		float f1 = this.getMaxU();
		float f2 = this.getMinV();
		float f3 = this.getMaxV();
        float f4 = 0.1F * this.particleScale;
		float scaleY = 0.4F * this.particleScale;

		float scale1 = 0.1F * this.particleScale;
		float scale2 = 0.1F * this.particleScale;
		float scale3 = 0.1F * this.particleScale;
		float scale4 = 0.1F * this.particleScale;

		float fixY = 0;

        /*if (this.particleTexture != null)
        {*/
            /*f = this.getMinU();
            f1 = this.getMaxU();
            f2 = this.getMinV();
            f3 = this.getMaxV();*/

			/*f = this.particleTexture.getInterpolatedU((double)(this.particleTextureJitterX / 4.0F * 16.0F));
			f1 = this.particleTexture.getInterpolatedU((double)((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
			f2 = this.particleTexture.getInterpolatedV((double)(this.particleTextureJitterY / 4.0F * 16.0F));
			f3 = this.particleTexture.getInterpolatedV((double)((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));*/

			float part = 16F / 3F;
			float offset = 0;
			float posBottom = (float)(this.posY - 10D);

			float height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, new BlockPos(this.posX, this.posY, this.posZ)).getY();

			if (posBottom < height) {
				float diff = height - posBottom;
				offset = diff;
				fixY = 0;//diff * 1.0F;
				if (offset > part) offset = part;
			}

			/*f = this.particleTexture.getInterpolatedU(part);
			f1 = this.particleTexture.getInterpolatedU(part*2F);
			f2 = this.particleTexture.getInterpolatedV((part*2F) + offset);
			f3 = this.particleTexture.getInterpolatedV(part + offset);*/

			/*f = this.particleTexture.getInterpolatedU(4);
			f1 = this.particleTexture.getInterpolatedU(12);
			f2 = this.particleTexture.getInterpolatedV(16);
			f3 = this.particleTexture.getInterpolatedV(8);*/
        //}

		//int rainDrops = extraParticlesBaseAmount + ((Math.max(0, severityOfRainRate-1)) * 5);
		int renderAmount = 0;
		if (noExtraParticles) {
			renderAmount = 1;
		} else {
			renderAmount = Math.min(extraParticlesBaseAmount + ((Math.max(0, severityOfRainRate-1)) * 5), CoroUtilParticle.maxRainDrops);
		}

        //test
		//rainDrops = 100;

		//catch code hotload crash, doesnt help much anyways
		try {
			for (int ii = 0; ii < renderAmount/*(noExtraParticles ? 1 : Math.min(rainDrops, CoroUtilParticle.maxRainDrops))*/; ii++) {
				float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
				float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY) + fixY;
				float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);

				double xx = 0;
				double zz = 0;
				double yy = 0;
				if (ii != 0) {
					xx = CoroUtilParticle.rainPositions[ii].xCoord;
					zz = CoroUtilParticle.rainPositions[ii].zCoord;
					yy = CoroUtilParticle.rainPositions[ii].yCoord;

					f5 += xx;
					f6 += yy;
					f7 += zz;
				}

				//prevent precip under overhangs/inside for extra render
				if (this.isDontRenderUnderTopmostBlock()) {
					int height2 = world.getHeight(Heightmap.Type.MOTION_BLOCKING, new BlockPos(this.posX + xx, this.posY, this.posZ + zz)).getY();
					if (this.posY + yy <= height2) continue;
				}

				//TODO: 1.14 uncomment
				/*if (ii != 0) {
					RotatingParticleManager.debugParticleRenderCount++;
				}*/

				/*int height = entityIn.world.getPrecipitationHeight(new BlockPos(ActiveRenderInfo.getPosition().xCoord + f5, this.posY + f6, ActiveRenderInfo.getPosition().zCoord + f7)).getY();
				if (ActiveRenderInfo.getPosition().yCoord + f6 <= height) continue;*/

				int i = this.getBrightnessForRender(partialTicks);
				i = 15728640;
				int j = i >> 16 & 65535;
				int k = i & 65535;

				//range between 0 and 240 for first value, second value always 0
				//j = 240;
				//k = 120;

				/*int what = 13 << 20 | 15 << 4;
				int what2 = what >> 16 & 65535;
				int what3 = what & 65535;*/

				Vec3d[] avec3d = new Vec3d[] {
						new Vec3d((double)(-rotationX * scale1 - rotationXY * scale1), (double)(-rotationZ * scale1), (double)(-rotationYZ * scale1 - rotationXZ * scale1)),
						new Vec3d((double)(-rotationX * scale2 + rotationXY * scale2), (double)(rotationZ * scale2), (double)(-rotationYZ * scale2 + rotationXZ * scale2)),
						new Vec3d((double)(rotationX * scale3 + rotationXY * scale3), (double)(rotationZ * scale3), (double)(rotationYZ * scale3 + rotationXZ * scale3)),
						new Vec3d((double)(rotationX * scale4 - rotationXY * scale4), (double)(-rotationZ * scale4), (double)(rotationYZ * scale4 - rotationXZ * scale4))};

				/*if (this.field_190014_F != 0.0F)
				{
					float f8 = this.field_190014_F + (this.field_190014_F - this.field_190015_G) * partialTicks;
					float f9 = MathHelper.cos(f8 * 0.5F);
					float f10 = MathHelper.sin(f8 * 0.5F) * (float)field_190016_K.xCoord;
					float f11 = MathHelper.sin(f8 * 0.5F) * (float)field_190016_K.yCoord;
					float f12 = MathHelper.sin(f8 * 0.5F) * (float)field_190016_K.zCoord;
					Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);

					for (int l = 0; l < 4; ++l)
					{
						avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double)(2.0F * f9)));
					}
				}*/

				worldRendererIn.pos((double)f5 + avec3d[0].x, (double)f6 + avec3d[0].y, (double)f7 + avec3d[0].z).tex((double)f1, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
				worldRendererIn.pos((double)f5 + avec3d[1].x, (double)f6 + avec3d[1].y, (double)f7 + avec3d[1].z).tex((double)f1, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
				worldRendererIn.pos((double)f5 + avec3d[2].x, (double)f6 + avec3d[2].y, (double)f7 + avec3d[2].z).tex((double)f, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
				worldRendererIn.pos((double)f5 + avec3d[3].x, (double)f6 + avec3d[3].y, (double)f7 + avec3d[3].z).tex((double)f, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		


        
	}

	//TODO: 1.14 uncomment
	/*public void renderParticleForShader(InstancedMeshParticle mesh, Transformation transformation, Matrix4fe viewMatrix, Entity entityIn,
										float partialTicks, float rotationX, float rotationZ,
										float rotationYZ, float rotationXY, float rotationXZ) {

		float posX = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks);
		float posY = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks);
		float posZ = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks);
		//Vector3f pos = new Vector3f((float) (entityIn.posX - particle.posX), (float) (entityIn.posY - particle.posY), (float) (entityIn.posZ - particle.posZ));

		int renderAmount = 0;
		if (noExtraParticles) {
			renderAmount = 1;
		} else {
			renderAmount = Math.min(extraParticlesBaseAmount + ((Math.max(0, severityOfRainRate-1)) * 5), CoroUtilParticle.maxRainDrops);
		}

		for (int iii = 0; iii < renderAmount; iii++) {

			if (mesh.curBufferPos >= mesh.numInstances) return;

			Vector3f pos;

			if (iii != 0) {
				pos = new Vector3f(posX + (float) CoroUtilParticle.rainPositions[iii].xCoord,
						posY + (float) CoroUtilParticle.rainPositions[iii].yCoord,
						posZ + (float) CoroUtilParticle.rainPositions[iii].zCoord);
			} else {
				pos = new Vector3f(posX, posY, posZ);
			}

			if (false && useRotationAroundCenter) {
				float deltaRot = rotationAroundCenterPrev + (rotationAroundCenter - rotationAroundCenterPrev) * partialTicks;
				float rotX = (float) Math.sin(Math.toRadians(deltaRot));
				float rotZ = (float) Math.cos(Math.toRadians(deltaRot));
				pos.x += rotX * rotationDistAroundCenter;
				pos.z += rotZ * rotationDistAroundCenter;
			}

			if (this.isDontRenderUnderTopmostBlock()) {
				int height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, new BlockPos(pos.x, this.posY, pos.z)).getY();
				if (pos.y <= height) continue;
			}

			//adjust to relative to camera positions finally
			pos.x -= interpPosX;
			pos.y -= interpPosY;
			pos.z -= interpPosZ;

			Matrix4fe modelMatrix = transformation.buildModelMatrix(this, pos, partialTicks);

			//adjust to perspective and camera
			//Matrix4fe modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
			//upload to buffer
			modelMatrix.get(mesh.INSTANCE_SIZE_FLOATS * (mesh.curBufferPos), mesh.instanceDataBuffer);

			//brightness
			float brightness;
			//brightness = CoroUtilBlockLightCache.getBrightnessCached(worldObj, pos.x, pos.y, pos.z);
			//brightness = this.brightnessCache;
			if (fastLight) {
				brightness = CoroUtilBlockLightCache.brightnessPlayer;
			} else {
				brightness = CoroUtilBlockLightCache.getBrightnessCached(world, (float)this.posX, (float)this.posY, (float)this.posZ);
			}

			//brightness to buffer
			mesh.instanceDataBuffer.put(mesh.INSTANCE_SIZE_FLOATS * (mesh.curBufferPos) + mesh.MATRIX_SIZE_FLOATS, brightness);

			//rgba to buffer
			int rgbaIndex = 0;
			mesh.instanceDataBuffer.put(mesh.INSTANCE_SIZE_FLOATS * (mesh.curBufferPos)
					+ mesh.MATRIX_SIZE_FLOATS + 1 + (rgbaIndex++), this.particleRed);
			mesh.instanceDataBuffer.put(mesh.INSTANCE_SIZE_FLOATS * (mesh.curBufferPos)
					+ mesh.MATRIX_SIZE_FLOATS + 1 + (rgbaIndex++), this.particleGreen);
			mesh.instanceDataBuffer.put(mesh.INSTANCE_SIZE_FLOATS * (mesh.curBufferPos)
					+ mesh.MATRIX_SIZE_FLOATS + 1 + (rgbaIndex++), this.particleBlue);
			mesh.instanceDataBuffer.put(mesh.INSTANCE_SIZE_FLOATS * (mesh.curBufferPos)
					+ mesh.MATRIX_SIZE_FLOATS + 1 + (rgbaIndex++), this.getAlphaF());

			mesh.curBufferPos++;
		}

	}*/

	/*@Override
	public void renderParticleForShaderTest(InstancedMeshParticle mesh, Transformation transformation, Matrix4fe viewMatrix, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {


		float posX = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks);
		float posY = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks);
		float posZ = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks);

		int renderAmount = 0;
		if (noExtraParticles) {
			renderAmount = 1;
		} else {
			renderAmount = Math.min(extraParticlesBaseAmount + ((Math.max(0, severityOfRainRate-1)) * 5), CoroUtilParticle.maxRainDrops);
		}

		for (int iii = 0; iii < renderAmount; iii++) {

			if (mesh.curBufferPos >= mesh.numInstances) return;

			Vector3f pos;

			if (iii != 0) {
				pos = new Vector3f(posX + (float) CoroUtilParticle.rainPositions[iii].xCoord,
						posY + (float) CoroUtilParticle.rainPositions[iii].yCoord,
						posZ + (float) CoroUtilParticle.rainPositions[iii].zCoord);
			} else {
				pos = new Vector3f(posX, posY, posZ);
			}

			if (this.isDontRenderUnderTopmostBlock()) {
				int height = this.world.getPrecipitationHeight(new BlockPos(pos.x, this.posY, pos.z)).getY();
				if (pos.y <= height) continue;
			}

			//adjust to relative to camera positions finally
			pos.x -= interpPosX;
			pos.y -= interpPosY;
			pos.z -= interpPosZ;

			int rgbaIndex = 0;
			mesh.instanceDataBufferTest.put(mesh.INSTANCE_SIZE_FLOATS_TEST * (mesh.curBufferPos)
					+ (rgbaIndex++), this.getRedColorF());
			mesh.instanceDataBufferTest.put(mesh.INSTANCE_SIZE_FLOATS_TEST * (mesh.curBufferPos)
					+ (rgbaIndex++), this.getGreenColorF());
			mesh.instanceDataBufferTest.put(mesh.INSTANCE_SIZE_FLOATS_TEST * (mesh.curBufferPos)
					+ (rgbaIndex++), this.getBlueColorF());
			mesh.instanceDataBufferTest.put(mesh.INSTANCE_SIZE_FLOATS_TEST * (mesh.curBufferPos)
					+ (rgbaIndex++), this.getAlphaF());

			mesh.curBufferPos++;

		}

	}*/

	/*@Override
	public void updateQuaternion(Entity camera) {

		if (camera != null) {
			if (this.facePlayer) {
				this.rotationYaw = camera.rotationYaw;
				this.rotationPitch = camera.rotationPitch;
			} else if (facePlayerYaw) {
				this.rotationYaw = camera.rotationYaw;
			}
		}

		Quaternion qY = new Quaternion();
		Quaternion qX = new Quaternion();
		qY.setFromAxisAngle(new Vector4f(0, 1, 0, (float)Math.toRadians(-this.rotationYaw - 180F)));
		qX.setFromAxisAngle(new Vector4f(1, 0, 0, (float)Math.toRadians(-this.rotationPitch)));
		if (this.rotateOrderXY) {
			Quaternion.mul(qX, qY, this.rotation);
		} else {
			Quaternion.mul(qY, qX, this.rotation);

			if (extraYRotation != 0) {
				//float rot = (new Random()).nextFloat() * 360F;
				qY = new Quaternion();
				qY.setFromAxisAngle(new Vector4f(0, 1, 0, extraYRotation));
				Quaternion.mul(this.rotation, qY, this.rotation);
			}
		}
	}*/

}
