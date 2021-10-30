package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.block.AbstractBlock.Properties;

public final class BoardwalkBlock extends Block implements IWaterLoggable {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape BOTTOM_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    private static final VoxelShape TOP_SHAPE = Block.box(0.0, 11.0, 0.0, 16.0, 16.0, 16.0);

    public BoardwalkBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X).setValue(TYPE, Type.SHORT).setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        Type type = state.getValue(TYPE);
        if (type.isTall()) {
            return type.hasPost() ? VoxelShapes.block() : TOP_SHAPE;
        } else {
            return BOTTOM_SHAPE;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        boolean tall = context.getClickLocation().y - pos.getY() > 0.5;

        BlockState state = this.defaultBlockState()
                .setValue(AXIS, context.getHorizontalDirection().getAxis())
                .setValue(TYPE, tall ? Type.TALL : Type.SHORT)
                .setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
        state = this.applyConnections(state, world, pos);

        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        if (facing != Direction.UP) {
            return this.applyConnections(state, world, currentPos);
        } else {
            return state;
        }
    }

    private BlockState applyConnections(BlockState state, IWorld world, BlockPos pos) {
        Direction.Axis axis = state.getValue(AXIS);
        boolean tall = state.getValue(TYPE).isTall();

        BlockPos downPos = pos.below();
        boolean posted = canSupportCenter(world, downPos, Direction.UP);

        if (tall) {
            boolean front = this.connectsTo(world, pos, axis, Direction.AxisDirection.POSITIVE);
            boolean back = this.connectsTo(world, pos, axis, Direction.AxisDirection.NEGATIVE);
            if (front || back) posted = true;

            Type type = Type.tall(posted, front, back);
            return state.setValue(TYPE, type);
        } else {
            return state.setValue(TYPE, posted ? Type.SHORT_POST : Type.SHORT);
        }
    }

    private boolean connectsTo(IWorld world, BlockPos pos, Direction.Axis axis, Direction.AxisDirection direction) {
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
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90: switch (state.getValue(AXIS)) {
                case Z: return state.setValue(AXIS, Direction.Axis.X);
                case X: return state.setValue(AXIS, Direction.Axis.Z);
                default: return state;
            }
            default: return state;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AXIS, TYPE, WATERLOGGED);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, IBlockReader world, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
        return false;
    }

    public enum Type implements IStringSerializable {
        TALL("tall"),
        TALL_POST("tall_post"),
        TALL_POST_FRONT("tall_post_front"),
        TALL_POST_BACK("tall_post_back"),
        TALL_POST_FRONT_BACK("tall_post_front_back"),
        SHORT("short"),
        SHORT_POST("short_post");

        public static final Type[] TALLS = new Type[] { TALL, TALL_POST, TALL_POST_FRONT, TALL_POST_BACK, TALL_POST_FRONT_BACK };
        public static final Type[] SHORTS = new Type[] { SHORT, SHORT_POST };
        public static final Type[] TALL_POSTS = new Type[] { TALL_POST, TALL_POST_FRONT, TALL_POST_BACK, TALL_POST_FRONT_BACK };
        public static final Type[] SHORT_POSTS = new Type[] { SHORT_POST };
        public static final Type[] FRONTS = new Type[] { TALL_POST_FRONT, TALL_POST_FRONT_BACK };
        public static final Type[] BACKS = new Type[] { TALL_POST_BACK, TALL_POST_FRONT_BACK };

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
            return !this.isShort();
        }

        public boolean hasPost() {
            return this == TALL_POST || this == TALL_POST_FRONT || this == TALL_POST_BACK || this == TALL_POST_FRONT_BACK;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
