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
    @SuppressWarnings("unchecked")
	public List<EntityFX>[] fxLayers = new List[layers];
    //public RenderEngine renderer;
    public TextureManager renderer;
    public Random rand = new Random();
    public float hmm = 0F;
    
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
            this.fxLayers[var3] = new ArrayList<EntityFX>();
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
        //if (true) return;
        //float var3 = MathHelper.cos(hmm * (float)Math.PI / 180.0F);
        //float var4 = MathHelper.sin(hmm * (float)Math.PI / 180.0F);
        EntityFX.interpPosX = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var2;
        EntityFX.interpPosY = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var2;
        EntityFX.interpPosZ = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var2;

        try
        {
            for (int var8 = 0; var8 < layers; ++var8)
            {
                /*if (var8 == 3)
                {
                    continue;
                }*/

                if (this.fxLayers[var8].size() != 0)
                {
                    //int var9 = 0;

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

                    //GL11.glBindTexture(GL11.GL_TEXTURE_2D, var9);
                    Tessellator var10 = Tessellator.instance;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    
                    //tutorials say should be false for best transparency without ordering rule
                    //also, transparency layering issue exists for tornadoes with this as false because:
                    //FALSE: different layers are used for tornado animated funnel particles vs cloud texture -
                    //whats real issue with old system?
                    //same clouds showing strongly through tornado issue happens with icon/hd ones!!!!!!!!!!!!
                    //GL11.glDisable(GL11.GL_DEPTH_TEST); != GL11.glDepthMask(false); 
                    
                    //there really is no easy solution, but theres a possible shortcut to full sort rendering
                    //- render all cloud based particles first (they are likely to be furthest away)
                    //- render the rest... (tornado funnel)
                    
                    //maybe add a system to add an order index of render for each particle
                    
                    //also note, non hd funnel particles worked better because they are opaque, no partial transparency, just 0 or 100
                    //this made the depth buffer function properly
                    //https://stackoverflow.com/questions/3388294/opengl-question-about-the-usage-of-gldepthmask
                    
                    GL11.glDepthMask(false);
                    //GL11.glDisable(GL11.GL_DEPTH_TEST);						// Disables Depth Testing
                    //GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                    
                    //old way
                    //GL11.glClearDepth(1.0f);							// Depth Buffer Setup
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);					// Type Of Blending To Perform
                    //GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);			// Really Nice Perspective Calculations
                    //GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_NICEST);
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
                    
                    //nehe way for particles
                  /* TODO commented out so there are no warnings
                   *  if (false) {
	                    GL11.glShadeModel(GL11.GL_SMOOTH);						// Enables Smooth Shading
	                    GL11.glClearColor(0.0f,0.0f,0.0f,0.0f);					// Black Background
	                    GL11.glClearDepth(1.0f);							// Depth Buffer Setup
	                    GL11.glDisable(GL11.GL_DEPTH_TEST);						// Disables Depth Testing
	                    GL11.glEnable(GL11.GL_BLEND);							// Enable Blending
	                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);					// Type Of Blending To Perform
	                    GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);			// Really Nice Perspective Calculations
	                    GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_NICEST);
                    }*/
                    
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    //GL11.glRotatef(180.0F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                    //GL11.glRotatef(-RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
                    //GL11.glPushMatrix();
                    
                    //RenderHelper.enableStandardItemLighting();
                    
                    var10.startDrawingQuads();
                    //var10.setBrightness(15728880);
                    //GL11.glDisable(GL11.GL_ALPHA_TEST);
                    
                    //GL11.glDisable(GL11.GL_BLEND);

                    //GL11.glEnable(GL11.GL_BLEND);
                    //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    //GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
                    //GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                    //GL11.glRotatef(hmm, 0.0F, 1.0F, 0.0F);

                    //int brightness = 0;
                    if (FMLClientHandler.instance().getClient().thePlayer != null)
                    {
                        //brightness = ModLoader.getMinecraftInstance().thePlayer.getBrightnessForRender(var2);
                        //ExtendedRenderer.plBrightness = ModLoader.getMinecraftInstance().thePlayer.getBrightnessForRender(var2);
                    }

                    //for (int var11 = 0; var11 < this.fxLayers[var8].size(); ++var11)
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

                        //System.out.println("bn " + ModLoader.getMinecraftInstance().thePlayer.getBrightness(var2));
                        //caaaaache
                        //var10.setBrightness(mod_ExtendedRenderer.plBrightness);
                        //no cache
                        //var10.setBrightness(var12.getBrightnessForRender(var2));
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
                    
                    //RenderHelper.disableStandardItemLighting();
                    
                    //GL11.glPopMatrix();
                    //GL11.glRotatef(-hmm, 0.0F, 1.0F, 0.0F);
                    //GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                    //GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_CULL_FACE);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glDepthMask(true);
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    //GL11.glEnable(GL11.GL_ALPHA_TEST);
                    hmm += 1F;

                    if (hmm < -180)
                    {
                        hmm = 180;
                    }

                    if (hmm > 180)
                    {
                        hmm = -180;
                    }
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

    /*public void addBlockDestroyEffects(int var1, int var2, int var3, int var4, int var5)
    {
        if (var4 != 0)
        {
            Block var6 = Block.blocksList[var4];
            byte var7 = 4;

            for (int var8 = 0; var8 < var7; ++var8)
            {
                for (int var9 = 0; var9 < var7; ++var9)
                {
                    for (int var10 = 0; var10 < var7; ++var10)
                    {
                        double var11 = (double)var1 + ((double)var8 + 0.5D) / (double)var7;
                        double var13 = (double)var2 + ((double)var9 + 0.5D) / (double)var7;
                        double var15 = (double)var3 + ((double)var10 + 0.5D) / (double)var7;
                        int var17 = this.rand.nextInt(6);
                        //this.addEffect((new EntityDiggingFX(this.worldObj, var11, var13, var15, var11 - (double)var1 - 0.5D, var13 - (double)var2 - 0.5D, var15 - (double)var3 - 0.5D, var6, var17, var5)).applyColourMultiplier(var1, var2, var3));
                    }
                }
            }
        }
    }*/

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
