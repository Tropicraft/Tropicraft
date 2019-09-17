package net.tropicraft.core.common.entity.placeable;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class WallItemEntity extends HangingEntity implements IEntityAdditionalSpawnData {

    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(WallItemEntity.class, DataSerializers.ITEMSTACK);

    public WallItemEntity(EntityType<? extends HangingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(ITEM, ItemStack.EMPTY);
    }

    public void setHangingPosition(final BlockPos pos) {
        hangingPosition = pos;
    }

    @Override
    public void updateFacingWithBoundingBox(final Direction facingDir) {
        super.updateFacingWithBoundingBox(facingDir);
    }

    @Override
    public int getWidthPixels() {
        return 16;
    }

    @Override
    public int getHeightPixels() {
        return 16;
    }

    @Override
    public void onBroken(@Nullable Entity brokenEntity) {
        if (world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            if (brokenEntity instanceof PlayerEntity) {
                final PlayerEntity player = (PlayerEntity) brokenEntity;

                if (player.abilities.isCreativeMode) {
                    return;
                }
            }

            playSound(SoundEvents.ENTITY_ITEM_FRAME_BREAK, 1.0F, 1.0F);
            entityDropItem(dataManager.get(ITEM), 0.0F);
        }
    }

    @Override
    public void playPlaceSound() {
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeByte(facingDirection.getHorizontalIndex());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        updateFacingWithBoundingBox(Direction.byHorizontalIndex(additionalData.readByte()));
    }

    public ItemStack getDisplayedItem() {
        return getDataManager().get(ITEM);
    }

    public void setDisplayedItem(@Nullable ItemStack stack) {
        setDisplayedItemWithUpdate(stack);
    }

    private void setDisplayedItemWithUpdate(@Nullable ItemStack stack) {
        if (stack != null) {
            stack = stack.copy();
            stack.setCount(1);
        }

        dataManager.set(ITEM, stack);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        final CompoundNBT tagCompound = compound.getCompound("Item");

        if (!tagCompound.isEmpty()) {
            setDisplayedItemWithUpdate(ItemStack.read(tagCompound));
        }

        super.readAdditional(compound);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        if (getDisplayedItem() != null) {
            compound.put("Item", getDisplayedItem().write(new CompoundNBT()));
        }

        super.writeAdditional(compound);
    }
}
