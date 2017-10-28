package net.tropicraft.core.common.enums;

public enum TropicraftFlowerType implements ITropicraftVariant {
	EMPTY("empty"),
    COMMELINA_DIFFUSA("commelina_diffusa"),
    CROCOSMIA("crocosmia"),
    ORCHID("orchid"),
    CANNA("canna"),
    ANEMONE("anemone"),
    ORANGE_ANTHURIUM("orange_anthurium"),
    RED_ANTHURIUM("red_anthurium"),
    MAGIC_MUSHROOM("magic_mushroom"),
    PATHOS("pathos"),
    ACAI_VINE("acai_vine"),
    CROTON("croton"),
    DRACAENA("dracaena"),
    FERN("fern"),
    FOILAGE("foilage"),
    BROMELIAD("bromeliad"),
    IRIS("iris"),
    PINEAPPLE("pineapple"),
    LEMON_SAPLING("lemon_sapling"),
    LIME_SAPLING("lime_sapling"),
    GRAPEFRUIT_SAPLING("grapefruit_sapling"),
    ORANGE_SAPLING("orange_sapling"),
    PALM_SAPLING("palm_sapling"),
    MAHOGANY_SAPLING("mahogany_sapling");

    private final String name;
    
    public static final TropicraftFlowerType VALUES[] = values();

    private TropicraftFlowerType(String name) {
        this.name = name;
    }

    @Override
    public String getUnlocName() {
        return getSimpleName();
    }
    
    @Override
    public String getTypeName() {
        return "null";
    }
    
    @Override
    public String getSimpleName() {
        return name;
    }
}
