package net.tropicraft.proxy;

import net.minecraft.client.model.ModelBiped;

public class CommonProxy implements ISuperProxy {

	public CommonProxy() {

	}

	@Override
	public void initRenderHandlersAndIDs() {

	}

	@Override
	public void initRenderRegistry() {

	}

    @Override
    public ModelBiped getArmorModel(int id) {
        return null;
    }

}
