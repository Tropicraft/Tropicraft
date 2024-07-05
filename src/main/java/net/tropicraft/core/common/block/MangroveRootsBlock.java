package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ByteMap;
import it.unimi.dsi.fastutil.objects.Reference2ByteOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class MangroveRootsBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<MangroveRootsBlock> CODEC = simpleCodec(MangroveRootsBlock::new);

    private static final Reference2ByteMap<BlockState> STATE_TO_KEY = new Reference2ByteOpenHashMap<>();
    private static final VoxelShape[] SHAPE_TABLE = buildShapeTable();

    private static final int PIANGUA_GROW_CHANCE = 80;
    private static final int PIANGUA_RADIUS = 1;

    public static final BooleanProperty TALL = BooleanProperty.create("tall");
    public static final BooleanProperty GROUNDED = BooleanProperty.create("grounded");
    public static final EnumProperty<Connection> NORTH = EnumProperty.create("north", Connection.class);
    public static final EnumProperty<Connection> EAST = EnumProperty.create("east", Connection.class);
    public static final EnumProperty<Connection> SOUTH = EnumProperty.create("south", Connection.class);
    public static final EnumProperty<Connection> WEST = EnumProperty.create("west", Connection.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final Direction[] DIRECTIONS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    public static final EnumProperty<Connection>[] CONNECTIONS = new EnumProperty[]{NORTH, EAST, SOUTH, WEST};

    public MangroveRootsBlock(Block.Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
                .setValue(TALL, true)
                .setValue(GROUNDED, false)
                .setValue(NORTH, Connection.NONE)
                .setValue(EAST, Connection.NONE)
                .setValue(SOUTH, Connection.NONE)
                .setValue(WEST, Connection.NONE)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected MapCodec<MangroveRootsBlock> codec() {
        return CODEC;
    }

    private static VoxelShape[] buildShapeTable() {
        VoxelShape[] table = new VoxelShape[1 << 5];

        for (int key = 0; key < table.length; key++) {
            boolean tall = (key & 1) != 0;
            boolean north = (key >> 1 & 1) != 0;
            boolean east = (key >> 2 & 1) != 0;
            boolean south = (key >> 3 & 1) != 0;
            boolean west = (key >> 4 & 1) != 0;
            table[key] = computeShapeFor(tall, north, east, south, west);
        }

        return table;
    }

    private static VoxelShape computeShapeFor(boolean tall, boolean north, boolean east, boolean south, boolean west) {
        double height = tall ? 16.0 : 10.0;

        VoxelShape shape = Block.box(6.0, 0.0, 6.0, 10.0, height, 10.0);
        if (north) shape = Shapes.or(shape, Block.box(6.0, 0.0, 0.0, 10.0, height, 6.0));
        if (east) shape = Shapes.or(shape, Block.box(10.0, 0.0, 6.0, 16.0, height, 10.0));
        if (south) shape = Shapes.or(shape, Block.box(6.0, 0.0, 10.0, 10.0, height, 16.0));
        if (west) shape = Shapes.or(shape, Block.box(0.0, 0.0, 6.0, 6.0, height, 10.0));

        return shape;
    }

    private static int shapeKey(BlockState state) {
        return (state.getValue(TALL) ? 1 : 0)
                | (state.getValue(NORTH).exists() ? 1 << 1 : 0)
                | (state.getValue(EAST).exists() ? 1 << 2 : 0)
                | (state.getValue(SOUTH).exists() ? 1 << 3 : 0)
                | (state.getValue(WEST).exists() ? 1 << 4 : 0);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        byte key = STATE_TO_KEY.computeByteIfAbsent(state, MangroveRootsBlock::shapeKey);
        return SHAPE_TABLE[key];
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return super.getBlockSupportShape(state, reader, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getConnectedState(context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return getConnectedState(world, currentPos);
    }

    private BlockState getConnectedState(LevelReader world, BlockPos pos) {
        BlockState state = defaultBlockState()
                .setValue(WATERLOGGED, world.getFluidState(pos).is(FluidTags.WATER))
                .setValue(GROUNDED, isGrounded(world, pos));

        state = state
                .setValue(NORTH, getConnectionFor(world, pos, Direction.NORTH))
                .setValue(EAST, getConnectionFor(world, pos, Direction.EAST))
                .setValue(SOUTH, getConnectionFor(world, pos, Direction.SOUTH))
                .setValue(WEST, getConnectionFor(world, pos, Direction.WEST));

        if (!isTall(state) && !canConnectUp(world, pos.above())) {
            state = state.setValue(TALL, false)
                    .setValue(NORTH, state.getValue(NORTH).shorten())
                    .setValue(EAST, state.getValue(EAST).shorten())
                    .setValue(SOUTH, state.getValue(SOUTH).shorten())
                    .setValue(WEST, state.getValue(WEST).shorten());
        }

        return state;
    }

    private Connection getConnectionFor(LevelReader world, BlockPos pos, Direction direction) {
        BlockPos adjacentPos = pos.relative(direction);
        BlockState adjacentState = world.getBlockState(adjacentPos);

        if (canConnectTo(adjacentState, world, adjacentPos, direction)) {
            // don't add a connection if the block above us has that connection too
            if (world.getBlockState(pos.above()).is(this) && canConnectTo(world, adjacentPos.above(), direction)) {
                return Connection.NONE;
            }

            if (adjacentState.is(this)) {
                boolean tall = isAdjacentTall(world, adjacentPos, direction.getOpposite());
                return tall ? Connection.HIGH : Connection.LOW;
            } else {
                return Connection.HIGH;
            }
        }

        return Connection.NONE;
    }

    private boolean isAdjacentTall(LevelReader world, BlockPos pos, Direction sourceDirection) {
        if (canConnectUp(world, pos.above())) {
            return true;
        }

        for (Direction direction : DIRECTIONS) {
            if (direction != sourceDirection && canConnectTo(world, pos.relative(direction), direction)) {
                return true;
            }
        }

        return false;
    }

    private boolean isTall(BlockState state) {
        int count = 0;
        if (state.getValue(NORTH).exists()) count++;
        if (state.getValue(EAST).exists()) count++;
        if (state.getValue(SOUTH).exists()) count++;
        if (state.getValue(WEST).exists()) count++;
        return count > 1;
    }

    private boolean canConnectTo(BlockGetter world, BlockPos pos, Direction direction) {
        return canConnectTo(world.getBlockState(pos), world, pos, direction);
    }

    // TODO: make method for exceptions
    private boolean canConnectTo(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return (state.is(this) || state.isFaceSturdy(world, pos, direction)) && !FenceBlock.isExceptionForConnection(state) && (!(state.getBlock() instanceof TrapDoorBlock));
    }

    private boolean canConnectUp(LevelReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return (state.is(this) || Block.canSupportCenter(world, pos, Direction.DOWN)) && !(state.getBlock() instanceof TrapDoorBlock);
    }

    private boolean isGrounded(BlockGetter world, BlockPos pos) {
        BlockPos groundPos = pos.below();
        return world.getBlockState(groundPos).isFaceSturdy(world, groundPos, Direction.UP);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 -> state
                    .setValue(NORTH, state.getValue(SOUTH))
                    .setValue(EAST, state.getValue(WEST))
                    .setValue(SOUTH, state.getValue(NORTH))
                    .setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> state
                    .setValue(NORTH, state.getValue(EAST))
                    .setValue(EAST, state.getValue(SOUTH))
                    .setValue(SOUTH, state.getValue(WEST))
                    .setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90 -> state
                    .setValue(NORTH, state.getValue(WEST))
                    .setValue(EAST, state.getValue(NORTH))
                    .setValue(SOUTH, state.getValue(EAST))
                    .setValue(WEST, state.getValue(SOUTH));
            default -> state;
        };
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default -> super.mirror(state, mirror);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TALL, GROUNDED, NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(GROUNDED) && state.getValue(TALL);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(PIANGUA_GROW_CHANCE) == 0) {
            tryGrowPianguas(world, pos, random);
        }
    }

    private void tryGrowPianguas(ServerLevel world, BlockPos pos, RandomSource random) {
        BlockPos soilPos = pos.below();
        if (!world.getBlockState(soilPos).is(TropicraftBlocks.MUD.get())) {
            return;
        }

        BlockPos growPos = soilPos.offset(
                random.nextInt(PIANGUA_RADIUS * 2 + 1) - PIANGUA_RADIUS,
                -random.nextInt(PIANGUA_RADIUS + 1),
                random.nextInt(PIANGUA_RADIUS * 2 + 1) - PIANGUA_RADIUS
        );

        BlockState growIn = world.getBlockState(growPos);
        if (growIn.is(TropicraftBlocks.MUD.get()) && !world.getBlockState(growPos.above()).canOcclude()) {
            if (!hasNearPianguas(world, growPos)) {
                world.setBlockAndUpdate(growPos, TropicraftBlocks.MUD_WITH_PIANGUAS.get().defaultBlockState());
            }
        }
    }

    private boolean hasNearPianguas(ServerLevel world, BlockPos source) {
        Block mudWithPianguas = TropicraftBlocks.MUD_WITH_PIANGUAS.get();
        BlockPos minSpacingPos = source.offset(-PIANGUA_RADIUS, -PIANGUA_RADIUS, -PIANGUA_RADIUS);
        BlockPos maxSpacingPos = source.offset(PIANGUA_RADIUS, 0, PIANGUA_RADIUS);

        for (BlockPos pos : BlockPos.betweenClosed(minSpacingPos, maxSpacingPos)) {
            if (world.getBlockState(pos).is(mudWithPianguas)) {
                return true;
            }
        }

        return false;
    }

    public enum Connection implements StringRepresentable {
        NONE("none"),
        HIGH("high"),
        LOW("low");

        private final String key;

        Connection(String key) {
            this.key = key;
        }

        public boolean exists() {
            return this != NONE;
        }

        @Override
        public String getSerializedName() {
            return key;
        }

        public Connection shorten() {
            return this == HIGH ? LOW : this;
        }
    }
}
