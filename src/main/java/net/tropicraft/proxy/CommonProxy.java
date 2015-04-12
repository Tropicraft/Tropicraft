package net.tropicraft.proxy;

import net.minecraft.client.model.ModelBiped;

public abstract class CommonProxy implements ISuperProxy {

	public CommonProxy() {

	}
	
	@Override
	public void registerBooks() {
		
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

    @Override
    public void preInit() {
        
    }
}
