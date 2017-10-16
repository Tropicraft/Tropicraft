package build.world;

import net.minecraft.util.math.BlockPos;

public class BuildDirectionHelper {

	/*if (isClear(parCoords, parWorld, 1, 0)) return 0;
    if (isClear(parCoords, parWorld, 0, -1)) return 1;
	if (isClear(parCoords, parWorld, -1, 0)) return 2;
	if (isClear(parCoords, parWorld, 0, 1)) return 3;*/

    public static BlockPos getDirectionToCoords(int parDirection) {
        if (parDirection == 0) return new BlockPos(1, 0, 0);
        if (parDirection == 1) return new BlockPos(0, 0, -1);
        if (parDirection == 2) return new BlockPos(-1, 0, 0);
        if (parDirection == 3) return new BlockPos(0, 0, 1);

        System.out.println("warning, misuse of getDirectionToCoords()");
        return null;
    }

    public static int getCoordsToDirection(BlockPos parCoords) {
        if (parCoords.getX() > 0) return 0;
        if (parCoords.getZ() < 0) return 1;
        if (parCoords.getX() < 0) return 2;
        if (parCoords.getZ() > 0) return 3;

        System.out.println("warning, misuse of getCoordsToDirection()");
        return -1;
    }

}
