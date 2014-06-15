package net.tropicraft.proxy;

import net.minecraft.client.model.ModelBiped;

/**
 * Interface to make it a little bit easier for ClientProxy and CommonProxy to implement
 * the necessary methods.
 */
public interface ISuperProxy {
	/**
	 * Register all block and item render handlers here and ids associated with them
	 */
	public void initRenderHandlersAndIDs();
	
	/**
	 * Initializes TCRenderRegistry (for entities)
	 */
	public void initRenderRegistry();
	
	/**
	 * Returns a ModelBiped for a given custom armor id
	 */
	public ModelBiped getArmorModel(int id);
}
