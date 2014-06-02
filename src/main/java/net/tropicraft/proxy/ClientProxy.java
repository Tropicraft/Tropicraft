package net.tropicraft.proxy;

import net.tropicraft.client.renderer.block.CoffeePlantRenderHandler;
import net.tropicraft.client.renderer.block.TikiTorchRenderHandler;
import net.tropicraft.info.TCRenderIDs;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy implements ISuperProxy {

	public ClientProxy() {

	}

	@Override
	public void initRenderHandlersAndIDs() {
		TCRenderIDs.coffeePlant = RenderingRegistry.getNextAvailableRenderId();
		TCRenderIDs.tikiTorch = RenderingRegistry.getNextAvailableRenderId();
		
		RenderingRegistry.registerBlockHandler(new CoffeePlantRenderHandler());
		RenderingRegistry.registerBlockHandler(new TikiTorchRenderHandler());
	}

}
