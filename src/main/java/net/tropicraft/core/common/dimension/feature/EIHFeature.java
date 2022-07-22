package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import net.tropicraft.core.common.block.TropicraftBlocks;

import java.util.Random;
import java.util.function.Supplier;

import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.goesBeyondWorldSize;
import static net.tropicraft.core.common.dimension.feature.TropicraftFeatureUtil.isBBAvailable;

public class EIHFeature extends Feature<NoneFeatureConfiguration> {

    private static final Supplier<BlockState> EIH_STATE = () -> TropicraftBlocks.CHUNK.get().defaultBlockState();
    private static final BlockState LAVA_STATE = Blocks.LAVA.defaultBlockState();

    public EIHFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        Random rand = context.random();
        BlockPos pos = context.origin();

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

        if (!TropicraftFeatureUtil.isSoil(world, pos.below()) && world.getBlockState(pos.below()).getMaterial() != Material.SAND) {
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
    
    private void setBlock(LevelAccessor world, int i, int i1, int i2, final BlockState state) {
        world.setBlock(new BlockPos(i, i1, i2), state, 3);
    }

    /**
     * Place an eye on the head
     * @param x xCoord
     * @param y yCoord
     * @param z zCoord
     */
    private void placeEye(LevelAccessor world, int x, int y, int z, int eyeRand) {
        if (world.getRandom().nextInt(1000) == 0) {
            eyeRand = world.getRandom().nextInt(9);
        }

        BlockState blockState;
        switch (eyeRand) {
            case 0:
            case 5:
                blockState = Blocks.GLOWSTONE.defaultBlockState();
                break;
            case 1:
                blockState = Blocks.OBSIDIAN.defaultBlockState();
                break;
            case 2:
                blockState = Blocks.DIAMOND_BLOCK.defaultBlockState();
                break;
            case 3:
                blockState = Blocks.IRON_BLOCK.defaultBlockState();
                break;
            case 4:
                blockState = Blocks.GOLD_BLOCK.defaultBlockState();
                break;
            case 6:
                blockState = TropicraftBlocks.AZURITE_BLOCK.get().defaultBlockState();
                break;
            case 7:
                blockState = TropicraftBlocks.EUDIALYTE_BLOCK.get().defaultBlockState();
                break;
            case 8:
                blockState = TropicraftBlocks.ZIRCON_BLOCK.get().defaultBlockState();
                break;
            default:    // Should never get called, if so, redstone in tropics :o
                blockState = Blocks.REDSTONE_BLOCK.defaultBlockState();
                break;
        }

        setBlock(world, new BlockPos(x, y, z), blockState);
    }
}
