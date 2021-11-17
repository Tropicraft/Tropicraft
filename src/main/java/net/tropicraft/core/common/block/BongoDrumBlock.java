package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.core.common.sound.Sounds;

import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

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
            this.shape = box(offset, 0, offset, 16 - offset, 16, 16 - offset);
            this.soundEvent = soundEvent;
        }
    }

    private final Size size;

    public BongoDrumBlock(final Size size, final Properties properties) {
        super(properties);
        this.size = size;
        registerDefaultState(stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    public Size getSize() {
        return size;
    }

    @Override
    public VoxelShape getShape(final BlockState state, final BlockGetter worldIn, final BlockPos pos, final CollisionContext context) {
        return size.shape;
    }

    @Override
    public VoxelShape getCollisionShape(final BlockState state, final BlockGetter worldIn, final BlockPos pos, final CollisionContext context) {
        return getShape(state, worldIn, pos, context);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        // Only play drum sound if player hits the top
        if (result.getDirection() != Direction.UP) {
            return InteractionResult.PASS;
        }

        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        playBongoSound(world, pos, state, getAdjustedPitch(result));
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
        final Level world = event.getWorld();
        final BlockState state = world.getBlockState(event.getPos());
        final Block block = state.getBlock();
        if (state.getBlock() instanceof BongoDrumBlock && event.getFace() == Direction.UP) {
            ((BongoDrumBlock)block).playBongoSound(world, event.getPos(), state);
        }
    }

    public void playBongoSound(Level world, BlockPos pos, BlockState state) {
        playBongoSound(world, pos, state, 1F);
    }

    /**
     * Play the bongo sound in game. Sound played determined by the size
     */
    public void playBongoSound(Level world, BlockPos pos, BlockState state, float pitch) {
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, size.soundEvent.get(), SoundSource.BLOCKS, 1.0F, pitch);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    public float getAdjustedPitch(HitResult hitVec) {
        if (hitVec == null || hitVec.getLocation() == null) return 1F;
        double distX = Math.abs(hitVec.getLocation().x - Math.floor(hitVec.getLocation().x) - 0.5);
        double distZ = Math.abs(hitVec.getLocation().z - Math.floor(hitVec.getLocation().z) - 0.5);
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
