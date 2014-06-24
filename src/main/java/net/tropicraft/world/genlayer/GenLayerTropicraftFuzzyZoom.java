package net.tropicraft.world.genlayer;

public class GenLayerTropicraftFuzzyZoom extends GenLayerTropicraftZoom {

    public GenLayerTropicraftFuzzyZoom(long seed, GenLayerTropicraft parent) {
        super(seed, parent);
    }
    
    protected int selectModeOrRandom(int b1, int b2, int b3, int b4) {
        return this.selectRandom(new int[] {b1, b2, b3, b4});
    }
}