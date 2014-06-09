package extendedrenderer.particle.entity;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.ExtendedRenderer;
import extendedrenderer.particle.behavior.ParticleBehaviors;

@SideOnly(Side.CLIENT)
public class EntityRotFX extends EntityFX
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
    public int renderOrder = -1;
    
    public int getParticleTextureIndex()
    {
        return this.particleTextureIndexInt;
    }
    
    public void setMaxAge(int par) {
    	particleMaxAge = par;
    }
    
    public float getAlphaF()
    {
        return this.particleAlpha;
    }
    
    @Override
    public void setDead() {
    	if (pb != null) pb.particles.remove(this);
    	super.setDead();
    }
    
    @Override
    public void onUpdate() {
    	if (callUpdateSuper) super.onUpdate();
    	this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        
        if (callUpdatePB && pb != null) pb.tickUpdate(this);
        
        //calling required stuff the super did
        if (!callUpdateSuper) {
        	this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (this.particleAge++ >= this.particleMaxAge)
            {
                this.setDead();
            }
            
            if (spawnY != -1) {
            	setPosition(posX, spawnY, posZ);
            }
            
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        }
    }
    
    public void setParticleTextureIndex(int par1)
    {
        this.particleTextureIndexInt = par1;
        if (this.getFXLayer() == 0) super.setParticleTextureIndex(par1);
    }
    
    public int getFXLayer()
    {
        return 5;
    }

    public EntityRotFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        super(par1World, par2, par4, par6, par8, par10, par12);
        setSize(0.3F, 0.3F);
        this.isImmuneToFire = true;
    }

    public EntityRotFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, int colorIndex)
    {
        super(var1, var2, var4, var6, var8, var10, var12);
        setSize(0.3F, 0.3F);
        this.isImmuneToFire = true;
    }

    public EntityRotFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, int texIDs[])
    {
        super(var1, var2, var4, var6, var8, var10, var12);
        setSize(0.3F, 0.3F);
        this.isImmuneToFire = true;
    }

    public void spawnAsWeatherEffect()
    {
        weatherEffect = true;
        ExtendedRenderer.rotEffRenderer.addEffect(this);
        this.worldObj.addWeatherEffect(this);
    }

    public int getAge()
    {
        return particleAge;
    }

    public void setAge(int age)
    {
        particleAge = age;
    }

    public int getMaxAge()
    {
        return particleMaxAge;
    }

    public void setSize(float par1, float par2)
    {
        super.setSize(par1, par2);
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
    
    public float getScale() {
    	return particleScale;
    }
}
