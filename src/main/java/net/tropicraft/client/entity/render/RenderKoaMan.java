package net.tropicraft.client.entity.render;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.tropicraft.client.entity.model.ModelKoaMan;
import net.tropicraft.entity.koa.EntityKoaHunter;
import net.tropicraft.entity.koa.EntityKoaShaman;
import net.tropicraft.entity.koa.EntityKoaTrader;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

public class RenderKoaMan extends RenderLiving
{

	//lots of code copied from RenderBiped, but maybe we should extend RenderBiped and add to it for future maintainability?
	//same for ModelBiped instead of ModelBase
	
	int counter;

	public RenderKoaMan(ModelKoaMan modelbase, float f)
	{
		super(modelbase, f);
		mainModel = (ModelKoaMan)modelbase;
		shadowSize = f;
		counter = 300;
	}
	
	@Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {		
		if (entity instanceof EntityKoaShaman) {
			return TropicraftUtils.bindTextureEntity("koa/KoaShaman");
		} else if (entity instanceof EntityKoaTrader) {
			return TropicraftUtils.bindTextureEntity("koa/KoaManTrader");
		} else if (entity instanceof EntityKoaHunter) {
			return TropicraftUtils.bindTextureEntity("koa/KoaManHunter");
		} else {
			return TropicraftUtils.bindTextureEntity("koa/KoaMan3");
		}		
	}

	public void setRenderPassModel(ModelKoaMan modelbase)
	{
		renderPassModel = modelbase;
	}

	protected void renderLivingAt(EntityLiving entityliving, double d, double d1, double d2)
	{
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
	}

	protected void rotateCorpse(EntityLiving entityliving, float f, float f1, float f2)
	{
		GL11.glRotatef(180F - f1, 0.0F, 1.0F, 0.0F);
		if(entityliving.deathTime > 0)
		{
			float f3 = ((((float)entityliving.deathTime + f2) - 1.0F) / 20F) * 1.6F;
			f3 = MathHelper.sqrt_float(f3);
			if(f3 > 1.0F)
			{
				f3 = 1.0F;
			}
			GL11.glRotatef(f3 * getDeathMaxRotation(entityliving), 0.0F, 0.0F, 1.0F);
		}
	}

	protected float renderSwingProgress(EntityLiving entityliving, float f)
	{
		return entityliving.getSwingProgress(f);
	}

	protected float handleRotationFloat(EntityLiving entityliving, float f)
	{
		return (float)entityliving.ticksExisted + f;
	}

	protected int inheritRenderPass(EntityLiving entityliving, int i, float f)
	{
		return shouldRenderPass(entityliving, i, f);
	}

	protected int shouldRenderPass(EntityLiving entityliving, int i, float f)
	{
		return -1;
	}

	protected float getDeathMaxRotation(EntityLiving entityliving)
	{
		return 90F;
	}

	protected int getColorMultiplier(EntityLiving entityliving, float f, float f1)
	{
		return 0;
	}

	protected void preRenderCallback(EntityLiving entityliving, float f)
	{
	}

	protected void renderLivingLabel(EntityLiving entityliving, String s, double d, double d1, double d2, int i)
	{
		float f = entityliving.getDistanceToEntity(renderManager.livingPlayer);
		if(f > (float)i)
		{
			return;
		}
		FontRenderer fontrenderer = getFontRendererFromRenderManager();
		float f1 = 1.6F;
		float f2 = 0.01666667F * f1;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d + 0.0F, (float)d1 + 2.3F, (float)d2);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-f2, -f2, f2);
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glDepthMask(false);
		//GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(770, 771);
		Tessellator tessellator = Tessellator.instance;
		byte byte0 = 0;
		if(s != null && s.equals("deadmau5"))
		{
			byte0 = -10;
		}
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		tessellator.startDrawingQuads();
		int j = fontrenderer.getStringWidth(s) / 2;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
		tessellator.addVertex(-j - 1, -1 + byte0, 0.0D);
		tessellator.addVertex(-j - 1, 8 + byte0, 0.0D);
		tessellator.addVertex(j + 1, 8 + byte0, 0.0D);
		tessellator.addVertex(j + 1, -1 + byte0, 0.0D);
		tessellator.draw();
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, byte0, 0x20ffffff);
		//GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
		GL11.glDepthMask(true);
		fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, byte0, -1);
		GL11.glEnable(2896 /*GL_LIGHTING*/);
		GL11.glDisable(3042 /*GL_BLEND*/);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	protected void passSpecialRender(EntityLiving entityliving, double d, double d1, double d2)
	{
		if (entityliving instanceof EntityLiving) {
			if (renderManager.livingPlayer.getDistanceToEntity(entityliving) < 12) {
				renderName((EntityLiving)entityliving, d, d1, d2);
			}
		}
		//c_CoroAIUtil.renderJobs((c_EnhAI)entityliving);
	}


	protected void renderName(EntityLiving entitykoa, double d, double d1, double d2)
	{
		//	System.out.println("counter: " + counter);

		if(Minecraft.isGuiEnabled() && entitykoa != renderManager.livingPlayer)
		{
			float f = 1.6F;
			float f1 = 0.01666667F * f;
			float f2 = entitykoa.getDistanceToEntity(renderManager.livingPlayer);
			float f3 = entitykoa.isSneaking() ? 32F : 64F;
			String s = entitykoa.getCustomNameTag();
			//s = entitykoa.debugInfo;
			
			if(f2 < f3)
			{


				if(!entitykoa.isSneaking())
				{
					if(entitykoa.isPlayerSleeping())
					{
						renderLivingLabel(entitykoa, s, d, d1 - 1.5D, d2, 24);
					} else
					{	
						//put all talk logic code in here

					}
					renderLivingLabel(entitykoa, s, d, d1, d2, 24);

				} else
				{
					FontRenderer fontrenderer = getFontRendererFromRenderManager();
					GL11.glPushMatrix();
					GL11.glTranslatef((float)d + 0.0F, (float)d1 + 2.3F, (float)d2);
					GL11.glNormal3f(0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
					GL11.glScalef(-f1, -f1, f1);
					GL11.glDisable(2896 /*GL_LIGHTING*/);
					GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
					GL11.glDepthMask(false);
					GL11.glEnable(3042 /*GL_BLEND*/);
					GL11.glBlendFunc(770, 771);
					Tessellator tessellator = Tessellator.instance;
					GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
					tessellator.startDrawingQuads();
					int i = fontrenderer.getStringWidth(s) / 2;
					tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
					tessellator.addVertex(-i - 1, -1D, 0.0D);
					tessellator.addVertex(-i - 1, 8D, 0.0D);
					tessellator.addVertex(i + 1, 8D, 0.0D);
					tessellator.addVertex(i + 1, -1D, 0.0D);
					tessellator.draw();
					GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
					GL11.glDepthMask(true);
					fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 0x20ffffff);
					GL11.glEnable(2896 /*GL_LIGHTING*/);
					GL11.glDisable(3042 /*GL_BLEND*/);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glPopMatrix();
				}
			}
		}
	}/*

	public void doRender(Entity entity, double d, double d1, double d2, 
			float f, float f1)
	{
		doRenderLiving((EntityLiving)entity, d, d1, d2, f, f1);
	}*/

	private World getWorldFromRenderManager() {
		return this.renderManager.worldObj;
	}

	private void renderShadowOnBlock(Block block, double d, double d1, double d2, 
			int i, int j, int k, float f, float f1, double d3, 
			double d4, double d5)
	{
		Tessellator tessellator = Tessellator.instance;
		if(!block.renderAsNormalBlock())
		{
			return;
		}
		double d6 = ((double)f - (d1 - ((double)j + d4)) / 2D) * 0.5D * (double)getWorldFromRenderManager().getLightBrightness(i, j, k);
		if(d6 < 0.0D)
		{
			return;
		}
		if(d6 > 1.0D)
		{
			d6 = 1.0D;
		}
		tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)d6);
		double d7 = (double)i + block.getBlockBoundsMinX() + d3;
		double d8 = (double)i + block.getBlockBoundsMaxX() + d3;
		double d9 = (double)j + block.getBlockBoundsMinY() + d4 + 0.015625D;
		double d10 = (double)k + block.getBlockBoundsMinZ() + d5;
		double d11 = (double)k + block.getBlockBoundsMaxZ() + d5;
		float f2 = (float)((d - d7) / 2D / (double)f1 + 0.5D);
		float f3 = (float)((d - d8) / 2D / (double)f1 + 0.5D);
		float f4 = (float)((d2 - d10) / 2D / (double)f1 + 0.5D);
		float f5 = (float)((d2 - d11) / 2D / (double)f1 + 0.5D);
		tessellator.addVertexWithUV(d7, d9, d10, f2, f4);
		tessellator.addVertexWithUV(d7, d9, d11, f2, f5);
		tessellator.addVertexWithUV(d8, d9, d11, f3, f5);
		tessellator.addVertexWithUV(d8, d9, d10, f3, f4);
	}
	
	@Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_,
			float p_77029_2_) {
		super.renderEquippedItems(p_77029_1_, p_77029_2_);
		
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
        super.renderEquippedItems(p_77029_1_, p_77029_2_);
        ItemStack itemstack = p_77029_1_.getHeldItem();
        ItemStack itemstack1 = ((EntityLiving)p_77029_1_).func_130225_q(3);
        Item item;
        float f1;

        if (itemstack1 != null)
        {
            GL11.glPushMatrix();
            this.mainModel.bipedHead.postRender(0.0625F);
            item = itemstack1.getItem();

            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack1, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack1, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

            if (item instanceof ItemBlock)
            {
                if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType()))
                {
                    f1 = 0.625F;
                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f1, -f1, -f1);
                }

                this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack1, 0);
            }
            else if (item == Items.skull)
            {
                f1 = 1.0625F;
                GL11.glScalef(f1, -f1, -f1);
                GameProfile gameprofile = null;

                if (itemstack1.hasTagCompound())
                {
                    NBTTagCompound nbttagcompound = itemstack1.getTagCompound();

                    if (nbttagcompound.hasKey("SkullOwner", 10))
                    {
                        gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                    }
                    else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner")))
                    {
                        gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    }
                }

                TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack1.getItemDamage(), gameprofile);
            }

            GL11.glPopMatrix();
        }

        if (itemstack != null && itemstack.getItem() != null)
        {
            item = itemstack.getItem();
            GL11.glPushMatrix();

            if (this.mainModel.isChild)
            {
                f1 = 0.5F;
                GL11.glTranslatef(0.0F, 0.625F, 0.0F);
                GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
                GL11.glScalef(f1, f1, f1);
            }

            this.mainModel.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

            if (item instanceof ItemBlock && (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType())))
            {
                f1 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f1 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-f1, -f1, f1);
            }
            else if (item == Items.bow)
            {
                f1 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (item.isFull3D())
            {
                f1 = 0.625F;

                if (item.shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                this.func_82422_c();
                GL11.glScalef(f1, -f1, f1);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                f1 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f1, f1, f1);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            float f2;
            int i;
            float f5;

            if (itemstack.getItem().requiresMultipleRenderPasses())
            {
                for (i = 0; i < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++i)
                {
                    int j = itemstack.getItem().getColorFromItemStack(itemstack, i);
                    f5 = (float)(j >> 16 & 255) / 255.0F;
                    f2 = (float)(j >> 8 & 255) / 255.0F;
                    float f3 = (float)(j & 255) / 255.0F;
                    GL11.glColor4f(f5, f2, f3, 1.0F);
                    this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, i);
                }
            }
            else
            {
                i = itemstack.getItem().getColorFromItemStack(itemstack, 0);
                float f4 = (float)(i >> 16 & 255) / 255.0F;
                f5 = (float)(i >> 8 & 255) / 255.0F;
                f2 = (float)(i & 255) / 255.0F;
                GL11.glColor4f(f4, f5, f2, 1.0F);
                this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, 0);
            }

            GL11.glPopMatrix();
        }
	}
	
	protected void func_82422_c()
    {
        GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
    }

	protected ModelKoaMan mainModel;
	protected ModelKoaMan renderPassModel;
}
