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
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock.Properties;

public final class HugePlantBlock extends BushBlock {
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    private Supplier<RegistryObject<? extends IItemProvider>> pickItem;

    public HugePlantBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, Type.SEED));
    }

    public HugePlantBlock setPickItem(Supplier<RegistryObject<? extends IItemProvider>> item) {
        this.pickItem = item;
        return this;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        for (BlockPos plantPos : Shape.fromSeed(this, pos)) {
            if (plantPos.equals(pos)) continue;

            if (!world.getBlockState(plantPos).canBeReplaced(context)) {
                return null;
            }
        }

        return this.defaultBlockState().setValue(TYPE, Type.SEED);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        Shape shape = Shape.fromSeed(this, pos);
        for (BlockPos plantPos : shape) {
            if (!plantPos.equals(pos)) {
                BlockState plantState = shape.blockAt(plantPos);
                world.setBlock(plantPos, plantState, Constants.BlockFlags.DEFAULT);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
        Shape shape = Shape.match(this, world, pos);
        if (shape == null) {
            return Blocks.AIR.defaultBlockState();
        }

        if (this.isValidPosition(world, shape)) {
            return state;
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    private boolean isValidPosition(IWorld world, Shape shape) {
        BlockPos seedPos = shape.seed();
        BlockState seedState = world.getBlockState(seedPos);
        return super.canSurvive(seedState, world, seedPos);
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        if (isSeedBlock(this, state)) {
            BlockState worldState = world.getBlockState(pos);
            if (worldState != state && !this.isValidPositionToPlace(world, pos)) {
                return false;
            }

            return super.canSurvive(state, world, pos);
        } else {
            return Shape.match(this, world, pos) != null;
        }
    }

    private boolean isValidPositionToPlace(IWorldReader world, BlockPos pos) {
        for (BlockPos plantPos : Shape.fromSeed(this, pos)) {
            if (!world.getBlockState(plantPos).getMaterial().isReplaceable()) {
                return false;
            }
        }
        return true;
    }

    public void placeAt(IWorld world, BlockPos pos, int flags) {
        Shape shape = Shape.fromSeed(this, pos);
        for (BlockPos plantPos : shape) {
            BlockState plantState = shape.blockAt(plantPos);
            world.setBlock(plantPos, plantState, flags);
        }
    }

    @Override
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClientSide) {
            Shape shape = Shape.match(this, world, pos);
            if (shape == null) return;

            if (!player.isCreative()) {
                dropResources(state, world, shape.seed(), null, player, player.getMainHandItem());
            }

            int flags = Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.UPDATE_NEIGHBORS | Constants.BlockFlags.NO_NEIGHBOR_DROPS;

            for (BlockPos plantPos : shape) {
                world.setBlock(plantPos, Blocks.AIR.defaultBlockState(), flags);
                world.levelEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, plantPos, Block.getId(state));
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if (state.getValue(TYPE) == Type.SEED) {
            return super.getDrops(state, builder);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        if (this.pickItem != null) {
            return new ItemStack(this.pickItem.get().get());
        }
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    static boolean isSeedBlock(Block block, BlockState state) {
        return state.is(block) && state.getValue(TYPE) == Type.SEED;
    }

    public enum Type implements IStringSerializable {
        SEED("seed"),
        CENTER("center"),
        OUTER("outer");

        private final String key;

        Type(String key) {
            this.key = key;
        }

        @Override
        public String getSerializedName() {
            return this.key;
        }
    }

    public static final class Shape implements Iterable<BlockPos> {
        public static final int RADIUS = 1;

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
            for (BlockPos plantPos : matchPositions(pos)) {
                if (isSeedBlock(block, world.getBlockState(plantPos))) {
                    Shape shape = Shape.fromSeed(block, plantPos);
                    if (shape.validate(world)) {
                        return shape;
                    }
                }
            }
            return null;
        }

        @Nullable
        public static Shape matchIncomplete(Block block, IBlockReader world, BlockPos pos) {
            for (BlockPos plantPos : matchPositions(pos)) {
                if (isSeedBlock(block, world.getBlockState(plantPos))) {
                    return Shape.fromSeed(block, plantPos);
                }
            }
            return null;
        }

        private static Iterable<BlockPos> matchPositions(BlockPos pos) {
            BlockPos minPos = pos.offset(-RADIUS, -RADIUS * 2, -RADIUS);
            BlockPos maxPos = pos.offset(RADIUS, 0, RADIUS);
            return BlockPos.betweenClosed(minPos, maxPos);
        }

        public boolean validate(IBlockReader world) {
            for (BlockPos pos : this) {
                if (!world.getBlockState(pos).is(this.block)) {
                    return false;
                }
            }
            return true;
        }

        public BlockState blockAt(BlockPos pos) {
            Type type = Type.OUTER;
            if (pos.equals(this.seed())) {
                type = Type.SEED;
            } else if (pos.equals(this.center())) {
                type = Type.CENTER;
            }

            return this.block.defaultBlockState().setValue(TYPE, type);
        }

        public BlockPos seed() {
            return this.seed;
        }

        public BlockPos center() {
            return this.seed.offset(0, RADIUS, 0);
        }

        public AxisAlignedBB asAabb() {
            BlockPos seed = this.seed;
            return new AxisAlignedBB(
                    seed.getX() - RADIUS,
                    seed.getY(),
                    seed.getZ() - RADIUS,
                    seed.getX() + RADIUS + 1.0,
                    seed.getY() + 2 * RADIUS + 1.0,
                    seed.getZ() + RADIUS + 1.0
            );
        }

        @Override
        public Iterator<BlockPos> iterator() {
            BlockPos center = this.center();
            return BlockPos.betweenClosed(
                    center.offset(-RADIUS, -RADIUS, -RADIUS),
                    center.offset(RADIUS, RADIUS, RADIUS)
            ).iterator();
        }
    }
}
