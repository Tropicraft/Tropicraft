package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.core.common.dimension.TropicraftWorldUtils;
import net.tropicraft.core.common.sound.Sounds;

@Mod.EventBusSubscriber
public class BongoDrumBlock extends Block {

    public enum Size {
        SMALL(Sounds.BONGO_HIGH), MEDIUM(Sounds.BONGO_MED), LARGE(Sounds.BONGO_LOW);

        private final SoundEvent soundEvent;
        public static final Size VALUES[] = values();

        Size(final SoundEvent soundEvent) {
            this.soundEvent = soundEvent;
        }

        public SoundEvent getSoundEvent() {
            return this.soundEvent;
        }
    }

    public static final float SMALL_DRUM_SIZE = 0.5f;
    public static final float MEDIUM_DRUM_SIZE = 0.6f;
    public static final float BIG_DRUM_SIZE = 0.7f;

    public static final float SMALL_DRUM_OFFSET = (1.0f - SMALL_DRUM_SIZE)/2.0f;
    public static final float MEDIUM_DRUM_OFFSET = (1.0f - MEDIUM_DRUM_SIZE)/2.0f;
    public static final float BIG_DRUM_OFFSET = (1.0f - BIG_DRUM_SIZE)/2.0f;
    public static final float DRUM_HEIGHT = 1.0f;

    protected final VoxelShape BONGO_SMALL_AABB = Block.makeCuboidShape(SMALL_DRUM_OFFSET, 0.0f, SMALL_DRUM_OFFSET, 1 - SMALL_DRUM_OFFSET, DRUM_HEIGHT, 1 - SMALL_DRUM_OFFSET);
    protected final VoxelShape BONGO_MEDIUM_AABB = Block.makeCuboidShape(MEDIUM_DRUM_OFFSET, 0.0f, MEDIUM_DRUM_OFFSET, 1 - MEDIUM_DRUM_OFFSET, DRUM_HEIGHT, 1 - MEDIUM_DRUM_OFFSET);
    protected final VoxelShape BONGO_LARGE_AABB = Block.makeCuboidShape(BIG_DRUM_OFFSET, 0.0f, BIG_DRUM_OFFSET, 1-BIG_DRUM_OFFSET, DRUM_HEIGHT, 1-BIG_DRUM_OFFSET);

    private Size BONGO_SIZE;

    public BongoDrumBlock(final Size size, final Properties properties) {
        super(properties);
        this.BONGO_SIZE = size;
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        switch (BONGO_SIZE) {
            default:
            case SMALL:
                return BONGO_SMALL_AABB;
            case MEDIUM:
                return BONGO_MEDIUM_AABB;
            case LARGE:
                return BONGO_LARGE_AABB;
        }
    }

    @Override
    public VoxelShape getCollisionShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return getShape(state, worldIn, pos, context);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        // Only play drum sound if player hits the top
        if (result.getFace() != Direction.UP) {
            return false;
        }

        playBongoSound(world, player, pos, state);
        return true;
    }

    @Deprecated
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {

    }

    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        System.out.println(TropicraftWorldUtils.TROPICS_DIMENSION);
        if (event.getEntityPlayer() instanceof ServerPlayerEntity) {
            TropicraftWorldUtils.teleportPlayer((ServerPlayerEntity) event.getEntityPlayer());
        }
        final World world = event.getWorld();
        final BlockState state = world.getBlockState(event.getPos());
        final Block block = state.getBlock();
        // TODO blocktag
        if (state.getBlock() instanceof BongoDrumBlock && event.getFace() == Direction.UP) {
            ((BongoDrumBlock)block).playBongoSound(world, event.getEntityPlayer(), event.getPos(), state);
        }
    }

    /**
     * Play the bongo sound in game. Sound played determined by the size
     */
    public void playBongoSound(World world, PlayerEntity entity, BlockPos pos, BlockState state) {
        world.playSound(entity, pos.getX(), pos.getY() + 0.5D, pos.getZ(), BONGO_SIZE.getSoundEvent(), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState stateBefore, BlockState state2, Direction direction) {
        if (direction == Direction.DOWN) {
            return true;
        }

        return super.isSideInvisible(stateBefore, state2, direction);
    }
}
