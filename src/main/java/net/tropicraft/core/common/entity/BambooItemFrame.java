package net.tropicraft.core.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.tropicraft.core.common.item.TropicraftItems;

public class BambooItemFrame extends ItemFrame implements IEntityWithComplexSpawn {

    public BambooItemFrame(final EntityType<? extends ItemFrame> type, final Level world) {
        super(type, world);
    }

    public BambooItemFrame(Level worldIn, BlockPos pos, Direction direction) {
        this(TropicraftEntities.BAMBOO_ITEM_FRAME.get(), worldIn, pos, direction);
    }

    protected BambooItemFrame(final EntityType<? extends BambooItemFrame> type, final Level world, final BlockPos pos,
            final Direction direction) {
        super(type, world);
        this.pos = pos;
        this.setDirection(direction);
    }
    
    @Override
    public ItemStack getFrameItemStack() { return new ItemStack(TropicraftItems.BAMBOO_ITEM_FRAME.get()); }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeByte(direction.get3DDataValue());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        this.pos = additionalData.readBlockPos();
        setDirection(Direction.from3DDataValue(additionalData.readByte()));
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.BAMBOO_ITEM_FRAME.get());
    }
}
