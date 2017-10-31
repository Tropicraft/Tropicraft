package net.tropicraft.core.common.enums;

public enum TropicraftPlanks implements ITropicraftVariant {
    MAHOGANY(BlockHardnessValues.MAHOGANY.hardness, BlockHardnessValues.MAHOGANY.resistance),
    PALM(BlockHardnessValues.PALM.hardness, BlockHardnessValues.PALM.resistance);

    private final float hardness;
    private final float resistance;
    public static final TropicraftPlanks VALUES[] = values();

    TropicraftPlanks(float hardness, float resistance) {
        this.hardness = hardness;
        this.resistance = resistance;
    }

    public float getHardness() {
        return this.hardness;
    }

    public float getResistance() {
        return this.resistance;
    }

    @Override
    public String getTypeName() {
        return "plank";
    }
};
