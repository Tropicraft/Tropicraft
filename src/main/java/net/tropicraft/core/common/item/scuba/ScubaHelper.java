package net.tropicraft.core.common.item.scuba;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ScubaHelper {
    public static boolean isFullyUnderwater(World world, EntityPlayer player) {
//        int x = MathHelper.ceil(player.posX);
//        int y = MathHelper.ceil(player.posY + player.height - 0.5F);
//        int z = MathHelper.ceil(player.posZ);

        // return world.getBlock(x, y, z).getMaterial().isLiquid();
        return player.isInsideOfMaterial(Material.WATER);
    }
}
