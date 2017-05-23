package net.tropicraft.core.common.block.tileentity;

import net.minecraft.tileentity.TileEntity;

/**
 * Class to get TileEntity instances for Tropicraft. Should make updating a little less painful having
 * things in one place. Idk. Maybe. :)
 */
public class TileEntityFactory {

//    public static TileEntity getAirCompressorTE() {
//        return new TileEntityAirCompressor();
//    }

    public static TileEntity getBambooChestTE() {
        return new TileEntityBambooChest();
    }
//    
//    public static TileEntity getBambooMugTE() {
//        return new TileEntityBambooMug();
//    }
//
//    public static TileEntity getCurareBowlTE() {
//        return new TileEntityCurareBowl();
//    }
//
//    public static TileEntity getFirePitTE() {
//        return new TileEntityFirePit();
//    }
//
//    public static TileEntity getFlowerPotTE() {
//        return new TileEntityTropicraftFlowerPot();
//    }
//
    public static TileEntity getSifterTE() {
        return new TileEntitySifter();
    }
    
    public static TileEntity getVolcanoTE() {
    	return new TileEntityVolcano();
    }
    
    public static TileEntity getDrinkMixerTE() {
    	return new TileEntityDrinkMixer();
    }
}
