package net.tropicraft.core.common.block.tileentity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.common.config.TropicsConfigs;
import net.tropicraft.core.common.entity.EntityLavaBall;
import net.tropicraft.core.common.volcano.VolcanoState;
import net.tropicraft.core.common.worldgen.mapgen.MapGenVolcano;
import net.tropicraft.core.registry.BlockRegistry;

import javax.annotation.Nullable;

public class TileEntityDonation extends TileEntity implements ITickable {



	public TileEntityDonation() {

	}

	@Override
	public void update() {

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		return nbt;
	}

	public void triggerDonation() {
		System.out.println("tile trigger");
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public boolean receiveClientEvent(int id, int type) {
		return super.receiveClientEvent(id, type);
	}
}
