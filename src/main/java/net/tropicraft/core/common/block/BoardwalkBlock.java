package net.tropicraft.core.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public final class BoardwalkBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<BoardwalkBlock> CODEC = simpleCodec(BoardwalkBlock::new);

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape BOTTOM_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    private static final VoxelShape TOP_SHAPE = Block.box(0.0, 11.0, 0.0, 16.0, 16.0, 16.0);

    public BoardwalkBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.X).setValue(TYPE, Type.SHORT).setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<BoardwalkBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Type type = state.getValue(TYPE);
        if (type.isTall()) {
            return type.hasPost() ? Shapes.block() : TOP_SHAPE;
        } else {
            return BOTTOM_SHAPE;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        boolean tall = context.getClickLocation().y - pos.getY() > 0.5;

        BlockState state = defaultBlockState()
                .setValue(AXIS, context.getHorizontalDirection().getAxis())
                .setValue(TYPE, tall ? Type.TALL : Type.SHORT)
                .setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
        state = applyConnections(state, world, pos);

        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        if (facing != Direction.UP) {
            return applyConnections(state, world, currentPos);
        } else {
            return state;
        }
    }

    private BlockState applyConnections(BlockState state, LevelAccessor world, BlockPos pos) {
        Direction.Axis axis = state.getValue(AXIS);
        boolean tall = state.getValue(TYPE).isTall();

        BlockPos downPos = pos.below();
        boolean posted = canSupportCenter(world, downPos, Direction.UP);

        if (tall) {
            boolean front = connectsTo(world, pos, axis, Direction.AxisDirection.POSITIVE);
            boolean back = connectsTo(world, pos, axis, Direction.AxisDirection.NEGATIVE);
            if (front || back) posted = true;

            Type type = Type.tall(posted, front, back);
            return state.setValue(TYPE, type);
        } else {
            return state.setValue(TYPE, posted ? Type.SHORT_POST : Type.SHORT);
        }
    }

    private boolean connectsTo(LevelAccessor world, BlockPos pos, Direction.Axis axis, Direction.AxisDirection direction) {
        BlockPos connectPos = pos.relative(Direction.fromAxisAndDirection(axis, direction));
        BlockState connectState = world.getBlockState(connectPos);
        return connectState.is(this) && connectState.getValue(TYPE).isShort();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(AXIS)) {
                case Z -> state.setValue(AXIS, Direction.Axis.X);
                case X -> state.setValue(AXIS, Direction.Axis.Z);
                default -> state;
            };
            default -> state;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, TYPE, WATERLOGGED);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public enum Type implements StringRepresentable {
        TALL("tall"),
        TALL_POST("tall_post"),
        TALL_POST_FRONT("tall_post_front"),
        TALL_POST_BACK("tall_post_back"),
        TALL_POST_FRONT_BACK("tall_post_front_back"),
        SHORT("short"),
        SHORT_POST("short_post");

        public static final Type[] TALLS = new Type[]{TALL, TALL_POST, TALL_POST_FRONT, TALL_POST_BACK, TALL_POST_FRONT_BACK};
        public static final Type[] SHORTS = new Type[]{SHORT, SHORT_POST};
        public static final Type[] TALL_POSTS = new Type[]{TALL_POST, TALL_POST_FRONT, TALL_POST_BACK, TALL_POST_FRONT_BACK};
        public static final Type[] SHORT_POSTS = new Type[]{SHORT_POST};
        public static final Type[] FRONTS = new Type[]{TALL_POST_FRONT, TALL_POST_FRONT_BACK};
        public static final Type[] BACKS = new Type[]{TALL_POST_BACK, TALL_POST_FRONT_BACK};

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public static Type tall(boolean posted, boolean front, boolean back) {
            if (posted) {
                if (front && back) return Type.TALL_POST_FRONT_BACK;
                else if (front) return Type.TALL_POST_FRONT;
                else if (back) return Type.TALL_POST_BACK;
                else return Type.TALL_POST;
            } else {
                return Type.TALL;
            }
        }

        public boolean isShort() {
            return this == SHORT || this == SHORT_POST;
        }

        public boolean isTall() {
            return !isShort();
        }

        public boolean hasPost() {
            return this == TALL_POST || this == TALL_POST_FRONT || this == TALL_POST_BACK || this == TALL_POST_FRONT_BACK;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
