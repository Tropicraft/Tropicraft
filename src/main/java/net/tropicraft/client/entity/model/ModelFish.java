package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFish extends ModelBase {

    public ModelRenderer Body;
    public ModelRenderer Tail;

    public ModelFish() {
        setTextureOffset("Body.Body", 0, 0);
        setTextureOffset("Tail.Tail", 0, 0);
        Body = new ModelRenderer(this, "Body");
        Body.setRotationPoint(0F, 16F, 0F);
        Body.addBox("Body", 0, 0, 0, 0, 1, 1);
        Tail = new ModelRenderer(this, "Tail");
        Tail.setRotationPoint(0, 0, -1);
        Tail.addBox("Tail", 0, 0, 0, 0, 1, 1);
        Body.addChild(Tail);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, null);
        Body.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        Tail.rotateAngleY = (float) (Math.sin(f2 * .25F)) * .25F;
    }
}