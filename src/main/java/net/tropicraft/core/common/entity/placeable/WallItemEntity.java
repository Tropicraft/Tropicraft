package net.tropicraft.core.common.entity.placeable;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.BambooItemFrame;
import net.tropicraft.core.common.entity.TropicraftEntities;

public class WallItemEntity extends BambooItemFrame {

    public WallItemEntity(EntityType<? extends WallItemEntity> entityType, World world) {
        super(entityType, world);
    }

	public WallItemEntity(World worldIn, BlockPos pos, Direction on) {
		super(TropicraftEntities.WALL_ITEM.get(), worldIn, pos, on);
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
    protected void dropItemOrSelf(@Nullable Entity entityIn, boolean p_146065_2_) {
    	super.dropItemOrSelf(entityIn, false);
    	this.remove();
    }

    @Override
    public void playPlaceSound() {
    }
}
