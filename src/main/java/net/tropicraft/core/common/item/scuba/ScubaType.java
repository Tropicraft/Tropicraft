package net.tropicraft.core.common.item.scuba;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.item.TropicraftArmorMaterials;

public enum ScubaType {
    YELLOW(TropicraftArmorMaterials.SCUBA_YELLOW, "yellow"),
    PINK(TropicraftArmorMaterials.SCUBA_PINK, "pink"),
    ;

    private final ArmorMaterial material;
    private final Identifier textureLocation;

    ScubaType(ArmorMaterial material, String textureName) {
        this.material = material;
        textureLocation = Tropicraft.id("textures/models/armor/scuba_gear_" + textureName + ".png");
    }

    public ArmorMaterial material() {
        return material;
    }

    public Identifier textureLocation() {
        return textureLocation;
    }
}
