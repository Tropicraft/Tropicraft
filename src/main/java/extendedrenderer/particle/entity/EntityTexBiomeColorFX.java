package extendedrenderer.particle.entity;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class EntityTexBiomeColorFX extends EntityRotFX
{
    public int age;
    public float brightness;
    public int textureID;

    //Types, for diff physics rules in wind code
    //Leaves = 0
    //Sand = 1
    public int type = 0;

    public EntityTexBiomeColorFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, int colorIndex, int texID, int id, int meta, int x, int y, int z)
    {
        super(var1, var2, var4, var6, var8, var10, var12);
        textureID = texID;
        this.motionX = var8 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.motionY = var10 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.motionZ = var12 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        //Color IDS
        //0 = black/regular/default
        //1 = dirt
        //2 = sand
        //3 = water
        //4 = snow
        //5 = stone
        Color color = null;

        if (colorIndex == 0)
        {
            this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.3F/* + 0.7F*/;
        }
        else if (colorIndex == 1)
        {
            color = new Color(0x79553a);
        }
        else if (colorIndex == 2)
        {
            color = new Color(0xd6cf98);
        }
        else if (colorIndex == 3)
        {
            color = new Color(0x002aDD);
        }
        else if (colorIndex == 4)
        {
            color = new Color(0xeeffff);
        }
        else if (colorIndex == 5)
        {
            color = new Color(0x79553a);
        }
        
        //int x = 0;
        //int y = 0;
        //int z = 0;
        
        //biome color override
        //int meta = this.worldObj.getBlockMetadata((x, y, z)
        color = new Color(Blocks.leaves.colorMultiplier(worldObj, x, y, z));

        //BRIGHTNESS OVERRIDE! for textures
        this.particleRed = this.particleGreen = this.particleBlue = 0.7F;
        brightness = 0.5F;

        //if (colorIndex != 0)
        //{
            this.particleRed = color.getRed() / 255F;
            this.particleGreen = color.getGreen() / 255F;
            this.particleBlue = color.getBlue() / 255F;
        //}

        this.particleScale = this.rand.nextFloat() * this.rand.nextFloat() * 2.0F + 1.0F;
        this.particleMaxAge = (int)(16.0D/* / ((double)this.rand.nextFloat() * 0.8D + 0.2D)*/) + 2;
        this.particleMaxAge = (int)((float)this.particleMaxAge * var14);
        this.particleGravity = 1.0F;
        //this.particleScale = 5F;
        this.setParticleTextureIndex(textureID);
        renderDistanceWeight = 10.0D;
        setSize(0.2F, 0.2F);
        noClip = false;
    }
    
    public boolean isBurning()
    {
    	return false;
    }
    
    protected boolean getFlag(int par1)
    {
    	return false;
    }

    public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        //this.rotationYaw = ModLoader.getMinecraftInstance().thePlayer.rotationYaw;
        //this.rotationPitch += this.rand.nextInt(4) - 2;//ModLoader.getMinecraftInstance().thePlayer.rotationPitch;
        /*float var8 = (float)(this.getParticleTextureIndex() % 16) / 16.0F;
        float var9 = var8 + 0.0624375F;
        float var10 = (float)(this.getParticleTextureIndex() / 16) / 16.0F;
        float var11 = var10 + 0.0624375F;*/
        
        float framesX = 5;
    	float framesY = 1;
    	
    	float index = this.getParticleTextureIndex();
    	
    	//test
    	index = 0;
    	
    	float var8 = (float)index / framesX;
        float var9 = var8 + (1F / framesX);
        float var10 = (float)index / framesY;
        float var11 = var10 + (1F / framesY);
        
        float var12 = 0.1F * this.particleScale;
        float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)var2 - interpPosX);
        float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)var2 - interpPosY) + 0.0F;
        float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var2 - interpPosZ);
        //var13 += i;//rand.nextInt(6)-3;
        //var14 += j;
        //var15 += k;
        //System.out.println("!!!");
        /*float var16 = this.getEntityBrightness(var2) * brightness;
        
        this.getBrightness(var2) * 
        
        float adjSubtracted = (this.worldObj.calculateSkylightSubtracted(var2) / 15F) * 0.5F;
        
        var16 = (1F + ModLoader.getMinecraftInstance().gameSettings.gammaSetting) - (this.worldObj.calculateSkylightSubtracted(var2) * 0.13F);
        
        var16 = (-0.5F + ModLoader.getMinecraftInstance().gameSettings.gammaSetting) - (this.worldObj.calculateSkylightSubtracted(var2) * 0.17F);
        
        var16 = 0.4F - adjSubtracted + (ModLoader.getMinecraftInstance().gameSettings.gammaSetting * 0.7F);*/
        
        Minecraft mc = Minecraft.getMinecraft();
        float br = ((0.9F + (mc.gameSettings.gammaSetting * 0.1F)) - (mc.theWorld.calculateSkylightSubtracted(var2) * 0.03F)) * mc.theWorld.getSunBrightness(1F);
        br = 0.55F * Math.max(0.3F, br) * (2F);
        
        EntityPlayer pl = Minecraft.getMinecraft().thePlayer;
        //System.out.println("brightness: " + adjSubtracted);
        //System.out.println(this.worldObj.calculateSkylightSubtracted(var2) * 0.12F);
        var1.setColorOpaque_F(this.particleRed * br, this.particleGreen * br, this.particleBlue * br);
        var1.addVertexWithUV((double)(var13 - var3 * var12 - var6 * var12), (double)(var14 - var4 * var12), (double)(var15 - var5 * var12 - var7 * var12), (double)var9, (double)var11);
        var1.addVertexWithUV((double)(var13 - var3 * var12 + var6 * var12), (double)(var14 + var4 * var12), (double)(var15 - var5 * var12 + var7 * var12), (double)var9, (double)var10);
        var1.addVertexWithUV((double)(var13 + var3 * var12 + var6 * var12), (double)(var14 + var4 * var12), (double)(var15 + var5 * var12 + var7 * var12), (double)var8, (double)var10);
        var1.addVertexWithUV((double)(var13 + var3 * var12 - var6 * var12), (double)(var14 - var4 * var12), (double)(var15 + var5 * var12 - var7 * var12), (double)var8, (double)var11);
        /*}
        }
        }*/
    }

    public void onUpdate2()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        //this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
        //this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
        this.setParticleTextureIndex(textureID);//mod_EntMover.effWindID;
        //this.motionY += 0.0040D;
        this.motionY -= 0.04D * (double)this.particleGravity;
        //this.motionY -= 0.05000000074505806D;
        float var20 = 0.98F;
        this.motionX *= (double)var20;
        this.motionY *= (double)var20;
        this.motionZ *= (double)var20;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        /*this.motionX *= 0.8999999761581421D;
        this.motionY *= 0.8999999761581421D;
        this.motionZ *= 0.8999999761581421D;
        if(this.onGround) {
           this.motionX *= 0.699999988079071D;
           this.motionZ *= 0.699999988079071D;
        }*/
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        double speed = Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        
        if (speed > 0.04F)
        {
        	int speed2 = (int)(speed * 100);
            this.rotationPitch += this.rand.nextInt(speed2) - (speed2 / 2);
            this.rotationYaw += this.rand.nextInt(speed2) - (speed2 / 2);
        }

        //this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
        //this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
        setParticleTextureIndex(textureID);//mod_EntMover.effWindID;
        //this.motionY += 0.0040D;
        this.motionY -= (0.04D * this.rand.nextFloat()) * (double)this.particleGravity;
        //this.motionY -= 0.05000000074505806D;
        float var20 = 1F - (0.08F * this.rand.nextFloat());
        this.motionX *= (double)var20;
        this.motionY *= (double)var20;
        this.motionZ *= (double)var20;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.setPosition(posX, posY, posZ);
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        //this.boundingBox. = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
        /*this.motionX *= 0.8999999761581421D;
        this.motionY *= 0.8999999761581421D;
        this.motionZ *= 0.8999999761581421D;
        if(this.onGround) {
           this.motionX *= 0.699999988079071D;
           this.motionZ *= 0.699999988079071D;
        }*/
    }

    public int getFXLayer()
    {
        return 5;
    }
}
