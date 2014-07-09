package extendedrenderer.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.ExtendedRenderer;
import extendedrenderer.particle.entity.EntityRotFX;
@SideOnly(Side.CLIENT)
public class RotatingEffectRenderer
{
	//Layers 2 and 3 use same icon/item based texture sheet, they are used to help fix transparency render ordering in scene
	//layer 2: clouds
	//layer 3: tornado funnel
	//layer 3+?: particle rain?
	
    public int layers = 6;
    public World worldObj;
    public List[] fxLayers = new List[layers];
    //public RenderEngine renderer;
    public TextureManager renderer;
    public Random rand = new Random();
    
    public static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
    public static final ResourceLocation resLayer4 = new ResourceLocation(ExtendedRenderer.modid + ":textures/particles/particles_64.png");
    public static final ResourceLocation resLayer5 = new ResourceLocation(ExtendedRenderer.modid + ":textures/particles/particles_16.png");;

    public RotatingEffectRenderer(World var1, TextureManager par2TextureManager)
    {
        if (var1 != null)
        {
            this.worldObj = var1;
        }

        this.renderer = par2TextureManager;

        for (int var3 = 0; var3 < layers; ++var3)
        {
            this.fxLayers[var3] = new ArrayList();
        }
    }

    public void addEffect(EntityFX var1)
    {
        int var2 = var1.getFXLayer();

        if (this.fxLayers[var2].size() >= 4000)
        {
            this.fxLayers[var2].remove(0);
        }

        this.fxLayers[var2].add(var1);
    }

    public void updateEffects()
    {
        for (int var1 = 0; var1 < layers; ++var1)
        {
            for (int var2 = 0; var2 < this.fxLayers[var1].size(); ++var2)
            {
                EntityFX var3 = (EntityFX)this.fxLayers[var1].get(var2);

                if (var3 == null)
                {
                    //System.out.println("Null particle!");
                    continue;
                }

                //if (var3 instanceof EntityRotFX) {
                //Prevents double updates if you are adding particles to the mc weatherEffects list
                if (!((EntityRotFX)var3).weatherEffect)
                {
                    var3.onUpdate();
                }

                //} else {
                //var3.onUpdate();
                //}

                if (var3.getDistanceToEntity(FMLClientHandler.instance().getClient().thePlayer) > 64F)
                {
                    //var3.setDead();
                }

                if (var3.isDead)
                {
                    this.fxLayers[var1].remove(var2--);
                }
            }
        }
    }

    public void renderParticles(Entity var1, float var2)
    {
        EntityFX.interpPosX = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var2;
        EntityFX.interpPosY = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var2;
        EntityFX.interpPosZ = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var2;

        try
        {
            for (int var8 = 0; var8 < layers; ++var8)
            {

                if (this.fxLayers[var8].size() != 0)
                {
                    if (var8 == 0)
                    {
                        this.renderer.bindTexture(particleTextures);
                    }

                    if (var8 == 1)
                    {
                        this.renderer.bindTexture(TextureMap.locationBlocksTexture);
                    }

                    if (var8 == 2 || var8 == 3)
                    {
                        this.renderer.bindTexture(TextureMap.locationItemsTexture);
                    }

                    if (var8 == 4)
                    {
                        this.renderer.bindTexture(resLayer4);
                    }
                    
                    if (var8 == 5)
                    {
                        this.renderer.bindTexture(resLayer5);
                    }

                    Tessellator var10 = Tessellator.instance;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    
                    GL11.glDepthMask(false);
                    
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
                    
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    
                    var10.startDrawingQuads();

                    for (int var11 = this.fxLayers[var8].size()-1; var11 >= 0; --var11)
                    {
                        EntityFX var12 = (EntityFX)this.fxLayers[var8].get(var11);

                        if (var12 == null)
                        {
                            //System.out.println("Null particle2!");
                            continue;
                        }
                        
                        boolean render = true;
                        
                        if (var12 instanceof EntityRotFX) {
                        	if (var1.getDistanceToEntity(var12) > ((EntityRotFX) var12).maxRenderRange()) render = false;
                        }
                        
                        if (render) {
	                        float var3 = MathHelper.cos(var12.rotationYaw * (float)Math.PI / 180.0F);
	                        float var4 = MathHelper.sin(var12.rotationYaw * (float)Math.PI / 180.0F);
	                        float var5 = -var4 * MathHelper.sin(var12.rotationPitch * (float)Math.PI / 180.0F);
	                        float var6 = var3 * MathHelper.sin(var12.rotationPitch * (float)Math.PI / 180.0F);
	                        float var7 = MathHelper.cos(var12.rotationPitch * (float)Math.PI / 180.0F);
	                        var12.renderParticle(var10, var2, var3, var7, var4, var5, var6);
                        }
                    }

                    var10.draw();
                    
                    GL11.glEnable(GL11.GL_CULL_FACE);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glDepthMask(true);
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                }
            }
        }
        catch (IndexOutOfBoundsException ex)
        {
            //super small thread desync, caught and ignored
        }
    }

    public void clearEffects(World var1)
    {
        this.worldObj = var1;

        for (int var2 = 0; var2 < layers; ++var2)
        {
            this.fxLayers[var2].clear();
        }
    }

    public String getStatistics()
    {
    	int count = 0;
    	for (int i = 0; i < layers; i++) {
    		count += fxLayers[i].size();
    	}
    	/*return "Particle Sheet Main: " + this.fxLayers[0].size() + 
    			" - Block Sheet: " + this.fxLayers[1].size() + 
    			" - Item Sheet: " + (this.fxLayers[2].size() + this.fxLayers[3].size()) + 
    			" - Particle Sheet 64x: " + this.fxLayers[1].size() + 
    			" - Particle Sheet 16x: " + this.fxLayers[1].size();*/
    	//item sheet seems only one used now
        return "" + count;
    }
}
