package net.tropicraft.core.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public final class ParticleEffects {
    public static void breakBlockWithFewerParticles(World world, BlockState state, BlockPos pos) {
        ParticleManager particles = Minecraft.getInstance().particles;

        VoxelShape voxelshape = state.getShape(world, pos);
        voxelshape.forEachBox((x1, y1, z1, x2, y2, z2) -> {
            double xDist = Math.min(1.0D, x2 - x1);
            double yDist = Math.min(1.0D, y2 - y1);
            double zDist = Math.min(1.0D, z2 - z1);
            // 0.25 -> 0.5, reduce particle count by 8x
            int xCount = Math.max(2, MathHelper.ceil(xDist / 0.5D));
            int yCount = Math.max(2, MathHelper.ceil(yDist / 0.5D));
            int zCount = Math.max(2, MathHelper.ceil(zDist / 0.5D));

            for (int x = 0; x < xCount; ++x) {
                for (int y = 0; y < yCount; ++y) {
                    for (int z = 0; z < zCount; ++z) {
                        double currX = ((double) x + 0.5D) / (double) xCount;
                        double currY = ((double) y + 0.5D) / (double) yCount;
                        double currZ = ((double) z + 0.5D) / (double) zCount;
                        double xOffset = currX * xDist + x1;
                        double yOffset = currY * yDist + y1;
                        double zOffset = currZ * zDist + z1;
                        particles.addEffect((new DiggingParticle((ClientWorld) world, (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, currX - 0.5D, currY - 0.5D, currZ - 0.5D, state)).setBlockPos(pos));
                    }
                }
            }

        });
    }
}
