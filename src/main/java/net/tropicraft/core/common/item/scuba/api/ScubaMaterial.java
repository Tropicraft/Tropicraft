package net.tropicraft.core.common.item.scuba.api;

import java.util.Arrays;

import net.minecraft.client.resources.I18n;

public enum ScubaMaterial {

    PINK(35, "pink"),
    YELLOW(35, "yellow");

    /** The y-level that a player can safely dive to while wearing this gear material */
    private int maxDepth;

    /** The image prefix of this material type */
    private String id;

    private ScubaMaterial(int maxDepth, String id) {
        this.maxDepth = maxDepth;
        this.id = id;
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }
    
    public String getID() {
        return this.id;
    }

    public String getImagePrefix() {
        return getID();
    }

    public String getDisplayName() {
        return I18n.format("tropicraft.gui.suit." + getID());
    }
    
    public static ScubaMaterial byID(String id) {
        return Arrays.stream(values()).filter(m -> m.getID().equals(id)).findFirst().orElse(null);
    }
}
