package net.tropicraft.core.client.tileentity;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.tileentity.BambooChestBlockEntity;

@OnlyIn(Dist.CLIENT)
public class BambooChestRenderer extends ChestRenderer<BambooChestBlockEntity> {

    public static final Material BAMBOO_CHEST_MATERIAL = getChestMaterial("bamboo");
    public static final Material BAMBOO_CHEST_LEFT_MATERIAL = getChestMaterial("bamboo_left");
    public static final Material BAMBOO_CHEST_RIGHT_MATERIAL = getChestMaterial("bamboo_right");

    private static Material getChestMaterial(ChestType chestType, Material normalMaterial, Material leftMaterial, Material rightMaterial) {
        return switch (chestType) {
            case LEFT -> leftMaterial;
            case RIGHT -> rightMaterial;
            default -> normalMaterial;
        };
    }

    private static Material getChestMaterial(String chestName) {
        return new Material(Sheets.CHEST_SHEET, new ResourceLocation(Constants.MODID, "entity/chest/" + chestName));
    }

    public BambooChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Material getMaterial(BambooChestBlockEntity tileEntity, ChestType chestType) {
        return getChestMaterial(chestType, BAMBOO_CHEST_MATERIAL, BAMBOO_CHEST_LEFT_MATERIAL, BAMBOO_CHEST_RIGHT_MATERIAL);
    }
}
