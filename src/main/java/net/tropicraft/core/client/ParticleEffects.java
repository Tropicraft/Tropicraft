package net.tropicraft.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class ParticleEffects {
    public static void breakBlockWithFewerParticles(Level level, BlockState state, BlockPos pos) {
        ParticleEngine particles = Minecraft.getInstance().particleEngine;

        VoxelShape shape = state.getShape(level, pos);
        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            double xDist = Math.min(1.0, x2 - x1);
            double yDist = Math.min(1.0, y2 - y1);
            double zDist = Math.min(1.0, z2 - z1);
            // 0.25 -> 0.5, reduce particle count by 8x
            int xCount = Math.max(2, Mth.ceil(xDist / 0.5));
            int yCount = Math.max(2, Mth.ceil(yDist / 0.5));
            int zCount = Math.max(2, Mth.ceil(zDist / 0.5));

            for (int x = 0; x < xCount; ++x) {
                for (int y = 0; y < yCount; ++y) {
                    for (int z = 0; z < zCount; ++z) {
                        double currX = (x + 0.5) / xCount;
                        double currY = (y + 0.5) / yCount;
                        double currZ = (z + 0.5) / zCount;
                        double xOffset = currX * xDist + x1;
                        double yOffset = currY * yDist + y1;
                        double zOffset = currZ * zDist + z1;
                        particles.add(new TerrainParticle(
                                (ClientLevel) level,
                                pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset,
                                currX - 0.5, currY - 0.5, currZ - 0.5,
                                state
                        ).updateSprite(state, pos));
                    }
                }
            }
        });
    }
}
