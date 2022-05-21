package net.tropicraft.core.common.block.huge_plant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.core.client.ParticleEffects;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public final class HugePlantBlock extends BushBlock {
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    private Supplier<RegistryObject<? extends ItemLike>> pickItem;

    public HugePlantBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, Type.SEED));
    }

    public HugePlantBlock setPickItem(Supplier<RegistryObject<? extends ItemLike>> item) {
        this.pickItem = item;
        return this;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
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
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        Shape shape = Shape.fromSeed(this, pos);
        for (BlockPos plantPos : shape) {
            if (!plantPos.equals(pos)) {
                BlockState plantState = shape.blockAt(plantPos);
                world.setBlock(plantPos, plantState, Block.UPDATE_ALL);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos) {
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

    private boolean isValidPosition(LevelAccessor world, Shape shape) {
        BlockPos seedPos = shape.seed();
        BlockState seedState = world.getBlockState(seedPos);
        return super.canSurvive(seedState, world, seedPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
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

    private boolean isValidPositionToPlace(LevelReader world, BlockPos pos) {
        for (BlockPos plantPos : Shape.fromSeed(this, pos)) {
            if (!world.getBlockState(plantPos).getMaterial().isReplaceable()) {
                return false;
            }
        }
        return true;
    }

    public void placeAt(LevelAccessor world, BlockPos pos, int flags) {
        Shape shape = Shape.fromSeed(this, pos);
        for (BlockPos plantPos : shape) {
            BlockState plantState = shape.blockAt(plantPos);
            world.setBlock(plantPos, plantState, flags);
        }
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        Shape shape = Shape.match(this, world, pos);
        if (shape == null) {
            return;
        }

        if (!world.isClientSide) {
            if (!player.isCreative()) {
                dropResources(state, world, shape.seed(), null, player, player.getMainHandItem());
            }

            int flags = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_SUPPRESS_DROPS;

            // Play break sound
            SoundType soundtype = state.getSoundType(world, pos, null);
            world.playSound(null, pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

            for (BlockPos plantPos : shape) {
                world.setBlock(plantPos, Blocks.AIR.defaultBlockState(), flags);
            }
        } else {
            // We need to manually play the breaking particles to prevent vanilla logic from creating too many

            for (BlockPos plantPos : shape) {
                ParticleEffects.breakBlockWithFewerParticles(world, state, plantPos);
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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        if (this.pickItem != null) {
            return new ItemStack(this.pickItem.get().get());
        }
        return super.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    private static boolean isSeedBlock(Block block, BlockState state) {
        return state.is(block) && state.getValue(TYPE) == Type.SEED;
    }

    public enum Type implements StringRepresentable {
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
        public static Shape match(Block block, BlockGetter world, BlockPos pos) {
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
        public static Shape matchIncomplete(Block block, BlockGetter world, BlockPos pos) {
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

        public boolean validate(BlockGetter world) {
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

        public AABB asAabb() {
            BlockPos seed = this.seed;
            return new AABB(
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
