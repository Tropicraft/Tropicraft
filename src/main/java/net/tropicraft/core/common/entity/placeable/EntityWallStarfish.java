package net.tropicraft.core.common.entity.placeable;

import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;

import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.entity.underdasea.StarfishType;

import com.google.common.base.Predicate;

public class EntityWallStarfish extends EntityHanging {
	private StarfishType starfishType;
	
	private static final DataParameter<EnumFacing> FACING = EntityDataManager.<EnumFacing>createKey(EntityWallStarfish.class, DataSerializers.FACING);
	private static final DataParameter<Byte> STARFISH_TYPE = EntityDataManager.<Byte>createKey(EntityWallStarfish.class, DataSerializers.BYTE);

	public EntityWallStarfish(World par1World) {
		super(par1World);
		setStarfishType(StarfishType.values()[0]);
	}

	public EntityWallStarfish(World world, BlockPos pos, EnumFacing direction, StarfishType starfishType) {
		super(world, pos);
		setStarfishType(starfishType);
		this.facingDirection = direction;
		this.updateFacingWithBoundingBox(direction);
	}
	
	@Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (FACING.equals(key)) {
            this.facingDirection = this.getFacing();
            this.updateFacingWithBoundingBox(this.facingDirection);
        }
        super.notifyDataManagerChange(key);
    }
	
	@Override
	protected void entityInit() {
		this.getDataManager().register(FACING, EnumFacing.SOUTH);
		this.getDataManager().register(STARFISH_TYPE, Byte.valueOf((byte) 0));
		super.entityInit();
	}
	
	public void setFacing(EnumFacing facing) {
		this.dataManager.set(FACING, facing);
		this.facingDirection = getFacing();
		this.updateFacingWithBoundingBox(facing);
	}

	public EnumFacing getFacing() {
		return this.dataManager.get(FACING);
	}

	@Override
	public float getCollisionBorderSize() {
		return 0.0F;
	}

	/**
	 * Set the position and rotation values directly without any clamping.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
	{
		BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
		this.setPosition((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
	}

	/**
	 * Sets the location and Yaw/Pitch of an entity in the world
	 */
	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
		this.setPosition(x, y, z);
	}

	@Override
	public int getWidthPixels() {
		return 9;
	}

	@Override
	public int getHeightPixels() {
		return 9;
	}

    /**
     * checks to make sure painting can be placed there
     */
	@Override
    public boolean onValidSurface()
    {
		System.out.println("on");
        return super.onValidSurface();
    }

	//	@Override
	public void dropItemStack() {
		//this.entityDropItem(new ItemStack(TropicraftItems.shells, 1, getShellType()), 0.0F);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setByte("StarfishType", StarfishType.getMetaFromType(getStarfishType()));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setStarfishType(StarfishType.VALUES[nbt.getByte("StarfishType")]);
	}

	public StarfishType getStarfishType() {
		return StarfishType.VALUES[this.getDataManager().get(STARFISH_TYPE)];
	}

	public void setStarfishType(StarfishType starfishType) {
		this.getDataManager().set(STARFISH_TYPE, StarfishType.getMetaFromType(starfishType));
	}

	/**
	 * Called when this entity is broken. Entity parameter may be null.
	 */
	@Override
	public void onBroken(Entity entity) {
		this.dropItemStack();
	}
//
//	@Override
//	public void writeSpawnData(ByteBuf data) {
//		BlockPos blockpos = this.getHangingPosition();
//		data.writeInt(blockpos.getX());
//		data.writeInt(blockpos.getY());
//		data.writeInt(blockpos.getZ());
//		data.writeByte(starfishType.ordinal());
//		//data.writeByte((byte)this.facingDirection.getHorizontalIndex());
//	}
//
//	@Override
//	public void readSpawnData(ByteBuf data) {
//		int x = data.readInt();
//		int y = data.readInt();
//		int z = data.readInt();
//		BlockPos pos = new BlockPos(x, y, z);
//		this.hangingPosition = pos;
//		starfishType = StarfishType.values()[data.readByte()];
//		//this.facingDirection = EnumFacing.getHorizontal(data.readByte());
//	}

	@Override
	public void playPlaceSound() {
		this.playSound(SoundEvents.BLOCK_WATERLILY_PLACE, 1.0F, 1.0F);
	}
}