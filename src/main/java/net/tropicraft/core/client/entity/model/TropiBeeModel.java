package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.tropicraft.core.common.entity.TropiBeeEntity;

public class TropiBeeModel extends BeeModel<TropiBeeEntity> {
    private final ModelPart hat1;
    private final ModelPart hat2;
    private final ModelPart hat3;

    public TropiBeeModel() {
        super();
        hat1 = new ModelPart(this, 0, 32);
        hat1.addBox(-5F, -6F, -5F, 12, 1, 6);
        hat1.setPos(-1F, 1F, -1F);
        hat1.mirror = true;
        ModelPart body = getBody();
        body.addChild(hat1);
        hat2 = new ModelPart(this, 0, 48);
        hat2.addBox(0F, -6F, 0F, 6, 2, 6);
        hat2.setPos(-3F, -1F, -3F);
        hat2.mirror = false;
        body.addChild(hat2);
        hat3 = new ModelPart(this, 0, 32);
        hat3.addBox(-5F, -6F, 0F, 12, 1, 6);
        hat3.setPos(-1F, 1F, 0F);
        hat3.mirror = false;
        body.addChild(hat3);
    }

    public ModelPart getBody() {
        ModelPart body = null;
        for (ModelPart b : bodyParts()) {
            body = b;
            break;
        }
        return body;
    }
}
