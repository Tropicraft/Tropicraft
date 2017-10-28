package net.tropicraft.core.common.dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.tropicraft.core.common.block.BlockTikiTorch;
import net.tropicraft.core.common.block.BlockTropicraftSands;
import net.tropicraft.core.common.block.BlockTropicsPortal;
import net.tropicraft.core.common.block.tileentity.TileEntityBambooChest;
import net.tropicraft.core.common.enums.TropicraftBundles;
import net.tropicraft.core.common.enums.TropicraftSands;
import net.tropicraft.core.registry.BlockRegistry;
import net.tropicraft.core.registry.ItemRegistry;


public class TeleporterTropics extends Teleporter {

    private static Block PORTAL_WALL_BLOCK;
    private static Block PORTAL_BLOCK;
    private IBlockState thatchBlock = BlockRegistry.bundles.defaultForVariant(TropicraftBundles.THATCH);

    private final WorldServer world;
    private final Random random;

    /** Stores successful portal placement locations for rapid lookup. */
    private final Long2ObjectMap<Teleporter.PortalPosition> destinationCoordinateCache = new Long2ObjectOpenHashMap(4096);

    /**
     * A list of valid keys for the destinationCoordainteCache. These are based on the X & Z of the players initial
     * location.
     */
    private final List destinationCoordinateKeys = new ArrayList();

    public TeleporterTropics(WorldServer world) {
        super(world);
        PORTAL_BLOCK = BlockRegistry.tropicsPortal;
        PORTAL_WALL_BLOCK = BlockRegistry.portalWall;
        this.world = world;
        this.random = new Random(world.getSeed());
    }

    @Override
    public void placeInPortal(Entity entity, float yaw) {
        long startTime = System.currentTimeMillis();
        if (!placeInExistingPortal(entity, yaw)) {
            makePortal(entity);
            placeInExistingPortal(entity, yaw);
        }

        long finishTime = System.currentTimeMillis();

        System.out.printf("It took %f seconds for TeleporterTropics.placeInPortal to complete\n", (finishTime - startTime) / 1000.0F);
    }

    @Override
    public boolean placeInExistingPortal(Entity entity, float f)
    {
        int searchArea = 148;
        double closestPortal = -1D;
        int foundX = 0;
        int foundY = 0;
        int foundZ = 0;
        int entityX = MathHelper.floor(entity.posX);
        int entityZ = MathHelper.floor(entity.posZ);
        BlockPos blockpos = BlockPos.ORIGIN;
        boolean notInCache = true;

        long j1 = ChunkPos.asLong(entityX, entityZ);

        if (destinationCoordinateCache.containsKey(j1)) {
            //	System.out.println("Setting closest portal to 0");
            PortalPosition portalposition = (PortalPosition)destinationCoordinateCache.get(j1);
            closestPortal = 0.0D;
            blockpos = portalposition;
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
                        BlockPos pos = new BlockPos(x, y, z);
                        if (world.getBlockState(pos).getBlock() == PORTAL_BLOCK)
                        {
                            pos = pos.down();
                            while (world.getBlockState(pos).getBlock() == PORTAL_BLOCK)
                            {
                                --y;
                                pos = pos.down();
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
                this.destinationCoordinateCache.put(j1, new Teleporter.PortalPosition(blockpos, this.world.getTotalWorldTime()));
            }

            int x = foundX;
            int y = foundY;
            int z = foundZ;
            double newLocX = x + 0.5D;
            double newLocY = y + 0.5D;
            double newLocZ = z + 0.5D;

            BlockPos pos = new BlockPos(x, y, z);

            if (world.getBlockState(pos.offset(EnumFacing.WEST)).getBlock() == PORTAL_BLOCK)
            {
                newLocX -= 0.5D;
            }
            if (world.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() == PORTAL_BLOCK)
            {
                newLocX += 0.5D;
            }
            if (world.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() == PORTAL_BLOCK)
            {
                newLocZ -= 0.5D;
            }
            if (world.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock() == PORTAL_BLOCK)
            {
                newLocZ += 0.5D;
            }
            entity.setLocationAndAngles(newLocX, newLocY + 2, newLocZ, entity.rotationYaw, 0.0F);
            int worldSpawnX = MathHelper.floor(newLocX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
            int worldSpawnZ = MathHelper.floor(newLocZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
            int worldSpawnY = world.getHeight(new BlockPos(worldSpawnX, 0, worldSpawnZ)).getY() + 3;

            entity.motionX = entity.motionY = entity.motionZ = 0.0D;

            // If the player is entering the tropics, spawn an Encyclopedia Tropica
            // in the spawn portal chest (if they don't already have one AND one isn't
            // already in the chest)
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (world.provider instanceof WorldProviderTropicraft) {
                    //TODO improve this logical check to an NBT tag or something?
                    if (!player.inventory.hasItemStack(new ItemStack(ItemRegistry.encyclopedia))) {
                        // Search for the spawn chest
                        TileEntityBambooChest chest = null;
                        int chestX = MathHelper.floor(newLocX);
                        int chestZ = MathHelper.floor(newLocZ);
                        chestSearch:
                            for (int searchX = -3; searchX < 4; searchX++) {
                                for (int searchZ = -3; searchZ < 4; searchZ++) {
                                    for (int searchY = -4; searchY < 5; searchY++) {
                                        BlockPos chestPos = new BlockPos(chestX + searchX, worldSpawnY + searchY, chestZ + searchZ);
                                        if (world.getBlockState(chestPos).getBlock() == BlockRegistry.bambooChest) {
                                            chest = (TileEntityBambooChest)world.getTileEntity(chestPos);
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
                                if (stack != null && stack.getItem() == ItemRegistry.encyclopedia) {
                                    hasEncyclopedia = true;
                                }
                            }

                            // Give out a new encyclopedia
                            if (!hasEncyclopedia) {
                                for (int inv = 0; inv < chest.getSizeInventory(); inv++) {
                                    ItemStack stack = chest.getStackInSlot(inv);
                                    if (stack == null) {
                                        chest.setInventorySlotContents(inv, new ItemStack(ItemRegistry.encyclopedia, 1));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean makePortal(Entity entity) {
        System.out.println("Start make portal");
        int searchArea = 16;
        double closestSpot = -1D;
        int entityX = MathHelper.floor(entity.posX);
        int entityY = MathHelper.floor(entity.posY);
        int entityZ = MathHelper.floor(entity.posZ);
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
                    BlockPos pos = new BlockPos(x, y, z);
                    for (; y >= 63 - 1 && (world.getBlockState(pos).getBlock() == Blocks.AIR ||
                            !world.getBlockState(pos).isOpaqueCube()); pos = pos.down()) {
                        y = pos.getY();
                    }
                    // Only generate portal between sea level and sea level + 20
                    if (y > 63 + 20 || y < 63) {
                        continue;
                    }

                    BlockPos tryPos = new BlockPos(x, y, z);
                    if (getValidBuildBlocks().contains(world.getBlockState(tryPos))) {
                        for (int xOffset = -2; xOffset <= 2; xOffset++) {
                            for (int zOffset = -2; zOffset <= 2; zOffset++) {
                                int otherY = world.getHeight() - 1;
                                BlockPos pos1 = new BlockPos(x + xOffset, otherY, z + zOffset);
                                BlockPos pos2 = tryPos.toImmutable(); 
                                for (; otherY >= 63 && (world.getBlockState(pos1).getBlock() == Blocks.AIR ||
                                        !world.getBlockState(pos2).isOpaqueCube()); pos1 = pos1.down()) {
                                    otherY = pos1.getY();
                                }
                                if (Math.abs(y - otherY) >= 3) {
                                    continue nextCoords;
                                }

                                //if (!getValidBuildBlocks().contains(world.getBlock(x + xOffset, otherY, z + zOffset))) {
                                //	continue nextCoords;
                                //}
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

        int worldSpawnX = MathHelper.floor(foundX);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
        int worldSpawnZ = MathHelper.floor(foundZ);//TODO + ((new Random()).nextBoolean() ? 3 : -3);
        int worldSpawnY = getTerrainHeightAt(worldSpawnX, worldSpawnZ);//world.getHeightValue(worldSpawnX, worldSpawnZ) - 2;

        // Max distance to search in every direction for the nearest landmass to build a bridge to
        int SEARCH_FOR_LAND_DISTANCE_MAX = 200;

        // If we can't find a spot (e.g. we're in the middle of the ocean),
        // just put the portal at sea level
        if(closestSpot < 0.0D) {
            // Perhaps this was the culprit
            /*  Random r = new Random();
            foundX += r.nextInt(16) - 8;
            foundZ += r.nextInt(16) - 8;*/
            foundY = worldSpawnY - 2;
            boolean foundLand = false;

            for (int dist = 1; !foundLand && dist < SEARCH_FOR_LAND_DISTANCE_MAX; dist++) {
                for (EnumFacing dir : EnumFacing.HORIZONTALS) {
                    BlockPos pos = new BlockPos(worldSpawnX, worldSpawnY, worldSpawnZ).offset(dir, 3 + dist);
                    IBlockState state = world.getBlockState(pos);
                    if (getValidBuildBlocks().contains(state)) {
                        foundLand = true;
                        BlockPos buildpos = new BlockPos(worldSpawnX, worldSpawnY + 1, worldSpawnZ).offset(dir, 3);
                        while (!buildpos.equals(pos.up())) {
                            world.setBlockState(buildpos, thatchBlock);
                            world.setBlockState(buildpos.offset(dir.rotateY()), thatchBlock);
                            world.setBlockState(buildpos.offset(dir.rotateYCCW()), thatchBlock);
                            buildpos = buildpos.offset(dir);
                        }

                        BlockPos stairPosMid = new BlockPos(pos.getX(), worldSpawnY + 1, worldSpawnZ);
                        placeStairs(stairPosMid, dir.getOpposite());
                        generateThatchBorder(worldSpawnX, worldSpawnY + 1, worldSpawnZ);
                        break;
                    }
                }
            }
        }

        //      System.out.printf("Building teleporter at x:<%d>, y:<%d>, z:<%d>\n", foundX, foundY, foundZ);

        entity.setLocationAndAngles(foundX, foundY + 2, foundZ, entity.rotationYaw, 0.0F);
        buildTeleporterAt(worldSpawnX, worldSpawnY + 1, worldSpawnZ, entity);

        System.out.println("End makePortal");

        return true;
    }

    private void placeStairs(BlockPos pos, EnumFacing dir) {
        if (dir == EnumFacing.EAST || dir == EnumFacing.WEST) {
            BlockPos stairPosLeft = pos.add(0, 0, -1);
            BlockPos stairPosMid = pos;
            BlockPos stairPosRight = pos.add(0, 0, 1);

            IBlockState thatchStairState = BlockRegistry.thatchStairs.getDefaultState().withProperty(BlockStairs.FACING, dir);

            world.setBlockState(stairPosLeft, thatchStairState);
            world.setBlockState(stairPosMid, thatchStairState);
            world.setBlockState(stairPosRight, thatchStairState);
        } else if (dir == EnumFacing.NORTH || dir == EnumFacing.SOUTH) {
            BlockPos stairPosLeft = pos.add(-1, 0, 0);
            BlockPos stairPosMid = pos;
            BlockPos stairPosRight = pos.add(1, 0, 0);

            IBlockState thatchStairState = BlockRegistry.thatchStairs.getDefaultState().withProperty(BlockStairs.FACING, dir);

            world.setBlockState(stairPosLeft, thatchStairState);
            world.setBlockState(stairPosMid, thatchStairState);
            world.setBlockState(stairPosRight, thatchStairState);
        }
    }

    private void generateThatchBorder(int x, int y, int z) {
        for (int zOffset = -4; zOffset <= 4; zOffset++) {
            for (int xOffset = -4; xOffset <= 4; xOffset++) {
                boolean isWall = xOffset < -2 || xOffset > 2 || zOffset < -2 || zOffset > 2;
                if (isWall) {
                    BlockPos thatchPos = new BlockPos(x + xOffset, y, z + zOffset);
                    IBlockState thatchState = BlockRegistry.bundles.defaultForVariant(TropicraftBundles.THATCH);
                    world.setBlockState(thatchPos, thatchState);
                }
            }
        }
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
            IBlockState state = world.getBlockState(new BlockPos(x, y, z));
            Block block = state.getBlock();
            if(block == Blocks.DIRT || state.getMaterial() == Material.ROCK ||
                    state.getMaterial() == Material.WATER || state.getMaterial() == Material.GRASS ||
                    state.getMaterial() == Material.SAND)
            {
                return y;
            }
        }
        return 0;
    }

    public void buildTeleporterAt(int x, int y, int z, Entity entity) {
        System.out.println("start buildTeleporterAt");
        y = y < 9 ? 9 : y;

        for (int yOffset = 4; yOffset >= -7; yOffset--) {
            for (int zOffset = -2; zOffset <= 2; zOffset++) {
                for (int xOffset = -2; xOffset <= 2; xOffset++) {
                    int blockX = x + xOffset;
                    int blockY = y + yOffset;
                    int blockZ = z + zOffset;
                    BlockPos pos = new BlockPos(blockX, blockY, blockZ);

                    if (yOffset == -7) {
                        // Set bottom of portal to be solid
                        world.setBlockState(pos, PORTAL_WALL_BLOCK.getDefaultState());
                    } else if (yOffset > 0) {
                        // Set 4 blocks above portal to air
                        world.setBlockToAir(pos);
                    } else {
                        boolean isWall = xOffset == -2 || xOffset == 2 || zOffset == -2 || zOffset == 2;
                        if (isWall) {
                            // Set walls around portal
                            world.setBlockState(pos, PORTAL_WALL_BLOCK.getDefaultState());
                        } else {
                            // Set inside of portal
                            boolean isTeleportBlock = yOffset <= -5;
                            if (isTeleportBlock) {
                                IBlockState state = PORTAL_BLOCK.getDefaultState().withProperty(BlockTropicsPortal.TELEPORTABLE, Integer.valueOf(1));
                                world.setBlockState(pos, state);								
                            } else {
                                world.setBlockState(pos, PORTAL_BLOCK.getDefaultState());
                            }

                        }
                    }

                    boolean isCorner = (xOffset == -2 || xOffset == 2) && (zOffset == -2 || zOffset == 2);
                    if (yOffset == 0 && isCorner) {
                        world.setBlockState(pos.up(), BlockRegistry.tikiTorch.getDefaultState().withProperty(BlockTikiTorch.SECTION, BlockTikiTorch.TorchSection.LOWER), 3);
                        world.setBlockState(pos.up(2), BlockRegistry.tikiTorch.getDefaultState().withProperty(BlockTikiTorch.SECTION, BlockTikiTorch.TorchSection.MIDDLE), 3);
                        world.setBlockState(pos.up(3), BlockRegistry.tikiTorch.getDefaultState().withProperty(BlockTikiTorch.SECTION, BlockTikiTorch.TorchSection.UPPER), 3);
                    }

                }
            }
        }

        // Add an unbreakable chest to place encyclopedia in
        // NOTE: using instanceof instead of world.getWorldInfo().getDimension()
        // because getWorldInfo() may not be set/correct yet
        if (world.provider instanceof WorldProviderTropicraft) {
            BlockPos chestPos = new BlockPos(x + 2, y + 1, z);
            world.setBlockState(chestPos, BlockRegistry.bambooChest.getDefaultState(), 3);
            TileEntityBambooChest tile = (TileEntityBambooChest)world.getTileEntity(chestPos);
            if (tile != null) {
                tile.setIsUnbreakable(true);
            }
        }

        System.out.println("end buildTeleporterAt");
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
                PortalPosition portalposition = (PortalPosition)destinationCoordinateCache.get(olong.longValue());

                if (portalposition == null || portalposition.lastUpdateTime < j)
                {
                    iterator.remove();
                    destinationCoordinateCache.remove(olong.longValue());
                }
            }
        }
    }

    /**
     * @return List of valid block states to build portal on
     */
    private List<IBlockState> getValidBuildBlocks() {
        return Arrays.asList(
                Blocks.SAND.getDefaultState(),
                Blocks.GRASS.getDefaultState(),
                Blocks.DIRT.getDefaultState(),
                BlockRegistry.sands.getDefaultState().withProperty(BlockTropicraftSands.VARIANT, TropicraftSands.PURIFIED));
    }
}
