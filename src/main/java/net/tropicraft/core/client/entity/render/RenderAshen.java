package net.tropicraft.core.client.entity.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelAshen;
import net.tropicraft.core.common.entity.hostile.EntityAshen;
import net.tropicraft.core.common.entity.hostile.EntityAshenHunter;
import net.tropicraft.core.registry.ItemRegistry;

public class RenderAshen extends RenderLiving<EntityAshen> {

    private ModelAshen modelAshen;

    public RenderAshen(ModelBase modelbase, float f) {
        super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
        modelAshen = (ModelAshen) modelbase;
        this.addLayer(new LayerMaskAshen(modelAshen));
        this.addLayer(new LayerHeldItemAshen(this, modelAshen));
    }

    public void renderAshen(EntityAshen entityAshen, double d, double d1, double d2,
            float f, float f1) {

        /*if (entityAshen.getEntityToAttack() != null && entityAshen.getDistanceToEntity(entityAshen.getEntityToAttack()) < 2.0F) {
            modelAshen.swinging = true;
        } else {*/
            modelAshen.swinging = false;
        //}
        modelAshen.actionState = entityAshen.getActionState();
        super.doRender(entityAshen, d, d1, d2, f, f1);

    }

    @Override
    public void doRender(EntityAshen entityliving, double d, double d1, double d2,
            float f, float f1) {
        renderAshen(entityliving, d, d1, d2, f, f1);
    }

//    @Override
//    protected void renderEquippedItems(EntityAshen entityliving, float f) {
//        if (entityliving instanceof EntityAshenHunter) {
//            if (((EntityAshen) entityliving).getActionState() == 2) {
//                GL11.glPushMatrix();
//                modelAshen.leftArm.postRender(0.0625F);
//                GL11.glTranslatef(-0.35F, -0.45F, -0.225F);
//                //GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
//                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
//                float scale = 0.3F;
//                GL11.glScalef(scale, scale, scale);
//         //TODO       RenderManager.instance.itemRenderer.renderItem(entityliving, new ItemStack(TCItemRegistry.spearBamboo), 0);
//                
//                GL11.glPopMatrix();
//                GL11.glPushMatrix();
//                modelAshen.rightArm.postRender(0.0625F);
//                
//                /*GL11.glTranslatef(0.4F, -0.20F, -0.245F);
//                GL11.glRotatef(05F, 1.0F, 0.0F, 0.0F);
//                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
//                GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);*/
//                
//                //GL11.glTranslatef(0.4F, -0.50F, -0.245F);
//                //GL11.glRotatef(12F, 0.0F, 0.0F, 1.0F);
//                
//                GL11.glTranslatef(0.4F, -0.30F, -0.245F);
//                GL11.glRotatef(130F, 1.0F, 0.0F, 0.0F);
//                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
//                GL11.glRotatef(5F, 0.0F, 0.0F, 1.0F);
//                
//                /*GL11.glRotatef(15F, 1.0F, 0.0F, 0.0F);
//                GL11.glRotatef(130F, 0.0F, 1.0F, 0.0F);
//                GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
//                
//                GL11.glRotatef(160F, 0.0F, 1.0F, 0.0F);*/
//                
//                GL11.glScalef(scale, scale, scale);
//                Minecraft.getMinecraft().getItemRenderer().renderItem(entityliving, new ItemStack(ItemRegistry.blowGun), 0);
//                GL11.glPopMatrix();
//            }
//        }
//        if (((EntityAshen) entityliving).hasMask()) {	
//            renderMask(entityliving);
//        }
//    }

	@Override
	protected ResourceLocation getEntityTexture(EntityAshen entity) {
		return TropicraftRenderUtils.bindTextureEntity("ashen/nativetext");
	}
}
