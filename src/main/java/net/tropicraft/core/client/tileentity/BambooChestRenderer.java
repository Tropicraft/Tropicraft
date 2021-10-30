package net.tropicraft.core.client.tileentity;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.resources.ResourceLocation;
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
public class BambooChestRenderer extends ChestRenderer<BambooChestTileEntity> {

    public static final Material BAMBOO_CHEST_MATERIAL = getChestMaterial("bamboo_chest/normal");
    public static final Material BAMBOO_CHEST_LEFT_MATERIAL = getChestMaterial("bamboo_chest/normal_left");
    public static final Material BAMBOO_CHEST_RIGHT_MATERIAL = getChestMaterial("bamboo_chest/normal_right");

    @SubscribeEvent
    public static void onTextureStitchPre(TextureStitchEvent.Pre event) {
        if (event.getMap().location().equals(Sheets.CHEST_SHEET)) {
            event.addSprite(BAMBOO_CHEST_MATERIAL.texture());
            event.addSprite(BAMBOO_CHEST_LEFT_MATERIAL.texture());
            event.addSprite(BAMBOO_CHEST_RIGHT_MATERIAL.texture());
        }
    }

    private static Material getChestMaterial(ChestType chestType, Material normalMaterial, Material leftMaterial, Material rightMaterial) {
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

    private static Material getChestMaterial(String chestName) {
        return new Material(Sheets.CHEST_SHEET, new ResourceLocation(Constants.MODID, "block/te/" + chestName));
    }

    public BambooChestRenderer(BlockEntityRenderDispatcher renderDispatcher) {
        super(renderDispatcher);
    }

    @Override
    protected Material getMaterial(BambooChestTileEntity tileEntity, ChestType chestType) {
        return getChestMaterial(chestType, BAMBOO_CHEST_MATERIAL, BAMBOO_CHEST_LEFT_MATERIAL, BAMBOO_CHEST_RIGHT_MATERIAL);
    }
}
