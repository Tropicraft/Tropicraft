package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.core.common.sound.Sounds;

import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class BongoDrumBlock extends Block {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public enum Size {
        SMALL(8, () -> Sounds.BONGO_HIGH),
        MEDIUM(10, () -> Sounds.BONGO_MED),
        LARGE(12, () -> Sounds.BONGO_LOW);

        public final VoxelShape shape;
        final Supplier<SoundEvent> soundEvent;

        Size(int size, final Supplier<SoundEvent> soundEvent) {
            double offset = (16 - size) / 2;
            this.shape = makeCuboidShape(offset, 0, offset, 16 - offset, 16, 16 - offset);
            this.soundEvent = soundEvent;
        }
    }

    private final Size size;

    public BongoDrumBlock(final Size size, final Properties properties) {
        super(properties);
        this.size = size;
        setDefaultState(stateContainer.getBaseState().with(POWERED, Boolean.FALSE));
    }

    public Size getSize() {
        return size;
    }

    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return size.shape;
    }

    @Override
    public VoxelShape getCollisionShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return getShape(state, worldIn, pos, context);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        // Only play drum sound if player hits the top
        if (result.getFace() != Direction.UP) {
            return ActionResultType.PASS;
        }

        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        }

        playBongoSound(world, pos, state, getAdjustedPitch(result));
        return ActionResultType.CONSUME;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.isRemote) {
            return;
        }
        boolean flag = worldIn.isBlockPowered(pos);
        if (flag != state.get(POWERED)) {
            if (flag) {
                playBongoSound(worldIn, pos, state);
            }

            worldIn.setBlockState(pos, state.with(POWERED, flag), 3);
        }
    }

    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        final World world = event.getWorld();
        final BlockState state = world.getBlockState(event.getPos());
        final Block block = state.getBlock();
        if (state.getBlock() instanceof BongoDrumBlock && event.getFace() == Direction.UP) {
            ((BongoDrumBlock)block).playBongoSound(world, event.getPos(), state);
        }
    }

    public void playBongoSound(World world, BlockPos pos, BlockState state) {
        playBongoSound(world, pos, state, 1F);
    }

    /**
     * Play the bongo sound in game. Sound played determined by the size
     */
    public void playBongoSound(World world, BlockPos pos, BlockState state, float pitch) {
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, size.soundEvent.get(), SoundCategory.BLOCKS, 1.0F, pitch);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    public float getAdjustedPitch(RayTraceResult hitVec) {
        if (hitVec == null || hitVec.getHitVec() == null) return 1F;
        double distX = Math.abs(hitVec.getHitVec().x - (int)hitVec.getHitVec().x - 0.5);
        double distZ = Math.abs(hitVec.getHitVec().z - (int)hitVec.getHitVec().z - 0.5);
        double dist = (float) Math.sqrt(distX * distX + distZ * distZ);
        double radiusMax = 1F;
        if (size == Size.SMALL) {
            radiusMax = 8D / 16D / 2D;
        } else if (size == Size.MEDIUM) {
            radiusMax = 10D / 16D / 2D;
        } else if (size == Size.LARGE) {
            radiusMax = 12D / 16D / 2D;
        }
        double adjPitch = dist / radiusMax;
        //adjust to auto tuned nths
        /*float noteCount = 18F;
        adjPitch = ((int)(adjPitch * noteCount)) / noteCount;*/
        return 1F + (float) adjPitch;
    }
}
