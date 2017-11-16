package net.tropicraft.core.common.block.tileentity;

import javax.annotation.Nullable;

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
import net.tropicraft.configuration.GenRates;
import net.tropicraft.core.common.config.TropicsConfigs;
import net.tropicraft.core.common.entity.EntityLavaBall;
import net.tropicraft.core.common.volcano.VolcanoState;
import net.tropicraft.core.common.worldgen.mapgen.MapGenVolcano;
import net.tropicraft.core.registry.BlockRegistry;

public class TileEntityVolcano extends TileEntity implements ITickable {

	private static final int RAND_DORMANT_DURATION = 4000;
	private static final int MAX_LAVA_LEVEL_DURING_RISE = MapGenVolcano.VOLCANO_CRUST - 1;
	private static final int MAX_LAVA_LEVEL_DURING_ERUPTION = MapGenVolcano.VOLCANO_CRUST + 1;
	private static final int LAVA_BASE_LEVEL = 79;

	private int ticksUntilEruption = VolcanoState.getTimeBefore(VolcanoState.ERUPTING);
	private int ticksUntilSmoking = VolcanoState.getTimeBefore(VolcanoState.SMOKING);
	private int ticksUntilRetreating = VolcanoState.getTimeBefore(VolcanoState.RETREATING);
	private int ticksUntilDormant = VolcanoState.getTimeBefore(VolcanoState.DORMANT);
	private int ticksUntilRising = VolcanoState.getTimeBefore(VolcanoState.RISING);

	/** How high the lava is during the rising phase */
	private int lavaLevel = -1;

	/** Volcano radius */
	private int radius = -1;

	private VolcanoState state = VolcanoState.DORMANT;

	public TileEntityVolcano() {

	}

	@Override
	public void update() {
		if (!TropicsConfigs.allowVolcanoEruption) return;

		if (!getWorld().isRemote) {
			//System.out.println(radius + " Volcano Update: " + pos.getX() + " " + pos.getZ() + " State:" + state + " lvl: " + lavaLevel);
			//System.out.println("smoking: " + ticksUntilSmoking + " rising: " + ticksUntilRising + " eruption: " + ticksUntilEruption + " retreating: " + ticksUntilRetreating + " dormant: " + ticksUntilDormant);	
		}

		// If radius needs to be initialized
		if (radius == -1) {
			radius = findRadius();
		}

		if (lavaLevel == -1) {
			setLavaLevel();
		}

		updateStates();

		switch(state) {
		case DORMANT:
			break;
		case ERUPTING:
			if (!getWorld().isRemote) {
				//	if ((ticksUntilRetreating % (getWorld().rand.nextInt(40) + 10) == 0)/* && time > 800 && !falling*/) {
				if (getWorld().rand.nextInt(15) == 0)
					throwLavaFromCaldera(0.05 + Math.abs(getWorld().rand.nextGaussian()) * (lavaLevel > 90 ? 1 : 0.75));
				//	}

				//	if ((ticksUntilRetreating % (getWorld().rand.nextInt(40) + 10) == 0) && lavaLevel > 90) {
				if (getWorld().rand.nextInt(15) == 0)
					throwLavaFromCaldera(0.05 + Math.abs(getWorld().rand.nextGaussian()) * (lavaLevel > 90 ? 1 : 0.75));
				//	}
			}
			break;
		case RETREATING:
			if (ticksUntilDormant % 30 == 0) {
				lowerLavaLevels();
			}
			break;
		case RISING:
			if (getWorld().isRemote) {
				spewSmoke();
			}

			if (ticksUntilEruption % 20 == 0) {
				if (lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION) {
					raiseLavaLevels();	
				} else {
					ticksUntilEruption = 0;
					getWorld().playSound(this.pos.getX(), 73, this.pos.getY(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1.0F, getWorld().rand.nextFloat() / 4 + 0.825F, false);
					int balls = getWorld().rand.nextInt(25) + 15;

					for (int i = 0; i < balls; i++) {
						throwLavaFromCaldera((getWorld().rand.nextDouble() * 0.5) + 1.25);
					}
					break;
				}
			}
			break;
		case SMOKING:
			// TODO: Client only in the future if this is particles
			if (getWorld().isRemote) {
				//if (ticksUntilRising % 100 == 0) {
				//if (getWorld().rand.nextInt(10) == 0)
				spewSmoke();
				//}
			}
			break;
		default:
			break;
		}
	}

	public void cleanUpFromEruption() {
		int xPos = this.pos.getX();
		int zPos = this.pos.getZ();

		for (int x = xPos - (radius * 2); x < xPos + (radius * 2); x++) {
			for (int z = zPos - (radius * 2); z < zPos + (radius * 2); z++) {
				for (int y = LAVA_BASE_LEVEL; y < 140; y++) {
					BlockPos outBlockPos = new BlockPos(x, y, z);
					if (getWorld().getBlockState(outBlockPos).getBlock() == Blocks.LAVA) {
						getWorld().setBlockToAir(outBlockPos);
					}
				}
			}
		}
	}

	public void throwLavaFromCaldera(double force) {
		// Create vector at center facing in the +x direction
		Vec3d pos = new Vec3d(((getWorld().rand.nextDouble() / 2) + 0.3) * radius, lavaLevel + 2, 0);
		// Get a random angle from 0 to 2PI (radians)
		float angle = getWorld().rand.nextFloat() * (float) Math.PI * 2;
		// Rotate the center vector to this angle, and offset it to the volcano's position
		pos = pos.rotateYaw(angle).add(new Vec3d(getPos()));
		// Compute x/y components of angle
		double motX = force * Math.cos(angle);
		double motZ = force * Math.sin(-angle);
		throwLava(pos.x, pos.y, pos.z, motX, 0.86, motZ);
	}

	public void throwLava(double i, double j, double k, double xMot, double yMot, double zMot) {
		if (!getWorld().isRemote) {
			getWorld().spawnEntity(new EntityLavaBall(getWorld(), i, j, k, xMot, yMot, zMot));	
		}
	}

	private void raiseLavaLevels() {
		if (lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION) {
			lavaLevel++;
			setBlocksOnLavaLevel(Blocks.LAVA.getDefaultState(), 3);	
		}
	}

	private void lowerLavaLevels() {
		if (lavaLevel > LAVA_BASE_LEVEL) {
			setBlocksOnLavaLevel(Blocks.AIR.getDefaultState(), 3);
			lavaLevel--;
		}
	}

	private void setBlocksOnLavaLevel(IBlockState state, int updateFlag) {
		int xPos = this.pos.getX();
		int zPos = this.pos.getZ();

		for (int x = xPos - radius; x < xPos + radius; x++) {
			for (int z = zPos - radius; z < zPos + radius; z++) {
				if (Math.sqrt(Math.pow(x - xPos, 2) + Math.pow(z - zPos, 2)) < radius + 3) {
					BlockPos botPos = new BlockPos(x, 10, z);
					if (getWorld().getBlockState(botPos).getBlock() == Blocks.LAVA) {
						BlockPos pos2 = new BlockPos(x, lavaLevel, z);

						if (lavaLevel >= MAX_LAVA_LEVEL_DURING_RISE && lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION) {
							if (getWorld().getBlockState(pos2).getBlock() != BlockRegistry.chunk) {
								getWorld().setBlockState(pos2, state, updateFlag);
							}
						} else {
							getWorld().setBlockState(pos2, state, updateFlag);
							// System.out.println("Setting block " + x + " " + lavaLevel + " " + z + " to whatever");
						}
						//getWorld().setBlockWithNotify(x, lavaLevel, z, falling ? 0 : lavaLevel >= 95 ? TropicraftMod.tempLavaMoving.blockID : Block.lavaStill.blockID);
					}
				}
			}
		}

		//		if (updateFlag == 0) {
		//			getWorld().markBlockRangeForRenderUpdate(xPos - radius, lavaLevel, zPos - radius, xPos + radius, lavaLevel, zPos + radius);
		//		}
	}

	public void spewSmoke() {
		// System.out.println("Spewing smoke");
		int n = getWorld().rand.nextInt(100) + 4;
		for (int i = 0; i < n; i++) {
			// getWorld().spawnEntity(new EntitySmoke(getWorld(), xPos + rand.nextInt(10) - 5, lavaLevel + rand.nextInt(4), zPos + rand.nextInt(10) - 5));
			int x = this.pos.getX() + getWorld().rand.nextInt(radius) * (getWorld().rand.nextBoolean() ? -1 : 1);
			int y = this.lavaLevel + getWorld().rand.nextInt(6);
			int z = this.pos.getZ() + getWorld().rand.nextInt(radius) * (getWorld().rand.nextBoolean() ? -1 : 1);
			getWorld().spawnParticle(EnumParticleTypes.SMOKE_LARGE, true, x, y, z, 0, 0.7, 0, 0);
			//System.out.println("Spewing smoke " + x + " " + z);
		}
	}

	private void updateStates() {
		switch (state) {
		// If volcano is dormant, count down to smoking
		case DORMANT:
			ticksUntilSmoking--;

			// If it's time to smoke, then let's smoke
			if (ticksUntilSmoking <= 0) {
				state = VolcanoState.SMOKING;
				ticksUntilSmoking = VolcanoState.getTimeBefore(VolcanoState.SMOKING);
			}
			break;
			// If volcano is smoking, count down to raising lava
		case SMOKING:
			ticksUntilRising--;

			// If it's time to raise lava, then raise the roof...of lava!
			if (ticksUntilRising <= 0) {
				state = VolcanoState.RISING;
				ticksUntilRising = VolcanoState.getTimeBefore(VolcanoState.RISING);
			}
			break;
			// If volcano is done smoking, raise up lava levels
		case RISING:
			ticksUntilEruption--;

			// If it's time to erupt, then LET'S ERUPT
			if (ticksUntilEruption <= 0) {
				state = VolcanoState.ERUPTING;
				ticksUntilEruption = VolcanoState.getTimeBefore(VolcanoState.ERUPTING);
			}
			break;
			// If lava is done rising, ERUPT
		case ERUPTING:
			ticksUntilRetreating--;

			// If it's time to become dormant, then let's chill, yo
			if (ticksUntilRetreating <= 0) {
				state = VolcanoState.RETREATING;
				ticksUntilRetreating = VolcanoState.getTimeBefore(VolcanoState.RETREATING);
			}
			break;
			// If eruption is complete, lower lava levels
		case RETREATING:
			ticksUntilDormant--;

			// If it's time to go to sleep, then let's do it
			if (ticksUntilDormant <= 0) {
				// cleanUpFromEruption();
				state = VolcanoState.DORMANT;
				ticksUntilDormant = VolcanoState.getTimeBefore(VolcanoState.DORMANT) + getWorld().rand.nextInt(RAND_DORMANT_DURATION);
			}
			break;
		default:
			break;
		}
	}

	private void setLavaLevel() {
		for(int y = LAVA_BASE_LEVEL; y < 128; y++) {
			BlockPos pos2 = new BlockPos(this.pos.getX(), y, this.pos.getZ());
			//if(getWorld().getBlockState(pos).getBlock() != Blocks.LAVA && getWorld().getBlockId(xPos, y, zPos) != TropicraftMod.tempLavaMoving.blockID) {\
			if (getWorld().getBlockState(pos2).getMaterial() != Material.LAVA) {
				lavaLevel = y - 1;
				return;
			}
		}
	}

	/**
	 * Calculate the radius of this volcano
	 * @return the number of lava blocks going outwards in the +x direction from this block
	 */
	private int findRadius() {
		for (int x = 0; x < 20; x++) {
			if (getWorld().getBlockState(new BlockPos(x + this.pos.getX(), 10, this.pos.getZ())).getBlock() != Blocks.LAVA) {
				return x;
			}
		}

		return -1;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		state = VolcanoState.valueOf(nbt.getString("state"));
		ticksUntilDormant = nbt.getInteger("ticksUntilDormant");
		ticksUntilSmoking = nbt.getInteger("ticksUntilSmoking");
		ticksUntilRising = nbt.getInteger("ticksUntilRising");
		ticksUntilEruption = nbt.getInteger("ticksUntilEruption");
		ticksUntilRetreating = nbt.getInteger("ticksUntilRetreating");
		lavaLevel = nbt.getInteger("lavaLevel");
		radius = nbt.getInteger("radius");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("state", state.name());
		nbt.setInteger("ticksUntilDormant", ticksUntilDormant);
		nbt.setInteger("ticksUntilSmoking", ticksUntilSmoking);
		nbt.setInteger("ticksUntilRising", ticksUntilRising);
		nbt.setInteger("ticksUntilEruption", ticksUntilEruption);
		nbt.setInteger("ticksUntilRetreating", ticksUntilRetreating);
		nbt.setInteger("lavaLevel", lavaLevel);
		nbt.setInteger("radius", radius);

		return nbt;
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
