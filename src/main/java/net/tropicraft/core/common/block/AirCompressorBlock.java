package net.tropicraft.core.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.block.tileentity.AirCompressorTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AirCompressorBlock extends Block {

    @Nonnull
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public AirCompressorBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = player.getMainHandItem();

        AirCompressorTileEntity mixer = (AirCompressorTileEntity)world.getBlockEntity(pos);

        if (mixer.isDoneCompressing()) {
            mixer.ejectTank();
            return InteractionResult.CONSUME;
        }

        if (stack.isEmpty()) {
            mixer.ejectTank();
            return InteractionResult.CONSUME;
        }

        ItemStack ingredientStack = stack.copy();
        ingredientStack.setCount(1);

        if (mixer.addTank(ingredientStack)) {
            player.inventory.removeItem(player.inventory.selected, 1);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide) {
            AirCompressorTileEntity te = (AirCompressorTileEntity) world.getBlockEntity(pos);
            te.ejectTank();
        }

        super.onRemove(state, world, pos, newState, isMoving);
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @Nullable
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new AirCompressorTileEntity();
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState ret = super.getStateForPlacement(context);
        return ret.setValue(FACING, context.getHorizontalDirection());
    }
}
