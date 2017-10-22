package net.tropicraft.core.common.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.BlockTropicraftLeaves;
import net.tropicraft.core.common.block.BlockTropicraftLog;
import net.tropicraft.core.common.enums.TropicraftLeaves;
import net.tropicraft.core.common.enums.TropicraftLogs;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;

public class WorldGenHomeTree extends TCGenBase {

	private class BranchNode 
	{
		public int x1, y1, z1;
		public int x2, y2, z2;

		public BranchNode(int i, int j, int k, int x, int y, int z)
		{
			x1 = i;
			y1 = j;
			z1 = k;
			x2 = x;
			y2 = y;
			z2 = z;
		}
	}

	private final IBlockState wood = BlockRegistry.logs.getDefaultState().withProperty(BlockTropicraftLog.VARIANT, TropicraftLogs.MAHOGANY);
	private final IBlockState leaves = BlockRegistry.leaves.getDefaultState().withProperty(BlockTropicraftLeaves.VARIANT, TropicraftLeaves.KAPOK);

	private ArrayList<BranchNode> branchList = new ArrayList<BranchNode>();
	private int trunkRadius;

	public WorldGenHomeTree(World world, Random random) {
		super(world, random);
	}

	@Override
	public boolean generate(BlockPos pos) {
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();

		j = 127;

		trunkRadius = rand.nextInt(3) + 7;

		for(int x = i - trunkRadius; x < i + trunkRadius; x++) {
			for(int z = k - trunkRadius; z < k + trunkRadius; z++) {
				int tHeight = getTerrainHeightAt(x, z);
				if(tHeight < j) {
					j = tHeight;
				}
			}
		}

		/*		BiomeGenBase currentBiome = worldObj.getWorldChunkManager().getBiomeGenAt(i, k);
		if(!(currentBiome instanceof BiomeGenRainforest)) {
			return false;
		}*/

		int height = rand.nextInt(20) + 30;

		if (height + j + 12 > 255) {
			return false;
		}

		System.err.println("HOME TREE INCOMING!" + " " + i + " " + j + " " + k);

		int[] top = generateTrunk(i, j, k, height);

		generateBranches(top[0], top[1], height + j);
		return true;
	}

	public int[] generateTrunk(int i, int j, int k, int height) {
		int bn = 0;
		int chanceToDisplace = 0;
		int xDisplace = rand.nextBoolean() ? 1 : -1;
		int zDisplace = rand.nextBoolean() ? 1 : -1;

		int trunkX = i;
		int trunkZ = k;

		for (int y = j; y < height + j; y++) {
			chanceToDisplace++;
			genCircle(trunkX, y, trunkZ, trunkRadius, trunkRadius - 3, wood, false);
			if(y == height + j - 1 || ((y - j) % 6 == 0)) {
				genCircle(trunkX, y, trunkZ, trunkRadius, 2, wood, false);
				placeBlock(trunkX + 1, y, trunkZ + 1, wood, false);
				placeBlock(trunkX + 1, y, trunkZ - 1, wood, false);
				placeBlock(trunkX - 1, y, trunkZ + 1, wood, false);
				placeBlock(trunkX - 1, y, trunkZ - 1, wood, false);
				double angle = rand.nextDouble() * 3.141592D * 2D;
				if (rand.nextInt(3) == 0) {
					double length = rand.nextDouble() * trunkRadius - 4;
					int spawnerX = trunkX - 4 + rand.nextInt(9);
					int spawnerZ = trunkZ - 4 + rand.nextInt(9);
					setBlock(spawnerX, y + 1, spawnerZ, Blocks.MOB_SPAWNER);
					StringBuilder sb = new StringBuilder(String.format("%s.", Info.MODID));
					sb.append(rand.nextBoolean() ? "ashen" : "iguana");
					BlockPos spawnerPos = new BlockPos(spawnerX, y + 1, spawnerZ);
					TileEntityMobSpawner spawner = ((TileEntityMobSpawner)worldObj.getTileEntity(spawnerPos));
					if (spawner != null) {
						spawner.getSpawnerBaseLogic().setEntityName(sb.toString());
					}
				}
			}
			IBlockState vineState = Blocks.VINE.getDefaultState().withProperty(BlockVine.SOUTH, Boolean.valueOf(true));
			placeBlock(trunkX, y, trunkZ, wood, false);
			placeBlock(trunkX + 1, y, trunkZ, vineState, false);
			placeBlock(trunkX - 1, y, trunkZ, vineState, false);
			placeBlock(trunkX, y, trunkZ + 1, vineState, false);
			placeBlock(trunkX, y, trunkZ - 1, vineState, false);
			if (rand.nextInt(6) == 0); {
				if (y >= j + height - 6) {
					int branches = rand.nextInt(4) + 2;
					for (int x = 0; x < branches; x++) {
						int branchLength = rand.nextInt(10) + 15;
						int branchX1 = trunkX;
						int branchZ1 = trunkZ;
						double angle = rand.nextDouble() * 3.141592D * 2D;
						branchX1 = (int)((trunkRadius * Math.sin(angle)) + branchX1);
						branchZ1 = (int)((trunkRadius * Math.cos(angle)) + branchZ1); 
						int branchX2 = (int)((branchLength * Math.sin(angle)) + branchX1);
						int branchZ2 = (int)((branchLength * Math.cos(angle)) + branchZ1); 
						int branchY2 = rand.nextInt(4) + 4;
						branchList.add(new BranchNode(branchX1, y, branchZ1, branchX2, y + branchY2, branchZ2));
						bn++;
					}
				}
			}
			if (rand.nextInt(6) + 4 <= chanceToDisplace && chanceToDisplace * 9 > y) {
				if (rand.nextBoolean()) {
					trunkX += xDisplace;
					if (rand.nextBoolean()) {
						trunkZ += zDisplace;
					}
				} else if (rand.nextBoolean()) {
					trunkZ += zDisplace;
					if (rand.nextBoolean()) {
						trunkZ += xDisplace;
					}
				}
				chanceToDisplace = 0;
			}
			placeBlock(trunkX, y, trunkZ, BlockRegistry.logs.getDefaultState(), false);
		}
		setBlock(trunkX - 1, height + j, trunkZ - 1, BlockRegistry.bambooChest);
		BlockPos trunkPos = new BlockPos(trunkX - 1, height + j, trunkZ - 1);
		TileEntityChest chest = (TileEntityChest)worldObj.getTileEntity(trunkPos);
		if (chest != null) {
			int treasure = rand.nextInt(6) + 4;
			for (int x = 0; x < treasure; x++) {
				chest.setInventorySlotContents(rand.nextInt(chest.getSizeInventory()), randLoot());
			}
		}
		return new int[] {trunkX, trunkZ};
	}

	public void generateBranches(int topX, int topZ, int height) {
		for(int x = 0; x < branchList.size(); x++) {
			BranchNode bnode = branchList.get(x);
			int lSize = 3;
			if(!checkBlockLine(new int[] { bnode.x1, bnode.y1, bnode.z1 }, new int[] {bnode.x2, bnode.y2, bnode.z2 }, this.standardAllowedBlocks) && !checkBlockLine(new int[] { bnode.x1 + 1, bnode.y1, bnode.z1 }, new int[] {bnode.x2 + 1, bnode.y2, bnode.z2 }, this.standardAllowedBlocks) && !checkBlockLine(new int[] { bnode.x1 - 1, bnode.y1, bnode.z1 }, new int[] {bnode.x2 - 1, bnode.y2, bnode.z2 }, this.standardAllowedBlocks) && !checkBlockLine(new int[] { bnode.x1, bnode.y1, bnode.z1 + 1 }, new int[] {bnode.x2, bnode.y2, bnode.z2 + 1 }, this.standardAllowedBlocks) && !checkBlockLine(new int[] { bnode.x1, bnode.y1, bnode.z1 - 1 }, new int[] {bnode.x2, bnode.y2, bnode.z2 - 1 }, this.standardAllowedBlocks) && !checkBlockLine(new int[] { bnode.x1, bnode.y1 - 1, bnode.z1 }, new int[] {bnode.x2, bnode.y2 - 1, bnode.z2 }, this.standardAllowedBlocks)) {
				continue;
			}
			placeBlockLine(new int[] { bnode.x1, bnode.y1, bnode.z1 }, new int[] {bnode.x2, bnode.y2, bnode.z2 }, wood);
			placeBlockLine(new int[] { bnode.x1 + 1, bnode.y1, bnode.z1 }, new int[] {bnode.x2 + 1, bnode.y2, bnode.z2 }, wood);
			placeBlockLine(new int[] { bnode.x1 - 1, bnode.y1, bnode.z1 }, new int[] {bnode.x2 - 1, bnode.y2, bnode.z2 }, wood);
			placeBlockLine(new int[] { bnode.x1, bnode.y1, bnode.z1 + 1 }, new int[] {bnode.x2, bnode.y2, bnode.z2 + 1 }, wood);
			placeBlockLine(new int[] { bnode.x1, bnode.y1, bnode.z1 - 1 }, new int[] {bnode.x2, bnode.y2, bnode.z2 - 1 }, wood);
			placeBlockLine(new int[] { bnode.x1, bnode.y1 - 1, bnode.z1 }, new int[] {bnode.x2, bnode.y2 - 1, bnode.z2 }, wood);
			if(bnode.y2 + 1 <= height) {
				placeBlockLine(new int[] { bnode.x1, bnode.y1 + 1, bnode.z1 }, new int[] {bnode.x2, bnode.y2 + 1, bnode.z2 }, wood);
			}
			genLeafCircle(bnode.x2, bnode.y2 - 1, bnode.z2, lSize + 5, lSize + 3, leaves, true);
			genLeafCircle(bnode.x2, bnode.y2, bnode.z2, lSize + 6, 0, leaves, true);
			genLeafCircle(bnode.x2, bnode.y2 + 1, bnode.z2, lSize + 10, 0, leaves, true);
			genLeafCircle(bnode.x2, bnode.y2 + 2, bnode.z2, lSize + 9, 0, leaves, true);
		}

		int topBranches = rand.nextInt(6) + 6;
		/*for(int x = 0; x < topBranches; x++)
		{
			int branchLength = rand.nextInt(10) + 15;
			int baseDistance = rand.nextInt(trunkRadius - 3);
			int branchX1 = topX;
			int branchZ1 = topZ;
			double angle = rand.nextDouble() * 3.141592D * 2D;
			branchX1 = (int)((baseDistance * Math.sin(angle)) + branchX1);
			branchZ1 = (int)((baseDistance * Math.cos(angle)) + branchZ1); 
			int branchX2 = (int)((branchLength * Math.sin(angle)) + branchX1);
			int branchZ2 = (int)((branchLength * Math.cos(angle)) + branchZ1); 
			int branchHeight = height + rand.nextInt(4);
			int leafRadius = rand.nextInt(5) - 3;
			genTopBranch(branchX1, height, branchZ1, branchX1, height + rand.nextInt(4) + 4, branchZ1, topX, topZ);
			genLeafCircle(branchX1, branchHeight - 1, branchZ1, leafRadius + 5, leafRadius + 3, leafID, leafMeta, true);
			genLeafCircle(branchX1, branchHeight, branchZ1, leafRadius + 6, 0, leafID, leafMeta, true);
			genLeafCircle(branchX1, branchHeight + 1, branchZ1, leafRadius + 10, 0, leafID, leafMeta, true);
			genLeafCircle(branchX1, branchHeight + 2, branchZ1, leafRadius + 9, 0, leafID, leafMeta, true);
		}*/
	}

	public boolean genTopBranch(int i, int j, int k, int sX, int sY, int sZ, int topX, int topZ) {
		ArrayList<Block> allowedBlocks = new ArrayList(this.standardAllowedBlocks);
		allowedBlocks.add(wood.getBlock());
		allowedBlocks.add(leaves.getBlock());
		allowedBlocks.add(Blocks.VINE);
		int branchSize = rand.nextInt(2) + 4;
		ArrayList<int[]> lines = new ArrayList<int[]>();
		for (int x = i - branchSize; x < i + branchSize; x++) {
			for (int z = k - branchSize; z < k + branchSize; z++) {
				if ((x - i) * (x - i) + (z - k) * (z - k) < branchSize * branchSize && (x - topX) * (x - topX) + (z - topZ) * (z - topZ) < trunkRadius * trunkRadius) {
					if (!checkBlockLine(new int[] { x, j, z }, new int[] {  sX + (i - x), sY, sZ + (k - z) }, allowedBlocks)) {
						return false;
					}
				}
			}
		}
		for (int x = i - branchSize; x < i + branchSize; x++) {
			for (int z = k - branchSize; z < k + branchSize; z++) {
				if ((x - i) * (x - i) + (z - k) * (z - k) < branchSize * branchSize && (x - topX) * (x - topX) + (z - topZ) * (z - topZ) < trunkRadius * trunkRadius) {
					placeBlockLine(new int[] { x, j, z }, new int[] {  sX + (i - x), sY, sZ + (k - z) }, wood);
				}
			}
		}
		return true;
	}

	public void genLeafCircle(int x, int y, int z, int outerRadius, int innerRadius, IBlockState state, boolean vines) {
		int outerRadiusSquared = outerRadius * outerRadius;
		int innerRadiusSquared = innerRadius * innerRadius;

		for (int i = -outerRadius + x; i < outerRadius + x; i++) {
			for (int k = -outerRadius + z; k < outerRadius + z; k++) {
				double d = (x - i) * (x - i) + (z - k) * (z - k);
				if (d <= outerRadiusSquared && d >= innerRadiusSquared) {
					if (isAirBlock(i, y, k) || getBlock(i, y, k) == state.getBlock()) {
						placeBlock(i, y, k, state, false);
					}

					if (rand.nextInt(20) == 0 && vines) {
						genVines(i, y - 1, k);
					}
				}
			}
		}
	} 

	public void genVines(int i, int j, int k) {
		int length = rand.nextInt(15) + 8;
		int dir = rand.nextInt(4);
		IProperty direction = null;
		if (dir == 0) direction = BlockVine.NORTH;
		if (dir == 1) direction = BlockVine.SOUTH;
		if (dir == 2) direction = BlockVine.EAST;
		if (dir == 3) direction = BlockVine.WEST;

		for (int y = j; y > j - length; y--) {
			if (isAirBlock(i, y, k)) {
				placeBlock(i, y, k, Blocks.VINE.getDefaultState().withProperty(direction, Boolean.valueOf(true)), false);
			}
			else break;
		}
	}

	public boolean placeBlock(int i, int j, int k, IBlockState state, boolean force) {
		Block bID = getBlock(i, j, k);
		if(force || bID == Blocks.WATER || bID == Blocks.FLOWING_WATER || bID == BlockRegistry.tropicsWater 
				|| bID == Blocks.AIR) {
			return setBlockState(i, j, k, state, 0);
		}
		return false;
	}

	@Override
	public boolean genCircle(int i, int j, int k, double outerRadius, double innerRadius, IBlockState state, boolean solid) {
		boolean hasGenned = false;
		double outerRadiusSquared = outerRadius * outerRadius;
		double innerRadiusSquared = innerRadius * innerRadius;
		for (int x = (int)-outerRadius + i; x < (int)outerRadius + i; x++) {
			for (int z = (int)-outerRadius + k; z < (int)outerRadius + k; z++) {
				double d = (x - i) * (x - i) + (z - k) * (z - k);
				if (d <= outerRadiusSquared && d >= innerRadiusSquared) {
					Block bID = getBlock(x, j, z);
					if ((bID == Blocks.AIR || bID == Blocks.WATER || bID == Blocks.FLOWING_WATER) 
							|| bID == BlockRegistry.tropicsWater 
							|| /*bID == TCBlockRegistry.tropicsWaterFlowing ||*/ solid)
					{
						if (placeBlock(x, j, z, state, solid)) {
							hasGenned = true;
						}
					}
				}
			}
		}
		return hasGenned;
	}

	@Override
	public ArrayList<int[]> placeBlockLine(int ai[], int ai1[], IBlockState state)
	{
		ArrayList<int[]> places = new ArrayList<int[]>();
		int ai2[] = {
				0, 0, 0
		};
		byte byte0 = 0;
		int j = 0;
		for(; byte0 < 3; byte0++)
		{
			ai2[byte0] = ai1[byte0] - ai[byte0];
			if(Math.abs(ai2[byte0]) > Math.abs(ai2[j]))
			{
				j = byte0;
			}
		}

		if(ai2[j] == 0)
		{
			return null;
		}
		byte byte1 = otherCoordPairs[j];
		byte byte2 = otherCoordPairs[j + 3];
		byte byte3;
		if(ai2[j] > 0)
		{
			byte3 = 1;
		} else
		{
			byte3 = -1;
		}
		double d = (double)ai2[byte1] / (double)ai2[j];
		double d1 = (double)ai2[byte2] / (double)ai2[j];
		int ai3[] = {
				0, 0, 0
		};
		int k = 0;
		for(int l = ai2[j] + byte3; k != l; k += byte3)
		{
			ai3[j] = MathHelper.floor(ai[j] + k + 0.5D);
			ai3[byte1] = MathHelper.floor(ai[byte1] + k * d + 0.5D);
			ai3[byte2] = MathHelper.floor(ai[byte2] + k * d1 + 0.5D);
			placeBlock(ai3[0], ai3[1], ai3[2], state, true);
			places.add(new int[] { ai3[0], ai3[1], ai3[2] });
		}
		return places;
	}

	@Override
	public ArrayList<int[]> checkAndPlaceBlockLine(int ai[], int ai1[], IBlockState state, List a)
	{
		ArrayList<int[]> places = new ArrayList<int[]>();
		int ai2[] = {
				0, 0, 0
		};
		byte byte0 = 0;
		int j = 0;
		for (; byte0 < 3; byte0++) {
			ai2[byte0] = ai1[byte0] - ai[byte0];
			if (Math.abs(ai2[byte0]) > Math.abs(ai2[j])) {
				j = byte0;
			}
		}

		if(ai2[j] == 0) {
			return null;
		}

		byte byte1 = otherCoordPairs[j];
		byte byte2 = otherCoordPairs[j + 3];
		byte byte3;
		if(ai2[j] > 0) {
			byte3 = 1;
		} else {
			byte3 = -1;
		}
		double d = (double)ai2[byte1] / (double)ai2[j];
		double d1 = (double)ai2[byte2] / (double)ai2[j];
		int ai3[] = {
				0, 0, 0
		};
		int k = 0;

		for (int l = ai2[j] + byte3; k != l; k += byte3) {
			ai3[j] = MathHelper.floor(ai[j] + k + 0.5D);
			ai3[byte1] = MathHelper.floor(ai[byte1] + k * d + 0.5D);
			ai3[byte2] = MathHelper.floor(ai[byte2] + k * d1 + 0.5D);
			Block bId = getBlock(ai3[0], ai3[1], ai3[2]);
			if (!a.contains(bId)) {
				return null;
			}
		}

		for (int l = ai2[j] + byte3; k != l; k += byte3) {
			ai3[j] = MathHelper.floor(ai[j] + k + 0.5D);
			ai3[byte1] = MathHelper.floor(ai[byte1] + k * d + 0.5D);
			ai3[byte2] = MathHelper.floor(ai[byte2] + k * d1 + 0.5D);
			placeBlock(ai3[0], ai3[1], ai3[2], state, true);
			places.add(new int[] { ai3[0], ai3[1], ai3[2] });
		}
		return places;
	}

	public ItemStack randLoot() {
		int picker = rand.nextInt(18);
		if(picker < 6)
		{
			return new ItemStack(ItemRegistry.bambooShoot, rand.nextInt(20) + 1);
		}
//		else if(picker < 8)
//		{
//			return new ItemStack(ItemRegistry.coconutBomb, rand.nextInt(3) + 1);
//		}
		else if(picker < 10)
		{
			return new ItemStack(ItemRegistry.scale, rand.nextInt(3) + 1);
		}
		else if(picker < 14)
		{
			return new ItemStack(ItemRegistry.cookedFrogLeg, rand.nextInt(4) + 1);
		}
		/*		else if(picker == 14)
		{
			return new ItemStack(TCItemRegistry.ashenMasks, 1, rand.nextInt(7));
		}*/
		else if(picker <= 15)
		{
			return new ItemStack(ItemRegistry.recordTradeWinds, 1);
		}
		else if(picker == 16)
		{
			return new ItemStack(ItemRegistry.recordEasternIsles, 1);
		}
		else
		{
			return new ItemStack(ItemRegistry.azurite, 1, 3);
		}
	}
}