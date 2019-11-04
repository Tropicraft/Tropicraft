package extendedrenderer.particle.entity;

import CoroUtil.api.weather.IWindHandler;
import CoroUtil.util.Vec3;
import extendedrenderer.particle.behavior.ParticleBehaviors;
import extendedrenderer.placeholders.Quaternion;
import extendedrenderer.placeholders.Vector4f;
import extendedrenderer.shader.IShaderRenderedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.vecmath.Vector3f;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public class EntityRotFX extends SpriteTexturedParticle implements IWindHandler, IShaderRenderedEntity
{
    public boolean weatherEffect = false;

    public float spawnY = -1;
    
    //this field and 2 methods below are for backwards compatibility with old particle system from the new icon based system
    public int particleTextureIndexInt = 0;
    
    public float brightness = 0.7F;
    
    public ParticleBehaviors pb = null; //designed to be a reference to the central objects particle behavior
    
    public boolean callUpdateSuper = true;
    public boolean callUpdatePB = true;
    
    public float renderRange = 128F;
    
    //used in RotatingEffectRenderer to assist in solving some transparency ordering issues, eg, tornado funnel before clouds
    public int renderOrder = 0;
    
    //not a real entity ID now, just used for making rendering of entities slightly unique
    private int entityID = 0;
    
    public int debugID = 0;
    
    public float rotationYaw;
    public float rotationPitch;
    
    public float windWeight = 5;
    public int particleDecayExtra = 0;
    public boolean isTransparent = true;
    
    public boolean killOnCollide = false;
	
	public boolean facePlayer = false;

	//facePlayer will override this
    public boolean facePlayerYaw = false;
	
	public boolean vanillaMotionDampen = true;

    //for particle behaviors
    public double aboveGroundHeight = 4.5D;
    public boolean checkAheadToBounce = true;
    public boolean collisionSpeedDampen = true;

    public double bounceSpeed = 0.05D;
    public double bounceSpeedMax = 0.15D;
    public double bounceSpeedAhead = 0.35D;
    public double bounceSpeedMaxAhead = 0.25D;

    public boolean spinFast = false;

    private float ticksFadeInMax = 0;
    private float ticksFadeOutMax = 0;

    private boolean dontRenderUnderTopmostBlock = false;

    private boolean killWhenUnderTopmostBlock = false;
    private int killWhenUnderTopmostBlock_ScanAheadRange = 0;

    public int killWhenUnderCameraAtLeast = 0;

    public int killWhenFarFromCameraAtLeast = 0;

    private float ticksFadeOutMaxOnDeath = -1;
    private float ticksFadeOutCurOnDeath = 0;
    protected boolean fadingOut = false;

    public float avoidTerrainAngle = 0;

    //this is for yaw only
    public boolean useRotationAroundCenter = false;
    public float rotationAroundCenter = 0;
    public float rotationAroundCenterPrev = 0;
    public float rotationSpeedAroundCenter = 0;
    public float rotationDistAroundCenter = 0;

    private boolean slantParticleToWind = false;

    public Quaternion rotation;
    public Quaternion rotationPrev;

    //set to true for direct quaternion control, not EULER conversion helper
    public boolean quatControl = false;

    public boolean fastLight = false;

    public float brightnessCache = 0.5F;

    public boolean rotateOrderXY = false;

    public float extraYRotation = 0;

    public boolean isCollidedHorizontally = false;
    public boolean isCollidedVerticallyDownwards = false;
    public boolean isCollidedVerticallyUpwards = false;

    //used for translational rotation around a point
    public Vector3f rotationAround = new Vector3f();

    public EntityRotFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        super(par1World, par2, par4, par6, par8, par10, par12);
        setSize(0.3F, 0.3F);
        //this.isImmuneToFire = true;
        //this.setMaxAge(100);
        
        this.entityID = par1World.rand.nextInt(100000);

        rotation = new Quaternion();

        //TODO: 1.14 uncomment for shaders
        //brightnessCache = CoroUtilBlockLightCache.getBrightnessCached(world, (float)posX, (float)posY, (float)posZ);
    }

    public boolean isSlantParticleToWind() {
        return slantParticleToWind;
    }

    public void setSlantParticleToWind(boolean slantParticleToWind) {
        this.slantParticleToWind = slantParticleToWind;
    }

    public float getTicksFadeOutMaxOnDeath() {
        return ticksFadeOutMaxOnDeath;
    }

    public void setTicksFadeOutMaxOnDeath(float ticksFadeOutMaxOnDeath) {
        this.ticksFadeOutMaxOnDeath = ticksFadeOutMaxOnDeath;
    }

    public boolean isKillWhenUnderTopmostBlock() {
        return killWhenUnderTopmostBlock;
    }

    public void setKillWhenUnderTopmostBlock(boolean killWhenUnderTopmostBlock) {
        this.killWhenUnderTopmostBlock = killWhenUnderTopmostBlock;
    }

    public boolean isDontRenderUnderTopmostBlock() {
        return dontRenderUnderTopmostBlock;
    }

    public void setDontRenderUnderTopmostBlock(boolean dontRenderUnderTopmostBlock) {
        this.dontRenderUnderTopmostBlock = dontRenderUnderTopmostBlock;
    }

    public float getTicksFadeInMax() {
        return ticksFadeInMax;
    }

    public void setTicksFadeInMax(float ticksFadeInMax) {
        this.ticksFadeInMax = ticksFadeInMax;
    }

    public float getTicksFadeOutMax() {
        return ticksFadeOutMax;
    }

    public void setTicksFadeOutMax(float ticksFadeOutMax) {
        this.ticksFadeOutMax = ticksFadeOutMax;
    }
    
    public int getParticleTextureIndex()
    {
        return this.particleTextureIndexInt;
    }
    
    public void setMaxAge(int par) {
    	maxAge = par;
    }
    
    public float getAlphaF()
    {
        return this.particleAlpha;
    }
    
    @Override
    public void setExpired() {
    	if (pb != null) pb.particles.remove(this);
    	super.setExpired();
    }
    
    @Override
    public void tick() {
    	super.tick();

        Entity ent = Minecraft.getInstance().getRenderViewEntity();

        //if (this.entityID % 400 == 0) System.out.println("tick time: " + this.worldObj.getGameTime());
    	
    	if (!isVanillaMotionDampen()) {
    		//cancel motion dampening (which is basically air resistance)
    		//keep this up to date with the inverse of whatever Particle.tick uses
        	this.motionX /= 0.9800000190734863D;
            this.motionY /= 0.9800000190734863D;
            this.motionZ /= 0.9800000190734863D;
    	}

    	if (!this.isExpired && !fadingOut) {
            if (killOnCollide) {
                if (this.isCollided()) {
                    startDeath();
                }

            }

            BlockPos pos = new BlockPos(new BlockPos(this.posX, this.posY, this.posZ));

            if (killWhenUnderTopmostBlock) {


                //int height = this.world.getPrecipitationHeight(new BlockPos(this.posX, this.posY, this.posZ)).getY();
                int height = world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos).getY();
                if (this.posY - killWhenUnderTopmostBlock_ScanAheadRange <= height) {
                    startDeath();
                }
            }

            //case: when on high pillar and rain is falling far below you, start killing it / fading it out
            if (killWhenUnderCameraAtLeast != 0) {
                if (this.posY < ent.posY - killWhenUnderCameraAtLeast) {
                    startDeath();
                }
            }

            if (killWhenFarFromCameraAtLeast != 0) {
                if (getAge() > 20 && getAge() % 5 == 0) {

                    if (ent.getDistanceSq(this.posX, this.posY, this.posZ) > killWhenFarFromCameraAtLeast * killWhenFarFromCameraAtLeast) {
                        //System.out.println("far kill");
                        startDeath();
                    }
                }
            }
        }

    	if (!collisionSpeedDampen) {
            //if (this.isCollided()) {
            if (this.onGround) {
                this.motionX /= 0.699999988079071D;
                this.motionZ /= 0.699999988079071D;
            }
        }

        if (spinFast) {
            this.rotationPitch += this.entityID % 2 == 0 ? 10 : -10;
            this.rotationYaw += this.entityID % 2 == 0 ? -10 : 10;
        }

        if (!fadingOut) {
            if (ticksFadeInMax > 0 && this.getAge() < ticksFadeInMax) {
                //System.out.println("particle.getAge(): " + particle.getAge());
                this.setAlphaF((float)this.getAge() / ticksFadeInMax);
                //particle.setAlphaF(1);
            } else if (ticksFadeOutMax > 0 && this.getAge() > this.getMaxAge() - ticksFadeOutMax) {
                float count = this.getAge() - (this.getMaxAge() - ticksFadeOutMax);
                float val = (ticksFadeOutMax - (count)) / ticksFadeOutMax;
                //System.out.println(val);
                this.setAlphaF(val);
                //make sure fully visible otherwise
            } else if (ticksFadeInMax > 0 || ticksFadeOutMax > 0) {
                this.setAlphaF(1F);
            }
        } else {
    	    if (ticksFadeOutCurOnDeath < ticksFadeOutMaxOnDeath) {
                ticksFadeOutCurOnDeath++;
            } else {
    	        this.setExpired();
            }
            float val = 1F - (ticksFadeOutCurOnDeath / ticksFadeOutMaxOnDeath);
            //System.out.println(val);
            this.setAlphaF(val);
        }

        if (world.getGameTime() % 5 == 0) {
            //TODO: 1.14 uncomment
            //brightnessCache = CoroUtilBlockLightCache.getBrightnessCached(world, (float)posX, (float)posY, (float)posZ);
        }

        rotationAroundCenter += rotationSpeedAroundCenter;
        rotationAroundCenter %= 360;
        /*while (rotationAroundCenter >= 360) {
            System.out.println(rotationAroundCenter);
            rotationAroundCenter -= 360;
        }*/

        tickExtraRotations();
    }

    public void tickExtraRotations() {
        if (slantParticleToWind) {
            double motionXZ = Math.sqrt(motionX * motionX + motionZ * motionZ);
            rotationPitch = (float)Math.atan2(motionY, motionXZ);
        }

        if (!quatControl) {
            rotationPrev = new Quaternion(rotation);
            Entity ent = Minecraft.getInstance().getRenderViewEntity();
            updateQuaternion(ent);
        }
    }

    public void startDeath() {
        if (ticksFadeOutMaxOnDeath > 0) {
            ticksFadeOutCurOnDeath = 0;//ticksFadeOutMaxOnDeath;
            fadingOut = true;
        } else {
            this.setExpired();
        }
    }
    
    /*public void setParticleTextureIndex(int par1)
    {
        this.particleTextureIndexInt = par1;
        if (this.getFXLayer() == 0) super.setParticleTextureIndex(par1);
    }*/

    /*@Override
    public int getFXLayer()
    {
        return 5;
    }*/

    public void spawnAsWeatherEffect()
    {
        weatherEffect = true;
        //TODO: 1.14 uncomment
        /*ExtendedRenderer.rotEffRenderer.addEffect(this);*/
        //TODO: 1.14 remove after above fixed
        Minecraft.getInstance().particles.addEffect(this);
        //RELOCATED TO CODE AFTER CALLING spawnAsWeatherEffect(), also uses list in WeatherManagerClient
        //this.world.addWeatherEffect(this);
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        age = age;
    }

    public int getMaxAge()
    {
        return maxAge;
    }

    public void setSize(float par1, float par2)
    {
        super.setSize(par1, par2);
        // MC-12269 - fix particle being offset to the NW
        this.setPosition(posX, posY, posZ);
    }
    
    public void setGravity(float par) {
    	particleGravity = par;
    }
    
    public float maxRenderRange() {
    	return renderRange;
    }

    public void setScale(float parScale) {
    	particleScale = parScale;
    }

    @Override
    public Vector3f getPosition() {
        return new Vector3f((float)posX, (float)posY, (float)posZ);
    }

    @Override
    public Quaternion getQuaternion() {
        return this.rotation;
    }

    @Override
    public Quaternion getQuaternionPrev() {
        return this.rotationPrev;
    }

    @Override
    public float getScale() {
    	return particleScale;
    }

    public Vec3 getPos() {
        return new Vec3(posX, posY, posZ);
    }

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getPosZ() {
		return posZ;
	}

	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	public double getMotionX() {
		return motionX;
	}

	public void setMotionX(double motionX) {
		this.motionX = motionX;
	}

	public double getMotionY() {
		return motionY;
	}

	public void setMotionY(double motionY) {
		this.motionY = motionY;
	}

	public double getMotionZ() {
		return motionZ;
	}

	public void setMotionZ(double motionZ) {
		this.motionZ = motionZ;
	}

	public double getPrevPosX() {
		return prevPosX;
	}

	public void setPrevPosX(double prevPosX) {
		this.prevPosX = prevPosX;
	}

	public double getPrevPosY() {
		return prevPosY;
	}

	public void setPrevPosY(double prevPosY) {
		this.prevPosY = prevPosY;
	}

	public double getPrevPosZ() {
		return prevPosZ;
	}

	public void setPrevPosZ(double prevPosZ) {
		this.prevPosZ = prevPosZ;
	}

	public int getEntityId() {
		return entityID;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public void setCanCollide(boolean val) {
		this.canCollide = val;
	}
	
	public boolean getCanCollide() {
		return this.canCollide;
	}
	
	public boolean isCollided() {
		return this.onGround;
	}
	
	public double getDistance(double x, double y, double z)
    {
        double d0 = this.posX - x;
        double d1 = this.posY - y;
        double d2 = this.posZ - z;
        return (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
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
		}
		
		/*IBlockState state = this.getWorld().getBlockState(new BlockPos(posX, posY, posZ));
		if (state.getBlock() != Blocks.AIR) {
			System.out.println("particle in: " + state);
		}*/

		//particleScale = 30;

        //1.12 did 0.1F * scale in render, but removed it, this makes my scales backwards compatible
        //changing just for render, yes, very hacky
        particleScale *= 0.1F;

        super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX,
                rotationZ, rotationYZ, rotationXY, rotationXZ);

        particleScale *= 10F;
	}

    //TODO: 1.14 uncomment
	/*public void renderParticleForShader(InstancedMeshParticle mesh, Transformation transformation, Matrix4fe viewMatrix, Entity entityIn,
                                        float partialTicks, float rotationX, float rotationZ,
                                        float rotationYZ, float rotationXY, float rotationXZ) {

        if (mesh.curBufferPos >= mesh.numInstances) return;

        //camera relative positions, for world position, remove the interpPos values
        float posX = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - this.interpPosX);
        float posY = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - this.interpPosY);
        float posZ = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - this.interpPosZ);
        //Vector3f pos = new Vector3f((float) (entityIn.posX - particle.posX), (float) (entityIn.posY - particle.posY), (float) (entityIn.posZ - particle.posZ));
        Vector3f pos = new Vector3f(posX, posY, posZ);

        Matrix4fe modelMatrix = transformation.buildModelMatrix(this, pos, partialTicks);

        //adjust to perspective and camera
        //Matrix4fe modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
        //upload to buffer
        modelMatrix.get(mesh.INSTANCE_SIZE_FLOATS * (mesh.curBufferPos), mesh.instanceDataBuffer);

        //brightness
        float brightness;
        //brightness = CoroUtilBlockLightCache.getBrightnessCached(world, (float)this.posX, (float)this.posY, (float)this.posZ);
        brightness = brightnessCache;
        //brightness = -1F;
        //brightness = CoroUtilBlockLightCache.brightnessPlayer;
        mesh.instanceDataBuffer.put(mesh.INSTANCE_SIZE_FLOATS * (mesh.curBufferPos) + mesh.MATRIX_SIZE_FLOATS, brightness);

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
        
    }*/

    /*public void renderParticleForShaderTest(InstancedMeshParticle mesh, Transformation transformation, Matrix4fe viewMatrix, Entity entityIn,
                                            float partialTicks, float rotationX, float rotationZ,
                                            float rotationYZ, float rotationXY, float rotationXZ) {

        if (mesh.curBufferPos >= mesh.numInstances) return;

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
    }*/

	@Override
	public float getWindWeight() {
		return windWeight;
	}

	@Override
	public int getParticleDecayExtra() {
		return particleDecayExtra;
	}

	//TODO: 1.14 now sets depth buffer use in IParticleRenderType types
    /*@Override
    public boolean shouldDisableDepth() {
    	return isTransparent;
    }*/
    
    public void setKillOnCollide(boolean val) {
    	this.killOnCollide = val;
    }
    
    //override for extra isCollided types
    @Override
    public void move(double x, double y, double z) {
        double xx = x;
        double yy = y;
        double zz = z;
        if (this.canCollide && (x != 0.0D || y != 0.0D || z != 0.0D)) {
            Vec3d vec3d = Entity.func_223307_a((Entity)null, new Vec3d(x, y, z), this.getBoundingBox(), this.world, ISelectionContext.dummy(), new ReuseableStream<>(Stream.empty()));
            x = vec3d.x;
            y = vec3d.y;
            z = vec3d.z;
        }

        if (x != 0.0D || y != 0.0D || z != 0.0D) {
            this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
            this.resetPositionToBB();
        }

        this.onGround = yy != y && yy < 0.0D;
        this.isCollidedHorizontally = xx != x || zz != z;
        this.isCollidedVerticallyDownwards = yy < y;
        this.isCollidedVerticallyUpwards = yy > y;
        if (xx != x) {
            this.motionX = 0.0D;
        }

        if (zz != z) {
            this.motionZ = 0.0D;
        }

    }
    
    public void setFacePlayer(boolean val) {
    	this.facePlayer = val;
    }
    
    public TextureAtlasSprite getParticleTexture() {
    	return this.sprite;
    }
    
    public boolean isVanillaMotionDampen() {
		return vanillaMotionDampen;
	}

	public void setVanillaMotionDampen(boolean motionDampen) {
		this.vanillaMotionDampen = motionDampen;
	}

    @Override
    public int getBrightnessForRender(float p_189214_1_) {
        return super.getBrightnessForRender(p_189214_1_);//(int)((float)super.getBrightnessForRender(p_189214_1_))/* * this.world.getSunBrightness(1F))*/;
    }

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
        }
    }

    @Override
    public void setColor(float particleRedIn, float particleGreenIn, float particleBlueIn) {
        super.setColor(particleRedIn, particleGreenIn, particleBlueIn);
        //TODO: 1.14 uncomment
        /*RotatingParticleManager.markDirtyVBO2();*/
    }

    @Override
    public void setAlphaF(float alpha) {
        super.setAlphaF(alpha);
        //TODO: 1.14 uncomment
        /*RotatingParticleManager.markDirtyVBO2();*/
    }

    public int getKillWhenUnderTopmostBlock_ScanAheadRange() {
        return killWhenUnderTopmostBlock_ScanAheadRange;
    }

    public void setKillWhenUnderTopmostBlock_ScanAheadRange(int killWhenUnderTopmostBlock_ScanAheadRange) {
        this.killWhenUnderTopmostBlock_ScanAheadRange = killWhenUnderTopmostBlock_ScanAheadRange;
    }

    public boolean isCollidedVertically() {
	    return isCollidedVerticallyDownwards || isCollidedVerticallyUpwards;
    }

    @Override
    public IParticleRenderType getRenderType() {
        //TODO: replaces getFXLayer of 5, possibly reimplement extra layers later for clouds etc
        //actually anything > 2 was custom texture sheet, then it just uses higher numbers for diff render orders, higher = later
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void setSprite(TextureAtlasSprite sprite) {
        super.setSprite(sprite);
    }

    public TextureAtlasSprite getSprite() {
        return sprite;
    }
}
