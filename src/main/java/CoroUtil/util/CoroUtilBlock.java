package CoroUtil.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class CoroUtilBlock {
	
	public static boolean isAir(Block parBlock) {
		Material mat = parBlock.getDefaultState().getMaterial();
		if (mat == Material.AIR) {
			return true;
		} else {
			return false;
		}
	}
	
}
