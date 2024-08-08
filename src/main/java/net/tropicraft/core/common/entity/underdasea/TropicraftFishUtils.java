package net.tropicraft.core.common.entity.underdasea;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.tropicraft.core.common.dimension.TropicraftDimension;

public class TropicraftFishUtils {
    /**
     * Only needed because of a vanilla bug where many sea creatures cannot spawn > y63. Once that bug is fixed, this can be reverted to use the Vanilla behavior
     * which depends on getSeaLevel()
     */
    public static boolean checkSurfaceWaterAnimalSpawnRules(EntityType<? extends WaterAnimal> pWaterAnimal, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        int i = TropicraftDimension.getSeaLevel(pLevel);
        int j = i - 13;
        return pPos.getY() >= j
                && pPos.getY() <= i
                && pLevel.getFluidState(pPos.below()).is(FluidTags.WATER)
                && pLevel.getBlockState(pPos.above()).is(Blocks.WATER);
    }
}
