package net.tropicraft.core.client;

import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.item.scuba.ScubaType;

import java.util.function.BiConsumer;

public class TropicraftEquipmentAssets {
    public static final ResourceKey<EquipmentAsset> NONE = key("none");
    public static final ResourceKey<EquipmentAsset> SCALE = key("scale");
    public static final ResourceKey<EquipmentAsset> FIRE = key("fire");
    public static final ResourceKey<EquipmentAsset> PINK_SCUBA = key("scuba/pink");
    public static final ResourceKey<EquipmentAsset> YELLOW_SCUBA = key("scuba/yellow");

    private static ResourceKey<EquipmentAsset> key(String id) {
        return Tropicraft.resourceKey(EquipmentAssets.ROOT_ID, id);
    }

    public static class Provider extends EquipmentAssetProvider {
        public Provider(PackOutput output) {
            super(output);
        }

        @Override
        protected void registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
            output.accept(SCALE, EquipmentClientInfo.builder()
                    .addHumanoidLayers(Tropicraft.location("scale"))
                    .build()
            );
            output.accept(FIRE, EquipmentClientInfo.builder()
                    .addHumanoidLayers(Tropicraft.location("fire"))
                    .build()
            );

            // Hack: these are not truthful layers at all, and ideally the texture should be in the expected folder
            output.accept(PINK_SCUBA, EquipmentClientInfo.builder()
                    .addMainHumanoidLayer(ScubaType.PINK.textureLocation(), false)
                    .build()
            );
            output.accept(YELLOW_SCUBA, EquipmentClientInfo.builder()
                    .addMainHumanoidLayer(ScubaType.YELLOW.textureLocation(), false)
                    .build()
            );
        }
    }
}
