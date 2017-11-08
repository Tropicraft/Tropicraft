package net.tropicraft.core.client.block.model;

import net.minecraft.client.model.ModelBase;

public abstract class ModelBlock extends ModelBase {

    public abstract void renderAsBlock();

    public abstract String getTexture();
}
