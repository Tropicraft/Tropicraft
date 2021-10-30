package net.tropicraft.core.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.tropicraft.core.common.item.TropicraftItems;

public class BambooItemFrame extends ItemFrameEntity implements IEntityAdditionalSpawnData {

    public BambooItemFrame(final EntityType<? extends ItemFrameEntity> type, final World world) {
        super(type, world);
    }

    public BambooItemFrame(World worldIn, BlockPos pos, Direction direction) {
        this(TropicraftEntities.BAMBOO_ITEM_FRAME.get(), worldIn, pos, direction);
    }

    protected BambooItemFrame(final EntityType<? extends BambooItemFrame> type, final World world, final BlockPos pos,
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
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeByte(direction.get3DDataValue());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.pos = additionalData.readBlockPos();
        setDirection(Direction.from3DDataValue(additionalData.readByte()));
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(TropicraftItems.BAMBOO_ITEM_FRAME.get());
    }
}
