package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.tropicraft.Tropicraft;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public enum StarfishType implements StringRepresentable {
    /**
     * The red knobbed starfish.
     */
    RED("starfishRed", "Red Starfish", new String[]{
            "starfish_red_0",
            "starfish_red_1",
            "starfish_red_2"
    }, new float[]{
            0.03125f,
            0.03125f / 2.0f,
            0.03125f / 2.0f
    }),
    /**
     * Gorgeous 5 limbed starfish. Blue with orange rim.
     */
    ROYAL("starfishRoyal", "Royal Starfish", new String[]{
            "starfish_royal_0",
            "starfish_royal_1"
    });

    StarfishType(String unlocalizedName, String displayName, String[] textures, @Nullable float[] heights) {
        this.unlocalizedName = unlocalizedName;
        this.displayName = displayName;

        if (heights == null) {
            layerHeights = new float[textures.length];
            for (int i = 0; i < textures.length; i++) {
                layerHeights[i] = 0.03125f;
            }
        } else {
            layerHeights = heights;
        }

        this.textures = new ArrayList<>(textures.length);

        for (String texture : textures) {
            this.textures.add(Tropicraft.location("textures/entity/" + texture + ".png"));
        }
    }

    StarfishType(String unlocalizedName, String displayName, String[] textures) {
        this(unlocalizedName, displayName, textures, null);
    }

    public static final StarfishType[] VALUES = values();

    public static byte getMetaFromType(StarfishType type) {
        return (byte) (type == RED ? 0 : 1);
    }

    private final String unlocalizedName;
    private final String displayName;
    private final List<ResourceLocation> textures;
    private final float[] layerHeights;

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<ResourceLocation> getTextures() {
        return textures;
    }

    public float[] getLayerHeights() {
        return layerHeights;
    }

    public int getLayerCount() {
        return textures.size();
    }

    public static StarfishType getRandomType() {
        int type = RandomSource.create().nextInt(2);
        if (type == 0) return StarfishType.RED;
        else return StarfishType.ROYAL;
    }

    @Override
    public String getSerializedName() {
        return unlocalizedName;
    }
}
