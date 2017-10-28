package net.tropicraft.core.common.enums;


public enum TropicraftShells implements ITropicraftVariant {
    
    SOLO,
    FROX,
    PAB,
    RUBE,
    STARFISH,
    TURTLE,
    ;
    
    @Override
    public String getTypeName() {
        return "shell";
    }

}
