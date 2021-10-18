package net.tropicraft.core.common.block.huge_plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class HugePlantBlock extends BushBlock {
    public static final BooleanProperty CENTER = BooleanProperty.create("center");
    public static final BooleanProperty SEED = BooleanProperty.create("seed");

    public HugePlantBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CENTER, false).with(SEED, false));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();

        for (BlockPos plantPos : Shape.fromSeed(this, pos)) {
            if (plantPos.equals(pos)) continue;

            if (!world.getBlockState(plantPos).isReplaceable(context)) {
                return null;
            }
        }

        return this.getDefaultState().with(SEED, true);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        Shape shape = Shape.fromSeed(this, pos);
        for (BlockPos plantPos : shape) {
            if (!plantPos.equals(pos)) {
                BlockState plantState = shape.blockAt(plantPos);
                world.setBlockState(plantPos, plantState, Constants.BlockFlags.DEFAULT);
            }
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
        Shape shape = Shape.match(this, world, pos);
        if (shape == null) {
            return Blocks.AIR.getDefaultState();
        }

        if (this.isValidPosition(world, shape)) {
            return state;
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }

    private boolean isValidPosition(IWorld world, Shape shape) {
        BlockPos seedPos = shape.seed();
        BlockState seedState = world.getBlockState(seedPos);
        return super.isValidPosition(seedState, world, seedPos);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        if (isSeedBlock(this, state)) {
            return super.isValidPosition(state, world, pos);
        } else {
            return Shape.match(this, world, pos) != null;
        }
    }

    public void placeAt(IWorld world, BlockPos pos, int flags) {
        Shape shape = Shape.fromSeed(this, pos);
        for (BlockPos plantPos : shape) {
            BlockState plantState = shape.blockAt(plantPos);
            world.setBlockState(plantPos, plantState, flags);
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isRemote) {
            Shape shape = Shape.match(this, world, pos);
            if (shape == null) return;

            if (!player.isCreative()) {
                spawnDrops(state, world, shape.seed(), null, player, player.getHeldItemMainhand());
            }

            int flags = Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.UPDATE_NEIGHBORS | Constants.BlockFlags.NO_NEIGHBOR_DROPS;

            for (BlockPos plantPos : shape) {
                world.setBlockState(plantPos, Blocks.AIR.getDefaultState(), flags);
                world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, plantPos, Block.getStateId(state));
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if (state.get(SEED)) {
            return super.getDrops(state, builder);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CENTER, SEED);
    }

    static boolean isSeedBlock(Block block, BlockState state) {
        return state.matchesBlock(block) && state.get(SEED);
    }

    public static final class Shape implements Iterable<BlockPos> {
        public static final int RADIUS = 1;
        public static final int SIZE = RADIUS * 2 + 1;

        private final Block block;
        private final BlockPos seed;

        private Shape(Block block, BlockPos seed) {
            this.block = block;
            this.seed = seed;
        }

        public static Shape fromSeed(Block block, BlockPos seed) {
            return new Shape(block, seed);
        }

        @Nullable
        public static Shape match(Block block, IBlockReader world, BlockPos pos) {
            BlockPos minPos = pos.add(-RADIUS, -RADIUS * 2, -RADIUS);
            BlockPos maxPos = pos.add(RADIUS, 0, RADIUS);
            for (BlockPos plantPos : BlockPos.getAllInBoxMutable(minPos, maxPos)) {
                if (isSeedBlock(block, world.getBlockState(plantPos))) {
                    Shape shape = Shape.fromSeed(block, plantPos);
                    if (shape.validate(world)) {
                        return shape;
                    }
                }
            }

            return null;
        }

        public boolean validate(IBlockReader world) {
            for (BlockPos pos : this) {
                if (!world.getBlockState(pos).matchesBlock(this.block)) {
                    return false;
                }
            }
            return true;
        }

        public BlockState blockAt(BlockPos pos) {
            return this.block.getDefaultState()
                    .with(CENTER, pos.equals(this.center()))
                    .with(SEED, pos.equals(this.seed()));
        }

        public BlockPos seed() {
            return this.seed;
        }

        public BlockPos center() {
            return this.seed.add(0, RADIUS, 0);
        }

        public AxisAlignedBB asAabb(Vector3d view) {
            BlockPos seed = this.seed;
            return new AxisAlignedBB(
                    seed.getX() - RADIUS - view.x,
                    seed.getY() - view.y,
                    seed.getZ() - RADIUS - view.z,
                    seed.getX() + RADIUS + 1.0 - view.x,
                    seed.getY() + RADIUS * 2 + 1.0 - view.y,
                    seed.getZ() + RADIUS + 1.0 - view.z
            );
        }

        @Override
        public Iterator<BlockPos> iterator() {
            BlockPos center = this.center();
            return BlockPos.getAllInBoxMutable(
                    center.add(-RADIUS, -RADIUS, -RADIUS),
                    center.add(RADIUS, RADIUS, RADIUS)
            ).iterator();
        }
    }
}
