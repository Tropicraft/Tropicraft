package net.tropicraft.core.common.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.tropicraft.core.common.item.TropicraftItems;

public class BambooItemFrame extends ItemFrameEntity implements IEntityAdditionalSpawnData {

    public BambooItemFrame(final EntityType<? extends ItemFrameEntity> type, final World world) {
        super(type, world);
    }

    public BambooItemFrame(World worldIn, BlockPos pos, Direction direction) {
        super(TropicraftEntities.BAMBOO_ITEM_FRAME, worldIn);
        this.hangingPosition = pos;
        this.updateFacingWithBoundingBox(direction);
     }

    public void setHangingPosition(final BlockPos pos) {
        hangingPosition = pos;
    }

    @Override
    public void updateFacingWithBoundingBox(final Direction facingDir) {
        super.updateFacingWithBoundingBox(facingDir);
    }

    @Override
    public boolean onValidSurface() {
        return super.onValidSurface();
     }

    @Override
    public void onBroken(@Nullable final Entity brokenEntity) {
        playSound(SoundEvents.ENTITY_ITEM_FRAME_BREAK, 1.0F, 1.0F);
        dropItemOrSelf(brokenEntity, true);
    }

    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        } else if (!source.isExplosion() && !getDisplayedItem().isEmpty()) {
            if (!world.isRemote) {
                dropItemOrSelf(source.getTrueSource(), false);
                playSound(SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
            }

            return true;
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    private void dropItemOrSelf(@Nullable Entity entityIn, boolean p_146065_2_) {
        if (!this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            if (entityIn == null) {
                removeItem(this.getDisplayedItem());
            }

        } else {
            ItemStack itemstack = this.getDisplayedItem();
            this.setDisplayedItem(ItemStack.EMPTY);
            if (entityIn instanceof PlayerEntity) {
                PlayerEntity playerentity = (PlayerEntity)entityIn;
                if (playerentity.abilities.isCreativeMode) {
                    removeItem(itemstack);
                    return;
                }
            }

            if (p_146065_2_) {
                this.entityDropItem(TropicraftItems.BAMBOO_ITEM_FRAME);
            }

            if (!itemstack.isEmpty()) {
                itemstack = itemstack.copy();
                removeItem(itemstack);
                if (rand.nextFloat() < 1.0F) {
                    entityDropItem(itemstack);
                }
            }
        }
    }

    private void removeItem(final ItemStack stack) {
        if (stack.getItem() instanceof FilledMapItem) {
            MapData mapdata = FilledMapItem.getMapData(stack, world);
            mapdata.removeItemFrame(hangingPosition, getEntityId());
            mapdata.setDirty(true);
        }

        stack.setItemFrame(null);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeBlockPos(this.hangingPosition);
        buffer.writeByte(facingDirection.getIndex());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.hangingPosition = additionalData.readBlockPos();
        updateFacingWithBoundingBox(Direction.byIndex(additionalData.readByte()));
    }
}
