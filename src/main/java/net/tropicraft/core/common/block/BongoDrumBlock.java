package net.tropicraft.core.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.sound.Sounds;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Tropicraft.ID)
public final class BongoDrumBlock extends Block {
    public static final MapCodec<BongoDrumBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Size.CODEC.fieldOf("size").forGetter(b -> b.size),
            propertiesCodec()
    ).apply(i, BongoDrumBlock::new));

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public enum Size implements StringRepresentable {
        SMALL("small", 8, Sounds.BONGO_HIGH, 1),
        MEDIUM("medium", 10, Sounds.BONGO_MED, 2),
        LARGE("large", 12, Sounds.BONGO_LOW, 3);

        public static final Codec<Size> CODEC = StringRepresentable.fromEnum(Size::values);

        private final String name;
        public final VoxelShape shape;
        final Supplier<SoundEvent> soundEvent;
        public final int recipeColumns;

        Size(String name, int size, Supplier<SoundEvent> soundEvent, int recipeColumns) {
            this.name = name;
            this.recipeColumns = recipeColumns;
            double offset = (16 - size) / 2;
            shape = box(offset, 0, offset, 16 - offset, 16, 16 - offset);
            this.soundEvent = soundEvent;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    private final Size size;

    public BongoDrumBlock(Size size, Properties properties) {
        super(properties);
        this.size = size;
        registerDefaultState(stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    public Size getSize() {
        return size;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return size.shape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return getShape(state, worldIn, pos, context);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        // Only play drum sound if player hits the topR
        if (result.getDirection() != Direction.UP) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        playBongoSound(level, pos, state, getAdjustedPitch(result));
        return InteractionResult.CONSUME;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.isClientSide) {
            return;
        }
        boolean flag = worldIn.hasNeighborSignal(pos);
        if (flag != state.getValue(POWERED)) {
            if (flag) {
                playBongoSound(worldIn, pos, state);
            }

            worldIn.setBlock(pos, state.setValue(POWERED, flag), 3);
        }
    }

    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Level level = event.getLevel();
        BlockState state = level.getBlockState(event.getPos());
        Block block = state.getBlock();
        if (state.getBlock() instanceof BongoDrumBlock && event.getFace() == Direction.UP) {
            ((BongoDrumBlock) block).playBongoSound(level, event.getPos(), state);
        }
    }

    public void playBongoSound(Level world, BlockPos pos, BlockState state) {
        playBongoSound(world, pos, state, 1.0f);
    }

    /**
     * Play the bongo sound in game. Sound played determined by the size
     */
    public void playBongoSound(Level world, BlockPos pos, BlockState state, float pitch) {
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, size.soundEvent.get(), SoundSource.BLOCKS, 1.0f, pitch);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    private float getAdjustedPitch(HitResult hitVec) {
        double distX = Math.abs(hitVec.getLocation().x - Math.floor(hitVec.getLocation().x) - 0.5);
        double distZ = Math.abs(hitVec.getLocation().z - Math.floor(hitVec.getLocation().z) - 0.5);
        double dist = (float) Math.sqrt(distX * distX + distZ * distZ);
        double radiusMax = 1.0f;
        if (size == Size.SMALL) {
            radiusMax = 8D / 16 / 2D;
        } else if (size == Size.MEDIUM) {
            radiusMax = 10 / 16 / 2D;
        } else if (size == Size.LARGE) {
            radiusMax = 12 / 16 / 2D;
        }
        double adjPitch = dist / radiusMax;
        //adjust to auto tuned nths
        /*float noteCount = 18.0f;
        adjPitch = ((int)(adjPitch * noteCount)) / noteCount;*/
        return 1.0f + (float) adjPitch;
    }
}
