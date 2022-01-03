package net.tropicraft.core.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.tropicraft.core.common.item.TropicraftItems;

public class BambooItemFrame extends ItemFrame implements IEntityAdditionalSpawnData {

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
    protected void dropItem(Entity entityIn, boolean dropSelf) {
        super.dropItem(entityIn, false);
        if (dropSelf) {
            this.spawnAtLocation(TropicraftItems.BAMBOO_ITEM_FRAME.get());
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeByte(direction.get3DDataValue());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.pos = additionalData.readBlockPos();
        setDirection(Direction.from3DDataValue(additionalData.readByte()));
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(TropicraftItems.BAMBOO_ITEM_FRAME.get());
    }
}
