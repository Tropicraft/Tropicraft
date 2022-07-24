package net.tropicraft.core.common.block.experimental;

import net.minecraft.core.Vec3i;

public enum DiagonalDirections {
    DOWN(0,  new Vec3i(0, -1, 0),1),

    DOWN_N(1,  new Vec3i(0, -1, -1),2),
    DOWN_E(2,  new Vec3i(1, -1, 0),2),
    DOWN_S(3,  new Vec3i(0, -1, 1),2),
    DOWN_W(4,  new Vec3i(-1, -1, 0),2),

    UP(5,  new Vec3i(0, 1, 0),1),

    UP_N(6,  new Vec3i(0, 1, -1),2),
    UP_E(7,  new Vec3i(1, 1, 0),2),
    UP_S(8,  new Vec3i(0, 1, 1),2),
    UP_W(9,  new Vec3i(-1, 1, 0),2),

    NORTH(10,  new Vec3i(0, 0, -1),1),

    NORTH_E(11,  new Vec3i(1, 0, -1),2),
    NORTH_E_U(12,  new Vec3i(1, 1, -1), 3),
    NORTH_E_D(13,  new Vec3i(1, -1, -1), 3),

    SOUTH(14,  new Vec3i(0, 0, 1),1),

    SOUTH_W(15,  new Vec3i(-1, 0, 1),2),
    SOUTH_W_U(16,  new Vec3i(-1, 1, 1), 3),
    SOUTH_W_D(17,  new Vec3i(-1, -1, 1), 3),

    WEST(18,  new Vec3i(-1, 0, 0),1),

    WEST_N(19,  new Vec3i(-1, 0, -1),2),
    WEST_N_U(20,  new Vec3i(-1, 1, -1), 3),
    WEST_N_D(21,  new Vec3i(-1, -1, -1), 3),

    EAST(22,  new Vec3i(1, 0, 0),1),

    EAST_S(23,  new Vec3i(1, 0, 1),2),
    EAST_S_U(24,  new Vec3i(1, 1, 1), 3),
    EAST_S_D(25,  new Vec3i(1, -1, 1), 3);

    public final int id;
    public final Vec3i vector;
    public final int additionAmount;

    DiagonalDirections(int id, Vec3i vector, int additionAmount) {
        this.id = id;
        this.vector = vector;
        this.additionAmount = additionAmount;
    }

    public int getId() {
        return id;
    }

    public Vec3i getVector() {
        return vector;
    }

    public int getX(){
        return vector.getX();
    }

    public int getY(){
        return vector.getY();
    }

    public int getZ(){
        return vector.getZ();
    }
}
