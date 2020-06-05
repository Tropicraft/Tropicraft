package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.BeeModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.tropicraft.core.common.entity.TropiBeeEntity;

public class TropiBeeModel extends BeeModel<TropiBeeEntity> {
    private final ModelRenderer hat1;
    private final ModelRenderer hat2;
    private final ModelRenderer hat3;

    public TropiBeeModel() {
        super();
        hat1 = new ModelRenderer(this, 0, 32);
        hat1.addBox(-5F, -6F, -5F, 12, 1, 6);
        hat1.setRotationPoint(-1F, 1F, -1F);
        hat1.mirror = true;
        ModelRenderer body = getBody();
        body.addChild(hat1);
        hat2 = new ModelRenderer(this, 0, 48);
        hat2.addBox(0F, -6F, 0F, 6, 2, 6);
        hat2.setRotationPoint(-3F, -1F, -3F);
        hat2.mirror = false;
        body.addChild(hat2);
        hat3 = new ModelRenderer(this, 0, 32);
        hat3.addBox(-5F, -6F, 0F, 12, 1, 6);
        hat3.setRotationPoint(-1F, 1F, 0F);
        hat3.mirror = false;
        body.addChild(hat3);
    }

    public ModelRenderer getBody() {
        ModelRenderer body = null;
        for (ModelRenderer b : getBodyParts()) {
            body = b;
            break;
        }
        return body;
    }
}
