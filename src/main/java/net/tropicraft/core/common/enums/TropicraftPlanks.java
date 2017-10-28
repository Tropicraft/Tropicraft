package net.tropicraft.core.common.enums;

public enum TropicraftPlanks implements ITropicraftVariant {
    MAHOGANY, PALM;
    
    public static final TropicraftPlanks VALUES[] = values();

    @Override
    public String getTypeName() {
        return "plank";
    }
};
