package net.tropicraft.proxy;

import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.client.MinecraftForgeClient;
import net.tropicraft.client.entity.model.ModelScubaGear;
import net.tropicraft.client.renderer.block.AirCompressorRenderHandler;
import net.tropicraft.client.renderer.block.BambooChestRenderHandler;
import net.tropicraft.client.renderer.block.CoffeePlantRenderHandler;
import net.tropicraft.client.renderer.block.FlowerPotRenderHandler;
import net.tropicraft.client.renderer.block.TikiTorchRenderHandler;
import net.tropicraft.client.renderer.item.ItemDiveComputerRenderer;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.registry.TCRenderRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy implements ISuperProxy {

    public ClientProxy() {

    }

    @Override
    public void initRenderHandlersAndIDs() {
        TCRenderIDs.coffeePlant = RenderingRegistry.getNextAvailableRenderId();
        TCRenderIDs.tikiTorch = RenderingRegistry.getNextAvailableRenderId();
        TCRenderIDs.flowerPot = RenderingRegistry.getNextAvailableRenderId();
        TCRenderIDs.bambooChest = RenderingRegistry.getNextAvailableRenderId();
        TCRenderIDs.airCompressor = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(new CoffeePlantRenderHandler());
        RenderingRegistry.registerBlockHandler(new TikiTorchRenderHandler());
        RenderingRegistry.registerBlockHandler(new FlowerPotRenderHandler());
        RenderingRegistry.registerBlockHandler(new BambooChestRenderHandler());
        RenderingRegistry.registerBlockHandler(new AirCompressorRenderHandler());

        MinecraftForgeClient.registerItemRenderer(TCItemRegistry.diveComputer, new ItemDiveComputerRenderer());
    }

    @Override
    public void initRenderRegistry() {
        TCRenderRegistry.initEntityRenderers();
        TCRenderRegistry.initTileEntityRenderers();
    }

    @Override
    public ModelBiped getArmorModel(int id) {
        if (id == 0) {
            return new ModelScubaGear();
        }

        return null;
    }

}
