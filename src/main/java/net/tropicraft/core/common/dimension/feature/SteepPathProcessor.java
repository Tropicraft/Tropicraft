package net.tropicraft.core.common.dimension.feature;

import javax.annotation.Nullable;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class SteepPathProcessor extends StructureProcessor {

    static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Info.MODID + ":steep_path", SteepPathProcessor::new);

    public SteepPathProcessor() {}

    public SteepPathProcessor(Dynamic<?> p_i51337_1_) {
        this();
    }

    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos seedPos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        BlockPos pos = blockInfo.pos;

        if (p_215194_3_.pos.getY() != 1 || p_215194_3_.state.getBlock() == TropicraftBlocks.BAMBOO_STAIRS) {
            return blockInfo;
        }

        pos = pos.up();
        
        Direction.Axis axis = getPathDirection(seedPos, p_215194_3_, placementSettingsIn, template);
        
        BlockState ladder = null;
        for (int i = 0; i < 4 && ladder == null; i++) {
            Direction dir = Direction.byHorizontalIndex(i);
            if (dir.getAxis() == axis) {
                ladder = canPlaceLadderAt(worldReaderIn, pos, dir);
            }
        }
        if (ladder == null) {
            return blockInfo;
        }
        Direction dir = ladder.get(LadderBlock.FACING).getOpposite();
        if (canPlaceLadderAt(worldReaderIn, pos.up(), dir) == null) {
            ((IWorld)worldReaderIn).setBlockState(pos, TropicraftBlocks.THATCH_STAIRS.getDefaultState().with(StairsBlock.FACING, dir), 2);
        } else {
            while (canPlaceLadderAt(worldReaderIn, pos, dir) != null) {
                ((IWorld)worldReaderIn).setBlockState(pos, ladder, 2);
                ((IWorld)worldReaderIn).setBlockState(pos.offset(dir), TropicraftBlocks.THATCH_BUNDLE.getDefaultState(), 2);
                pos = pos.up();
            }
        }
        return blockInfo;
    }
    
    private @Nullable Axis getPathDirection(BlockPos seedPos, BlockInfo current, PlacementSettings settings, Template template) {
        BlockInfo center = template.func_215381_a(seedPos, settings, Blocks.JIGSAW).stream()
                .filter(b -> b.nbt.getString("attachement_type").equals(Info.MODID + ":path_center"))
                .findFirst()
                .orElse(null);
        
        if (center == null) {
            return null;
        }
        
        BlockPos diff = Template.transformedBlockPos(settings, current.pos).subtract(center.pos.subtract(seedPos));
        return Math.abs(diff.getX()) > Math.abs(diff.getZ()) ? Axis.X : Axis.Z;
    }
    
    private BlockState canPlaceLadderAt(IWorldReader worldReaderIn, BlockPos pos, Direction dir) {
        BlockPos check = pos.offset(dir);
        BlockState state = worldReaderIn.getBlockState(check);
        if (!state.isAir(worldReaderIn, check)) {
            BlockState ladderState = TropicraftBlocks.BAMBOO_LADDER.getDefaultState().with(LadderBlock.FACING, dir.getOpposite());
            if (ladderState.isValidPosition(worldReaderIn, pos)) {
                return ladderState;
            }
        }
        return null;
    }

    @Override
    protected IStructureProcessorType getType() {
        return TYPE;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return null;
    }

}
