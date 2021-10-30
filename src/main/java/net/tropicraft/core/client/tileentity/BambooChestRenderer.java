package net.tropicraft.core.client.tileentity;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Constants.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class BambooChestRenderer extends ChestTileEntityRenderer<BambooChestTileEntity> {

    public static final RenderMaterial BAMBOO_CHEST_MATERIAL = getChestMaterial("bamboo_chest/normal");
    public static final RenderMaterial BAMBOO_CHEST_LEFT_MATERIAL = getChestMaterial("bamboo_chest/normal_left");
    public static final RenderMaterial BAMBOO_CHEST_RIGHT_MATERIAL = getChestMaterial("bamboo_chest/normal_right");

    @SubscribeEvent
    public static void onTextureStitchPre(TextureStitchEvent.Pre event) {
        if (event.getMap().location().equals(Atlases.CHEST_SHEET)) {
            event.addSprite(BAMBOO_CHEST_MATERIAL.texture());
            event.addSprite(BAMBOO_CHEST_LEFT_MATERIAL.texture());
            event.addSprite(BAMBOO_CHEST_RIGHT_MATERIAL.texture());
        }
    }

    private static RenderMaterial getChestMaterial(ChestType chestType, RenderMaterial normalMaterial, RenderMaterial leftMaterial, RenderMaterial rightMaterial) {
        switch(chestType) {
            case LEFT:
                return leftMaterial;
            case RIGHT:
                return rightMaterial;
            case SINGLE:
            default:
                return normalMaterial;
        }
    }

    private static RenderMaterial getChestMaterial(String chestName) {
        return new RenderMaterial(Atlases.CHEST_SHEET, new ResourceLocation(Constants.MODID, "block/te/" + chestName));
    }

    public BambooChestRenderer(TileEntityRendererDispatcher renderDispatcher) {
        super(renderDispatcher);
    }

    @Override
    protected RenderMaterial getMaterial(BambooChestTileEntity tileEntity, ChestType chestType) {
        return getChestMaterial(chestType, BAMBOO_CHEST_MATERIAL, BAMBOO_CHEST_LEFT_MATERIAL, BAMBOO_CHEST_RIGHT_MATERIAL);
    }
}
