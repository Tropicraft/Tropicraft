package net.tropicraft.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.tropicraft.block.tileentity.TileEntityBambooChest;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;
//import net.tropicraft.block.tileentity.TileEntityBambooChest; TODO
//import net.tropicraft.item.TropicraftItems; TODO

public class TeleporterTropics extends Teleporter {

	private static Block PORTAL_WALL_BLOCK;
	private static Block PORTAL_BLOCK;
	
	private final WorldServer world;
	private final Random random;

	/** Stores successful portal placement locations for rapid lookup. */
	private final LongHashMap destinationCoordinateCache = new LongHashMap();

	/**
	 * A list of valid keys for the destinationCoordainteCache. These are based on the X & Z of the players initial
	 * location.
	 */
	private final List destinationCoordinateKeys = new ArrayList();

	public TeleporterTropics(WorldServer world) {
		super(world);
		PORTAL_BLOCK = TCBlockRegistry.tropicsPortal;
		PORTAL_WALL_BLOCK = TCBlockRegistry.tropicsPortalWall;
		this.world = world;
		this.random = new Random(world.getSeed());
	}

	@Override
	public void placeInPortal(Entity entity, double d, double d2, double d3, float f) {
		long startTime = System.currentTimeMillis();
		if (!placeInExistingPortal(entity, d, d2, d3, f)) {
			makePortal(entity);
			placeInExistingPortal(entity, d, d2, d3, f);
		}

		long finishTime = System.currentTimeMillis();

		System.out.printf("It took %f seconds for TeleporterTropics.placeInPortal to complete\n", (finishTime - startTime) / 1000.0F);
	}

	@Override
	public boolean placeInExistingPortal(Entity entity, double d, double d2, double d3, float f)
	{
		int searchArea = 148;
		double closestPortal = -1D;
		int foundX = 0;
		int foundY = 0;
		int foundZ = 0;
		int entityX = MathHelper.floor_double(entity.posX);
		int entityZ = MathHelper.floor_double(entity.posZ);
		boolean notInCache = true;

		long j1 = ChunkCoordIntPair.chunkXZ2Int(entityX, entityZ);

		if (destinationCoordinateCache.containsItem(j1)) {
			//	System.out.println("Setting closest portal to 0");
			PortalPosition portalposition = (PortalPosition)destinationCoordinateCache.getValueByKey(j1);
			closestPortal = 0.0D;
			foundX = portalposition.posX;
			foundY = portalposition.posY;
			foundZ = portalposition.posZ;
			portalposition.lastUpdateTime = world.getTotalWorldTime();
			notInCache = false;
		} else {
			for (int x = entityX - searchArea; x <= entityX + searchArea; x ++)
			{
				double distX = x + 0.5D - entity.posX;

				for (int z = entityZ - searchArea; z <= entityZ + searchArea; z ++)
				{
					double distZ = z + 0.5D - entity.posZ;

					for (int y = world.getActualHeight() - 1; y >= 0; y--)
					{
						if (world.getBlock(x, y, z) == PORTAL_BLOCK)
						{

							while (world.getBlock(x, y - 1, z) == PORTAL_BLOCK)
							{
								--y;
							}

							double distY = y + 0.5D - entity.posY;
							double distance = distX * distX + distY * distY + distZ * distZ;
							if (closestPortal < 0.0D || distance < closestPortal)
							{
								closestPortal = distance;
								foundX = x;
								foundY = y;
								foundZ = z;
							}
						}
					}
				}
			}
		}

		//	System.out.println("Setting closest portal to " + closestPortal);

		if (closestPortal >= 0.0D)
		{
			if (notInCache) {
				destinationCoordinateCache.add(j1, new PortalPosition(foundX, foundY, foundZ, world.getTotalWorldTime()));
				destinationCoordinateKeys.add(Long.valueOf(j1));
			}

			int x = foundX;
			int y = foundY;
			int z = foundZ;
			double newLocX = x + 0.5D;
			double newLocY = y + 0.5D;
			double newLocZ = z + 0.5D;

			if (world.getBlock(x - 1, y, z) == PORTAL_BLOCK)
			{
				newLocX -= 0.5D;
			}
			if (world.getBlock(x + 1, y, z) == PORTAL_BLOCK)
			{
				newLocX += 0.5D;
			}
			if (world.getBlock(x, y, z - 1) == PORTAL_BLOCK)
			{
				newLocZ -= 0.5D;
			}
			if (world.getBlock(x, y, z + 1) == PORTAL_BLOCK)
			{
				newLocZ += 0.5D;
			}
			entity.setLocationAndAngles(newLocX, newLocY + 2, newLocZ, entity.rotationYaw, 0.0F);
			int worldSpawnX = MathHelper.floor_double(newLocX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
			int worldSpawnZ = MathHelper.floor_double(newLocZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
			int worldSpawnY = world.getHeightValue(worldSpawnX, worldSpawnZ) + 3;

			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			// If the player is entering the tropics, spawn an Encyclopedia Tropica
			// in the spawn portal chest (if they don't already have one AND one isn't
			// already in the chest)
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				if (world.provider instanceof WorldProviderTropicraft) {
					//TODO improve this logical check to an NBT tag or something?
					if (!player.inventory.hasItem(TCItemRegistry.encTropica)) {
						// Search for the spawn chest
						TileEntityBambooChest chest = null;
						int chestX = MathHelper.floor_double(newLocX);
						int chestZ = MathHelper.floor_double(newLocZ);
						chestSearch:
							for (int searchX = -3; searchX < 4; searchX++) {
								for (int searchZ = -3; searchZ < 4; searchZ++) {
									for (int searchY = -4; searchY < 5; searchY++) {
										if (world.getBlock(chestX + searchX, worldSpawnY + searchY, chestZ + searchZ) == TCBlockRegistry.bambooChest) {
											chest = (TileEntityBambooChest)world.getTileEntity(chestX + searchX, worldSpawnY + searchY, chestZ + searchZ);
											if (chest != null && chest.isUnbreakable()) {
												break chestSearch;
											}
										}
									}
								}
							}

						// Make sure chest doesn't have the encyclopedia
						if (chest!= null && chest.isUnbreakable()) {
							boolean hasEncyclopedia = false;
							for (int inv = 0; inv < chest.getSizeInventory(); inv++) {
								ItemStack stack = chest.getStackInSlot(inv);
								if (stack != null && stack.getItem() == TCItemRegistry.encTropica) {
									hasEncyclopedia = true;
								}
							}

							// Give out a new encyclopedia
							if (!hasEncyclopedia) {
								for (int inv = 0; inv < chest.getSizeInventory(); inv++) {
									ItemStack stack = chest.getStackInSlot(inv);
									if (stack == null) {
										chest.setInventorySlotContents(inv, new ItemStack(TCItemRegistry.encTropica, 1));
										break;
									}
								}
							}
						}
					}
				}
			}

			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean makePortal(Entity entity)
	{
		int searchArea = 16;
		double closestSpot = -1D;
		int entityX = MathHelper.floor_double(entity.posX);
		int entityY = MathHelper.floor_double(entity.posY);
		int entityZ = MathHelper.floor_double(entity.posZ);
		int foundX = entityX;
		int foundY = entityY;
		int foundZ = entityZ;

		for (int x = entityX - searchArea; x <= entityX + searchArea; x++) {
			double distX = (x + 0.5D) - entity.posX;
			nextCoords:
				for (int z = entityZ - searchArea; z <= entityZ + searchArea; z++) {
					double distZ = (z + 0.5D) - entity.posZ;

					// Find topmost solid block at this x,z location
					int y = world.getHeight() - 1;
					for (; y >= 63 - 1 && (world.getBlock(x, y, z) == Blocks.air ||
							!world.getBlock(x, y, z).isOpaqueCube()); y--) {
						;
					}
					// Only generate portal between sea level and sea level + 20
					if (y > 63 + 20 || y < 63) {
						continue;
					}

					if (getValidBuildBlocks().contains(world.getBlock(x, y, z))) {
						for (int xOffset = -2; xOffset <= 2; xOffset++) {
							for (int zOffset = -2; zOffset <= 2; zOffset++) {
								int otherY = world.getHeight() - 1;
								for (; otherY >= 63 && (world.getBlock(x + xOffset, otherY, z + zOffset) == Blocks.air ||
										!world.getBlock(x, y, z).isOpaqueCube()); otherY--) {
									;
								}
								if (Math.abs(y - otherY) >= 3) {
									continue nextCoords;
								}

								if (!getValidBuildBlocks().contains(world.getBlock(x + xOffset, otherY, z + zOffset))) {
									continue nextCoords;
								}
							}
						}

						double distY = (y + 0.5D) - entity.posY;
						double distance = distX * distX + distY * distY + distZ * distZ;
						if (closestSpot < 0.0D || distance < closestSpot)
						{
							closestSpot = distance;
							foundX = x;
							foundY = y;
							foundZ = z;

						}
					}
				}
		}

		int worldSpawnX = MathHelper.floor_double(foundX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
		int worldSpawnZ = MathHelper.floor_double(foundZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
		int worldSpawnY = getTerrainHeightAt(worldSpawnX, worldSpawnZ);//world.getHeightValue(worldSpawnX, worldSpawnZ) - 2;
		
	      // If we can't find a spot (e.g. we're in the middle of the ocean),
        // just put the portal at sea level
        if(closestSpot < 0.0D) {
            // Perhaps this was the culprit
            /*  Random r = new Random();
            foundX += r.nextInt(16) - 8;
            foundZ += r.nextInt(16) - 8;*/
            foundY = worldSpawnY - 2;
        }

        //      System.out.printf("Buliding teleporter at x:<%d>, y:<%d>, z:<%d>\n", foundX, foundY, foundZ);

        entity.setLocationAndAngles(foundX, foundY + 2, foundZ, entity.rotationYaw, 0.0F);
		buildTeleporterAt(worldSpawnX, worldSpawnY + 1, worldSpawnZ, entity);

		return true;
	}
	
	/**
     * Gets the terrain height at the specified coordinates
     * @param x The x coordinate
     * @param z The z coordinate
     * @return The terrain height at the specified coordinates
     */
    public int getTerrainHeightAt(int x, int z) {
        for(int y = 100; y > 0; y--)
        {
            Block block = world.getBlock(x, y, z);
            if(block == Blocks.dirt || block == Blocks.grass || block == Blocks.sand || block == Blocks.stone ||
                    block == TCBlockRegistry.tropicsWater || block == TCBlockRegistry.purifiedSand)
            {
                return y;
            }
        }
        return 0;
    }

	public void buildTeleporterAt(int x, int y, int z, Entity entity) {
		y = y < 9 ? 9 : y;

		for (int yOffset = 4; yOffset >= -7; yOffset--) {
			for (int zOffset = -2; zOffset <= 2; zOffset++) {
				for (int xOffset = -2; xOffset <= 2; xOffset++) {
					int blockX = x + xOffset;
					int blockY = y + yOffset;
					int blockZ = z + zOffset;

					if (yOffset == -7) {
						// Set bottom of portal to be solid
						world.setBlock(blockX, blockY, blockZ, PORTAL_WALL_BLOCK);
					} else if (yOffset > 0) {
						// Set 4 blocks above portal to air
						world.setBlock(blockX, blockY, blockZ, Blocks.air);
					} else {
						boolean isWall = xOffset == -2 || xOffset == 2 || zOffset == -2 || zOffset == 2;
						if (isWall) {
							// Set walls around portal
							world.setBlock(blockX, blockY, blockZ, PORTAL_WALL_BLOCK);
						} else {
							// Set inside of portal
							int metadata = yOffset <= -5 ? 8 : 0;
							world.setBlock(blockX, blockY, blockZ, PORTAL_BLOCK, metadata, 3);
						}
					}

					boolean isCorner = (xOffset == -2 || xOffset == 2) && (zOffset == -2 || zOffset == 2);
					if (yOffset == 0 && isCorner) {
						world.setBlock(blockX, blockY + 1, blockZ, TCBlockRegistry.tikiTorch, 1, 3);
						world.setBlock(blockX, blockY + 2, blockZ, TCBlockRegistry.tikiTorch, 1, 3);
						world.setBlock(blockX, blockY + 3, blockZ, TCBlockRegistry.tikiTorch, 0, 3);
					}

				}
			}
		}

		// Add chest
		// Add an unbreakable chest to place encyclopedia in
		// NOTE: using instanceof instead of world.getWorldInfo().getDimension()
		// because getWorldInfo() may not be set/correct yet
		if (world.provider instanceof WorldProviderTropicraft) {
			world.setBlock(x + 2, y + 1, z, TCBlockRegistry.bambooChest, 1, 3);
			TileEntityBambooChest tile = (TileEntityBambooChest)world.getTileEntity(x + 2, y + 1, z);
			if (tile != null) {
				tile.setIsUnbreakable(true);
			}
		}

		for (int yOffset = 5; yOffset >= -7; yOffset--) {
			for (int zOffset = -2; zOffset <= 2; zOffset++) {
				for (int xOffset = -2; xOffset <= 2; xOffset++) {
					int blockX = x + xOffset;
					int blockY = y + yOffset;
					int blockZ = z + zOffset;
					world.notifyBlocksOfNeighborChange(blockX, blockY, blockZ, world.getBlock(blockX, blockY, blockZ));
				}
			}
		}

	}

	/**
	 * called periodically to remove out-of-date portal locations from the cache list. Argument par1 is a
	 * WorldServer.getTotalWorldTime() value.
	 */
	@Override
	public void removeStalePortalLocations(long par1)
	{
		if (par1 % 100L == 0L)
		{
			Iterator iterator = destinationCoordinateKeys.iterator();
			long j = par1 - 600L;

			while (iterator.hasNext())
			{
				Long olong = (Long)iterator.next();
				PortalPosition portalposition = (PortalPosition)destinationCoordinateCache.getValueByKey(olong.longValue());

				if (portalposition == null || portalposition.lastUpdateTime < j)
				{
					iterator.remove();
					destinationCoordinateCache.remove(olong.longValue());
				}
			}
		}
	}

	/**
	 * @return List of valid block ids to build portal on
	 */
	private List<Block> getValidBuildBlocks() {
		return Arrays.asList(Blocks.sand, Blocks.grass, Blocks.dirt, TCBlockRegistry.purifiedSand);
	}
}
