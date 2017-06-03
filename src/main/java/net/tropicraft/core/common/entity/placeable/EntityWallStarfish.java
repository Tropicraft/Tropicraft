package net.tropicraft.core.common.entity.placeable;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.entity.underdasea.StarfishType;

public class EntityWallStarfish extends EntityHanging implements IEntityAdditionalSpawnData {
	private StarfishType starfishType;

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
	
    private static final Predicate<Entity> IS_HANGING_ENTITY = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof EntityHanging;
        }
    };
	
    /**
     * checks to make sure painting can be placed there
     */
	@Override
    public boolean onValidSurface()
    {
        if (!this.worldObj.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty())
        {
        	System.out.println("uh");
        	System.out.println(this.worldObj.getCollisionBoxes(this, this.getEntityBoundingBox()));
            return false;
        }
        else
        {
            int i = Math.max(1, this.getWidthPixels() / 16);
            int j = Math.max(1, this.getHeightPixels() / 16);
            BlockPos blockpos = this.hangingPosition.offset(this.facingDirection.getOpposite());
            EnumFacing enumfacing = this.facingDirection.rotateYCCW();

            for (int k = 0; k < i; ++k)
            {
                for (int l = 0; l < j; ++l)
                {
                    int i1 = i > 2 ? -1 : 0;
                    int j1 = j > 2 ? -1 : 0;
                    BlockPos blockpos1 = blockpos.offset(enumfacing, k + i1).up(l + j1);
                    IBlockState iblockstate = this.worldObj.getBlockState(blockpos1);

                    if (iblockstate.isSideSolid(this.worldObj, blockpos1, this.facingDirection))
                        continue;

                    if (!iblockstate.getMaterial().isSolid() && !BlockRedstoneDiode.isDiode(iblockstate))
                    {
                        return false;
                    }
                }
            }

            return this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), IS_HANGING_ENTITY).isEmpty();
        }
    }

	//	@Override
	public void dropItemStack() {
		//this.entityDropItem(new ItemStack(TropicraftItems.shells, 1, getShellType()), 0.0F);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setByte("StarfishType", (byte)getStarfishType().ordinal());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setStarfishType(StarfishType.values()[nbt.getByte("StarfishType")]);
	}

	public StarfishType getStarfishType() {
		return starfishType;
	}

	public void setStarfishType(StarfishType starfishType) {
		this.starfishType = starfishType;
	}

	/**
	 * Called when this entity is broken. Entity parameter may be null.
	 */
	@Override
	public void onBroken(Entity entity) {
		this.dropItemStack();
	}

	@Override
	public void writeSpawnData(ByteBuf data) {
		BlockPos blockpos = this.getHangingPosition();
		data.writeInt(blockpos.getX());
		data.writeInt(blockpos.getY());
		data.writeInt(blockpos.getZ());
		data.writeByte(starfishType.ordinal());
		data.writeByte((byte)this.facingDirection.getHorizontalIndex());
	}

	@Override
	public void readSpawnData(ByteBuf data) {
		int x = data.readInt();
		int y = data.readInt();
		int z = data.readInt();
		BlockPos pos = new BlockPos(x, y, z);
		this.hangingPosition = pos;
		starfishType = StarfishType.values()[data.readByte()];
		this.facingDirection = EnumFacing.getHorizontal(data.readByte());
	}

	@Override
	public void playPlaceSound() {
		this.playSound(SoundEvents.BLOCK_WATERLILY_PLACE, 1.0F, 1.0F);
	}
}