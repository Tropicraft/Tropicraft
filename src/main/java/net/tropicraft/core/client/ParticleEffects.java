package net.tropicraft.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.DustParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ParticleEffects {
    public static void breakBlockWithFewerParticles(Level world, BlockState state, BlockPos pos) {
        ParticleEngine particles = Minecraft.getInstance().particleEngine;

        VoxelShape voxelshape = state.getShape(world, pos);
        voxelshape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            double xDist = Math.min(1.0D, x2 - x1);
            double yDist = Math.min(1.0D, y2 - y1);
            double zDist = Math.min(1.0D, z2 - z1);
            // 0.25 -> 0.5, reduce particle count by 8x
            int xCount = (int) Math.max(2, Math.ceil(xDist / 0.5D));
            int yCount = (int) Math.max(2, Math.ceil(yDist / 0.5D));
            int zCount = (int) Math.max(2, Math.ceil(zDist / 0.5D));

            for (int x = 0; x < xCount; ++x) {
                for (int y = 0; y < yCount; ++y) {
                    for (int z = 0; z < zCount; ++z) {
                        double currX = ((double) x + 0.5D) / (double) xCount;
                        double currY = ((double) y + 0.5D) / (double) yCount;
                        double currZ = ((double) z + 0.5D) / (double) zCount;
                        double xOffset = currX * xDist + x1;
                        double yOffset = currY * yDist + y1;
                        double zOffset = currZ * zDist + z1;
                        world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, currX - 0.5D, currY - 0.5D, currZ - 0.5D);
                        //particles.add((new DiggingParticle((ClientLevel) world, (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, currX - 0.5D, currY - 0.5D, currZ - 0.5D, state)).setBlockPos(pos))
                        }
                }
            }

        });
    }
}
