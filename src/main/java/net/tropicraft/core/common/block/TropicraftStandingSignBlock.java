package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("NullableProblems")
public class TropicraftStandingSignBlock extends StandingSignBlock {

    public static EnumProperty<BarkType> BARK_TYPE = EnumProperty.create("bark_type", BarkType.class);

    public enum BarkType implements StringRepresentable {
        RED("red", TropicraftWoodTypes.MANGROVE_RED),
        LIGHT("light", TropicraftWoodTypes.MANGROVE_LIGHT),
        BLACK("black", TropicraftWoodTypes.MANGROVE_BLACK);

        public final String name;
        public final WoodType type;

        BarkType(String name, WoodType type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        @NotNull
        public String getSerializedName() {
            return this.name;
        }
    }

    public TropicraftStandingSignBlock(Properties prop, WoodType woodType) {
        super(prop, woodType);

        this.registerDefaultState(this.stateDefinition.any().setValue(BARK_TYPE, BarkType.RED));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide){
            if(pPlayer.getItemInHand(pHand).is(Items.DEBUG_STICK)){
                BarkType type = pState.getValue(BARK_TYPE);

                switch (type.toString()) {
                    case "red" -> type = BarkType.LIGHT;
                    case "light" -> type = BarkType.BLACK;
                    case "black" -> type = BarkType.RED;
                }

                pLevel.setBlock(pPos, pState.setValue(BARK_TYPE, type), Block.UPDATE_ALL);

                return InteractionResult.SUCCESS;
            }
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);

        pBuilder.add(BARK_TYPE);
    }
}
