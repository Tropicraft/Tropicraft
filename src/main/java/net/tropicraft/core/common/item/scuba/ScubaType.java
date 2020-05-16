package net.tropicraft.core.common.item.scuba;

public enum ScubaType {

    YELLOW("yellow"),
    PINK("pink"),;

    private final String textureName;

    private ScubaType(String textureName) {
        this.textureName = textureName;
    }
    
    public String getTextureName() {
        return textureName;
    }
}
