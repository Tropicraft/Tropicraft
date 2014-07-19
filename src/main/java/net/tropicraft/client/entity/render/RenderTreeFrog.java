package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.hostile.EntityTreeFrog;
import net.tropicraft.util.TropicraftUtils;

public class RenderTreeFrog extends RenderLiving {

    public RenderTreeFrog(ModelBase modelbase, float f) {
        super(modelbase, f);
    }
    
    @Override
	protected ResourceLocation getEntityTexture(Entity entity) {
    	if (((EntityTreeFrog)entity).type == 0) {
    		return TropicraftUtils.bindTextureEntity("treefrog/treefroggreen");
		} else if (((EntityTreeFrog)entity).type == 1) {
			return TropicraftUtils.bindTextureEntity("treefrog/treefrogred");
		} else if (((EntityTreeFrog)entity).type == 2) {
			return TropicraftUtils.bindTextureEntity("treefrog/treefrogblue");
		} else /*if (((TreeFrog)entity).type == 3)*/ {
			return TropicraftUtils.bindTextureEntity("treefrog/treefrogyellow");
		}
	}

    public void renderTreeFrog(EntityTreeFrog entitytreefrog, double d, double d1, double d2,
            float f, float f1) {
        super.doRender(entitytreefrog, d, d1, d2, f, f1);
    }

    @Override
    public void doRender(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1) {
        renderTreeFrog((EntityTreeFrog) entityliving, d, d1, d2, f, f1);
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1) {
        renderTreeFrog((EntityTreeFrog) entity, d, d1, d2, f, f1);
    }
}
