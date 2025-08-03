package net.tropicraft.core.common.item.scuba;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.item.TropicraftArmorMaterials;

public enum ScubaType {
    YELLOW(TropicraftArmorMaterials.SCUBA_YELLOW, "yellow"),
    PINK(TropicraftArmorMaterials.SCUBA_PINK, "pink"),
    ;

    private final ArmorMaterial material;
    private final ResourceLocation textureLocation;

    ScubaType(ArmorMaterial material, String textureName) {
        this.material = material;
        textureLocation = Tropicraft.location("textures/models/armor/scuba_gear_" + textureName + ".png");
    }

    public ArmorMaterial material() {
        return material;
    }

    public ResourceLocation textureLocation() {
        return textureLocation;
    }
}
