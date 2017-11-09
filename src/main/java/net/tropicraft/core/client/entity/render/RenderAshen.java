package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelAshen;
import net.tropicraft.core.client.entity.render.layers.LayerHeldItemAshen;
import net.tropicraft.core.client.entity.render.layers.LayerMaskAshen;
import net.tropicraft.core.common.entity.hostile.EntityAshen;

public class RenderAshen extends RenderLiving<EntityAshen> {

	private ModelAshen modelAshen;

	public RenderAshen(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
		modelAshen = (ModelAshen) modelbase;
		this.addLayer(new LayerMaskAshen(modelAshen));
		this.addLayer(new LayerHeldItemAshen(this, modelAshen));
		this.shadowOpaque = 0.5f;
		this.shadowSize = 0.3f;
	}

	public void renderAshen(EntityAshen entityAshen, double d, double d1, double d2, float f, float f1) {
		modelAshen.actionState = entityAshen.getActionState();
		if (entityAshen.getAttackTarget() != null && entityAshen.getDistanceToEntity(entityAshen.getAttackTarget()) < 5.0F && !entityAshen.isSwingInProgress) {
			modelAshen.swinging = true;
		} else {
			if (entityAshen.isSwingInProgress && entityAshen.swingProgressInt > 6) {
				modelAshen.swinging = false;
			}
		}
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
