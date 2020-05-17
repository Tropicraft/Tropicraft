package net.tropicraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.block.tileentity.AirCompressorTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AirCompressorBlock extends Block {

	@Nonnull
    public static final EnumProperty<Direction> FACING = HorizontalBlock.HORIZONTAL_FACING;

    public AirCompressorBlock(Block.Properties properties) {
		super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("tropicraft.tooltip.air_compressor"));
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (world.isRemote) {
			return ActionResultType.SUCCESS;
		}

		ItemStack stack = player.getHeldItemMainhand();

		AirCompressorTileEntity mixer = (AirCompressorTileEntity)world.getTileEntity(pos);

		if (mixer.isDoneCompressing()) {
			mixer.ejectTank();
            return ActionResultType.CONSUME;
		}

		if (stack.isEmpty()) {
			mixer.ejectTank();
            return ActionResultType.CONSUME;
		}

		ItemStack ingredientStack = stack.copy();
		ingredientStack.setCount(1);

		if (mixer.addTank(ingredientStack)) {
			player.inventory.decrStackSize(player.inventory.currentItem, 1);
		}

        return ActionResultType.CONSUME;
	}

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!world.isRemote) {
		    AirCompressorTileEntity te = (AirCompressorTileEntity) world.getTileEntity(pos);
			te.ejectTank();
		}

        super.onReplaced(state, world, pos, newState, isMoving);
	}
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new AirCompressorTileEntity();
	}

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState ret = super.getStateForPlacement(context);
        return ret.with(FACING, context.getPlacementHorizontalFacing());
    }
}