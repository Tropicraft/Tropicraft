package net.tropicraft.core.common.block.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.common.TropicsConfigs;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.chunk.VolcanoGenerator;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.projectile.LavaBallEntity;
import net.tropicraft.core.common.volcano.VolcanoState;

import javax.annotation.Nullable;

public class VolcanoTileEntity extends TileEntity implements ITickableTileEntity
{

	private static final int RAND_DORMANT_DURATION = 4000;
	private static final int MAX_LAVA_LEVEL_DURING_RISE = VolcanoGenerator.VOLCANO_CRUST - 1;
	private static final int MAX_LAVA_LEVEL_DURING_ERUPTION = VolcanoGenerator.VOLCANO_CRUST + 1;
	private static final int LAVA_BASE_LEVEL = VolcanoGenerator.LAVA_LEVEL;
	private static final int LAVA_ERUPT_LEVEL = VolcanoGenerator.LAVA_LEVEL + 11;

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
	private int heightOffset = Integer.MIN_VALUE;

	public VolcanoTileEntity() {
		super(TropicraftTileEntityTypes.VOLCANO.get());
	}

	@Override
	public void tick() {
		if (!TropicsConfigs.allowVolcanoEruption)
		{
			return;
		}

		if (this.heightOffset == Integer.MIN_VALUE) {
			this.heightOffset = VolcanoGenerator.getHeightOffsetForBiome(this.getPos().getY());
		}

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

		if (radius == -1 || lavaLevel == -1) {
			return;
		}

		updateStates();

		switch(state) {
		case DORMANT:
			break;
		case ERUPTING:
			if (!getWorld().isRemote) {
				//	if ((ticksUntilRetreating % (getWorld().rand.nextInt(40) + 10) == 0)/* && time > 800 && !falling*/) {
				if (getWorld().rand.nextInt(15) == 0)
				{
					throwLavaFromCaldera(0.05 + Math.abs(getWorld().rand.nextGaussian()) * (lavaLevel > 90 ? LAVA_ERUPT_LEVEL + this.heightOffset : 0.75));
				}
				//	}

				//	if ((ticksUntilRetreating % (getWorld().rand.nextInt(40) + 10) == 0) && lavaLevel > 90) {
				if (getWorld().rand.nextInt(15) == 0)
				{
					throwLavaFromCaldera(0.05 + Math.abs(getWorld().rand.nextGaussian()) * (lavaLevel > LAVA_ERUPT_LEVEL + this.heightOffset ? 1 : 0.75));
				}
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
				if (lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION + this.heightOffset) {
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
				for (int y = LAVA_BASE_LEVEL + this.heightOffset; y < 140; y++) {
					BlockPos outBlockPos = new BlockPos(x, y, z);
					if (getWorld().getBlockState(outBlockPos).getBlock() == Blocks.LAVA) {
						getWorld().setBlockState(outBlockPos, Blocks.AIR.getDefaultState());
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
			getWorld().addEntity(new LavaBallEntity(TropicraftEntities.LAVA_BALL.get(), getWorld(), i, j, k, xMot, yMot, zMot));
		}
	}

	private void raiseLavaLevels() {
		if (lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION + this.heightOffset) {
			lavaLevel++;
			setBlocksOnLavaLevel(Blocks.LAVA.getDefaultState(), 3);	
		}
	}

	private void lowerLavaLevels() {
		if (lavaLevel > LAVA_BASE_LEVEL + this.heightOffset) {
			setBlocksOnLavaLevel(Blocks.AIR.getDefaultState(), 3);
			lavaLevel--;
		}
	}

	private void setBlocksOnLavaLevel(BlockState state, int updateFlag) {
		int xPos = this.pos.getX();
		int zPos = this.pos.getZ();

		for (int x = xPos - radius; x < xPos + radius; x++) {
			for (int z = zPos - radius; z < zPos + radius; z++) {
				if (Math.sqrt(Math.pow(x - xPos, 2) + Math.pow(z - zPos, 2)) < radius + 3) {
					BlockPos botPos = new BlockPos(x, 10, z);
					if (getWorld().getBlockState(botPos).getBlock() == Blocks.LAVA) {
						BlockPos pos2 = new BlockPos(x, lavaLevel, z);

						if (lavaLevel >= MAX_LAVA_LEVEL_DURING_RISE + this.heightOffset && lavaLevel < MAX_LAVA_LEVEL_DURING_ERUPTION + this.heightOffset) {
							if (getWorld().getBlockState(pos2).getBlock() != TropicraftBlocks.CHUNK.get()) {
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
			double x = this.pos.getX() + getWorld().rand.nextInt(radius) * (getWorld().rand.nextBoolean() ? -1 : 1);
			double y = this.lavaLevel + getWorld().rand.nextInt(6);
			double z = this.pos.getZ() + getWorld().rand.nextInt(radius) * (getWorld().rand.nextBoolean() ? -1 : 1);
			getWorld().addParticle(ParticleTypes.LARGE_SMOKE, true, x, y, z, 0.0D, 0.7, 0.0D);
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
		for(int y = LAVA_BASE_LEVEL + this.heightOffset; y < VolcanoGenerator.CHUNK_SIZE_Y; y++) {
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
		for (int x = 0; x < 60; x++) {
			if (getWorld().getBlockState(new BlockPos(x + this.pos.getX(), 10, this.pos.getZ())).getBlock() != Blocks.LAVA) {
				return x;
			}
		}

		return -1;
	}

	@Override
	public void read(CompoundNBT nbt) {
		super.read(nbt);
		state = VolcanoState.valueOf(nbt.getString("state"));
		ticksUntilDormant = nbt.getInt("ticksUntilDormant");
		ticksUntilSmoking = nbt.getInt("ticksUntilSmoking");
		ticksUntilRising = nbt.getInt("ticksUntilRising");
		ticksUntilEruption = nbt.getInt("ticksUntilEruption");
		ticksUntilRetreating = nbt.getInt("ticksUntilRetreating");
		lavaLevel = nbt.getInt("lavaLevel");
		radius = nbt.getInt("radius");
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		super.write(nbt);
		nbt.putString("state", state.name());
		nbt.putInt("ticksUntilDormant", ticksUntilDormant);
		nbt.putInt("ticksUntilSmoking", ticksUntilSmoking);
		nbt.putInt("ticksUntilRising", ticksUntilRising);
		nbt.putInt("ticksUntilEruption", ticksUntilEruption);
		nbt.putInt("ticksUntilRetreating", ticksUntilRetreating);
		nbt.putInt("lavaLevel", lavaLevel);
		nbt.putInt("radius", radius);

		return nbt;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		read(pkt.getNbtCompound());
	}

	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}
}