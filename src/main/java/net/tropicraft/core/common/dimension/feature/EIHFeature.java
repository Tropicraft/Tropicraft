package net.tropicraft.core.common.dimension.feature;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class EIHFeature extends Feature<NoFeatureConfig> {

    private static final Supplier<BlockState> EIH_STATE = () -> TropicraftBlocks.CHUNK.get().getDefaultState();
    private static final BlockState LAVA_STATE = Blocks.LAVA.getDefaultState();

    public EIHFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49878_1_) {
        super(p_i49878_1_);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        byte height = 5;
        int i = pos.getX();
        int j = pos.getY() + 1;
        int k = pos.getZ();

        if (goesBeyondWorldSize(world, pos.getY(), height)) {
            return false;
        }

        if (!isBBAvailable(world, pos, height)) {
            return false;
        }

        if (!TropicraftFeatureUtil.isSoil(world, pos.down()) && world.getBlockState(pos.down()).getMaterial() != Material.SAND) {
            return false;
        }

        setBlock(world, i + 0, j + 0, k + 2, EIH_STATE.get());
        setBlock(world, i + 0, j + 0, k + 3, EIH_STATE.get());
        setBlock(world, i + 0, j + 0, k + 4, EIH_STATE.get());
        setBlock(world, i + -1, j + 0, k + 4, EIH_STATE.get());
        setBlock(world, i + -1, j + 0, k + 1, EIH_STATE.get());
        setBlock(world, i + -1, j + 0, k + 3, EIH_STATE.get());
        setBlock(world, i + -1, j + 1, k + 4, EIH_STATE.get());
        setBlock(world, i + 0, j + 1, k + 4, EIH_STATE.get());
        setBlock(world, i + -1, j + 1, k + 3, EIH_STATE.get());
        setBlock(world, i + 0, j + 1, k + 3, EIH_STATE.get());
        setBlock(world, i + 0, j + 1, k + 2, EIH_STATE.get());
        setBlock(world, i + -1, j + 1, k + 1, EIH_STATE.get());
        setBlock(world, i + -1, j + 1, k + 0, EIH_STATE.get());
        setBlock(world, i + 0, j + 2, k + 3, EIH_STATE.get());
        setBlock(world, i + -1, j + 2, k + 3, EIH_STATE.get());
        setBlock(world, i + 0, j + 2, k + 2, EIH_STATE.get());
        setBlock(world, i + -1, j + 2, k + 0, EIH_STATE.get());
        setBlock(world, i + -1, j + 3, k + 3, EIH_STATE.get());
        setBlock(world, i + 0, j + 3, k + 2, EIH_STATE.get());
        setBlock(world, i + 0, j + 3, k + 1, EIH_STATE.get());
        setBlock(world, i + 0, j + 3, k + 0, EIH_STATE.get());
        setBlock(world, i + 0, j + 4, k + 2, EIH_STATE.get());
        setBlock(world, i + -1, j + 3, k + -1, EIH_STATE.get());
        setBlock(world, i + 0, j + 3, k + -1, EIH_STATE.get());
        setBlock(world, i + -1, j + 2, k + -1, EIH_STATE.get());
        setBlock(world, i + 0, j + 4, k + -1, EIH_STATE.get());
        setBlock(world, i + -1, j + 4, k + 2, EIH_STATE.get());
        setBlock(world, i + -1, j + 4, k + -1, EIH_STATE.get());
        setBlock(world, i + -1, j + 5, k + -1, EIH_STATE.get());
        setBlock(world, i + 0, j + 5, k + -1, EIH_STATE.get());
        setBlock(world, i + -1, j + 5, k + 1, EIH_STATE.get());
        setBlock(world, i + -1, j + 5, k + 2, EIH_STATE.get());
        setBlock(world, i + -1, j + 3, k + 4, EIH_STATE.get());
        setBlock(world, i + -1, j + 4, k + 3, EIH_STATE.get());
        setBlock(world, i + 0, j + 6, k + -1, EIH_STATE.get());
        setBlock(world, i + 0, j + 6, k + 0, EIH_STATE.get());
        setBlock(world, i + -1, j + 6, k + -1, EIH_STATE.get());
        setBlock(world, i + -1, j + 6, k + 0, EIH_STATE.get());
        setBlock(world, i + -1, j + 6, k + 1, EIH_STATE.get());
        setBlock(world, i + 1, j + 5, k + 0, EIH_STATE.get());
        setBlock(world, i + 1, j + 5, k + 1, EIH_STATE.get());
        setBlock(world, i + 1, j + 4, k + 1, EIH_STATE.get());
        setBlock(world, i + 1, j + 4, k + 0, EIH_STATE.get());
        setBlock(world, i + 0, j + 2, k + 1, EIH_STATE.get());
        setBlock(world, i + 0, j + 2, k + 0, EIH_STATE.get());
        setBlock(world, i + 0, j + 1, k + 0, EIH_STATE.get());
        setBlock(world, i + 0, j + 0, k + 0, EIH_STATE.get());
        setBlock(world, i + -1, j + 0, k + 0, EIH_STATE.get());
        setBlock(world, i + 0, j + 6, k + 1, EIH_STATE.get());
        setBlock(world, i + 0, j + 5, k + 0, LAVA_STATE);
        setBlock(world, i + -1, j + 4, k + 0, LAVA_STATE);
        setBlock(world, i + -1, j + 5, k + 0, LAVA_STATE);
        setBlock(world, i + -1, j + 3, k + 0, LAVA_STATE);
        setBlock(world, i + -1, j + 4, k + 1, LAVA_STATE);
        setBlock(world, i + -1, j + 3, k + 1, LAVA_STATE);
        setBlock(world, i + -1, j + 2, k + 1, LAVA_STATE);
        setBlock(world, i + -1, j + 3, k + 2, LAVA_STATE);
        setBlock(world, i + -1, j + 2, k + 2, LAVA_STATE);
        setBlock(world, i + -1, j + 1, k + 2, LAVA_STATE);
        setBlock(world, i + -2, j + 3, k + 4, EIH_STATE.get());
        setBlock(world, i + -2, j + 3, k + 3, EIH_STATE.get());
        setBlock(world, i + -2, j + 2, k + 3, EIH_STATE.get());
        setBlock(world, i + -2, j + 1, k + 3, EIH_STATE.get());
        setBlock(world, i + -2, j + 1, k + 4, EIH_STATE.get());
        setBlock(world, i + -2, j + 0, k + 4, EIH_STATE.get());
        setBlock(world, i + -2, j + 0, k + 3, EIH_STATE.get());
        setBlock(world, i + -2, j + 0, k + 1, EIH_STATE.get());
        setBlock(world, i + -2, j + 0, k + 0, EIH_STATE.get());
        setBlock(world, i + -2, j + 1, k + 1, EIH_STATE.get());
        setBlock(world, i + -2, j + 1, k + 0, EIH_STATE.get());
        setBlock(world, i + -2, j + 2, k + 0, EIH_STATE.get());
        setBlock(world, i + -2, j + 2, k + -1, EIH_STATE.get());
        setBlock(world, i + -2, j + 3, k + -1, EIH_STATE.get());
        setBlock(world, i + -2, j + 4, k + -1, EIH_STATE.get());
        setBlock(world, i + -2, j + 5, k + -1, EIH_STATE.get());
        setBlock(world, i + -2, j + 6, k + -1, EIH_STATE.get());
        setBlock(world, i + -2, j + 6, k + 1, EIH_STATE.get());
        setBlock(world, i + -2, j + 6, k + 0, EIH_STATE.get());
        setBlock(world, i + -2, j + 5, k + 2, EIH_STATE.get());
        setBlock(world, i + -2, j + 5, k + 1, EIH_STATE.get());
        setBlock(world, i + -2, j + 4, k + 2, EIH_STATE.get());
        setBlock(world, i + -2, j + 4, k + 3, EIH_STATE.get());
        setBlock(world, i + -2, j + 5, k + 0, LAVA_STATE);
        setBlock(world, i + -2, j + 4, k + 0, LAVA_STATE);
        setBlock(world, i + -2, j + 3, k + 0, LAVA_STATE);
        setBlock(world, i + -2, j + 4, k + 1, LAVA_STATE);
        setBlock(world, i + -2, j + 3, k + 1, LAVA_STATE);
        setBlock(world, i + -2, j + 2, k + 1, LAVA_STATE);
        setBlock(world, i + -2, j + 3, k + 2, LAVA_STATE);
        setBlock(world, i + -2, j + 2, k + 2, LAVA_STATE);
        setBlock(world, i + -2, j + 1, k + 2, LAVA_STATE);
        setBlock(world, i + -3, j + 0, k + 0, EIH_STATE.get());
        setBlock(world, i + -3, j + 0, k + 2, EIH_STATE.get());
        setBlock(world, i + -3, j + 0, k + 3, EIH_STATE.get());
        setBlock(world, i + -3, j + 0, k + 4, EIH_STATE.get());
        setBlock(world, i + -3, j + 1, k + 4, EIH_STATE.get());
        setBlock(world, i + -3, j + 1, k + 3, EIH_STATE.get());
        setBlock(world, i + -3, j + 2, k + 3, EIH_STATE.get());
        setBlock(world, i + -3, j + 1, k + 0, EIH_STATE.get());
        setBlock(world, i + -3, j + 1, k + 2, EIH_STATE.get());
        setBlock(world, i + -3, j + 2, k + 2, EIH_STATE.get());
        setBlock(world, i + -3, j + 2, k + 1, EIH_STATE.get());
        setBlock(world, i + -3, j + 2, k + 0, EIH_STATE.get());
        setBlock(world, i + -3, j + 3, k + 2, EIH_STATE.get());
        setBlock(world, i + -3, j + 4, k + 2, EIH_STATE.get());
        setBlock(world, i + -3, j + 3, k + 1, EIH_STATE.get());
        setBlock(world, i + -3, j + 3, k + 0, EIH_STATE.get());
        setBlock(world, i + -3, j + 3, k + -1, EIH_STATE.get());
        setBlock(world, i + -3, j + 4, k + -1, EIH_STATE.get());
        setBlock(world, i + -3, j + 5, k + -1, EIH_STATE.get());
        setBlock(world, i + -3, j + 6, k + -1, EIH_STATE.get());
        setBlock(world, i + -3, j + 6, k + 0, EIH_STATE.get());
        setBlock(world, i + -3, j + 6, k + 1, EIH_STATE.get());
        setBlock(world, i + -4, j + 5, k + 0, EIH_STATE.get());
        setBlock(world, i + -4, j + 4, k + 0, EIH_STATE.get());
        setBlock(world, i + -4, j + 4, k + 1, EIH_STATE.get());
        setBlock(world, i + 0, j + 4, k + 0, LAVA_STATE);
        setBlock(world, i + 0, j + 4, k + 1, LAVA_STATE);
        setBlock(world, i + -3, j + 4, k + 0, LAVA_STATE);
        setBlock(world, i + -3, j + 4, k + 1, LAVA_STATE);
        setBlock(world, i + -3, j + 5, k + 0, LAVA_STATE);
        setBlock(world, i + -4, j + 5, k + 1, EIH_STATE.get());
        setBlock(world, i + -2, j + 1, k + -1, EIH_STATE.get());
        setBlock(world, i + -1, j + 1, k + -1, EIH_STATE.get());
        setBlock(world, i + -2, j + 0, k + -1, EIH_STATE.get());
        setBlock(world, i + -1, j + 0, k + -1, EIH_STATE.get());
        setBlock(world, i + -3, j + -1, k + 0, EIH_STATE.get());
        setBlock(world, i + -2, j + -1, k + 0, EIH_STATE.get());
        setBlock(world, i + -1, j + -1, k + 0, EIH_STATE.get());
        setBlock(world, i + 0, j + -1, k + 0, EIH_STATE.get());
        setBlock(world, i + -2, j + -1, k + 1, EIH_STATE.get());
        setBlock(world, i + -1, j + -1, k + 1, EIH_STATE.get());
        setBlock(world, i + -3, j + -1, k + 2, EIH_STATE.get());
        setBlock(world, i + 0, j + -1, k + 2, EIH_STATE.get());
        setBlock(world, i + -2, j + -1, k + 3, EIH_STATE.get());
        setBlock(world, i + -1, j + -1, k + 3, EIH_STATE.get());
        setBlock(world, i + -3, j + -2, k + 2, EIH_STATE.get());
        setBlock(world, i + -2, j + -2, k + 3, EIH_STATE.get());
        setBlock(world, i + -1, j + -2, k + 3, EIH_STATE.get());
        setBlock(world, i + 0, j + -2, k + 2, EIH_STATE.get());
        setBlock(world, i + -1, j + -2, k + 1, EIH_STATE.get());
        setBlock(world, i + -2, j + -2, k + 1, EIH_STATE.get());
        setBlock(world, i + -3, j + -2, k + 0, EIH_STATE.get());
        setBlock(world, i + -2, j + -2, k + 0, EIH_STATE.get());
        setBlock(world, i + -1, j + -2, k + 0, EIH_STATE.get());
        setBlock(world, i + 0, j + -2, k + 0, EIH_STATE.get());
        setBlock(world, i + 0, j + -3, k + 2, EIH_STATE.get());
        setBlock(world, i + -1, j + -3, k + 3, EIH_STATE.get());
        setBlock(world, i + -1, j + 0, k + 2, LAVA_STATE);
        setBlock(world, i + -2, j + 0, k + 2, LAVA_STATE);
        setBlock(world, i + -1, j + -1, k + 2, LAVA_STATE);
        setBlock(world, i + -2, j + -1, k + 2, LAVA_STATE);
        setBlock(world, i + -2, j + -2, k + 2, LAVA_STATE);
        setBlock(world, i + -1, j + -2, k + 2, LAVA_STATE);
        setBlock(world, i + -2, j + -3, k + 3, EIH_STATE.get());
        setBlock(world, i + -1, j + -3, k + 2, EIH_STATE.get());
        setBlock(world, i + -2, j + -3, k + 2, EIH_STATE.get());
        setBlock(world, i + -3, j + -3, k + 2, EIH_STATE.get());
        setBlock(world, i + -2, j + -3, k + 1, EIH_STATE.get());
        setBlock(world, i + -1, j + -3, k + 1, EIH_STATE.get());
        setBlock(world, i + -3, j + -3, k + 0, EIH_STATE.get());
        setBlock(world, i + -2, j + -3, k + 0, EIH_STATE.get());
        setBlock(world, i + -1, j + -3, k + 0, EIH_STATE.get());
        setBlock(world, i + 0, j + -3, k + 0, EIH_STATE.get());

        // Coords of the first eye
        final int eyeOneX = i;
        final int eyeOneY = j + 5;
        final int eyeOneZ = k + 1;

        // Coords of the second eye
        final int eyeTwoX = i - 3;
        final int eyeTwoY = j + 5;
        final int eyeTwoZ = k + 1;

        final int eyeRand = world.getRandom().nextInt(9);

        // Place eyes
        placeEye(world, eyeOneX, eyeOneY, eyeOneZ, eyeRand);
        placeEye(world, eyeTwoX, eyeTwoY, eyeTwoZ, eyeRand);

        return true;
    }
    
    private void setBlock(IWorld world, int i, int i1, int i2, final BlockState state) {
        world.setBlockState(new BlockPos(i, i1, i2), state, 3);
    }

    /**
     * Place an eye on the head
     * @param x xCoord
     * @param y yCoord
     * @param z zCoord
     */
    private void placeEye(IWorld world, int x, int y, int z, int eyeRand) {
        if (world.getRandom().nextInt(1000) == 0) {
            eyeRand = world.getRandom().nextInt(9);
        }

        BlockState blockState;
        switch (eyeRand) {
            case 0:
            case 5:
                blockState = Blocks.GLOWSTONE.getDefaultState();
                break;
            case 1:
                blockState = Blocks.OBSIDIAN.getDefaultState();
                break;
            case 2:
                blockState = Blocks.DIAMOND_BLOCK.getDefaultState();
                break;
            case 3:
                blockState = Blocks.IRON_BLOCK.getDefaultState();
                break;
            case 4:
                blockState = Blocks.GOLD_BLOCK.getDefaultState();
                break;
            case 6:
                blockState = TropicraftBlocks.AZURITE_BLOCK.get().getDefaultState();
                break;
            case 7:
                blockState = TropicraftBlocks.EUDIALYTE_BLOCK.get().getDefaultState();
                break;
            case 8:
                blockState = TropicraftBlocks.ZIRCON_BLOCK.get().getDefaultState();
                break;
            default:	// Should never get called, if so, redstone in tropics :o
                blockState = Blocks.REDSTONE_BLOCK.getDefaultState();
                break;
        }

        setBlockState(world, new BlockPos(x, y, z), blockState);
    }
}
